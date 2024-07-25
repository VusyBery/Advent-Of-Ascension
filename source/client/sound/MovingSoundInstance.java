package net.tslat.aoa3.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class MovingSoundInstance extends AbstractTickableSoundInstance {
    private final Vec3 velocity;

    public MovingSoundInstance(SoundEvent sound, SoundSource source, Vec3 velocity, float volume, float pitch, RandomSource random, double x, double y, double z) {
        this(sound, source, velocity, volume, pitch, random, false, 0, Attenuation.LINEAR, x, y, z);
    }

    public MovingSoundInstance(SoundEvent sound, SoundSource source, Vec3 velocity, float volume, float pitch, RandomSource random, boolean looping, int delay, Attenuation attenuation, double x, double y, double z) {
        this(sound, source, velocity, volume, pitch, random, looping, delay, attenuation, x, y, z, false);
    }

    public MovingSoundInstance(SoundEvent sound, SoundSource source, Vec3 velocity, float volume, float pitch, RandomSource random, boolean looping, int delay, Attenuation attenuation, double x, double y, double z, boolean relative) {
        super(sound, source, random);

        this.velocity = velocity;
        this.volume = volume;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.looping = looping;
        this.delay = delay;
        this.attenuation = attenuation;
        this.relative = relative;
    }

    @Override
    public void tick() {
        this.x += this.velocity.x;
        this.y += this.velocity.y;
        this.z += this.velocity.z;
    }
}
