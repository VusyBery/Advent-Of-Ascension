package net.tslat.aoa3.common.networking.packets.patchouli;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.common.registration.AoARegistries;

public record GivePatchouliBookPacket(ResourceLocation book) implements AoAPacket {
	public static final Type<GivePatchouliBookPacket> TYPE = new Type<>(AdventOfAscension.id("give_patchouli_book"));
	public static final StreamCodec<FriendlyByteBuf, GivePatchouliBookPacket> CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			GivePatchouliBookPacket::book,
			GivePatchouliBookPacket::new);

	@Override
	public Type<? extends GivePatchouliBookPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		Item guideBook = AoARegistries.ITEMS.getEntry(ResourceLocation.fromNamespaceAndPath("patchouli", "guide_book"));

		if (guideBook != null && guideBook != Items.AIR) {
			/*ItemStack book = new ItemStack(guideBook);
			ServerPlayer pl = (ServerPlayer)context.player();
			CommandSourceStack commandSource = pl.createCommandSourceStack();
			CommandNode<CommandSourceStack> giveCommand = pl.getServer().getCommands().getDispatcher().getRoot().getChild("give");

			book.getOrCreateTag().putString("patchouli:book", this.book.toString());

			if ((giveCommand != null && giveCommand.canUse(commandSource)) || commandSource.hasPermission(pl.getServer().getOperatorUserPermissionLevel())) {
				if (pl.getInventory().add(book) && book.isEmpty()) {
					book.setCount(1);

					ItemEntity itemEntity = pl.drop(book, false);

					if (itemEntity != null)
						itemEntity.makeFakeItem();

					pl.level().playSound(null, pl.getX(), pl.getY(), pl.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((pl.getRandom().nextFloat() - pl.getRandom().nextFloat()) * 0.7f + 1) * 2f);
					pl.containerMenu.broadcastChanges();
				}
				else {
					ItemEntity itemEntity = pl.drop(book, false);

					if (itemEntity != null) {
						itemEntity.setNoPickUpDelay();
						itemEntity.setThrower(pl);
					}
				}
			}*/ // TODO
		}
	}
}
