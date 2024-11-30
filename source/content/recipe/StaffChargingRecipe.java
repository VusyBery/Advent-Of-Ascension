package net.tslat.aoa3.content.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoARecipes;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.content.item.weapon.staff.BaseStaff;
import net.tslat.aoa3.util.RecipeUtil;
import org.jetbrains.annotations.Nullable;

public class StaffChargingRecipe extends CustomRecipe implements RecipeBookRecipe<CraftingInput> {
	private final RecipeUtil.RecipeBookDetails recipeBookDetails;

	public StaffChargingRecipe(String group, @Nullable CraftingBookCategory category, boolean showObtainNotification) {
		this(new RecipeUtil.RecipeBookDetails(group, category, showObtainNotification));
	}

	public StaffChargingRecipe(RecipeUtil.RecipeBookDetails recipeBookDetails) {
		super(recipeBookDetails.category());

		this.recipeBookDetails = recipeBookDetails;
	}

	@Override
	public RecipeUtil.RecipeBookDetails recipeBookDetails() {
		return this.recipeBookDetails;
	}

	@Override
	public RecipeSerializer<StaffChargingRecipe> getSerializer() {
		return AoARecipes.STAFF_CHARGING.serializer().get();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		Object2IntMap<Item> foundItems = new Object2IntArrayMap<>();
		Object2IntMap<Item> runeCosts = null;

		for (ItemStack stack : input.items()) {
			if (runeCosts == null && stack.has(AoADataComponents.STORED_SPELL_CASTS)) {
				BaseStaff.StoredCasts storedCasts = stack.get(AoADataComponents.STORED_SPELL_CASTS);

				if (storedCasts.stored() < 0 || storedCasts.stored() >= storedCasts.max().orElse(Integer.MAX_VALUE))
					return false;

				runeCosts = stack.has(AoADataComponents.STAFF_RUNE_COST) ? stack.get(AoADataComponents.STAFF_RUNE_COST).runeCosts() : null;
			}
			else if (!stack.isEmpty()) {
				foundItems.mergeInt(stack.getItem(), stack.getCount(), Integer::sum);
			}
		}

		if (foundItems.isEmpty() || runeCosts == null || foundItems.size() != runeCosts.size())
			return false;

		for (Object2IntMap.Entry<Item> foundItem : foundItems.object2IntEntrySet()) {
			if (!runeCosts.containsKey(foundItem.getKey()))
				return false;
		}

		for (Object2IntMap.Entry<Item> runeCost : runeCosts.object2IntEntrySet()) {
			if (!foundItems.containsKey(runeCost.getKey()) || foundItems.getInt(runeCost.getKey()) < runeCost.getIntValue())
				return false;
		}

		return true;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
		Object2IntMap<ItemStack> foundItems = new Object2IntArrayMap<>();
		Object2IntMap<Item> runeCosts = null;

		for (ItemStack stack : input.items()) {
			if (runeCosts == null && stack.has(AoADataComponents.STORED_SPELL_CASTS)) {
				runeCosts = stack.has(AoADataComponents.STAFF_RUNE_COST) ? new Object2IntArrayMap<>(stack.get(AoADataComponents.STAFF_RUNE_COST).runeCosts()) : null;
			}
			else if (!stack.isEmpty()) {
				foundItems.mergeInt(stack, stack.getCount(), Integer::sum);
			}
		}

		for (Object2IntMap.Entry<ItemStack> stack : foundItems.object2IntEntrySet()) {
			ItemStack runeStack = stack.getKey();
			Item rune = runeStack.getItem();

			runeCosts.computeIntIfPresent(rune, (item, remaining) -> {
				if (remaining < runeStack.getCount()) {
					remainingItems.add(runeStack.copyWithCount(runeStack.getCount() - remaining));

					return 0;
				}
				else {
					return remaining - runeStack.getCount();
				}
			});
		}

		return remainingItems;
	}

	@Override
	public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider holderLookup) {
		for (ItemStack stack : inventory.items()) {
			if (stack.has(AoADataComponents.STORED_SPELL_CASTS) && stack.has(AoADataComponents.STAFF_RUNE_COST)) {
				ItemStack newStack = stack.copy();

				newStack.set(AoADataComponents.STORED_SPELL_CASTS, BaseStaff.StoredCasts.increment(stack.get(AoADataComponents.STORED_SPELL_CASTS)));

				return newStack;
			}
		}

		return ItemStack.EMPTY;
	}

	public static class Factory implements RecipeSerializer<StaffChargingRecipe> {
		public static final MapCodec<StaffChargingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
				RecipeUtil.RecipeBookDetails.codec(builder, instance -> instance.recipeBookDetails)
						.apply(builder, StaffChargingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, StaffChargingRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtil.RecipeBookDetails.STREAM_CODEC, recipe -> recipe.recipeBookDetails,
				StaffChargingRecipe::new);

		@Override
		public MapCodec<StaffChargingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, StaffChargingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
