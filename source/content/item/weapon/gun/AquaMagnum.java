package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.AoASounds;
import org.jetbrains.annotations.Nullable;


public class AquaMagnum extends BaseGun {
	public AquaMagnum(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_RIFLE_HEAVY_FIRE_LONG.get();
	}

	@Override
	public boolean isFullAutomatic() {
		return false;
	}
}
