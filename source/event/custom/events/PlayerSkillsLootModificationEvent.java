package net.tslat.aoa3.event.custom.events;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Called when a loot table is being rolled.
 * <p>
 * Traditionally loot modification should be done through {@link net.neoforged.neoforge.common.loot.IGlobalLootModifier loot modifiers},
 * but due to the AoA skill/ability system's datapack-driven content, this is not practical.<br>
 * This event should only be used by AoA skills/abilities as needed to modify loot, and is only called when a {@link ServerPlayer} is found to be the instigator of the loot.
 * <p>
 * This event is <b><u>NOT</u></b> cancellable
 */
public class PlayerSkillsLootModificationEvent extends PlayerEvent {
    private final ObjectArrayList<ItemStack> generatedLoot;
    private final LootContext lootContext;

    public PlayerSkillsLootModificationEvent(ServerPlayer player, ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        super(player);

        this.generatedLoot = generatedLoot;
        this.lootContext = lootContext;
    }

    /**
     * Get the player instigator of this loot table roll
     */
    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer)super.getEntity();
    }

    /**
     * Get the loot that has been generated from rolling the loot table.
     * <p>
     * This list is the one that should be modified to use this event
     */
    public ObjectArrayList<ItemStack> getGeneratedLoot() {
        return this.generatedLoot;
    }

    /**
     * Get the LootContext for the current table roll
     */
    public LootContext getLootContext() {
        return this.lootContext;
    }
}
