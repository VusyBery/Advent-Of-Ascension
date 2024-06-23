package net.tslat.aoa3.content.item.tool.shovel;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.tslat.aoa3.library.constant.AttackSpeed;

public class BaseShovel extends ShovelItem {
	public BaseShovel(Tier tier, Item.Properties properties) {
		super(tier, properties);
	}

	public static Item.Properties baseProperties(Tier tier) {
		return baseProperties(tier, 0, AttackSpeed.SHOVEL);
	}

	public static Item.Properties baseProperties(Tier tier, float attackSpeed) {
		return baseProperties(tier, 0, attackSpeed);
	}

	public static Item.Properties baseProperties(Tier tier, float damageModifier, float attackSpeed) {
		return new Item.Properties().attributes(ShovelItem.createAttributes(tier, damageModifier, attackSpeed));
	}
}
