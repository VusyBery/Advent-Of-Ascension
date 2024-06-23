package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.command.WikiCommand;

public record WikiSearchPacket(String searchString) implements AoAPacket {
	public static final Type<WikiSearchPacket> TYPE = new Type<>(AdventOfAscension.id("wiki_search"));
	public static final StreamCodec<FriendlyByteBuf, WikiSearchPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			WikiSearchPacket::searchString,
			WikiSearchPacket::new);

	@Override
	public Type<? extends WikiSearchPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> WikiCommand.handleSearchRequest(this.searchString, ClientOperations.getPlayer()));
	}
}
