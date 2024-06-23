package net.tslat.aoa3.content.loottable.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.tslat.aoa3.util.LootUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RollEntityWorldTableLootModifier extends LootModifier {
	public static final MapCodec<RollEntityWorldTableLootModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> codecStart(builder).and(
			Codec.unboundedMap(
					Level.RESOURCE_KEY_CODEC,
					ResourceLocation.CODEC)
					.fieldOf("worlds").forGetter(instance -> instance.tables)
	).apply(builder, RollEntityWorldTableLootModifier::new));

	private final Map<ResourceKey<Level>, ResourceLocation> tables;

	public RollEntityWorldTableLootModifier(LootItemCondition[] conditions, Map<ResourceKey<Level>, ResourceLocation> tables) {
		super(conditions);

		this.tables = tables;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (!context.hasParam(LootContextParams.THIS_ENTITY))
			return generatedLoot;

		ResourceLocation tableId = this.tables.get(context.getParam(LootContextParams.THIS_ENTITY).level().dimension());

		if (tableId == null)
			return generatedLoot;

		LootTable table = LootUtil.getTable(context.getLevel(), tableId);

		if (table == LootTable.EMPTY || table.getParamSet() != LootContextParamSets.ENTITY)
			return generatedLoot;

		table.getRandomItemsRaw(context, generatedLoot::add);

		return generatedLoot;
	}
}
