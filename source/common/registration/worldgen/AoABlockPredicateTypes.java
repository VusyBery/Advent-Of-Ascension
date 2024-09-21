package net.tslat.aoa3.common.registration.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.world.gen.blockpredicate.CheckYRange;

public final class AoABlockPredicateTypes {
	public static void init() {}

	public static final DeferredHolder<BlockPredicateType<?>, BlockPredicateType<CheckYRange>> CHECK_Y_RANGE = register("check_y_range", CheckYRange.CODEC);

	private static <T extends BlockPredicate> DeferredHolder<BlockPredicateType<?>, BlockPredicateType<T>> register(String id, MapCodec<T> codec) {
		return AoARegistries.BLOCK_PREDICATE_TYPE.register(id, () -> () -> codec);
	}
}
