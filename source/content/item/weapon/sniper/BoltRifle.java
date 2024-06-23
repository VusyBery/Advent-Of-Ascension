package net.tslat.aoa3.content.item.weapon.sniper;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.AoASounds;
import org.jetbrains.annotations.Nullable;


public class BoltRifle extends BaseSniper {
	public BoltRifle(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_SNIPER_METALLIC_FIRE_LONG.get();
	}
}
