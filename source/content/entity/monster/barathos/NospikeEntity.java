package net.tslat.aoa3.content.entity.monster.barathos;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.content.entity.base.AoAEntityPart;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class NospikeEntity extends AoAMeleeMob<NospikeEntity> {
    public NospikeEntity(EntityType<? extends NospikeEntity> entityType, Level level) {
        super(entityType, level);

        setParts(new AoAEntityPart<>(this, getBbWidth(), 1.46875f, 0, 1.125f, getBbWidth()),
                new AoAEntityPart<>(this, 0.75f, 0.75f, 0, 2.59375f, getBbWidth() + 0.25f),
                new AoAEntityPart<>(this, 0.75f, 0.75f, 0, 1.65625f, -getBbWidth() + 0.095f),
                new AoAEntityPart<>(this, 0.6875f, 0.5f, 0, 1.90625f, -getBbWidth() - 0.625f));
    }

    public static AoAEntityStats.AttributeBuilder entityStats(EntityType<NospikeEntity> entityType) {
        return AoAEntityStats.AttributeBuilder.createMonster(entityType)
                .health(46)
                .moveSpeed(0.34)
                .meleeStrength(7.5f)
                .knockbackResist(0.25f)
                .aggroRange(12)
                .followRange(32);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericWalkRunIdleController(this));
    }
}
