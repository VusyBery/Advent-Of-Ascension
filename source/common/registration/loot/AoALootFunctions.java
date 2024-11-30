package net.tslat.aoa3.common.registration.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.loottable.function.EnchantSpecific;
import net.tslat.aoa3.content.loottable.function.GrantSkillXp;
import net.tslat.aoa3.content.loottable.function.SetPatchouliBook;

public final class AoALootFunctions {
    public static void init() {}

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<EnchantSpecific>> ENCHANT_SPECIFIC = register("enchant_specific", EnchantSpecific.CODEC);
    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<GrantSkillXp>> GRANT_SKILL_XP = register("grant_skill_xp", GrantSkillXp.CODEC);
    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetPatchouliBook>> SET_PATCHOULI_BOOK = register("set_patchouli_book", SetPatchouliBook.CODEC);

    private static <F extends LootItemFunction> DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<F>> register(String id, MapCodec<F> codec) {
        return AoARegistries.LOOT_FUNCTIONS.register(id, () -> new LootItemFunctionType<>(codec));
    }
}
