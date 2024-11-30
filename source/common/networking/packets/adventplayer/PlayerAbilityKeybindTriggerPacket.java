package net.tslat.aoa3.common.networking.packets.adventplayer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.player.AoAPlayerEventListener;

import java.util.List;

public record PlayerAbilityKeybindTriggerPacket(List<String> abilities) implements AoAPacket {
	public static final Type<PlayerAbilityKeybindTriggerPacket> TYPE = new Type<>(AdventOfAscension.id("player_ability_keybind_trigger"));
	public static final StreamCodec<FriendlyByteBuf, PlayerAbilityKeybindTriggerPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), PlayerAbilityKeybindTriggerPacket::abilities,
			PlayerAbilityKeybindTriggerPacket::new);

	@Override
	public Type<? extends PlayerAbilityKeybindTriggerPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> AoAPlayerEventListener.onKeyPress(context.player(), this.abilities));
	}
}