package net.tslat.aoa3.content.item.weapon.gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.tslat.aoa3.common.registration.AoASounds;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.content.entity.projectile.gun.BaseBullet;
import net.tslat.aoa3.content.entity.projectile.gun.SeedDartEntity;
import net.tslat.aoa3.util.InventoryUtil;
import org.jetbrains.annotations.Nullable;


public class Gardener extends BaseGun {
	public Gardener(Item.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public SoundEvent getFiringSound() {
		return AoASounds.ITEM_GUN_BLOWPIPE_SHOOT.get();
	}

	@Override
	public Item getAmmoItem() {
		return Items.WHEAT_SEEDS;
	}

	@Nullable
	@Override
	public BaseBullet findAndConsumeAmmo(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		if (!(shooter instanceof Player pl) || InventoryUtil.findItemForConsumption(pl, Tags.Items.SEEDS, pl.getAbilities().instabuild ? 0 : AoAEnchantments.modifyAmmoCost(pl.level(), gunStack, 1), true))
			return createProjectileEntity(shooter, gunStack, hand);

		return null;
	}

	@Override
	public BaseBullet createProjectileEntity(LivingEntity shooter, ItemStack gunStack, InteractionHand hand) {
		return new SeedDartEntity(shooter, this, hand, 120, 0);
	}
}
