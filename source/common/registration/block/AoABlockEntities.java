package net.tslat.aoa3.common.registration.block;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.block.blockentity.*;

import java.util.function.Supplier;

public final class AoABlockEntities {
	private static final Multimap<BlockEntityType<?>, Block> VANILLA_BLOCK_ENTITY_BLOCK_REGISTRATIONS = MultimapBuilder.hashKeys().arrayListValues().build();

	public static void init() {
		AdventOfAscension.getModEventBus().addListener(AoABlockEntities::registerVanillaBlockEntityBlocks);
	}

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TrophyBlockEntity>> TROPHY = register("trophy", () -> BlockEntityType.Builder.of(TrophyBlockEntity::new, AoABlocks.TROPHY.get(), AoABlocks.GOLD_TROPHY.get(), AoABlocks.ORNATE_TROPHY.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LunarCreationTableBlockEntity>> LUNAR_CREATION_TABLE = register("lunar_creation_table", () -> BlockEntityType.Builder.of(LunarCreationTableBlockEntity::new, AoABlocks.LUNAR_CREATION_TABLE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusionTableBlockEntity>> INFUSION_TABLE = register("infusion_table", () -> BlockEntityType.Builder.of(InfusionTableBlockEntity::new, AoABlocks.INFUSION_TABLE.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ImbuingChamberBlockEntity>> IMBUING_CHAMBER = register("imbuing_chamber", () -> BlockEntityType.Builder.of(ImbuingChamberBlockEntity::new, AoABlocks.IMBUING_CHAMBER.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BossAltarBlockEntity>> BOSS_ALTAR = register("boss_altar", () -> BlockEntityType.Builder.of(BossAltarBlockEntity::new, AoABlocks.BOSS_ALTAR.get()));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfusedPressBlockEntity>> INFUSED_PRESS = register("infused_press", () -> BlockEntityType.Builder.of(InfusedPressBlockEntity::new, AoABlocks.INFUSED_PRESS.get()));

	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType.Builder<T>> type) {
		return AoARegistries.BLOCK_ENTITIES.register(id, () -> type.get().build(null));
	}

	private static void registerVanillaBlockEntityBlocks(final BlockEntityTypeAddBlocksEvent ev) {
		for (BlockEntityType<?> blockEntity : VANILLA_BLOCK_ENTITY_BLOCK_REGISTRATIONS.keySet()) {
			ev.modify(blockEntity, VANILLA_BLOCK_ENTITY_BLOCK_REGISTRATIONS.get(blockEntity).toArray(Block[]::new));
		}

		VANILLA_BLOCK_ENTITY_BLOCK_REGISTRATIONS.clear();
	}

	public static void addBlockToExistingBlockEntityType(BlockEntityType<?> blockEntityType, Block block) {
		VANILLA_BLOCK_ENTITY_BLOCK_REGISTRATIONS.put(blockEntityType, block);
	}
}
