package net.tslat.aoa3.event.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.tslat.aoa3.content.entity.misc.HaulingFishingBobberEntity;
import net.tslat.aoa3.content.skill.hauling.HaulingSpawnPool;
import net.tslat.aoa3.event.custom.events.*;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.player.skill.AoASkill;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public final class AoAEvents {
	public static void firePlayerLevelChange(ServerPlayerDataManager playerDataManager, AoASkill.Instance skill, int oldLevel, boolean wasNaturallyChanged) {
		NeoForge.EVENT_BUS.post(new PlayerLevelChangeEvent(playerDataManager, skill, oldLevel, wasNaturallyChanged));
	}

	public static float firePlayerChangeXp(ServerPlayerDataManager playerDataManager, AoASkill.Instance skill, float xpGained, float xpAfterModifiers, boolean wasNaturallyGained) {
		PlayerChangeXpEvent event = NeoForge.EVENT_BUS.post(new PlayerChangeXpEvent(playerDataManager, skill, xpGained, xpAfterModifiers, wasNaturallyGained));

		return event.isCanceled() ? 0 : event.getNewXpGain();
	}

	public static HaulingItemFishedEvent fireHaulingItemFished(Entity hookedEntity, ItemStack rodStack, List<ItemStack> lootList, int baseXp, int rodDamage, HaulingFishingBobberEntity bobber) {
		HaulingItemFishedEvent event = new HaulingItemFishedEvent(hookedEntity, rodStack, lootList, baseXp, rodDamage, bobber);

		NeoForge.EVENT_BUS.post(event);

		return event;
	}

	public static HaulingRodPullEntityEvent fireHaulingRodPullEntity(Player player, ItemStack haulingRod, HaulingFishingBobberEntity bobber, Entity hookedEntity, int rodDamage, float pullStrength) {
		HaulingRodPullEntityEvent event = new HaulingRodPullEntityEvent(player, haulingRod, bobber, hookedEntity, rodDamage, pullStrength);

		NeoForge.EVENT_BUS.post(event);

		return event;
	}

	/**
	 * Returns true if cancelled
	 */
	public static boolean firePlayerCraftingEvent(Player player, ItemStack crafting, Container craftingInventory, ResultContainer outputInventory) {
		return NeoForge.EVENT_BUS.post(new ItemCraftingEvent(player, crafting, craftingInventory, outputInventory)).isCanceled();
	}

	/**
	 * Returns the modified output stack, or {@link ItemStack#EMPTY} if cancelled
	 */
	public static ItemStack firePlayerGrindstoneEvent(Player player, ItemStack result, Container inputSlots) {
		GrindstoneResultEvent event = NeoForge.EVENT_BUS.post(new GrindstoneResultEvent(player, result, inputSlots));

		return event.isCanceled() ? ItemStack.EMPTY : event.getOutput();
	}

	public static void firePlayerRetrieveSmeltedEvent(Player player, ItemStack smelting, Container outputInventory) {
		NeoForge.EVENT_BUS.post(new RetrieveSmeltedItemEvent(player, smelting, outputInventory));
	}

	public static MagicTeleportEvent fireMagicalTeleport(Entity entity, @Nullable Entity teleportSource, @Nullable Entity indirectTeleportSource, Vec3 teleportPosition) {
		MagicTeleportEvent event = new MagicTeleportEvent(entity, teleportSource, indirectTeleportSource, teleportPosition);

		NeoForge.EVENT_BUS.post(event);

		return event;
	}

	public static Optional<Entity> fireCheckHaulingEntitySpawn(@Nullable HaulingSpawnPool haulingPool, @Nullable Entity entity, ServerPlayer player, HaulingFishingBobberEntity bobber) {
		HaulingSpawnEntityEvent event = new HaulingSpawnEntityEvent(haulingPool, entity, player, bobber);

		NeoForge.EVENT_BUS.post(event);

		return event.isCanceled() ? Optional.empty() : Optional.ofNullable(event.getNewEntity());
	}

	public static void firePlayerSkillsLootModification(ServerPlayer player, ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
		NeoForge.EVENT_BUS.post(new PlayerSkillsLootModificationEvent(player, generatedLoot, lootContext));
	}

	public static void fireDynamicAttributeModifiers(LivingEntity entity) {
		NeoForge.EVENT_BUS.post(new ApplyDynamicAttributeModifiersEvent(entity));
	}
}
