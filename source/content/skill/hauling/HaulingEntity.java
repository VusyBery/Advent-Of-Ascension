package net.tslat.aoa3.content.skill.hauling;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.library.object.RandomEntryPool;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.Optional;
import java.util.function.BiFunction;

public record HaulingEntity(Either<ItemStack, EntityType<?>> object, Optional<CompoundTag> spawnData, int weight, int levelReq, float luckMod) implements BiFunction<Level, Boolean, Entity> {
    public static Codec<HaulingEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.mapEither(ItemStack.STRICT_CODEC.fieldOf("item"), BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")).fieldOf("object_id").forGetter(entry -> entry.object),
            CompoundTag.CODEC.optionalFieldOf("spawn_data").forGetter(entry -> entry.spawnData),
            Codec.INT.fieldOf("weight").forGetter(entry -> entry.weight),
            Codec.INT.optionalFieldOf("hauling_level_req", 0).forGetter(entry -> entry.levelReq),
            Codec.FLOAT.optionalFieldOf("luck_mod", 0f).forGetter(entry -> entry.luckMod)
    ).apply(instance, HaulingEntity::new));

    public RandomEntryPool.PoolEntry<HaulingEntity, ServerPlayer> toPoolEntry() {
        return new RandomEntryPool.PoolEntry<>(this, weight(), luckMod(), pl -> weight() <= 0 || PlayerUtil.doesPlayerHaveLevel(pl, AoASkills.HAULING.get(), levelReq()));
    }

    @Override
    public Entity apply(Level level, Boolean isInLava) {
        return object().map(stack -> {
            ItemEntity entity = new ItemEntity(EntityType.ITEM, level) {
                @Override
                public boolean fireImmune() {
                    return isInLava || super.fireImmune();
                }
            };

            entity.setItem(stack.copy());
            entity.setNeverPickUp();

            return entity;
        }, entityType -> {
            Entity entity = entityType.create(level);

            spawnData().ifPresent(entity::load);

            return entity;
        });
    }
}
