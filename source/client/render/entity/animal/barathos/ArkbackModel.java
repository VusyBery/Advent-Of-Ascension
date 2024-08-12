package net.tslat.aoa3.client.render.entity.animal.barathos;

import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.content.entity.animal.barathos.ArkbackEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ArkbackModel extends DefaultedEntityGeoModel<ArkbackEntity> {
	private static float defaultSandPos = -1;

	public ArkbackModel() {
		super(AdventOfAscension.id("animal/barathos/arkback"));
	}

	@Override
	public void setCustomAnimations(ArkbackEntity animatable, long instanceId, AnimationState<ArkbackEntity> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);

		getBone("sand_layer").ifPresent(bone -> {
			if (defaultSandPos == -1)
				defaultSandPos = bone.getPosY();

			bone.setPosY(defaultSandPos + animatable.getSandLevel() / 10f);
		});
	}
}
