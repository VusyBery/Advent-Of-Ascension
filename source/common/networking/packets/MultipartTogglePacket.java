package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.content.entity.base.AoAMultipartEntity;

public record MultipartTogglePacket(int entityId, boolean active) implements AoAPacket {
	public static final Type<MultipartTogglePacket> TYPE = new Type<>(AdventOfAscension.id("multipart_toggle"));
	public static final StreamCodec<FriendlyByteBuf, MultipartTogglePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			MultipartTogglePacket::entityId,
			ByteBufCodecs.BOOL,
			MultipartTogglePacket::active,
			MultipartTogglePacket::new);

	@Override
	public Type<? extends MultipartTogglePacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (ClientOperations.getLevel().getEntity(this.entityId) instanceof AoAMultipartEntity multipart) {
				multipart.toggleMultipart(this.active);
				((Entity)multipart).refreshDimensions();
			}
		});
	}
}
