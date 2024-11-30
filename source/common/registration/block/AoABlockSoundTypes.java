package net.tslat.aoa3.common.registration.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

import java.util.Optional;
import java.util.function.Supplier;

public final class AoABlockSoundTypes {
    public static final DeferredSoundType SCORIA = Builder.basedOn(SoundType.DEEPSLATE).placeSound(SoundEvents.DEEPSLATE_TILES_PLACE).build();

    private static class Builder {
        private final SoundType base;
        private float volume = 1;
        private float pitch = 1;
        private Optional<Supplier<SoundEvent>> breakSound = Optional.empty();
        private Optional<Supplier<SoundEvent>> stepSound = Optional.empty();
        private Optional<Supplier<SoundEvent>> placeSound = Optional.empty();
        private Optional<Supplier<SoundEvent>> hitSound = Optional.empty();
        private Optional<Supplier<SoundEvent>> fallSound = Optional.empty();

        Builder(final SoundType base) {
            this.base = base;
        }

        static Builder basedOn(SoundType base) {
            return new Builder(base);
        }

        Builder volume(float volume) {
            this.volume = volume;

            return this;
        }

        Builder pitch(float pitch) {
            this.pitch = pitch;

            return this;
        }

        Builder breakSound(Supplier<SoundEvent> breakSound) {
            this.breakSound = Optional.of(breakSound);

            return this;
        }

        Builder breakSound(SoundEvent breakSound) {
            this.breakSound = Optional.of(() -> breakSound);

            return this;
        }

        Builder stepSound(Supplier<SoundEvent> stepSound) {
            this.stepSound = Optional.of(stepSound);

            return this;
        }

        Builder stepSound(SoundEvent stepSound) {
            this.stepSound = Optional.of(() -> stepSound);

            return this;
        }

        Builder placeSound(Supplier<SoundEvent> placeSound) {
            this.placeSound = Optional.of(placeSound);

            return this;
        }

        Builder placeSound(SoundEvent placeSound) {
            this.placeSound = Optional.of(() -> placeSound);

            return this;
        }

        Builder hitSound(Supplier<SoundEvent> hitSound) {
            this.hitSound = Optional.of(hitSound);

            return this;
        }

        Builder hitSound(SoundEvent hitSound) {
            this.hitSound = Optional.of(() -> hitSound);

            return this;
        }

        Builder fallSound(Supplier<SoundEvent> fallSound) {
            this.fallSound = Optional.of(fallSound);

            return this;
        }

        Builder fallSound(SoundEvent fallSound) {
            this.fallSound = Optional.of(() -> fallSound);

            return this;
        }

        DeferredSoundType build() {
            return new DeferredSoundType(
                    this.volume,
                    this.pitch,
                    this.breakSound.orElse(this.base::getBreakSound),
                    this.stepSound.orElse(this.base::getStepSound),
                    this.placeSound.orElse(this.base::getPlaceSound),
                    this.hitSound.orElse(this.base::getHitSound),
                    this.fallSound.orElse(this.base::getFallSound));
        }
    }
}
