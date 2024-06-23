package net.tslat.aoa3.content.enchantment.valueeffect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public record Clamp(EnchantmentValueEffect value, LevelBasedValue min, LevelBasedValue max) implements EnchantmentValueEffect {
    public static final MapCodec<Clamp> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    EnchantmentValueEffect.CODEC.fieldOf("value").forGetter(Clamp::value),
                    LevelBasedValue.CODEC.fieldOf("min").forGetter(Clamp::min),
                    LevelBasedValue.CODEC.fieldOf("max").forGetter(Clamp::max))
            .apply(builder, Clamp::new));

    @Override
    public float process(int enchantLevel, RandomSource random, float value) {
        final float min = this.min.calculate(enchantLevel);
        final float max = this.max.calculate(enchantLevel);

        return Mth.clamp(this.value.process(enchantLevel, random, value), min, max);
    }

    @Override
    public MapCodec<Clamp> codec() {
        return CODEC;
    }
}
