package net.tslat.aoa3.client.render;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.function.Function;
import java.util.function.Predicate;

public final class AoAAnimations {
	public static final RawAnimation RECOVER = RawAnimation.begin().thenPlay("misc.rest");
	public static final RawAnimation EAT = RawAnimation.begin().thenPlay("misc.eat");
	public static final RawAnimation SUCCEED = RawAnimation.begin().thenPlay("misc.succeed");
	public static final RawAnimation INTERACT = RawAnimation.begin().thenPlay("misc.interact").thenPlay("misc.interact.hold");
	public static final RawAnimation INTERACT_END = RawAnimation.begin().thenPlay("misc.interact.end");

	public static final RawAnimation ATTACK_SPIN = RawAnimation.begin().thenPlay("attack.spin");
	public static final RawAnimation ATTACK_BLOCK = RawAnimation.begin().thenPlay("attack.block").thenPlay("attack.block.hold");
	public static final RawAnimation ATTACK_CHARGE = RawAnimation.begin().thenPlay("attack.charge").thenPlay("attack.charge.hold");
	public static final RawAnimation ATTACK_CHARGE_END = RawAnimation.begin().thenPlay("attack.charge.end");
	public static final RawAnimation ATTACK_POUNCE = RawAnimation.begin().thenPlay("attack.pounce");

	public static final RawAnimation ATTACK_SWIPE_LEFT = RawAnimation.begin().thenPlay("attack.swipe_left");
	public static final RawAnimation ATTACK_SWIPE_RIGHT = RawAnimation.begin().thenPlay("attack.swipe_right");
	public static final RawAnimation ATTACK_SHOOT_ALTERNATE = RawAnimation.begin().thenPlay("attack.shoot_alternate");

	public static final RawAnimation SWIM_SPRINT = RawAnimation.begin().thenLoop("move.swim_sprint");

	public static <T extends LivingEntity & GeoEntity> AnimationController<T> genericWalkRunSwimIdleController(T entity) {
		return new AnimationController<>(entity, "Walk/Run/Swim/Idle", 2, state -> {
			if (entity.isInWater() && !entity.onGround())
				return state.setAndContinue(DefaultAnimations.SWIM);

			if (state.isMoving())
				return state.setAndContinue(entity.isSprinting() ? DefaultAnimations.RUN : DefaultAnimations.WALK);

			return state.setAndContinue(DefaultAnimations.IDLE);
		});
	}

	public static <T extends LivingEntity & GeoEntity> AnimationController<T> dynamicAttackController(T entity, Function<AnimationState<T>, RawAnimation> animationSupplier) {
		return new AnimationController<T>(entity, "Attack", 0, state -> {
			if (entity.swinging)
				return state.setAndContinue(animationSupplier.apply(state));

			state.getController().forceAnimationReset();

			return PlayState.STOP;
		});
	}

	public static <T extends LivingEntity & GeoEntity> AnimationController<T> genericHeldPoseController(T entity, RawAnimation poseToHold, @Nullable RawAnimation animOnRelease, Predicate<T> posePredicate) {
		return new AnimationController<>(entity, "posing", 0, state -> {
			if (posePredicate.test(entity))
				return state.setAndContinue(poseToHold);

			if (animOnRelease != null && state.getController().getCurrentAnimation() != null)
				return state.setAndContinue(animOnRelease);

			return PlayState.STOP;
		});
	}
}
