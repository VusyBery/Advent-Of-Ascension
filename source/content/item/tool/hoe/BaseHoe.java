package net.tslat.aoa3.content.item.tool.hoe;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

public class BaseHoe extends HoeItem {
	public BaseHoe(Tier tier) {
		this(tier, 0, 0);
	}

	public BaseHoe(Tier tier, float damageMod, float speedMod) {
		this(tier, new Properties().attributes(createAttributes(tier, damageMod, speedMod)));
	}

	public BaseHoe(Tier tier, Properties properties) {
		super(tier, properties);
	}
}
