package net.tslat.aoa3.content.loottable.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RollExtraTablesLootModifier extends LootModifier {
	public static final MapCodec<RollExtraTablesLootModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> codecStart(builder).and(
			ResourceKey.codec(Registries.LOOT_TABLE).listOf().fieldOf("tables").forGetter(instance -> instance.additionalTables)
	).apply(builder, RollExtraTablesLootModifier::new));

	private final List<ResourceKey<LootTable>> additionalTables;

	public RollExtraTablesLootModifier(LootItemCondition[] conditions, ResourceLocation... additionalTables) {
		this(conditions, Arrays.stream(additionalTables).map(id -> ResourceKey.create(Registries.LOOT_TABLE, id)).toList());
	}

	public RollExtraTablesLootModifier(LootItemCondition[] conditions, List<ResourceKey<LootTable>> additionalTables) {
		super(conditions);

		this.additionalTables = additionalTables;
	}

	@Override
	public MapCodec<RollExtraTablesLootModifier> codec() {
		return CODEC;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		final HolderGetter.Provider lookup = context.getResolver();

		for (ResourceKey<LootTable> tableKey : this.additionalTables) {
			lookup.get(Registries.LOOT_TABLE, tableKey).ifPresent(table -> table.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add)));
		}

		return generatedLoot;
	}
}
