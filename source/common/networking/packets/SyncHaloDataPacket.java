package net.tslat.aoa3.common.networking.packets;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.advent.Logging;
import net.tslat.aoa3.player.halo.PlayerHaloContainer;
import net.tslat.aoa3.player.halo.PlayerHaloManager;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.UUID;

public record SyncHaloDataPacket(Map<UUID, PlayerHaloContainer> halos) implements AoAPacket {
	public static final Type<SyncHaloDataPacket> TYPE = new Type<>(AdventOfAscension.id("sync_halo_data"));
	public static final StreamCodec<FriendlyByteBuf, SyncHaloDataPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.map(Object2ObjectOpenHashMap::new, UUIDUtil.STREAM_CODEC, PlayerHaloContainer.STREAM_CODEC),
			SyncHaloDataPacket::halos,
			SyncHaloDataPacket::new);

	@Override
	public Type<? extends SyncHaloDataPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		Logging.logMessage(Level.DEBUG, "Received player halos map update");
		PlayerHaloManager.syncFromServer(this.halos);
	}
}
