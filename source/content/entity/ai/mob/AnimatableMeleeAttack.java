package net.tslat.aoa3.content.entity.ai.mob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.BiConsumer;

public class AnimatableMeleeAttack<E extends Mob> extends net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack<E> {
    private BiConsumer<E, LivingEntity> attackEffect = (entity, target) -> {};

    public AnimatableMeleeAttack(int delayTicks) {
        super(delayTicks);
    }

    public AnimatableMeleeAttack<E> attackEffect(BiConsumer<E, LivingEntity> effect) {
        this.attackEffect = effect;

        return this;
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));

        if (this.target == null)
            return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target))
            return;

        if (entity.doHurtTarget(this.target))
            this.attackEffect.accept(entity, this.target);
    }
}
