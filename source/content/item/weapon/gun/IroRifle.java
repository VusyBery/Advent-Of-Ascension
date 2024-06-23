package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.AoASounds;
import org.jetbrains.annotations.Nullable;

public class IroRifle extends BaseGun {
	public IroRifle(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_GENERIC_FIRE_2.get();
	}
}
