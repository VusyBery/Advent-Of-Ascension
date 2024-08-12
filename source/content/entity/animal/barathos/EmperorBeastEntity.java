package net.tslat.aoa3.content.entity.animal.barathos;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.content.entity.base.AoAAnimal;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class EmperorBeastEntity extends AoAAnimal<EmperorBeastEntity> {
    public EmperorBeastEntity(EntityType<? extends EmperorBeastEntity> entityType, Level world) {
        super(entityType, world);

        setParts(new AoAEntityPart<>(this, 3f, 6f, 0, getBbHeight(), getBbWidth() / 2f + 1f).setDamageMultiplier(1.1f),
                new AoAEntityPart<>(this, getBbWidth(), getBbHeight(), 0, 0, getBbWidth() / 4f),
                new AoAEntityPart<>(this, getBbWidth(), getBbHeight(), 0, 0, -getBbWidth() / 4f)
        );
    }

    @Override
    public BrainActivityGroup<? extends EmperorBeastEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new OneRandomBehaviour<>(
                                new SetRandomWalkTarget<>()
                                        .setRadius(30, 10)
                                        .speedModifier(0.9f),
                                new Idle<>()
                                        .runFor(entity -> entity.getRandom().nextInt(60, 240)))));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public int getHeadRotSpeed() {
        return 1;
    }

    @Override
    protected void pushEntities() {}

    public static AoAEntityStats.AttributeBuilder entityStats(EntityType<EmperorBeastEntity> entityType) {
        return AoAEntityStats.AttributeBuilder.create(entityType)
                .health(205)
                .knockbackResist(1)
                .armour(10, 40)
                .moveSpeed(0.22f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkIdleController(this));
    }
}
