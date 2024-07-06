package net.tslat.aoa3.player;

import com.google.common.collect.ArrayListMultimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.advent.Logging;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.adventplayer.PlayerDataSyncPacket;
import net.tslat.aoa3.common.networking.packets.adventplayer.PlayerDataUpdatePacket;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.custom.AoAResources;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.common.toast.AbilityUnlockToastData;
import net.tslat.aoa3.content.item.armour.AdventArmour;
import net.tslat.aoa3.content.world.teleporter.PortalCoordinatesContainer;
import net.tslat.aoa3.data.server.AoAResourcesReloadListener;
import net.tslat.aoa3.data.server.AoASkillReqReloadListener;
import net.tslat.aoa3.data.server.AoASkillsReloadListener;
import net.tslat.aoa3.event.custom.events.PlayerLevelChangeEvent;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.leaderboard.SkillsLeaderboard;
import net.tslat.aoa3.leaderboard.task.LeaderboardActions;
import net.tslat.aoa3.library.object.PositionAndRotation;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.resource.AoAResource;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.scheduling.AoAScheduler;
import net.tslat.aoa3.util.*;
import net.tslat.smartbrainlib.util.RandomUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import static net.tslat.aoa3.player.AoAPlayerEventListener.ListenerState.*;

public final class ServerPlayerDataManager implements AoAPlayerEventListener, PlayerDataManager, INBTSerializable<CompoundTag> {
	private ServerPlayer player;

	private PlayerEquipment equipment;

	private final Object2ObjectOpenHashMap<AoASkill, AoASkill.Instance> skills = new Object2ObjectOpenHashMap<>(10);
	private final Object2ObjectOpenHashMap<AoAResource, AoAResource.Instance> resources = new Object2ObjectOpenHashMap<>(3);

	private final ArrayListMultimap<ListenerType, AoAPlayerEventListener> activeEventListeners = ArrayListMultimap.create();
	private final ArrayListMultimap<ListenerType, AoAPlayerEventListener> disabledEventListeners = ArrayListMultimap.create();
	private final ObjectOpenHashSet<AoAPlayerEventListener> dirtyListeners = new ObjectOpenHashSet<>();

	private final Object2ObjectOpenHashMap<ResourceKey<Level>, PortalCoordinatesContainer> portalCoordinatesMap = new Object2ObjectOpenHashMap<>();
	private final CopyOnWriteArraySet<ResourceLocation> patchouliBooks = new CopyOnWriteArraySet<>();

	private ItemStack[] itemStorage = null;
	private PositionAndRotation checkpoint = null;

	private boolean syncBooks = false;
	private boolean isLegitimate = true;
	private boolean abilitiesRegionLocked = false;

	public ServerPlayerDataManager(ServerPlayer player) {
		this.player = player;
		this.equipment = new PlayerEquipment();

		populateSkillsAndResources();
		gatherListeners();
	}

	@Override
	public ServerPlayer player() {
		return player;
	}

	public PlayerEquipment equipment() {
		return equipment;
	}

	@Override
	public Collection<AoASkill.Instance> getSkills() {
		return this.skills.values();
	}

	@Override
	@NotNull
	public AoASkill.Instance getSkill(AoASkill skill) {
		return this.skills.getOrDefault(skill, AoASkills.DEFAULT);
	}

	@Nullable
	public AoAAbility.Instance getAbility(String abilityId) {
		for (AoASkill.Instance skill : getSkills()) {
			if (skill.getAbilityMap().containsKey(abilityId))
				return skill.getAbilityMap().get(abilityId);
		}

		return null;
	}

	@Override
	public Collection<AoAResource.Instance> getResources() {
		return this.resources.values();
	}

	@Override
	@NotNull
	public AoAResource.Instance getResource(AoAResource resource) {
		return this.resources.getOrDefault(resource, AoAResources.DEFAULT);
	}

