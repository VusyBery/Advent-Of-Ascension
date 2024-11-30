package net.tslat.aoa3.common.registration.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.loottable.entry.LootMultiEntry;

public final class AoALootEntryTypes {
    public static void init() {}

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> MULTI = register("multi", LootMultiEntry.CODEC);

    private static DeferredHolder<LootPoolEntryType, LootPoolEntryType> register(String id, MapCodec<? extends LootPoolEntryContainer> codec) {
        return AoARegistries.LOOT_ENTRY_TYPES.register(id, () -> new LootPoolEntryType(codec));
    }
}
