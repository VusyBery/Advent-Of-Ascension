package net.tslat.aoa3.content.loottable.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.tslat.aoa3.event.custom.AoAEvents;
import net.tslat.aoa3.util.PlayerUtil;
import org.jetbrains.annotations.NotNull;

public class PlayerEventListenerLootModifier extends LootModifier {
	public static final MapCodec<PlayerEventListenerLootModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> codecStart(builder).apply(builder, PlayerEventListenerLootModifier::new));
	private static final LootContextParam<?>[] ENTITY_SOURCE_PARAMS = new LootContextParam<?>[] {LootContextParams.THIS_ENTITY, LootContextParams.DIRECT_ATTACKING_ENTITY, LootContextParams.ATTACKING_ENTITY, LootContextParams.LAST_DAMAGE_PLAYER};

	public PlayerEventListenerLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		for (LootContextParam<?> parameter : ENTITY_SOURCE_PARAMS) {
			if (context.getParamOrNull(parameter) instanceof Entity entity && PlayerUtil.getPlayerOrOwnerIfApplicable(entity) instanceof ServerPlayer pl) {
				AoAEvents.firePlayerSkillsLootModification(pl, generatedLoot, context);

				break;
			}
		}

		return generatedLoot;
	}
}
