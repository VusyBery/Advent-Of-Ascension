package net.tslat.aoa3.content.entity.monster.barathos;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.entity.AoAEntityStats;
import net.tslat.aoa3.content.entity.base.AoAMeleeMob;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;

public class NospikeEntity extends AoAMeleeMob<NospikeEntity> {
    public NospikeEntity(EntityType<? extends NospikeEntity> entityType, Level level) {
        super(entityType, level);
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
