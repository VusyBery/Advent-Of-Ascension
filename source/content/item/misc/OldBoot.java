package net.tslat.aoa3.content.item.misc;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.tslat.aoa3.common.registration.item.AoAArmourMaterials;
import net.tslat.aoa3.content.item.armour.AdventArmour;

public class OldBoot extends AdventArmour {
	public OldBoot() {
		super(AoAArmourMaterials.OLD_BOOT, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(30)).rarity(Rarity.RARE));
	}

	@Override
	public Type getSetType() {
		return Type.NONE;
	}
}
