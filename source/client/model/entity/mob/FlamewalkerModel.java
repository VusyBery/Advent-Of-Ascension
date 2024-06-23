package net.tslat.aoa3.client.model.entity.mob;

import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.content.entity.monster.nether.FlamewalkerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class FlamewalkerModel extends DefaultedEntityGeoModel<FlamewalkerEntity> {
	private GeoBone rock1;
	private GeoBone rock2;
	private GeoBone rock3;
	private GeoBone rock4;
	private GeoBone rock5;

	public FlamewalkerModel() {
		super(AdventOfAscension.id("mob/nether/flamewalker"));
	}

	@Override
	public void setCustomAnimations(FlamewalkerEntity flamewalker, long instanceId, AnimationState<FlamewalkerEntity> animationState) {
		super.setCustomAnimations(flamewalker, instanceId, animationState);

		if (this.rock1 == null) {
			this.rock1 = getBone("rock_1").get();
			this.rock2 = getBone("rock_2").get();
			this.rock3 = getBone("rock_3").get();
			this.rock4 = getBone("rock_4").get();
			this.rock5 = getBone("rock_5").get();
		}

		if (flamewalker.onGround() && animationState.isMoving()) {
			this.rock1.setHidden(false);
			this.rock2.setHidden(false);
			this.rock3.setHidden(false);
			this.rock4.setHidden(false);
			this.rock5.setHidden(false);
		}
		else {
			this.rock1.setHidden(true);
			this.rock2.setHidden(true);
			this.rock3.setHidden(true);
			this.rock4.setHidden(true);
			this.rock5.setHidden(true);
		}
	}
}
