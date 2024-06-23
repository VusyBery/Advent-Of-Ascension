package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.AoASounds;
import org.jetbrains.annotations.Nullable;


public class Skullette extends BaseGun {
	public Skullette(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_GENERIC_FIRE_5.get();
	}

	@Override
	protected float getFiringSoundPitchAdjust() {
		return 0.75f;
	}

	@Override
	public boolean isFullAutomatic() {
		return false;
	}
}
