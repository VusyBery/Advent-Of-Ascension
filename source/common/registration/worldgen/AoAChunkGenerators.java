package net.tslat.aoa3.common.registration.worldgen;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.world.gen.chunkgenerator.EmptyChunkGenerator;

public final class AoAChunkGenerators {
	public static void init() {}

	public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<EmptyChunkGenerator>> EMPTY = register("empty", EmptyChunkGenerator.CODEC);

	private static <T extends ChunkGenerator> DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<T>> register(String id, MapCodec<T> codec) {
		return AoARegistries.CHUNK_GENERATORS.register(id, () -> codec);
	}
}
