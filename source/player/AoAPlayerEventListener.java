package net.tslat.aoa3.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.tslat.aoa3.client.player.AoAPlayerKeybindListener;
import net.tslat.aoa3.event.custom.events.*;
import net.tslat.aoa3.library.constant.ScreenImageEffect;
import net.tslat.aoa3.util.ColourUtil;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public interface AoAPlayerEventListener {
	/**
	 * Used to determine which events your listener should be listening for.
	 * <p>
	 * Override and return whichever {@link ListenerType} types you want to handle
	 *
	 * @return an array of {@link ListenerType} types this listener should receive events for
	 */
	default ListenerType[] getListenerTypes() {
		return new ListenerType[] {};
	}

	/**
	 * Used to determine whether the listener should be receiving event calls.
	 * <p>
	 * Override to handle enabling/disabling as needed.
	 *
	 * @return whether the listener is currently active.
	 */
	default ListenerState getListenerState() {
		return ListenerState.ACTIVE;
	}

	/**
	 * Used to determine whether the listener meets the requirements relevant to stay active.
	 * <p>
	 * This check is only made if the listener is currently active, and returning false will call for the listener to be disabled.
	 *
	 * @return whether the listener currently meets any requirements necessary to stay active.
	 */
	default boolean meetsRequirements() {
		return true;
	}

	/**
	 * Called when your listener is enabled from a previously disabled state.
	 * <p>
	 * Usually this occurs when the player manually re-enables it, or if AoA determines the listener now meets previously un-met conditions
	 */
	default void reenable(boolean isInit) {}

	/**
	 * Called when the listener is disabled regardless of the source. Usually called when the listener returns false from {@link #meetsRequirements()}.
	 * <p>
	 * If your listener isn't able to be disabled, then leave this method as default.
	 */
	default void disable(ListenerState reason, boolean isInit) {}

	/**
	 * This method gets triggered every tick as the player is being ticked.
	 * <p>
	 * Override to handle data changes or trigger effects that may need to occur at a given tick or each tick for the player.
	 * <p>
	 * NOTE: This method will be called regardless of tick phase. Filter out the tick phase you don't intend to use when overriding.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_TICK} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerTickEvent.Pre} event
	 */
	default void handlePlayerTick(final PlayerTickEvent.Pre ev) {}

	/**
	 * This method gets triggered when the player jumps.
	 * <p>
	 * Override to trigger effects that may need to occur when a player jumps.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_JUMP} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingEvent.LivingJumpEvent} event
	 */
	default void handlePlayerJump(final LivingEvent.LivingJumpEvent ev) {}

	/**
	 * This method gets triggered when the player lands from a fall.
	 * <p>
	 * Override to trigger effects that may need to occur when a player lands from a fall.
	 * <p>
	 * NOTE: This event can also be used to mitigate/negate fall damage, instead of using one of the attack methods.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_FALL} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingFallEvent} event
	 */
	default void handlePlayerFall(final LivingFallEvent ev) {}

	/**
	 * This method gets triggered when the player dies.
	 * <p>
	 * Override to handle data changes or effects that may need to occur when a player dies.
	 * <p>
	 * NOTE: While this event is cancellable (and will technically stop the player dying), it is recommended to not use this event to prevent death, and instead handling a damage event.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_DEATH} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingDeathEvent} event
	 */
	default void handlePlayerDeath(final LivingDeathEvent ev) {}

	/**
	 * This method gets triggered when the player respawns.
	 * <p>
	 * Override to handle data changes or effects that may need to occur when a player respawns.
	 * <p>
	 * NOTE: If a player chooses not to respawn until after a full server restart has occured, game data may not be in the state expected. Only trust serialized data and data manager data.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_RESPAWN} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.PlayerRespawnEvent} event
	 */
	default void handlePlayerRespawn(final PlayerEvent.PlayerRespawnEvent ev) {}

	/**
	 * This method gets triggered when the player logs in.
	 * <p>
	 * Override to handle data changes or effects that may need to occur when a player logs in.
	 * <p>
	 * At the stage this method is called, the player technically exists ingame, but is still shortly before the client's screen has loaded the world, so any short-lived effects may not be visible.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_LOGIN} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.PlayerLoggedInEvent} event
	 */
	default void handlePlayerLogin(final PlayerEvent.PlayerLoggedInEvent ev) {}

	/**
	 * This method gets triggered when the player logs out.
	 * <p>
	 * Override to handle data changes that may need to occur when a player logs out.
	 * <p>
	 * The player will not see any effects triggered at this stage, so this event should be used for data changes only.
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_LOGOUT} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.PlayerLoggedOutEvent} event
	 */
	default void handlePlayerLogout(final PlayerEvent.PlayerLoggedOutEvent ev) {}

	/**
	 * This method gets triggered when the player is being cloned to a new entity state.
	 * <p>
	 * Override to handle data changes that may need to occur when a player's data is cloned to a new state, usually through death or end-portal travel.
	 * <p>
	 * NOTE: This should not be used to trigger any effects or actions, and should only be used for transferring required data between old and new data states
	 * <p>
	 * Will only trigger if {@link ListenerType#PLAYER_CLONE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.Clone} event
	 */
	default void handlePlayerDataClone(final PlayerEvent.Clone ev) {}

	/**
	 * This method gets triggered when the player equips, unequips, or swaps out armour.
	 * <p>
	 * Override to handle data changes or trigger effects that may need to occur when the player changes armour.
	 * <p>
	 * Will only trigger if {@link ListenerType#EQUIPMENT_CHANGE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingEquipmentChangeEvent} event
	 */
	default void handleArmourChange(final LivingEquipmentChangeEvent ev) {}

	/**
	 * This method gets triggered when the player changes dimension.
	 * <p>
	 * Override to trigger effects for when a player changes dimension.
	 * <p>
	 * NOTE: At this stage, the player has already moved to the new dimension.
	 * <p>
	 * Will only trigger if {@link ListenerType#DIMENSION_CHANGE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.PlayerChangedDimensionEvent} event
	 */
	default void handleDimensionChange(final PlayerEvent.PlayerChangedDimensionEvent ev) {}

	/**
	 * This method gets triggered when the player changes gamemode.
	 * <p>
	 * Override to trigger effects for when a player has their gamemode changed.
	 * <p>
	 * Will only trigger if {@link ListenerType#GAMEMODE_CHANGE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.PlayerChangeGameModeEvent} event
	 */
	default void handleGamemodeChange(final PlayerEvent.PlayerChangeGameModeEvent ev) {}

	/**
	 * This method gets triggered when the player attempts to harvest a block in the world.
	 * <p>
	 * Override to oversee or modify the viability of a player harvesting a block.
	 * <p>
	 * NOTE: This event is intended to be used to determine additional requirements for obtaining a block's drops on break, but it is also used to determine break speed (E.G. pickaxe on stone).
	 * <p>
	 * Will only trigger if {@link ListenerType#BLOCK_HARVEST_ATTEMPT} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerEvent.HarvestCheck} event
	 */
	default void handleBlockHarvestAttempt(final PlayerEvent.HarvestCheck ev) {}

	/**
	 * This method gets triggered when the player starts to harvest a block in the world.
	 * <p>
	 * Override to oversee or modify the speed at which a player will break a given block.
	 * <p>
	 * Will only trigger if {@link ListenerType#BLOCK_BREAK_SPEED} is included in the returned event listener types in {@link #getListenerTypes}
	 * <p>
	 * This handler is called on both the client and the server indiscriminately.
	 *
	 * @param ev {@link PlayerEvent.BreakSpeed} event
	 */
	default void handleHarvestSpeedCheck(final PlayerEvent.BreakSpeed ev) {}

	/**
	 * This method gets triggered when the player breaks a block in the world.
	 * <p>
	 * Override to trigger effects for when a player breaks a block.
	 * <p>
	 * NOTE: This may not be triggered by modded effects or abilities.
	 * <p>
	 * Will only trigger if {@link ListenerType#BLOCK_BREAK} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link BlockEvent.BreakEvent} event
	 */
	default void handleBlockBreak(final BlockEvent.BreakEvent ev) {}

	/**
	 * This method gets triggered when the player places a block in the world.
	 * <p>
	 * Override to trigger effects for when a player places a block.
	 * <p>
	 * NOTE: This may not be triggered by modded effects or abilities.
	 * <p>
	 * Will only trigger if {@link ListenerType#BLOCK_PLACE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link BlockEvent.EntityPlaceEvent} event
	 */
	default void handleBlockPlacement(final BlockEvent.EntityPlaceEvent ev) {}

	/**
	 * This methods gets triggered when the player right clicks a block in the world.
	 * <p>
	 * Override to prevent interaction effects or trigger new ones.
	 * <p>
	 * Will only trigger if {@link ListenerType#BLOCK_INTERACT} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerInteractEvent.RightClickBlock} event
	 */
	default void handleBlockInteraction(final PlayerInteractEvent.RightClickBlock ev) {}

	/**
	 * This method gets triggered when the player throws out an item from their inventory or hand.
	 * <p>
	 * Override to trigger effects for when a player throws out an item.
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_THROW} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link ItemTossEvent} event
	 */
	default void handleItemToss(final ItemTossEvent ev) {}

	/**
	 * This method gets triggered when the player's level changes in any AoA skill.
	 * <p>
	 * Override to trigger effects for when a player's level changes an AoA skill.
	 * <p>
	 * NOTE: This method does not discriminate between skills. Do your own checks if required.
	 * <p>
	 * Will only trigger if {@link ListenerType#LEVEL_CHANGE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerLevelChangeEvent} event
	 */
	default void handleLevelChange(final PlayerLevelChangeEvent ev) {}

	/**
	 * This method gets triggered when the player's xp in any given skill is is changed.
	 * <p>
	 * Override to trigger effects for when a player is granted or loses AoA skill xp.
	 * <p>
	 * NOTE: This method does not discriminate between skills. Do your own checks if required.
	 * <p>
	 * Will only trigger if {@link ListenerType#GAIN_SKILL_XP} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerChangeXpEvent} event
	 */
	default void handleSkillXpGain(final PlayerChangeXpEvent ev) {}

	/**
	 * This method gets triggered when the player is granted xp via the {@link Player#giveExperiencePoints} method.
	 * <p>
	 * Override to trigger effects for when a player is granted vanilla xp.
	 * <p>
	 * Will only trigger if {@link ListenerType#GAIN_VANILLA_XP} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link PlayerXpEvent.XpChange} event
	 */
	default void handleVanillaXpGain(final PlayerXpEvent.XpChange ev) {}

	/**
	 * This method gets triggered when the player fulfils the criteria for a crafting recipe.
	 * <p>
	 * Override to modify the output stack set into the output slot. The crafting operation is not considered final at this stage, hook {@link #handleItemCrafted} if that is your requirement
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_CRAFTING} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link ItemCraftingEvent} event
	 */
	default void handleItemCrafting(final ItemCraftingEvent ev) {}

	/**
	 * This method gets triggered when the player takes an item out of a crafting device.
	 * <p>
	 * Override to trigger effects when a player has retrieved an item from a crafting table or crafting device. The crafted stack should not be modified here, hook {@link #handleItemCrafting} if that is your requirement
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_CRAFTED} is included in the returned event listener types in {@link #getListenerTypes}
	 * <p>
	 * This handler is called on both the client and the server indiscriminately.
	 *
	 * @param ev {@link PlayerEvent.ItemCraftedEvent} event
	 */
	default void handleItemCrafted(final PlayerEvent.ItemCraftedEvent ev) {}

	/**
	 * This method gets triggered when the player attempts to retrieve an item from a furnace, smoker, or blast furnace.
	 * <p>
	 * Override to modify the output stack set into the output slot. The smelting operation is not considered final at this stage, hook {@link #handleItemCrafted} if that is your requirement
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_SMELTING} is included in the returned event listener types in {@link #getListenerTypes}
	 * <p>
	 * This handler is called on both the client and the server indiscriminately.
	 *
	 * @param ev {@link RetrieveSmeltedItemEvent} event
	 */
	default void handleItemSmelting(final RetrieveSmeltedItemEvent ev) {}

	/**
	 * This method gets triggered when the player takes an item out of a smelting device or furnace.
	 * <p>
	 * Override to trigger effects when a player has retrieved an item from a furnace or smelting device. The smelted stack should not be modified here, hook {@link #handleItemSmelting} if that is your requirement
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_SMELTED} is included in the returned event listener types in {@link #getListenerTypes}
	 * <p>
	 * This handler is called on both the client and the server indiscriminately.
	 *
	 * @param ev {@link PlayerEvent.ItemSmeltedEvent} event
	 */
	default void handleItemSmelted(final PlayerEvent.ItemSmeltedEvent ev) {}

	/**
	 * This method gets triggered when the player attempts to grind an item on a grindstone.
	 * <p>
	 * Override to modify the output stack set into the output slot. The grinding operation is not considered final at this stage.
	 * <p>
	 * Will only trigger if {@link ListenerType#ITEM_GRINDSTONING} is included in the returned event listener types in {@link #getListenerTypes}
	 * <p>
	 * This handler is called on both the client and the server indiscriminately.
	 *
	 * @param ev {@link RetrieveSmeltedItemEvent} event
	 */
	default void handleGrindstoneModifying(final GrindstoneResultEvent ev) {}

	/**
	 * This method gets triggered when the player fishes up an item.
	 * <p>
	 * Override to trigger effects when a player has fished up an item via any applicable means. The resulting loot is not reliably modifiable here.
	 * <p>
	 * Will only trigger if {@link ListenerType#FISHED_ITEM} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link ItemFishedEvent} event
	 */
	default void handleItemFished(final ItemFishedEvent ev, boolean isHauling) {}

	/**
	 * This method gets triggered when the player attempts to reel in a hooked entity with a hauling rod. Does not apply to reeling in fish from fishing.
	 * <p>
	 * Override to trigger effects or modify some values when a player has fished up an item via any applicable means. The resulting loot is not modifiable here.
	 * <p>
	 * Will only trigger if {@link ListenerType#HAULING_ROD_PULL_ENTITY} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link ItemFishedEvent} event
	 */
	default void handleHaulingRodPullEntity(final HaulingRodPullEntityEvent ev) {}

	/**
	 * This method gets triggered when the player interacts with any entity.
	 * <p>
	 * Override to trigger effects or modify the interaction
	 * <p>
	 * Will only trigger if {@link AoAPlayerEventListener.ListenerType#ENTITY_INTERACT} is included in the returned event listener types in {@link #getListenerTypes()}
	 *
	 * @param ev {@link net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific} event
	 */
	default void handleEntityInteraction(final PlayerInteractEvent.EntityInteractSpecific ev) {}

	/**
	 * This method gets triggered when the player has a potion effect applied to them. It is also triggered if an existing effect is upgraded or extended.
	 * <p>
	 * Override to trigger effects for when a player is afflicted with a potion effect of any kind.
	 * <p>
	 * Will only trigger if {@link ListenerType#POTION_APPLIED} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link MobEffectEvent.Added} event
	 */
	default void handleAppliedPotion(final MobEffectEvent.Added ev) {}

	/**
	 * This method gets triggered when player attribute modifiers should be applied. Usually this is on login and during a clone of the player's data.
	 * <p>
	 * Consider this a trigger point for safely applying any attribute modifiers your listener may implement.
	 * <p>
	 * Will only trigger if {@link ListenerType#ATTRIBUTE_MODIFIERS} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param plData {@link ServerPlayerDataManager} player Advent data container
	 */
	default void applyAttributeModifiers(final ServerPlayerDataManager plData) {}

	/**
	 * This method gets triggered in most circumstances when a player's attribute modifiers should be removed. Usually this is when a listener is disabled or removed, or if the player no longer meets the requirements for the listener.
	 * <p>
	 * Consider this a trigger point for safely removing any attribute modifiers your listener may implement.
	 * <p>
	 * Will only trigger if {@link ListenerType#ATTRIBUTE_MODIFIERS} is included in the retuned event listener types in {@link #getListenerTypes}
	 *
	 * @param plData {@link ServerPlayerDataManager} player Advent data container
	 */
	default void removeAttributeModifiers(final ServerPlayerDataManager plData) {}

	/**
	 * This method gets triggered when a player is about to critically strike a target. This is called before the damage is done, but after any other modifications have been made.
	 * <p>
	 * Override to trigger effects or modify the damage modifier the critical hit will apply in the coming attack.
	 * <p>
	 * Will only trigger if {@link ListenerType#CRITICAL_HIT} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link CriticalHitEvent} event
	 */
	default void handleCriticalHit(final CriticalHitEvent ev) {}

	/**
	 * This method gets triggered when a player is being set as a target for an entity.
	 * <p>
	 * Override to trigger effects or cancel the targeting application if required. Note that you cannot cancel this targeting event, you must set the targeter's target to null manually to do so.
	 * <p>
	 * Will only trigger if {@link ListenerType#ENTITY_TARGET} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingChangeTargetEvent} event
	 */
	default void handleEntityTarget(final LivingChangeTargetEvent ev) {}

	/**
	 * This method gets triggered when the player is about to be attacked by another entity.
	 * <p>
	 * Override this method to trigger effects that may occur when being attacked by another entity, regardless of whether the attack is successful or not.
	 * <p>
	 * Will only trigger if {@link ListenerType#INCOMING_ATTACK_BEFORE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingAttackEvent} event
	 */
	default void handlePreIncomingAttack(final LivingAttackEvent ev) {}

	/**
	 * This method gets triggered when the player is about to attack another entity.
	 * <p>
	 * Override this method to trigger effects that may occur when attacking another entity, regardless of whether the attack is successful or not.
	 * <p>
	 * Will only trigger if {@link ListenerType#OUTGOING_ATTACK_BEFORE} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingAttackEvent} event
	 */
	default void handlePreOutgoingAttack(final LivingAttackEvent ev) {}

	/**
	 * This method gets triggered when the player is being attacked by another entity.
	 * <p>
	 * Override this method to modify the damage to be used for the event. The damage value of the attack provided here is prior to any armour, enchantment, or potion modifications take place.
	 * <p>
	 * Will only trigger if {@link ListenerType#INCOMING_ATTACK_DURING} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingHurtEvent} event
	 */
	default void handleIncomingAttack(final LivingHurtEvent ev) {}

	/**
	 * This method gets triggered when the player is attacking another entity.
	 * <p>
	 * Override this method to modify the damage to be used for the event. The damage value of the attack provided here is prior to any armour, enchantment, or potion modifications take place.
	 * <p>
	 * Will only trigger if {@link ListenerType#OUTGOING_ATTACK_DURING} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingHurtEvent} event
	 */
	default void handleOutgoingAttack(final LivingHurtEvent ev) {}

	/**
	 * This method gets triggered when the player has been successfully attacked by another entity.
	 * <p>
	 * Override this method to handle damage-triggered effects. The damage value of the attack is final here, use one of the earlier attack events to modify damage.
	 * <p>
	 * Will only trigger if {@link ListenerType#INCOMING_ATTACK_AFTER} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingDamageEvent} event
	 */
	default void handlePostIncomingAttack(final LivingDamageEvent ev) {}

	/**
	 * This method gets triggered when the player has attacked an entity successfully.
	 * <p>
	 * Override this method to handle attack-triggered effects. The damage value of the attack is final here, use one of the earlier attack events to modify damage.
	 * <p>
	 * Will only trigger if {@link ListenerType#OUTGOING_ATTACK_AFTER} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingDamageEvent} event
	 */
	default void handlePostOutgoingAttack(final LivingDamageEvent ev) {}

	/**
	 * This method gets triggered when an entity is killed and the player is determined to have at least partially contributed.
	 * <p>
	 * This method will <b>not</b> be called if the player dies. The {@link ListenerType#PLAYER_DEATH} listener can be used for that situation.
	 * <p>
	 * Override this method to handle actions caused by killing an entity. The entity's death may technically be cancelled here, but it's not recommended.
	 * <p>
	 * Will only trigger if {@link ListenerType#ENTITY_KILL} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link LivingDeathEvent} event
	 */
	default void handleEntityKill(final LivingDeathEvent ev) {}

	/**
	 * This method gets triggered when a baby is about to be spawned by two parent entities breeding.
	 * <p>
	 * Override this method to change or cancel the baby spawn interaction or trigger further effects.
	 * <p>
	 * Will only trigger if {@link ListenerType#ANIMAL_BREED} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param ev {@link BabyEntitySpawnEvent} event
	 */
	default void handleAnimalBreed(final BabyEntitySpawnEvent ev) {}

	/**
	 * This method gets triggered when a loot table is rolled that involves the player in some way.
	 * <p>
	 * Override this method to handle loot modification, either addition, removal, or negation.
	 * <p>
	 * Modify the provided loot list, do not create a new one. The remaining contents of the list will be dropped in the world as normal.
	 * <p>
	 * Will only trigger if {@link ListenerType#LOOT_MODIFICATION} is included in the returned event listener types in {@link #getListenerTypes}
	 *
	 * @param loot the existing list of loot stacks to be dropped.
	 * @param context the context for the loot table roll. Contains all the relevant info for the event itself.
	 */
	default void handleLootModification(final List<ItemStack> loot, LootContext context) {}

	/**
	 * Create an {@link AoAPlayerKeybindListener} instance for this listener, allowing for keybind handling in common code
	 *
	 * @param consumer The consumer to accept the newly created instance
	 */
	default void createKeybindListener(Consumer<AoAPlayerKeybindListener> consumer) {}

	/**
	 * This method gets triggered when the keycode assigned in {@link AoAPlayerKeybindListener#getKeycode()} is pressed.
	 * <p>
	 * It will only be called once per press, so ongoing press effects should be handled manually via the keybind itself.
	 * <p>
	 * NOTE: Currently this is only functional for {@link net.tslat.aoa3.player.ability.AoAAbility} abilities.
	 * <p>
	 * Will only trigger if {@link ListenerType#KEY_INPUT} is included in the returned event listener types in {@link #getListenerTypes()}
	 */
	default void handleKeyInput() {}

	default void activatedActionKey(ServerPlayer player) {
		new ScreenImageEffect(ScreenImageEffect.Type.ACTION_KEY_VIGNETTE).coloured(ColourUtil.makeARGB(ColourUtil.WHITE, 127)).fullscreen(true).duration(10).sendToPlayer(player);
	}

	/**
	 * This method gets triggered when an unknown source calls for a custom interaction for a given player.
	 * <p>
	 * All listeners that include {@link ListenerType#CUSTOM} in the returned event listener types in {@link #getListenerTypes()} will receive this call, regardless of intention.
	 * <p>
	 * Prior to acting on any call, ensure you're receiving the correct interaction via comparing the {@param interactionType} parameter to the expected value.
	 * <p>
	 * NOTE: This should only be used if no other event listeners are capable of supporting the interaction as this is the least efficient interaction handler.
	 *
	 * @param interactionType the "name" of the interaction. Use this to ensure you're interpreting the correct interaction prior to operating on any of the data.
	 * @param data the untyped data provided by the calling function. This can be blind-casted to the correct format assuming you've checked the {@param interactionType} parameter
	 */
	default void handleCustomInteraction(String interactionType, Object data) {}

	enum ListenerState {
		ACTIVE("active"),
		MANUALLY_DISABLED("disabled"),
		DEACTIVATED("deactivated"),
		REGION_LOCKED("region_locked");

		private final String id;

		ListenerState(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

		public static ListenerState fromId(String id) {
			return switch (id) {
				case "disabled" -> MANUALLY_DISABLED;
				case "deactivated" -> DEACTIVATED;
				case "active" -> ACTIVE;
				case "region_locked" -> REGION_LOCKED;
				default -> DEACTIVATED;
			};
		}
	}

	enum ListenerType {
		PLAYER_TICK,
		PLAYER_JUMP,
		PLAYER_FALL,
		PLAYER_DEATH,
		PLAYER_RESPAWN,
		PLAYER_LOGIN,
		PLAYER_LOGOUT,
		PLAYER_CLONE,
		EQUIPMENT_CHANGE,
		DIMENSION_CHANGE,
		GAMEMODE_CHANGE,
		BLOCK_HARVEST_ATTEMPT,
		BLOCK_BREAK_SPEED,
		BLOCK_BREAK,
		BLOCK_PLACE,
		BLOCK_INTERACT,
		ITEM_THROW,
		LEVEL_CHANGE,
		GAIN_SKILL_XP,
		GAIN_VANILLA_XP,
		ITEM_CRAFTING,
		ITEM_CRAFTED,
		ITEM_SMELTING,
		ITEM_SMELTED,
		ITEM_GRINDSTONING,
		ITEM_GRINDSTONED,
		FISHED_ITEM,
		HAULING_ROD_PULL_ENTITY,
		ENTITY_INTERACT,
		POTION_APPLIED,
		LOOT_MODIFICATION,
		ATTRIBUTE_MODIFIERS,
		CRITICAL_HIT,
		ENTITY_TARGET,
		INCOMING_ATTACK_BEFORE,
		INCOMING_ATTACK_DURING,
		INCOMING_ATTACK_AFTER,
		OUTGOING_ATTACK_BEFORE,
		OUTGOING_ATTACK_DURING,
		OUTGOING_ATTACK_AFTER,
		ENTITY_KILL,
		ANIMAL_BREED,
		KEY_INPUT,
		CUSTOM
	}
}
