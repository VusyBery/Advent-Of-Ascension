package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.player.halo.HaloTypes;
import net.tslat.aoa3.player.halo.PlayerHaloManager;

public record HaloSelectPacket(HaloTypes.Selectable selected) implements AoAPacket {
	public static final Type<HaloSelectPacket> TYPE = new Type<>(AdventOfAscension.id("halo_select"));
	public static final StreamCodec<RegistryFriendlyByteBuf, HaloSelectPacket> CODEC = StreamCodec.composite(
			NeoForgeStreamCodecs.enumCodec(HaloTypes.Selectable.class),
			HaloSelectPacket::selected,
			HaloSelectPacket::new);

	@Override
	public Type<? extends HaloSelectPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		PlayerHaloManager.updateSelectedHalo(context.player().getUUID(), this.selected.toBaseType(), true);
	}
}