	@Override
	public void loadFromNbt(CompoundTag baseTag) {
		this.isLegitimate = baseTag.getBoolean("legitimate");
		int hash = baseTag.getInt("hash");

		if (hash == 0) {
			applyLegitimacyPenalties();
		}
		else {
			baseTag.remove("hash");

			if (hash != baseTag.hashCode())
				applyLegitimacyPenalties();
		}

		if (baseTag.contains("skills")) {
			CompoundTag skillsNbt = baseTag.getCompound("skills");

			for (AoASkill.Instance skill : skills.values()) {
				String id = AoARegistries.AOA_SKILLS.getKey(skill.type()).toString();

				if (skillsNbt.contains(id))
					skill.loadFromNbt(skillsNbt.getCompound(id));
			}
		}

		if (baseTag.contains("resources")) {
			CompoundTag resourcesNbt = baseTag.getCompound("resources");

			for (AoAResource.Instance resource : resources.values()) {
				String id = AoARegistries.AOA_RESOURCES.getKey(resource.type()).toString();

				if (resourcesNbt.contains(id))
					resource.loadFromNbt(resourcesNbt.getCompound(id));
			}
		}

		if (baseTag.contains("ItemStorage")) {
			CompoundTag itemStorage = baseTag.getCompound("ItemStorage");

			if (!itemStorage.isEmpty()) {
				this.itemStorage = new ItemStack[player.getInventory().getContainerSize()];

				for (String key : itemStorage.getAllKeys()) {
					try {
						int index = Integer.parseInt(key);

						if (index >= this.itemStorage.length)
							this.itemStorage = Arrays.copyOf(this.itemStorage, index + 1);

						this.itemStorage[index] = ItemUtil.loadStackFromNbt(this.player.level(), itemStorage.getCompound(key));
					}
					catch (Exception ex) {
						Logging.logMessage(org.apache.logging.log4j.Level.WARN, "Invalid key in ItemStorage NBT data for player. Stop messing with player NBT!", ex);
					}
				}
			}
		}

		if (baseTag.contains("PortalMap")) {
			CompoundTag portalMapTag = baseTag.getCompound("PortalMap");

			for (String s : portalMapTag.getAllKeys()) {
				try {
					CompoundTag portalReturnTag = portalMapTag.getCompound(s);
					ResourceLocation fromDim = ResourceLocation.read(portalReturnTag.getString("FromDim")).getOrThrow();
					BlockPos portalPos = NbtUtils.readBlockPos(portalReturnTag, "PortalPos").get();
					ResourceKey<Level> toDimKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.read(s).getOrThrow());
					ResourceKey<Level> fromDimKey = ResourceKey.create(Registries.DIMENSION, fromDim);

					portalCoordinatesMap.put(toDimKey, new PortalCoordinatesContainer(fromDimKey, portalPos));
				} catch (Exception e) {
					Logging.logMessage(org.apache.logging.log4j.Level.WARN, "Found invalid portal map data, has someone been tampering with files? Data: " + s);
				}
			}
		}

		if (baseTag.contains("Checkpoint")) {
			try {
				CompoundTag checkpointTag = baseTag.getCompound("Checkpoint");
				double x = checkpointTag.getDouble("x");
				double y = checkpointTag.getDouble("y");
				double z = checkpointTag.getDouble("z");
				float pitch = checkpointTag.getFloat("pitch");
				float yaw = checkpointTag.getFloat("yaw");

				checkpoint = new PositionAndRotation(x, y, z, pitch, yaw);
			} catch (NumberFormatException e) {
				Logging.logMessage(org.apache.logging.log4j.Level.WARN, "Found invalid checkpoint data, has someone been tampering with files?");
			}
		}

		if (baseTag.contains("PatchouliBooks")) {
			ListTag booksNbt = baseTag.getList("PatchouliBooks", Tag.TAG_STRING);

			if (!patchouliBooks.isEmpty())
				patchouliBooks.clear();

			for (Tag book : booksNbt) {
				ResourceLocation.read(book.getAsString()).resultOrPartial(err -> Logging.logMessage(org.apache.logging.log4j.Level.WARN, err)).ifPresent(patchouliBooks::add);
			}
		}

		checkAndUpdateLegitimacy();

