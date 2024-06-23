package net.tslat.aoa3.common.networking.packets.patchouli;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.PlayerUtil;

public record AccountPatchouliBookPacket(ResourceLocation book) implements AoAPacket {
	public static final Type<AccountPatchouliBookPacket> TYPE = new Type<>(AdventOfAscension.id("account_patchouli_book"));
	public static final StreamCodec<FriendlyByteBuf, AccountPatchouliBookPacket> CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			AccountPatchouliBookPacket::book,
			AccountPatchouliBookPacket::new);

	@Override
	public Type<? extends AccountPatchouliBookPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		((ServerPlayerDataManager)PlayerUtil.getAdventPlayer(context.player())).addPatchouliBook(book);
	}
}
