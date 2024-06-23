package net.tslat.aoa3.content.item.weapon.shotgun;

import net.minecraft.world.item.Item;

public class Vivo extends BaseShotgun {
	public Vivo(Item.Properties properties) {
		super(properties);
	}

	@Override
	protected float getFiringSoundPitchAdjust() {
		return 1.3f;
	}
}
