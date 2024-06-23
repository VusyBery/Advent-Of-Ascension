package net.tslat.aoa3.content.item.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.item.weapon.gun.BaseGun;

public record GunStats(float damage, int ticksBetweenShots, float recoilModifier, float unholsterTimeModifier) {
    public static final Codec<GunStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("damage").forGetter(GunStats::damage),
            Codec.INT.fieldOf("ticks_between_shots").forGetter(GunStats::ticksBetweenShots),
            Codec.FLOAT.fieldOf("recoil_modifier").forGetter(GunStats::recoilModifier),
            Codec.FLOAT.fieldOf("unholster_time_modifier").forGetter(GunStats::unholsterTimeModifier)
    ).apply(builder, GunStats::new));
    public static final StreamCodec<FriendlyByteBuf, GunStats> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, GunStats::damage,
            ByteBufCodecs.VAR_INT, GunStats::ticksBetweenShots,
            ByteBufCodecs.FLOAT, GunStats::recoilModifier,
            ByteBufCodecs.FLOAT, GunStats::unholsterTimeModifier,
            GunStats::new);

    public static float calculateDefaultUnholsterMod(boolean isThrownWeapon, float baseDamage, int ticksBetweenShots) {
        if (baseDamage <= 0)
            return 0.8f;

        if (isThrownWeapon)
            return 0.5f;

        return 0.8f + 0.17f * Math.min(((20 / (float)ticksBetweenShots) * baseDamage) / 55f, 0.85f);
    }

    public static Item.Properties of(float damage, int ticksBetweenShots, float recoilModifier, float unholsterTimeModifier) {
        return new Item.Properties()
                .component(AoADataComponents.GUN_STATS.get(), new GunStats(damage, ticksBetweenShots, recoilModifier, unholsterTimeModifier))
                .attributes(BaseGun.createGunAttributeModifiers(unholsterTimeModifier));
    }
}
