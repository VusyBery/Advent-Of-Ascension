package net.tslat.aoa3.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.tslat.aoa3.common.registration.AoAAdvancementTriggers;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.player.skill.AoASkill;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AoAXpGainTrigger extends SimpleCriterionTrigger<AoAXpGainTrigger.Instance> {
	@Override
	public Codec<AoAXpGainTrigger.Instance> codec() {
		return AoAXpGainTrigger.Instance.CODEC;
	}

	public void trigger(ServerPlayer player, AoASkill skill, float xp) {
		trigger(player, instance -> instance.test(skill, xp));
	}

	public static Criterion<AoAXpGainTrigger.Instance> onSkillXpGain(@Nullable ContextAwarePredicate conditions, @Nullable AoASkill skill, Optional<Float> xpThreshold) {
		return AoAAdvancementTriggers.XP_GAIN.get().createCriterion(new AoAXpGainTrigger.Instance(Optional.ofNullable(conditions), skill != null ? Optional.of(skill) : Optional.empty(), xpThreshold));
	}

	public static Criterion<AoAXpGainTrigger.Instance> onSkillXpGain(@Nullable AoASkill skill, Optional<Float> xpThreshold) {
		return onSkillXpGain(null, skill, xpThreshold);
	}

	public static Criterion<AoAXpGainTrigger.Instance> onSkillXpGain(@Nullable AoASkill skill) {
		return onSkillXpGain(skill, Optional.empty());
	}

	public static Criterion<AoAXpGainTrigger.Instance> onSkillXpGain(Optional<Float> xpThreshold) {
		return onSkillXpGain(null, xpThreshold);
	}

	public record Instance(Optional<ContextAwarePredicate> player, Optional<AoASkill> skill, Optional<Float> xpThreshold) implements SimpleCriterionTrigger.SimpleInstance {
		private static final Codec<AoAXpGainTrigger.Instance> CODEC = RecordCodecBuilder.create(codec -> codec.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AoAXpGainTrigger.Instance::player),
				AoARegistries.AOA_SKILLS.lookupCodec().optionalFieldOf("skill").forGetter(AoAXpGainTrigger.Instance::skill),
				Codec.FLOAT.optionalFieldOf("xp").forGetter(AoAXpGainTrigger.Instance::xpThreshold)
		).apply(codec, AoAXpGainTrigger.Instance::new));

		public boolean test(AoASkill skill, float xp) {
			return (skill().isEmpty() || skill().get() == skill) && (xpThreshold().isEmpty() || xpThreshold().get() <= xp);
		}

		@Override
		public Optional<ContextAwarePredicate> player() {
			return Optional.empty();
		}
	}
}
