package net.tslat.aoa3.event.custom.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.tslat.aoa3.content.entity.misc.HaulingFishingBobberEntity;
import net.tslat.aoa3.content.skill.hauling.HaulingSpawnPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Called when a {@link HaulingFishingBobberEntity} is spawning an entity to be reeled in by a player using a Hauling rod.
 * <p>
 * Use this event to modify/replace the entity spawned prior to its position being set and added to the world.
 * <p>
 * This event is {@link ICancellableEvent cancellable}
 */
public class HaulingSpawnEntityEvent extends PlayerEvent implements ICancellableEvent {
    private final HaulingFishingBobberEntity bobber;
    @Nullable
    private final HaulingSpawnPool spawnPool;
    @Nullable
    private final Entity originalEntity;
    @Nullable
    private Entity newEntity;

    public HaulingSpawnEntityEvent(@Nullable HaulingSpawnPool spawnPool, @Nullable Entity originalEntity, ServerPlayer player, HaulingFishingBobberEntity bobber) {
        super(player);

        this.spawnPool = spawnPool;
        this.originalEntity = this.newEntity = originalEntity;
        this.bobber = bobber;
    }

    /**
     * When using a Hauling rod, the bobber looks up what spawn pools are available for the current location and context.
     * <p>
     * If no spawn pool is available for that context, normally nothing would spawn, and this would indicate a normally incompatible location for Hauling (either intentional or unintentional)
     *
     * @return A spawn pool if one was found for the given context, otherwise null
     */
    @Nullable
    public HaulingSpawnPool getSpawnPool() {
        return this.spawnPool;
    }

    /**
     * @return The original entity to be spawned
     */
    @Nullable
    public Entity getOriginalEntity() {
        return this.originalEntity;
    }

    /**
     * @return The event-modified entity to be spawned
     */
    @Nullable
    public Entity getNewEntity() {
        return this.newEntity;
    }

    /**
     * @return The player using the Hauling rod
     */
    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer)super.getEntity();
    }

    /**
     * @return The Hauling rod ItemStack that the bobber belongs to
     */
    public ItemStack getHaulingRod() {
        return this.bobber.getRod();
    }

    /**
     * Set the new entity to spawn for the Hauling spawn attempt, instead of the original one
     */
    public void setNewEntity(@NotNull final Entity newEntity) {
        this.newEntity = Objects.requireNonNull(newEntity);
    }
}
