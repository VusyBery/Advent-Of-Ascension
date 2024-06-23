package net.tslat.aoa3.common.registration.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.loottable.modifier.*;

public final class AoALootModifiers {
	public static void init() {}

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RollExtraTablesLootModifier>> ROLL_EXTRA_TABLES = registerSerializer("roll_extra_tables", RollExtraTablesLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AddItemsLootModifier>> ADD_ITEMS = registerSerializer("add_items", AddItemsLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceItemsLootModifier>> REPLACE_ITEMS = registerSerializer("replace_items", ReplaceItemsLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<LootModifyingItemLootModifier>> LOOT_MODIFYING_ITEM = registerSerializer("loot_modifying_items", LootModifyingItemLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<PlayerEventListenerLootModifier>> PLAYER_EVENT_LISTENER = registerSerializer("player_event_listener", PlayerEventListenerLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<HavenLootModifier>> HAVEN = registerSerializer("haven", HavenLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FertilisedFarmlandLootModifier>> FERTILISED_FARMLAND = registerSerializer("fertilised_farmland", FertilisedFarmlandLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RollEntityWorldTableLootModifier>> ROLL_ENTITY_WORLD_TABLE = registerSerializer("roll_entity_world_table", RollEntityWorldTableLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ExplosiveIdolBarteringModifier>> EXPLOSIVE_IDOL_BARTERING = registerSerializer("explosive_idol_bartering", ExplosiveIdolBarteringModifier.CODEC);

	private static <T extends LootModifier> DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<T>> registerSerializer(String id, MapCodec<T> codec) {
		return AoARegistries.LOOT_MODIFIERS.register(id, () -> codec);
	}
}
