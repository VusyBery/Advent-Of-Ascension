package net.tslat.aoa3.content.item.lootbox;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.LootUtil;

import java.util.List;

public class CrystalBox extends Item {
	public CrystalBox() {
		super(new Item.Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);

		if (player instanceof ServerPlayer pl) {
			InventoryUtil.giveItemsTo(pl, LootUtil.generateLoot(AdventOfAscension.id("items/crystal_box"), LootUtil.getGiftParameters(pl.serverLevel(), pl.position(), pl)));

			if (!pl.isCreative())
				heldStack.shrink(1);

			pl.inventoryMenu.broadcastChanges();

			return InteractionResultHolder.success(heldStack);
		}

		return InteractionResultHolder.pass(heldStack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(LocaleUtil.getFormattedItemDescriptionText(this, LocaleUtil.ItemDescriptionType.NEUTRAL, 1));
	}
}
