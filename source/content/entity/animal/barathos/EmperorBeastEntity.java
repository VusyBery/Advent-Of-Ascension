package net.tslat.aoa3.content.entity.animal.barathos;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.content.entity.base.AoAAnimal;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class EmperorBeastEntity extends AoAAnimal<EmperorBeastEntity> {
    public EmperorBeastEntity(EntityType<? extends EmperorBeastEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController(this));
    }
}
