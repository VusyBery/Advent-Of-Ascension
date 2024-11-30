package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.item.weapon.staff.BaseStaff;

public record StaffRuneCost(Object2IntMap<Item> runeCosts) {
    public static final Codec<StaffRuneCost> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.simpleMap(AoARegistries.ITEMS.lookupCodec(), Codec.INT, AoARegistries.ITEMS).<Object2IntMap<Item>>xmap(Object2IntArrayMap::new, map -> map).fieldOf("runes").forGetter(StaffRuneCost::runeCosts)
    ).apply(builder, StaffRuneCost::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, StaffRuneCost> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(Object2IntArrayMap::new, ByteBufCodecs.registry(Registries.ITEM), ByteBufCodecs.VAR_INT), StaffRuneCost::runeCosts,
            StaffRuneCost::new);

    public static Item.Properties of(Object2IntMap<Item> runeCosts) {
        return new Item.Properties().component(AoADataComponents.STAFF_RUNE_COST, new StaffRuneCost(runeCosts)).component(AoADataComponents.STORED_SPELL_CASTS, BaseStaff.StoredCasts.DISABLED);
    }
}
