package net.tslat.aoa3.common.registration;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public final class AoAGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> DESTRUCTIVE_WEAPON_PHYSICS = GameRules.register("destructiveWeaponPhysics", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

	public static void lateInit() {}

	public static boolean checkDestructiveWeaponPhysics(Level world) {
		return world.getGameRules().getBoolean(DESTRUCTIVE_WEAPON_PHYSICS);
	}
}
