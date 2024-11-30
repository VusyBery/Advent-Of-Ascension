package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.toast.CustomToastData;

public record ToastPopupPacket(CustomToastData toastData) implements AoAPacket {
	public static final Type<ToastPopupPacket> TYPE = new Type<>(AdventOfAscension.id("toast_trigger"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ToastPopupPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(AoARegistries.CUSTOM_TOAST_DATA_REGISTRY_KEY).dispatch(CustomToastData::type, CustomToastData.Type::streamCodec), ToastPopupPacket::toastData,
			ToastPopupPacket::new);

	@Override
	public Type<? extends ToastPopupPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(this.toastData::handleOnClient);
	}
}
