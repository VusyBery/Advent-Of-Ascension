package net.tslat.aoa3.content.item.weapon.blaster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.content.item.misc.SequenceVerifiedItem;
import org.jetbrains.annotations.Nullable;

public class ExperimentW801 extends BaseBlaster implements SequenceVerifiedItem {
	public ExperimentW801(Item.Properties properties) {
		super(properties);
	}

	@Override
	public String getSequenceName() {
		return "alien_orb";
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ENTITY_ARCWORM_HURT.get();
	}

	@Override
	public void fireBlaster(ServerLevel level, LivingEntity shooter, ItemStack blaster) {
		/*shooter.level.addFreshEntity(new ArcwormShotEntity(shooter, this, 120));*/
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!verifyStack(stack)) {
			stack.setCount(0);
			((Player)entityIn).getInventory().setItem(itemSlot, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		if (!verifyStack(entity.getItem())) {
			entity.setItem(ItemStack.EMPTY);
			entity.discard();
		}

		return false;
	}
}
