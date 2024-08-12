package net.tslat.aoa3.content.loottable.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.loot.AoALootFunctions;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.List;

public class GrantSkillXp extends LootItemConditionalFunction {
	public static final MapCodec<GrantSkillXp> CODEC = RecordCodecBuilder.mapCodec(builder -> commonFields(builder).and(builder.group(
					Codec.lazyInitialized(AoARegistries.AOA_SKILLS::lookupCodec).fieldOf("skill").forGetter(GrantSkillXp::getSkill),
					NumberProviders.CODEC.fieldOf("xp").forGetter(GrantSkillXp::getXp)))
			.apply(builder, GrantSkillXp::new));

	private final AoASkill skill;
	private final NumberProvider xp;

	protected GrantSkillXp(List<LootItemCondition> lootConditions, AoASkill skill, NumberProvider xp) {
		super(lootConditions);

		this.skill = skill;
		this.xp	= xp;
	}

	@Override
	public LootItemFunctionType<GrantSkillXp> getType() {
		return AoALootFunctions.GRANT_SKILL_XP.get();
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		Entity entity = context.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY);

		if (entity == null)
			entity = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);

		if (!(entity instanceof Player))
			entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

		if (entity instanceof ServerPlayer pl)
			PlayerUtil.giveXpToPlayer(pl, this.skill, xp.getFloat(context), false);

		return stack;
	}

	public AoASkill getSkill() {
		return this.skill;
	}

	public NumberProvider getXp() {
		return this.xp;
	}

	public static Builder<?> builder(AoASkill skill, float xp) {
		return builder(skill, ConstantValue.exactly(xp));
	}

	public static Builder<?> builder(AoASkill skill, NumberProvider xp) {
		return simpleBuilder((conditions) -> new GrantSkillXp(conditions, skill, xp));
	}
}
