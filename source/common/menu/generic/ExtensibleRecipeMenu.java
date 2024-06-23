package net.tslat.aoa3.common.menu.generic;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.event.custom.AoAEvents;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class ExtensibleRecipeMenu<C extends Container, I extends RecipeInput> extends ExtensibleContainerMenu<C> {
    public ExtensibleRecipeMenu(MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess accessValidator) {
        super(type, containerId, playerInventory, accessValidator);
    }

    protected abstract I createRecipeInput();

    protected <R extends Recipe<I>> void updateRecipeOutput(RecipeType<R> recipeType, Player player, ResultContainer resultContainer, Predicate<RecipeHolder<R>> recipePredicate) {
        if (player instanceof ServerPlayer serverPlayer) {
            final I recipeInput = createRecipeInput();
            final ServerLevel level = serverPlayer.serverLevel();
            final ItemStack outputStack = getOrFindRecipe(recipeType, recipeInput, resultContainer, level)
                    .filter(recipeHolder -> recipePredicate.test(recipeHolder) && resultContainer.setRecipeUsed(level, serverPlayer, recipeHolder))
                    .map(RecipeHolder::value)
                    .map(recipe -> recipe.assemble(recipeInput, level.registryAccess()))
                    .filter(recipeResult -> recipeResult.isItemEnabled(level.enabledFeatures()))
                    .filter(stack -> !AoAEvents.firePlayerCraftingEvent(player, stack, getInventory(), resultContainer))
                    .orElse(ItemStack.EMPTY);

            if (!ItemStack.isSameItemSameComponents(getOutputItem(), outputStack)) {
                setOutputItem(outputStack);
                setRemoteSlot(getOutputSlotIndex(), outputStack);
                serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, incrementStateId(), getOutputSlotIndex(), outputStack));
            }
        }
    }

    protected <R extends Recipe<I>> Optional<RecipeHolder<R>> getOrFindRecipe(RecipeType<R> recipeType, I recipeInput, ResultContainer resultContainer, Level level) {
        return Optional.ofNullable((RecipeHolder<R>)resultContainer.getRecipeUsed())
                .filter(holder -> holder.value().matches(recipeInput, level))
                .or(() -> level.getRecipeManager().getRecipeFor(recipeType, recipeInput, level));
    }
}
