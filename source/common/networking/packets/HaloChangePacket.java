package net.tslat.aoa3.common.networking.packets;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.player.halo.HaloTypes;
import net.tslat.aoa3.player.halo.PlayerHaloManager;

import java.util.UUID;

public record HaloChangePacket(UUID player, HaloTypes selected) implements AoAPacket {
	public static final Type<HaloChangePacket> TYPE = new Type<>(AdventOfAscension.id("halo_change"));
	public static final StreamCodec<FriendlyByteBuf, HaloChangePacket> CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC,
			HaloChangePacket::player,
			NeoForgeStreamCodecs.enumCodec(HaloTypes.class),
			HaloChangePacket::selected,
			HaloChangePacket::new);

	@Override
	public Type<? extends HaloChangePacket> type() {
		return TYPE;
	}

	public void receiveMessage(IPayloadContext context) {
		PlayerHaloManager.updateSelectedHalo(this.player, this.selected, false);
	}
}
