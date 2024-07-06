package net.tslat.aoa3.content.item.misc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.registration.item.AoAItems;
import net.tslat.aoa3.data.client.MiscellaneousReloadListener;
import net.tslat.aoa3.integration.IntegrationManager;
import net.tslat.aoa3.integration.patchouli.PatchouliIntegration;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.List;

public class WornBook extends WrittenBookItem {
	public WornBook() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack bookStack = player.getItemInHand(hand);

		if (player instanceof ServerPlayer pl) {
			if (!InventoryUtil.hasItem(pl, AoAItems.BLANK_REALMSTONE)) {
				InventoryUtil.giveItemTo(pl, AoAItems.BLANK_REALMSTONE);
				pl.sendSystemMessage(LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("wornBook.droppedRealmstone")));
				PlayerUtil.getAdventPlayer(pl).addPatchouliBook(AdventOfAscension.id("worn_book"));
			}
		}
		else {
			if (IntegrationManager.isPatchouliActive()) {
				PatchouliIntegration.openBook(AdventOfAscension.id("worn_book"));
			}
			else {
				ClientOperations.displayWornBookGui();
			}
		}

		return InteractionResultHolder.success(bookStack);
	}

	public static ItemStack makeBook() {
		ItemStack book = new ItemStack(AoAItems.WORN_BOOK.get());
		List<Filterable<Component>> pages = new ObjectArrayList<>();
		WrittenBookContent content = new WrittenBookContent(Filterable.passThrough(LocaleUtil.getLocaleString("item.aoa3.worn_book")), LocaleUtil.getLocaleString("entity.aoa3.corrupted_traveller"), 0, pages, true);
		String rawData = MiscellaneousReloadListener.DATA.get(AoAItems.WORN_BOOK.get());

		if (rawData != null) {
			String[] lines = rawData.split("\n");

			for (String line : lines) {
				pages.add(Filterable.passThrough(Component.literal(line.replaceAll("<br>", "\n"))));
			}
		}

		book.set(DataComponents.WRITTEN_BOOK_CONTENT, content);

		return book;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}
}
