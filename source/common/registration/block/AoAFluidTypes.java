package net.tslat.aoa3.common.registration.block;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.fluid.Tar;
import net.tslat.aoa3.content.fluid.ToxicWaste;

import java.util.function.Supplier;

public final class AoAFluidTypes {
	public static void init() {}

	public static final DeferredHolder<FluidType, FluidType> TOXIC_WASTE = register("toxic_waste", ToxicWaste::new);
	public static final DeferredHolder<FluidType, FluidType> TAR = register("tar", Tar::new);
	public static final DeferredHolder<FluidType, FluidType> CANDIED_WATER = register("candied_water", () -> new FluidType(FluidType.Properties.create()
			.canSwim(true)
			.canDrown(true)
			.supportsBoating(true)
			.canHydrate(true)
			.canExtinguish(true)
			.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
			.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
			.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
			.fallDistanceModifier(0)
			.viscosity(1200)
			.density(1200)));

	private static <T extends FluidType> DeferredHolder<FluidType, T> register(String id, Supplier<T> fluidType) {
		return AoARegistries.FLUID_TYPES.register(id, fluidType);
	}
}
