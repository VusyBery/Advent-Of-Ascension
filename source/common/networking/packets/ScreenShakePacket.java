package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;

public record ScreenShakePacket(double frequency, float strength, float dampening) implements AoAPacket {
	public static final Type<ScreenShakePacket> TYPE = new Type<>(AdventOfAscension.id("screen_shake"));
	public static final StreamCodec<FriendlyByteBuf, ScreenShakePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, ScreenShakePacket::frequency,
			ByteBufCodecs.FLOAT, ScreenShakePacket::strength,
			ByteBufCodecs.FLOAT, ScreenShakePacket::dampening,
            ScreenShakePacket::new);

	@Override
	public Type<? extends ScreenShakePacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		ClientOperations.addScreenShake(this.frequency, this.strength, this.dampening);
	}
}
