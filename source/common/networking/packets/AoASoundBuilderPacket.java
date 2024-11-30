package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.library.builder.SoundBuilder;

public record AoASoundBuilderPacket(SoundBuilder soundBuilder) implements AoAPacket {
	public static final Type<AoASoundBuilderPacket> TYPE = new Type<>(AdventOfAscension.id("sound_builder"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AoASoundBuilderPacket> CODEC = StreamCodec.composite(
			SoundBuilder.STREAM_CODEC, AoASoundBuilderPacket::soundBuilder,
			AoASoundBuilderPacket::new);
	@Override
	public Type<? extends AoASoundBuilderPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(this.soundBuilder::execute);
	}
}
