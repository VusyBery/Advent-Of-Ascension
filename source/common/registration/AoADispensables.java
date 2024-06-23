package net.tslat.aoa3.common.registration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.common.registration.item.AoAWeapons;
import net.tslat.aoa3.content.block.functional.misc.CarvedRuneOfPower;
import net.tslat.aoa3.content.item.misc.BlankRealmstone;
import net.tslat.aoa3.content.item.misc.Realmstone;

public final class AoADispensables {
	public static void lateInit() {
		registerFluidDispensables();
		registerProjectileDispensables();
		registerMiscDispensables();
	}

	private static void registerProjectileDispensables() {
		DispenserBlock.registerBehavior(AoAWeapons.HELLFIRE.get(), new ProjectileDispenseBehavior(AoAWeapons.HELLFIRE.get()));
		DispenserBlock.registerBehavior(AoAWeapons.GRENADE.get(), new ProjectileDispenseBehavior(AoAWeapons.GRENADE.get()));
		DispenserBlock.registerBehavior(AoAWeapons.CHAKRAM.get(), new ProjectileDispenseBehavior(AoAWeapons.CHAKRAM.get()));
		DispenserBlock.registerBehavior(AoAWeapons.GOO_BALL.get(), new ProjectileDispenseBehavior(AoAWeapons.GOO_BALL.get()));
		DispenserBlock.registerBehavior(AoAWeapons.RUNIC_BOMB.get(), new ProjectileDispenseBehavior(AoAWeapons.RUNIC_BOMB.get()));
		DispenserBlock.registerBehavior(AoAWeapons.VULKRAM.get(), new ProjectileDispenseBehavior(AoAWeapons.VULKRAM.get()));
		DispenserBlock.registerBehavior(AoAWeapons.SLICE_STAR.get(), new ProjectileDispenseBehavior(AoAWeapons.SLICE_STAR.get()));
		DispenserBlock.registerBehavior(AoAWeapons.HARDENED_PARAPIRANHA.get(), new ProjectileDispenseBehavior(AoAWeapons.HARDENED_PARAPIRANHA.get()));
	}

	private static void registerMiscDispensables() {
		DispenseItemBehavior realmstoneBehaviour = (source, stack) -> {
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			BlockPos pos = source.pos().offset(direction.getStepX(), direction.getStepY(), direction.getStepZ());

			if (source.level().getBlockState(pos).getBlock() instanceof CarvedRuneOfPower) {
				switch (stack.getItem()) {
					case Realmstone realmstone -> CarvedRuneOfPower.fillPortal(source.level(), pos, direction, stack, null);
					case BlankRealmstone blankRealmstone -> CarvedRuneOfPower.clearPortal(source.level(), pos, direction, stack, null);
					default -> {}
				}

				return stack;
			}

			Position position = DispenserBlock.getDispensePosition(source);
			ItemStack itemstack = stack.split(1);

			DefaultDispenseItemBehavior.spawnItem(source.level(), itemstack, 6, direction, position);
			source.level().levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, source.pos(), 0);
			source.level().levelEvent(LevelEvent.PARTICLES_SHOOT_SMOKE, source.pos(), direction.get3DDataValue());

			return stack;
		};

		DispenserBlock.registerBehavior(AoAItems.NETHER_REALMSTONE.get(), realmstoneBehaviour);
		DispenserBlock.registerBehavior(AoAItems.BLANK_REALMSTONE.get(), realmstoneBehaviour);
	}

	private static void registerFluidDispensables() {
		DefaultDispenseItemBehavior fluidDispenser = new DefaultDispenseItemBehavior() {
			private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				BucketItem bucket = (BucketItem)stack.getItem();
				BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
				Level world = source.level();

				if (bucket.emptyContents(null, world, pos, null)) {
					bucket.checkExtraContent(null, world, stack, pos);

					return new ItemStack(Items.BUCKET);
				}
				else {
					return this.defaultBehaviour.dispense(source, stack);
				}
			}
		};

		DispenserBlock.registerBehavior(AoARegistries.ITEMS.getEntry(AdventOfAscension.id("candied_water_bucket")), fluidDispenser);
		DispenserBlock.registerBehavior(AoARegistries.ITEMS.getEntry(AdventOfAscension.id("toxic_waste_bucket")), fluidDispenser);
		DispenserBlock.registerBehavior(AoARegistries.ITEMS.getEntry(AdventOfAscension.id("clear_water_bucket")), fluidDispenser);
	}
}