		baseTag.putInt("hash", baseTag.hashCode());
	}

	@Override
	public ListenerType[] getListenerTypes() {
		return new ListenerType[] {
				ListenerType.PLAYER_DEATH,
				ListenerType.PLAYER_RESPAWN,
				ListenerType.PLAYER_CLONE,
				ListenerType.EQUIPMENT_CHANGE,
				ListenerType.LEVEL_CHANGE
		};
	}

	@Override
	public void addListener(AoAPlayerEventListener listener, boolean active, ListenerType... types) {
		ArrayListMultimap<ListenerType, AoAPlayerEventListener> holder = active ? this.activeEventListeners : this.disabledEventListeners;

		if (types.length > 0) {
			for (ListenerType type : types) {
				holder.put(type, listener);
			}
		}
		else {
			holder.put(null, listener);
		}
	}

	public void removeListener(AoAPlayerEventListener listener, boolean active, ListenerType... types) {
		ArrayListMultimap<ListenerType, AoAPlayerEventListener> holder = active ? this.activeEventListeners : this.disabledEventListeners;

		if (types.length > 0) {
			for (ListenerType type : types) {
				holder.remove(type, listener);
			}
		}
		else {
			holder.remove(null, listener);
		}
	}

	@Override
	public List<AoAPlayerEventListener> getListeners(ListenerType eventType) {
		return activeEventListeners.get(eventType);
	}

	public void markListenerDirty(AoAPlayerEventListener listener) {
		dirtyListeners.add(listener);
	}

	@Override
	public void updatePlayerInstance(Player player) {
		this.player = (ServerPlayer)player;
	}

	private void selfDestruct() {
		player = null;
		equipment = null;
		checkpoint = null;

		skills.clear();
		resources.clear();
		portalCoordinatesMap.clear();
		activeEventListeners.clear();
		patchouliBooks.clear();

		if (itemStorage != null)
			Arrays.fill(this.itemStorage, null);
	}

	private void populateSkillsAndResources() {
		AoASkillsReloadListener.populateSkillMap(this, skills);
		AoAResourcesReloadListener.populateResourceMap(this, resources);
	}

	private void gatherListeners() {
		addListener(this, true, getListenerTypes());
		addListener(equipment, true, equipment.getListenerTypes());

		for (AoASkill.Instance skill : skills.values()) {
			addListener(skill, true, skill.getListenerTypes());

			for (AoAAbility.Instance ability : skill.getAbilityMap().values()) {
				addListener(ability, ability.meetsRequirements(), ability.getListenerTypes());
			}
		}

		for (AoAResource.Instance resource : resources.values()) {
			addListener(resource, resource.meetsRequirements(), resource.getListenerTypes());
		}
	}

	public static void syncNewPlayer(ServerPlayer pl) {
		ServerPlayerDataManager plData = PlayerUtil.getAdventPlayer(pl);

		if (plData.patchouliBooks.isEmpty() && IntegrationManager.isPatchouliActive())
			plData.patchouliBooks.add(AdventOfAscension.id("aoa_essentia"));

		if (SkillsLeaderboard.isEnabled())
			LeaderboardActions.addNewPlayer(plData);

		AoANetworking.sendToPlayer(pl, new PlayerDataSyncPacket(plData.savetoNbt(pl.level().registryAccess(), true)));
	}

	public void doPlayerTick() {
		if (player == null || player.isSpectator() || player.level().isClientSide)
			return;

		if (!dirtyListeners.isEmpty()) {
			Iterator<AoAPlayerEventListener> listeners = dirtyListeners.iterator();

			while (listeners.hasNext()) {
				AoAPlayerEventListener listener = listeners.next();

				if (listener.getListenerState() == ACTIVE) {
					addListener(listener, true, listener.getListenerTypes());
					removeListener(listener, false, listener.getListenerTypes());
				}
				else {
					addListener(listener, false, listener.getListenerTypes());
					removeListener(listener, true, listener.getListenerTypes());
				}

				listeners.remove();
			}
		}

		CompoundTag syncData = null;
		CompoundTag skillData = null;
		CompoundTag resourceData = null;

		for (AoASkill.Instance skill : getSkills()) {
			if (skill.needsSync) {
				if (skillData == null)
					skillData = new CompoundTag();

				skillData.put(AoARegistries.AOA_SKILLS.getKey(skill.type()).toString(), skill.getSyncData(false));
			}
		}

		for (AoAResource.Instance resource : getResources()) {
			if (resource.needsSync) {
				if (resourceData == null)
					resourceData = new CompoundTag();

				resourceData.put(AoARegistries.AOA_RESOURCES.getKey(resource.type()).toString(), resource.getSyncData(false));
			}
		}

		if (skillData != null) {
			syncData = new CompoundTag();

			syncData.put("skills", skillData);
		}

		if (resourceData != null) {
			if (syncData == null)
				syncData = new CompoundTag();

			syncData.put("resources", resourceData);
		}

		if (syncBooks) {
			if (syncData == null)
				syncData = new CompoundTag();

			ListTag booksNbt = new ListTag();

			for (ResourceLocation id : patchouliBooks) {
				booksNbt.add(StringTag.valueOf(id.toString()));
			}

			syncData.put("PatchouliBooks", booksNbt);
		}

		if (syncData != null) {
			syncData.putBoolean("legitimate", this.isLegitimate);

			AoANetworking.sendToPlayer(player, new PlayerDataUpdatePacket(syncData));
		}
	}

	private void storeInterventionItems() {
		if (this.itemStorage == null)
			this.itemStorage = new ItemStack[this.player.getInventory().getContainerSize()];

		for (int i = 0; i < this.player.getInventory().getContainerSize(); i++) {
			ItemStack stack = this.player.getInventory().getItem(i);

			if (EnchantmentUtil.hasEnchantment(this.player.level(), stack, AoAEnchantments.INTERVENTION)) {
				if (RandomUtil.oneInNChance(5))
					EnchantmentUtil.removeEnchantment(this.player.level(), stack, AoAEnchantments.INTERVENTION);

				this.itemStorage[i] = stack.copy();
				this.player.getInventory().setItem(i, ItemStack.EMPTY);
			}
		}
	}

	public void returnItemStorage() {
		if (this.itemStorage == null)
			return;

        Inventory inventory = this.player.getInventory();

        for (int i = 0; i < this.itemStorage.length; i++) {
            ItemStack slotItem = inventory.getItem(i);
            ItemStack storageItem = this.itemStorage[i];

            if (storageItem == null) {
                this.itemStorage[i] = ItemStack.EMPTY;

                continue;
            }

            if (slotItem.isEmpty()) {
                inventory.setItem(i, storageItem);

                this.itemStorage[i] = ItemStack.EMPTY;
            }
            else if (ItemStack.isSameItemSameComponents(slotItem, storageItem)) {
                int growSize = Math.min(slotItem.getMaxStackSize(), slotItem.getCount() + storageItem.getCount()) - slotItem.getCount();

                if (growSize > 0) {
                    slotItem.grow(growSize);
                    slotItem.setPopTime(5);
                }

                if (growSize < storageItem.getCount()) {
                    storageItem.setCount(storageItem.getCount() - growSize);
                }
                else {
                    this.itemStorage[i] = ItemStack.EMPTY;
                }
            }
        }

        InventoryUtil.giveItemsTo(this.player, this.itemStorage);

        this.itemStorage = null;
    }

	public void storeInventoryContents() {
		if (itemStorage == null)
			itemStorage = new ItemStack[player.getInventory().getContainerSize()];

		int slotIndex = 0;

		for (NonNullList<ItemStack> compartment : player.getInventory().compartments) {
			int compartmentIndex = 0;

			for (ItemStack stack : compartment) {
				if (!stack.isEmpty())
					storeItem(slotIndex + compartmentIndex++, stack);
			}

			slotIndex += compartment.size();
		}

		player.getInventory().clearContent();
	}

	public void storeItem(int slotIndex, ItemStack stack) {
		if (this.itemStorage == null)
			this.itemStorage = new ItemStack[Math.max(slotIndex, player.getInventory().getContainerSize())];

		if (this.itemStorage[slotIndex] != null) {
			slotIndex = 0;

			while (true) {
				if (this.itemStorage.length <= slotIndex++) {
					if (slotIndex >= this.itemStorage.length)
						this.itemStorage = Arrays.copyOf(this.itemStorage, slotIndex + 1);

					break;
				}
				else if (this.itemStorage[slotIndex] == null) {
					break;
				}
			}
		}

		this.itemStorage[slotIndex] = stack;
	}

	public void setPortalReturnLocation(ResourceKey<Level> toDim, PortalCoordinatesContainer coords) {
		portalCoordinatesMap.put(toDim, coords);
	}

	public void removePortalReturnLocation(ResourceKey<Level> toDim) {
		portalCoordinatesMap.remove(toDim);
	}

	public void flushPortalReturnLocations() {
		portalCoordinatesMap.clear();
	}

	@Nullable
	public PortalCoordinatesContainer getPortalReturnLocation(ResourceKey<Level> toDim) {
		return portalCoordinatesMap.get(toDim);
	}

	public void setCheckpoint(PositionAndRotation pos) {
		this.checkpoint = pos;
	}

	public void clearCheckpoint() {
		this.checkpoint = null;
	}

	@Nullable
	public PositionAndRotation getCheckpoint() {
		return this.checkpoint;
	}

	@Override
	public void handlePlayerDeath(final LivingDeathEvent ev) {
		storeInterventionItems();
	}

	@Override
	public void handlePlayerRespawn(final PlayerEvent.PlayerRespawnEvent ev) {
		returnItemStorage();
	}

	@Override
	public void handleLevelChange(PlayerLevelChangeEvent ev) {
		if (SkillsLeaderboard.isEnabled())
			LeaderboardActions.updatePlayer(this, ev.getSkill());

		if (!areAbilitiesRegionLocked())
			recheckSkills();
	}

	private void recheckSkills() {
		Consumer<Collection<AoAPlayerEventListener>> updateHandler = collection -> collection.forEach(listener -> {
			ListenerState state = listener.getListenerState();

			if (state == ACTIVE) {
				if (!listener.meetsRequirements())
					listener.disable(DEACTIVATED, false);
			}
			else if (state == DEACTIVATED || (state == REGION_LOCKED && !areAbilitiesRegionLocked())) {
				if (listener.meetsRequirements()) {
					listener.reenable(false);

					if (state == DEACTIVATED && listener instanceof AoAAbility.Instance ability)
						AbilityUnlockToastData.sendToastPopupTo(this.player, ability);
				}
			}
		});

		updateHandler.accept(activeEventListeners.values());
		updateHandler.accept(disabledEventListeners.values());
	}

	public void setInAbilityLockRegion() {
		if (activeEventListeners.isEmpty())
			return;

		for (AoAPlayerEventListener listener : activeEventListeners.values()) {
			listener.disable(REGION_LOCKED, false);
		}

		abilitiesRegionLocked = true;
	}

	public void leaveAbilityLockRegion() {
		if (!areAbilitiesRegionLocked())
			return;

		abilitiesRegionLocked = false;

		recheckSkills();
	}

	public boolean areAbilitiesRegionLocked() {
		return this.abilitiesRegionLocked;
	}

	public void checkAndUpdateLegitimacy() {
		if (this.player != null) {
			AdvancementUtil.getAdvancement(this.player.serverLevel(), AdventOfAscension.id("completionist/by_the_books")).ifPresent(adv -> {
				PlayerAdvancements plAdv = this.player.getAdvancements();
				boolean hasAdvancement = plAdv.getOrStartProgress(adv).isDone();

				if (hasAdvancement && !this.isLegitimate)
					plAdv.revoke(adv, "legitimate");
			});
		}
	}

	public void applyLegitimacyPenalties() {
		this.isLegitimate = false;

		checkAndUpdateLegitimacy();
	}

	@Override
	public boolean isLegitimate() {
		return this.isLegitimate;
	}

	@Override
	public int getTotalLevel() {
		int i = 0;

		for (AoASkill.Instance skill : this.getSkills()) {
			i += skill.getLevel(true);
		}

		return i;
	}

	public void addPatchouliBook(ResourceLocation book) {
		patchouliBooks.add(book);
		syncBooks = true;
	}

	@Override
	public void handlePlayerDataClone(final PlayerEvent.Clone ev) {
		ServerPlayerDataManager sourceData = PlayerUtil.getAdventPlayer((ServerPlayer)ev.getOriginal());

		for (Map.Entry<AoASkill, AoASkill.Instance> entry : sourceData.skills.entrySet()) {
			AoASkill.Instance newInstance = skills.get(entry.getKey());

			if (newInstance != null)
				newInstance.loadFromNbt(entry.getValue().saveToNbt());
		}

		for (Map.Entry<AoAResource, AoAResource.Instance> entry : sourceData.resources.entrySet()) {
			AoAResource.Instance newInstance = resources.get(entry.getKey());

			if (newInstance != null)
				newInstance.loadFromNbt(entry.getValue().saveToNbt());
		}

		this.equipment.cooldowns.putAll(sourceData.equipment.cooldowns);
		this.portalCoordinatesMap.putAll(sourceData.portalCoordinatesMap);
		this.patchouliBooks.addAll(sourceData.patchouliBooks);
		this.itemStorage = ArrayUtils.clone(sourceData.itemStorage);
		this.checkpoint = sourceData.checkpoint;
		this.isLegitimate = sourceData.isLegitimate;

		AoAScheduler.scheduleSyncronisedTask(sourceData::selfDestruct, 1);
	}

	public CompoundTag savetoNbt(HolderLookup.Provider registryLookup, boolean forClientSync) {
		CompoundTag baseTag = new CompoundTag();

		if (!skills.isEmpty()) {
			CompoundTag skillsNbt = new CompoundTag();

			for (AoASkill.Instance skill : skills.values()) {
				skillsNbt.put(AoARegistries.AOA_SKILLS.getKey(skill.type()).toString(), forClientSync ? skill.getSyncData(true) : skill.saveToNbt());
			}

			if (!skillsNbt.isEmpty())
				baseTag.put("skills", skillsNbt);
		}

		if (!resources.isEmpty()) {
			CompoundTag resourcesNbt = new CompoundTag();

			for (AoAResource.Instance resource : resources.values()) {
				resourcesNbt.put(AoARegistries.AOA_RESOURCES.getKey(resource.type()).toString(), forClientSync ? resource.getSyncData(true) : resource.saveToNbt());
			}

			if (!resourcesNbt.isEmpty())
				baseTag.put("resources", resourcesNbt);
		}

		if (patchouliBooks != null) {
			ListTag booksNbt = new ListTag();

			for (ResourceLocation id : patchouliBooks) {
				booksNbt.add(StringTag.valueOf(id.toString()));
			}

			baseTag.put("PatchouliBooks", booksNbt);
		}

		if (!forClientSync) {
			if (this.itemStorage != null) {
				CompoundTag itemStorage = new CompoundTag();

				for (int i = 0; i < this.itemStorage.length; i++) {
					if (this.itemStorage[i] != null)
						itemStorage.put(String.valueOf(i), this.itemStorage[i].save(registryLookup));
				}

				baseTag.put("ItemStorage", itemStorage);
			}

			if (!portalCoordinatesMap.isEmpty()) {
				CompoundTag portalCoordinatesNBT = new CompoundTag();

				for (Map.Entry<ResourceKey<Level>, PortalCoordinatesContainer> entry : portalCoordinatesMap.entrySet()) {
					CompoundTag portalReturnTag = new CompoundTag();
					PortalCoordinatesContainer container = entry.getValue();

					portalReturnTag.putString("FromDim", container.fromDim().location().toString());
					portalReturnTag.put("PortalPos", NbtUtils.writeBlockPos(container.portalPos()));

					portalCoordinatesNBT.put(entry.getKey().location().toString(), portalReturnTag);
				}

				baseTag.put("PortalMap", portalCoordinatesNBT);
			}

			if (checkpoint != null) {
				CompoundTag checkpointTag = new CompoundTag();

				checkpointTag.putDouble("x", this.checkpoint.x());
				checkpointTag.putDouble("y", this.checkpoint.y());
				checkpointTag.putDouble("z", this.checkpoint.z());
				checkpointTag.putFloat("pitch", this.checkpoint.pitch());
				checkpointTag.putFloat("yaw", this.checkpoint.yaw());

				baseTag.put("Checkpoint", checkpointTag);
			}
		}

		baseTag.putBoolean("legitimate", isLegitimate);
		baseTag.putInt("hash", baseTag.hashCode());

		return baseTag;
	}

	@UnknownNullability
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider registryLookup) {
		return savetoNbt(registryLookup, false);
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider registryLookup, CompoundTag nbt) {
		loadFromNbt(nbt);
	}

	public final class PlayerEquipment implements AoAPlayerEventListener {
		private final Object2IntMap<String> cooldowns = new Object2IntOpenHashMap<>();
		private final Map<Holder<ArmorMaterial>, AdventArmourSetContainer> armourMap = new Object2ObjectOpenHashMap<>(4);
		private final EnumMap<AdventArmour.Piece, AdventArmour> equippedArmour = new EnumMap<>(AdventArmour.Piece.class);

		private boolean checkNewArmour = false;

		@Override
		public ListenerType[] getListenerTypes() {
			return new ListenerType[] {
					ListenerType.EQUIPMENT_CHANGE,
					ListenerType.PLAYER_TICK,
					ListenerType.ENTITY_INVULNERABILITY,
					ListenerType.INCOMING_DAMAGE,
					ListenerType.INCOMING_DAMAGE_APPLICATION,
					ListenerType.INCOMING_DAMAGE_AFTER,
					ListenerType.OUTGOING_ATTACK,
					ListenerType.OUTGOING_ATTACK_AFTER,
					ListenerType.PLAYER_DEATH
			};
		}

		public boolean isCooledDown(String counter) {
			return !cooldowns.containsKey(counter);
		}

		public int getCooldown(String counter) {
			return cooldowns.getOrDefault(counter, 0);
		}

		public void setCooldown(String counter, int cooldownTicks) {
			cooldowns.put(counter, cooldownTicks);
		}

		@Nullable
		public Holder<ArmorMaterial> getCurrentFullArmourSet() {
			AdventArmour setArmour = this.equippedArmour.get(AdventArmour.Piece.FULL_SET);

			return setArmour != null ? setArmour.getMaterial() : null;
		}

		@Override
		public void handleEntityInvulnerability(final EntityInvulnerabilityCheckEvent ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.checkDamageInvulnerability(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handleIncomingDamage(final LivingIncomingDamageEvent ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.handleIncomingDamage(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handleOutgoingAttack(final LivingIncomingDamageEvent ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.handleOutgoingAttack(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handlePreDamageApplication(LivingDamageEvent.Pre ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.beforeTakingDamage(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handleAfterDamaged(final LivingDamageEvent.Post ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.afterTakingDamage(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handleAfterAttacking(final LivingDamageEvent.Post ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.afterOutgoingAttack(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handlePlayerDeath(final LivingDeathEvent ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.onEntityDeath(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handleArmourChange(final LivingEquipmentChangeEvent ev) {
			if (ev.getSlot().isArmor())
				this.checkNewArmour = true;
		}

		@Override
		public void handleEffectApplicability(MobEffectEvent.Applicable ev) {
			for (AdventArmourSetContainer wrapper : armourMap.values()) {
				wrapper.armour.onEffectApplication(player, wrapper.equippedPieces(), ev);
			}
		}

		@Override
		public void handlePlayerTick(final PlayerTickEvent.Pre ev) {
			if (!player.isAlive() || player.isSpectator())
				return;

			this.cooldowns.object2IntEntrySet().removeIf(entry -> entry.setValue(entry.getIntValue() - 1) <= 1);

			if (this.checkNewArmour)
				checkNewArmour();

			if (!checkEquippedItems())
				return;

			for (AdventArmourSetContainer container : this.armourMap.values()) {
				container.armour().onArmourTick(player, container.equippedPieces());
			}
		}

		private void checkNewArmour() {
			boolean armourChanged = false;

			for (AdventArmour.Piece piece : AdventArmour.Piece.values()) {
				EquipmentSlot slot = piece.toVanillaSlot();

				if (slot == null)
					continue;

				AdventArmour existingArmour = this.equippedArmour.get(piece);
				Item wornItem = player.getItemBySlot(slot).getItem();

				if (existingArmour == wornItem)
					continue;

				armourChanged = true;

				if (existingArmour != null)
					unequipAdventArmour(existingArmour, piece);

				AdventArmour newArmour = wornItem instanceof AdventArmour adventArmour ? adventArmour : null;

				this.equippedArmour.put(piece, newArmour);

				if (newArmour != null && (player.getAbilities().instabuild || AoASkillReqReloadListener.canEquip(ServerPlayerDataManager.this, newArmour, false)))
					equipAdventArmour(newArmour, piece);
			}

			if (armourChanged) {
				AdventArmour oldSet = this.equippedArmour.remove(AdventArmour.Piece.FULL_SET);
				AdventArmour newSet = null;

				if (ObjectUtil.allEquivalent((piece1, piece2) -> piece1 != null && piece2 != null && (piece1.getMaterial().is(piece2.getMaterial()) || piece1.isCompatibleWithAnySet() || piece2.isCompatibleWithAnySet()), this.equippedArmour.values().toArray(new AdventArmour[0]))) {
					for (AdventArmour armour : this.equippedArmour.values()) {
						if (!armour.isCompatibleWithAnySet()) {
							newSet = armour;

							break;
						}
					}
				}

				if (newSet != null)
					this.equippedArmour.put(AdventArmour.Piece.FULL_SET, newSet);

				if (newSet != oldSet) {
					if (oldSet != null)
						unequipAdventArmour(oldSet, AdventArmour.Piece.FULL_SET);

					if (newSet != null)
						equipAdventArmour(newSet, AdventArmour.Piece.FULL_SET);
				}
			}
		}

		private boolean checkEquippedItems() {
			if (player.getAbilities().instabuild)
				return true;

			boolean updateInventory = false;
			boolean doArmourTick = true;

			for (InteractionHand hand : InteractionHand.values()) {
				ItemStack heldStack = player.getItemInHand(hand);

				if (!AoASkillReqReloadListener.canEquip(ServerPlayerDataManager.this, heldStack.getItem(), true)) {
					updateInventory = true;

					ItemHandlerHelper.giveItemToPlayer(player, heldStack);
					player.setItemInHand(hand, ItemStack.EMPTY);
				}
			}

			for (AdventArmour.Piece piece : AdventArmour.Piece.values()) {
				if (piece == AdventArmour.Piece.FULL_SET)
					continue;

				AdventArmour armour = this.equippedArmour.get(piece);

				if (armour != null && !AoASkillReqReloadListener.canEquip(ServerPlayerDataManager.this, armour, true)) {
					updateInventory = true;
					doArmourTick = false;
					EquipmentSlot slot = armour.getEquipmentSlot();

					ItemHandlerHelper.giveItemToPlayer(player, player.getItemBySlot(slot));
					player.setItemSlot(slot, ItemStack.EMPTY);
					unequipAdventArmour(armour, piece);
				}
			}

			if (updateInventory)
				player.inventoryMenu.broadcastChanges();

			return doArmourTick;
		}

		private void equipAdventArmour(AdventArmour item, AdventArmour.Piece piece) {
			AdventArmourSetContainer pieceContainer = this.armourMap.computeIfAbsent(item.getMaterial(), material -> new AdventArmourSetContainer(item));

			item.onEquip(player, piece, pieceContainer.equippedPieces);
			pieceContainer.equippedPieces().add(piece);
		}

		private void unequipAdventArmour(AdventArmour item, AdventArmour.Piece piece) {
			AdventArmourSetContainer pieceContainer = this.armourMap.get(item.getMaterial());

			item.onUnequip(player, piece, pieceContainer.equippedPieces());
			pieceContainer.equippedPieces().remove(piece);

			if (pieceContainer.equippedPieces().isEmpty())
				this.armourMap.remove(item.getMaterial());
		}

		private record AdventArmourSetContainer(AdventArmour armour, EnumSet<AdventArmour.Piece> equippedPieces) {
			private AdventArmourSetContainer(AdventArmour armour) {
				this(armour, EnumSet.noneOf(AdventArmour.Piece.class));
			}
		}
	}
}
