package net.tslat.aoa3.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.common.registration.item.AoAEnchantments;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.content.item.armour.AdventArmour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class ItemUtil {
	public static final EquipmentSlot[] BOTH_HANDS = new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

	public static void damageItem(ItemStack stack, LivingEntity entity, InteractionHand hand) {
		damageItem(stack, entity, hand, 1);
	}

	public static void damageItem(ItemStack stack, LivingEntity entity, InteractionHand hand, int amount) {
		damageItem(stack, entity, amount, EntityUtil.handToEquipmentSlotType(hand));
	}

	public static void damageItem(ItemStack stack, LivingEntity entity, int amount, EquipmentSlot slot) {
		stack.hurtAndBreak(amount, entity, slot);
	}

	public static void givePlayerMultipleItems(Player pl, ItemStack... stacks) {
		givePlayerMultipleItems(pl, Arrays.asList(stacks));
	}

	public static void givePlayerMultipleItems(Player pl, Collection<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			if (!stack.isEmpty() && !pl.getInventory().add(stack))
				pl.spawnAtLocation(stack, 0.5f);
		}

		if (!stacks.isEmpty())
			pl.inventoryMenu.broadcastChanges();
	}

	public static void givePlayerItemOrDrop(Player player, ItemStack stack) {
		if (stack.isEmpty())
			return;

		if (!player.getInventory().add(stack))
			player.spawnAtLocation(stack, 0.5f);

		player.inventoryMenu.broadcastChanges();
	}

	public static boolean isHoldingItem(LivingEntity entity, Item item) {
		return entity.getMainHandItem().getItem() == item || entity.getOffhandItem().getItem() == item;
	}

	public static void clearInventoryOfItems(Player player, ItemStack... stacks) {
		if (player.isCreative())
			return;

		ItemStack checkStack;

		if (!(checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)).isEmpty()) {
			for (ItemStack stack : stacks) {
				if (areStacksEqualIgnoringData(checkStack, stack)) {
					checkStack.setCount(0);

					break;
				}
			}
		}

		if (!(checkStack = player.getItemInHand(InteractionHand.OFF_HAND)).isEmpty()) {
			for (ItemStack stack : stacks) {
				if (areStacksEqualIgnoringData(checkStack, stack)) {
					checkStack.setCount(0);

					break;
				}
			}
		}

		for (ItemStack checkStack2 : player.getInventory().items) {
			if (!checkStack2.isEmpty()) {
				for (ItemStack stack : stacks) {
					if (areStacksEqualIgnoringData(checkStack2, stack)) {
						checkStack2.setCount(0);

						break;
					}
				}
			}
		}

		for (ItemStack checkStack2 : player.getInventory().armor) {
			if (!checkStack2.isEmpty()) {
				for (ItemStack stack : stacks) {
					if (areStacksEqualIgnoringData(checkStack2, stack)) {
						checkStack2.setCount(0);

						break;
					}
				}
			}
		}
	}

	public static boolean findItemByTag(Player player, TagKey<Item> tag, boolean consumeItem, int amount) {
		if (amount <= 0 || player.isCreative())
			return true;

		if (amount == 1) {
			ItemStack checkStack;

			if ((checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)).is(tag) && !checkStack.isEmpty()) {
				if (consumeItem)
					checkStack.shrink(1);

				return true;
			}
			else if ((checkStack = player.getItemInHand(InteractionHand.OFF_HAND)).is(tag) && !checkStack.isEmpty()) {
				if (consumeItem)
					checkStack.shrink(1);

				return true;
			}
			else {
				for (ItemStack checkStack2 : player.getInventory().items) {
					if (!checkStack2.isEmpty() && checkStack2.is(tag)) {
						if (consumeItem)
							checkStack2.shrink(1);

						return true;
					}
				}

				for (ItemStack checkStack2 : player.getInventory().armor) {
					if (!checkStack2.isEmpty() && checkStack2.is(tag)) {
						if (consumeItem)
							checkStack2.shrink(1);

						return true;
					}
				}
			}

			return false;
		}
		else {
			List<ItemStack> matchedStacks = new ObjectArrayList<>();
			int foundCount = 0;
			ItemStack checkStack;

			if ((checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)).is(tag) && !checkStack.isEmpty()) {
				matchedStacks.add(checkStack);
				foundCount += checkStack.getCount();
			}

			if (foundCount < amount && (checkStack = player.getItemInHand(InteractionHand.OFF_HAND)).is(tag) && !checkStack.isEmpty()) {
				matchedStacks.add(checkStack);
				foundCount += checkStack.getCount();
			}

			if (foundCount < amount) {
				for (ItemStack checkStack2 : player.getInventory().items) {
					if (!checkStack2.isEmpty() && checkStack2.is(tag)) {
						matchedStacks.add(checkStack2);
						foundCount += checkStack2.getCount();

						if (foundCount >= amount)
							break;
					}
				}
			}

			if (foundCount < amount) {
				for (ItemStack checkStack2 : player.getInventory().armor) {
					if (!checkStack2.isEmpty() && checkStack2.is(tag)) {
						matchedStacks.add(checkStack2);
						foundCount += checkStack2.getCount();

						if (foundCount >= amount)
							break;
					}
				}
			}

			if (foundCount < amount)
				return false;

			if (!consumeItem)
				return true;

			for (ItemStack matchedStack : matchedStacks) {
				int consumeAmount = Math.min(matchedStack.getCount(), Math.min(amount, foundCount));

				matchedStack.shrink(consumeAmount);
				foundCount -= consumeAmount;
			}

			return true;
		}
	}

	public static boolean findInventoryItem(Player player, ItemStack stack, boolean consumeItem, int amount, boolean ignoreCreative) {
		if (stack.isEmpty())
			return false;

		if (amount <= 0 || (!ignoreCreative && player.getAbilities().instabuild))
			return true;

		if (amount == 1) {
			ItemStack checkStack;

			if (areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)), stack) && !checkStack.isEmpty()) {
				if (consumeItem)
					checkStack.shrink(1);

				return true;
			}
			else if (areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.OFF_HAND)), stack) && !checkStack.isEmpty()) {
				if (consumeItem)
					checkStack.shrink(1);

				return true;
			}
			else {
				for (ItemStack checkStack2 : player.getInventory().items) {
					if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2)) {
						if (consumeItem)
							checkStack2.shrink(1);

						return true;
					}
				}

				for (ItemStack checkStack2 : player.getInventory().armor) {
					if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2)) {
						if (consumeItem)
							checkStack2.shrink(1);

						return true;
					}
				}
			}

			return false;
		}
		else {
			List<ItemStack> matchedStacks = new ObjectArrayList<>();
			int foundCount = 0;
			ItemStack checkStack;

			if (areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)), stack) && !checkStack.isEmpty()) {
				matchedStacks.add(checkStack);
				foundCount += checkStack.getCount();
			}

			if (foundCount < amount && areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.OFF_HAND)), stack) && !checkStack.isEmpty()) {
				matchedStacks.add(checkStack);
				foundCount += checkStack.getCount();
			}

			if (foundCount < amount) {
				for (ItemStack checkStack2 : player.getInventory().items) {
					if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2)) {
						matchedStacks.add(checkStack2);
						foundCount += checkStack2.getCount();

						if (foundCount >= amount)
							break;
					}
				}
			}

			if (foundCount < amount) {
				for (ItemStack checkStack2 : player.getInventory().armor) {
					if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2)) {
						matchedStacks.add(checkStack2);
						foundCount += checkStack2.getCount();

						if (foundCount >= amount)
							break;
					}
				}
			}

			if (foundCount < amount)
				return false;

			if (!consumeItem)
				return true;

			for (ItemStack matchedStack : matchedStacks) {
				int consumeAmount = Math.min(matchedStack.getCount(), Math.min(amount, foundCount));

				matchedStack.shrink(consumeAmount);
				foundCount -= consumeAmount;
			}

			return true;
		}
	}

	public static boolean findAndConsumeRunes(Object2IntMap<Item> runeMap, ServerPlayer player, boolean allowBuffs, @NotNull ItemStack heldItem) {
		if (player.isCreative())
			return true;

		Reference2IntOpenHashMap<Item> requiredRunes = new Reference2IntOpenHashMap<>(runeMap);
		boolean nightmareArmour = allowBuffs && PlayerUtil.getAdventPlayer(player).equipment().getCurrentFullArmourSet() == AdventArmour.Type.NIGHTMARE;

		for (Item item : requiredRunes.keySet()) {
			requiredRunes.computeIntIfPresent(item, (rune, cost) -> {
				if (allowBuffs) {
					cost = Mth.ceil(AoAEnchantments.modifyRuneCost(player.serverLevel(), heldItem, cost));

					if (nightmareArmour)
						cost = rune == AoAItems.DISTORTION_RUNE.get() ? 0 : Math.max(cost - 1, 1);
				}

				return Mth.ceil(AoAEnchantments.modifyAmmoCost(player.serverLevel(), heldItem, cost));
			});
		}

		if (requiredRunes.isEmpty())
			return true;

		IntSet runeSlots = new IntOpenHashSet();
		Reference2IntOpenHashMap<Item> runeCounter = new Reference2IntOpenHashMap<>(requiredRunes);

		ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

		if (mainHandStack.is(AoATags.Items.ADVENT_RUNE)) {
			Item type = mainHandStack.getItem();

			if (runeCounter.containsKey(type)) {
				int amount = runeCounter.getInt(type);

				runeSlots.add(-1);
				amount -= mainHandStack.getCount();

				if (amount > 0) {
					runeCounter.put(type, amount);
				}
				else {
					runeCounter.removeInt(type);
				}
			}
		}

		if (!runeCounter.isEmpty() && offHandStack.is(AoATags.Items.ADVENT_RUNE)) {
			Item type = offHandStack.getItem();

			if (runeCounter.containsKey(type)) {
				int amount = runeCounter.getInt(type);

				runeSlots.add(-2);
				amount -= offHandStack.getCount();

				if (amount > 0) {
					runeCounter.put(type, amount);
				}
				else {
					runeCounter.removeInt(type);
				}
			}
		}

		if (!runeCounter.isEmpty()) {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);

				if (stack.is(AoATags.Items.ADVENT_RUNE)) {
					Item type = stack.getItem();

					if (runeCounter.containsKey(type)) {
						int amount = runeCounter.getInt(type);

						runeSlots.add(i);
						amount -= stack.getCount();

						if (amount > 0) {
							runeCounter.put(type, amount);
						}
						else {
							runeCounter.removeInt(type);
						}

						if (runeCounter.isEmpty())
							break;
					}
				}
			}
		}

		if (runeCounter.isEmpty()) {
			if (runeSlots.contains(-1)) {
				ItemStack rune = player.getItemInHand(InteractionHand.MAIN_HAND);
				Item type = rune.getItem();
				int amount = requiredRunes.getInt(type);
				int remaining = amount - rune.getCount();

				rune.shrink(amount);

				if (remaining <= 0) {
					requiredRunes.removeInt(type);
				}
				else {
					requiredRunes.put(type, remaining);
				}

				runeSlots.remove(-1);
			}

			if (runeSlots.contains(-2)) {
				ItemStack rune = player.getItemInHand(InteractionHand.OFF_HAND);
				Item type = rune.getItem();
				int amount = requiredRunes.getInt(type);
				int remaining = amount - rune.getCount();

				rune.shrink(amount);

				if (remaining <= 0) {
					requiredRunes.removeInt(type);
				}
				else {
					requiredRunes.put(type, remaining);
				}

				runeSlots.remove(-2);
			}

			for (int slotId : runeSlots) {
				ItemStack rune = player.getInventory().getItem(slotId);
				Item type = rune.getItem();
				int amount = requiredRunes.getInt(type);
				int remaining = amount - rune.getCount();

				rune.shrink(amount);

				if (remaining <= 0) {
					requiredRunes.removeInt(type);
				}
				else {
					requiredRunes.put(type, remaining);
				}

				if (requiredRunes.isEmpty())
					break;
			}

			return true;
		}

		return false;
	}

	public static boolean hasItemInHotbar(Player player, Item item) {
		return getStackFromInventory(player, item) != null;
	}

	@Nullable
	public static ItemStack getStackFromHotbar(Player player, Item item) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack;

			if ((stack = player.getInventory().getItem(i)).getItem() == item)
				return stack;
		}

		return null;
	}

	public static boolean hasItemInOffhand(Player player, Item item) {
		return player.getItemInHand(InteractionHand.OFF_HAND).getItem() == item;
	}

	@Nullable
	public static ItemStack getStackFromInventory(Player player, Item item) {
		ItemStack stack = new ItemStack(item);
		ItemStack checkStack;

		if (areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.MAIN_HAND)), stack) && !checkStack.isEmpty()) {
			return checkStack;
		}
		else if (areStacksEqualIgnoringData((checkStack = player.getItemInHand(InteractionHand.OFF_HAND)), stack) && !checkStack.isEmpty()) {
			return checkStack;
		}
		else {
			for (ItemStack checkStack2 : player.getInventory().items) {
				if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2))
					return checkStack2;
			}

			for (ItemStack checkStack2 : player.getInventory().armor) {
				if (!checkStack2.isEmpty() && areStacksEqualIgnoringData(stack, checkStack2))
					return checkStack2;
			}
		}

		return null;
	}

	public static boolean areStacksFunctionallyEqual(ItemStack a, ItemStack b) {
		if (!areStacksEqualIgnoringData(a, b))
			return false;

		return Objects.equals(a.getComponents(), b.getComponents());
	}

	public static boolean areStacksEqualIgnoringData(ItemStack a, ItemStack b) {
		if (a == b)
			return true;

		if (a.getItem() != b.getItem())
			return false;

		if (a.isDamageableItem() ^ b.isDamageableItem())
			return false;

		return a.isDamageableItem() || a.getDamageValue() == b.getDamageValue();
	}

	public static List<ItemStack> increaseStackSize(ItemStack stack, int addAmount) {
		int maxCount = stack.getMaxStackSize();

		if (stack.getCount() + addAmount <= maxCount) {
			stack.setCount(stack.getCount() + addAmount);

			return Collections.emptyList();
		}

		List<ItemStack> newStacks = new ObjectArrayList<>((int)((addAmount + stack.getCount()) / (float)maxCount));

		while (addAmount > 0) {
			ItemStack copy = stack.copy();

			copy.setCount(Math.min(maxCount, addAmount));
			newStacks.add(copy);

			addAmount -= copy.getCount();
		}

		return newStacks;
	}

	public static ItemStack loadStackFromNbt(Level level, CompoundTag nbt) {
		return ItemStack.CODEC.decode(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow().getFirst();
	}
}
