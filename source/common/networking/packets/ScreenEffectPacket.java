package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.gui.overlay.ScreenEffectRenderer;
import net.tslat.aoa3.library.constant.ScreenImageEffect;

public record ScreenEffectPacket(ScreenImageEffect effect) implements AoAPacket {
	public static final Type<ScreenEffectPacket> TYPE = new Type<>(AdventOfAscension.id("screen_effect"));
	public static final StreamCodec<FriendlyByteBuf, ScreenEffectPacket> CODEC = StreamCodec.composite(
			ScreenImageEffect.STREAM_CODEC,
			ScreenEffectPacket::effect,
			ScreenEffectPacket::new);

	@Override
	public Type<? extends ScreenEffectPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		ScreenEffectRenderer.addScreenEffect(this.effect);
	}
}
