package net.tslat.aoa3.util;

import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public final class InventoryUtil {
    public static boolean isHoldingItem(Player player, ItemLike item) {
        final Item item2 = item.asItem();

        return isHoldingItem(player, stack -> stack.is(item2));
    }

    public static boolean isHoldingItem(Player player, TagKey<Item> tag) {
        return isHoldingItem(player, stack -> stack.is(tag));
    }

    public static boolean isHoldingItem(Player player, Predicate<ItemStack> predicate) {
        return predicate.test(player.getMainHandItem()) || predicate.test(player.getOffhandItem());
    }

    public static boolean hasItemInHotbar(Player player, ItemLike item) {
        final Item item2 = item.asItem();

        return hasItemInHotbar(player, stack -> stack.is(item2));
    }

    public static boolean hasItemInHotbar(Player player, TagKey<Item> tag) {
        return hasItemInHotbar(player, stack -> stack.is(tag));
    }

    public static boolean hasItemInHotbar(Player player, Predicate<ItemStack> predicate) {
        return findHotbarItem(player, predicate).isPresent();
    }

    public static boolean hasItem(Player player, ItemLike item) {
        final Item item2 = item.asItem();

        return hasItem(player, stack -> stack.is(item2));
    }

    public static boolean hasItem(Player player, TagKey<Item> tag) {
        return hasItem(player, stack -> stack.is(tag));
    }

    public static boolean hasItem(Player player, Predicate<ItemStack> predicate) {
        return findItem(player, predicate).isPresent();
    }

    public static Optional<IntObjectPair<ItemStack>> findItem(Player player, ItemLike item) {
        final Item item2 = item.asItem();

        return findItem(player, stack -> stack.is(item2));
    }

    public static Optional<IntObjectPair<ItemStack>> findItem(Player player, TagKey<Item> tag) {
        return findItem(player, stack -> stack.is(tag));
    }

    public static Optional<IntObjectPair<ItemStack>> findItem(Player player, Predicate<ItemStack> predicate) {
        ItemStack mainHandStack = player.getMainHandItem();

        if (!mainHandStack.isEmpty() && predicate.test(mainHandStack))
            return Optional.of(IntObjectPair.of(player.getInventory().selected, mainHandStack));

        ItemStack offhandStack = player.getOffhandItem();

        if (!offhandStack.isEmpty() && predicate.test(offhandStack))
            return Optional.of(IntObjectPair.of(Inventory.SLOT_OFFHAND, offhandStack));

        int i = 0;

        for (NonNullList<ItemStack> compartment : player.getInventory().compartments) {
            for (ItemStack stack : compartment) {
                if (stack != mainHandStack && stack != offhandStack && !stack.isEmpty() && predicate.test(stack))
                    return Optional.of(IntObjectPair.of(i, stack));

                i++;
            }
        }

        return Optional.empty();
    }

    public static boolean findItemForConsumption(Player player, ItemLike item, int amount, boolean shouldConsume) {
        final Item item2 = item.asItem();

        return findItemForConsumption(player, stack -> stack.is(item2), amount, shouldConsume);
    }

    public static boolean findItemForConsumption(Player player, TagKey<Item> tag, int amount, boolean shouldConsume) {
        return findItemForConsumption(player, stack -> stack.is(tag), amount, shouldConsume);
    }

    public static boolean findItemForConsumption(Player player, Predicate<ItemStack> predicate, int amount, boolean shouldConsume) {
        if (amount <= 0)
            return true;

        final List<ItemStack> foundStacks = new ObjectArrayList<>(amount / 64);
        final AtomicInteger count = new AtomicInteger(amount);

        return findItem(player, stack -> {
            if (predicate.test(stack)) {
                foundStacks.add(stack);

                return count.updateAndGet(value -> value - stack.getCount()) <= 0;
            }

            return false;
        }).map(ignored -> {
            if (shouldConsume && !player.level().isClientSide) {
                int remaining = amount;

                for (ItemStack stack : foundStacks) {
                    int reduction = Math.min(remaining, stack.getCount());

                    stack.shrink(reduction);

                    if ((remaining -= reduction) <= 0)
                        break;
                }

                player.inventoryMenu.broadcastChanges();
            }

            return true;
        }).orElse(false);
    }

    public static Optional<IntObjectPair<ItemStack>> findHotbarItem(Player player, ItemLike item) {
        final Item item2 = item.asItem();

        return findHotbarItem(player, stack -> stack.is(item2));
    }

    public static Optional<IntObjectPair<ItemStack>> findHotbarItem(Player player, TagKey<Item> tag) {
        return findHotbarItem(player, stack -> stack.is(tag));
    }

    public static Optional<IntObjectPair<ItemStack>> findHotbarItem(Player player, Predicate<ItemStack> predicate) {
        final Inventory inventory = player.getInventory();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && predicate.test(stack))
                return Optional.of(IntObjectPair.of(i, stack));
        }

        return Optional.empty();
    }

    public static void giveItemsTo(ServerPlayer player, ItemLike... items) {
        giveItemsTo(player, Arrays.stream(items).map(item -> item.asItem().getDefaultInstance()).toArray(ItemStack[]::new));
    }

    public static void giveItemsTo(ServerPlayer player, ItemStack... stacks) {
        giveItemsTo(player, List.of(stacks));
    }

    public static void giveItemsTo(ServerPlayer player, Collection<ItemStack> stacks) {
        boolean update = false;

        for (ItemStack stack : stacks) {
            update |= giveItemTo(player, stack, false);
        }

        if (update)
            player.inventoryMenu.broadcastChanges();
    }

    public static boolean giveItemTo(ServerPlayer player, ItemLike item) {
        return giveItemTo(player, item.asItem().getDefaultInstance());
    }

    public static boolean giveItemTo(ServerPlayer player, ItemStack stack) {
        return giveItemTo(player, stack, true);
    }

    public static boolean giveItemTo(ServerPlayer player, ItemStack stack, boolean updateInventory) {
        if (stack.isEmpty())
            return false;

        int originalCount = stack.getCount();

        if (player.getInventory().add(stack) && stack.isEmpty()) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1) * 2);

            if (updateInventory)
                player.inventoryMenu.broadcastChanges();

            return true;
        }

        ItemEntity itemEntity = player.drop(stack, false);

        if (itemEntity != null) {
            itemEntity.setNoPickUpDelay();
            itemEntity.setTarget(player.getUUID());
        }

        if (originalCount <= stack.getCount())
            return false;

        if (updateInventory)
            player.inventoryMenu.broadcastChanges();

        return true;
    }

    public static void clearItems(ServerPlayer player, ItemLike... items) {
        clearItems(player, Arrays.stream(items).<Predicate<ItemStack>>map(item -> stack -> stack.is(item.asItem())).toArray(Predicate[]::new));
    }

    public static void clearItems(ServerPlayer player, ItemStack... stacks) {
        clearItems(player, Arrays.stream(stacks).<Predicate<ItemStack>>map(stack -> stack2 -> ItemStack.isSameItemSameComponents(stack, stack2)).toArray(Predicate[]::new));
    }

    public static void clearItems(ServerPlayer player, TagKey<Item>... tags) {
        clearItems(player, Arrays.stream(tags).<Predicate<ItemStack>>map(tag -> stack -> stack.is(tag)).toArray(Predicate[]::new));
    }

    public static void clearItems(ServerPlayer player, Predicate<ItemStack>... predicates) {
        for (NonNullList<ItemStack> compartment : player.getInventory().compartments) {
            for (ItemStack stack : compartment) {
                if (stack.isEmpty())
                    continue;

                for (Predicate<ItemStack> predicate : predicates) {
                    if (predicate.test(stack)) {
                        stack.setCount(0);

                        break;
                    }
                }
            }
        }
    }
}
