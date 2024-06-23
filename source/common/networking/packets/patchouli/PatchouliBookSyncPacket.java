package net.tslat.aoa3.common.networking.packets.patchouli;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.networking.packets.AoAPacket;

import java.util.List;

public record PatchouliBookSyncPacket(List<ResourceLocation> books) implements AoAPacket {
	public static final Type<PatchouliBookSyncPacket> TYPE = new Type<>(AdventOfAscension.id("patchouli_book_sync"));
	public static final StreamCodec<FriendlyByteBuf, PatchouliBookSyncPacket> CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
			PatchouliBookSyncPacket::books,
			PatchouliBookSyncPacket::new);

	@Override
	public Type<? extends PatchouliBookSyncPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		ClientOperations.syncModonomiconBooks(books);
	}
}
