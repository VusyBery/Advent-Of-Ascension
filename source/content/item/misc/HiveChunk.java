package net.tslat.aoa3.content.item.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HiveChunk extends Item {
	public HiveChunk() {
		super(new Item.Properties());
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		/*if (target.getType() == AoAMonsters.THARAFLY.get() && attacker instanceof Player) {
			if (stack.getCount() <= 1) {
				attacker.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(AoAItems.HIVE_EGG.get()));
			}
			else {
				ItemUtil.givePlayerItemOrDrop((Player)attacker, new ItemStack(AoAItems.HIVE_EGG.get()));
				stack.shrink(1);
			}

			return true;
		}*/

		return false;
	}
}
