package net.tslat.aoa3.common.networking.packets.setup;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.configtask.PlayerHalosHandshakeTask;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.player.halo.PlayerHaloContainer;
import net.tslat.aoa3.player.halo.PlayerHaloManager;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record PlayerHalosLoginSyncPacket(Optional<Map<UUID, PlayerHaloContainer>> halosMap) implements AoAPacket {
	public static final Type<PlayerHalosLoginSyncPacket> TYPE = new Type<>(AdventOfAscension.id("player_halos_sync"));
	public static final StreamCodec<FriendlyByteBuf, PlayerHalosLoginSyncPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ByteBufCodecs.map(Object2ObjectOpenHashMap::new, UUIDUtil.STREAM_CODEC, PlayerHaloContainer.STREAM_CODEC)), PlayerHalosLoginSyncPacket::halosMap,
			PlayerHalosLoginSyncPacket::new);

	public PlayerHalosLoginSyncPacket(Map<UUID, PlayerHaloContainer> halosMap) {
		this(Optional.of(halosMap));
	}

	private PlayerHalosLoginSyncPacket() {
		this(Optional.empty());
	}

	@Override
	public Type<? extends PlayerHalosLoginSyncPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		this.halosMap.ifPresentOrElse(map -> {
			PlayerHaloManager.syncFromServer(map);
			context.reply(new PlayerHalosLoginSyncPacket());
		}, () -> context.finishCurrentTask(PlayerHalosHandshakeTask.TYPE));
	}
}
