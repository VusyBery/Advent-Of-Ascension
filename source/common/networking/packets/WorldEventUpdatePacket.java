package net.tslat.aoa3.common.networking.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.world.event.AoAWorldEvent;
import net.tslat.aoa3.content.world.event.AoAWorldEventManager;

public record WorldEventUpdatePacket(AoAWorldEvent.Type<?> eventType, ResourceLocation eventId, CompoundTag data) implements AoAPacket {
	public static final Type<WorldEventUpdatePacket> TYPE = new Type<>(AdventOfAscension.id("world_event_update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, WorldEventUpdatePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(AoARegistries.WORLD_EVENT_TYPE_REGISTRY_KEY), WorldEventUpdatePacket::eventType,
			ResourceLocation.STREAM_CODEC, WorldEventUpdatePacket::eventId,
			ByteBufCodecs.TRUSTED_COMPOUND_TAG, WorldEventUpdatePacket::data,
			WorldEventUpdatePacket::new);

	@Override
	public Type<? extends WorldEventUpdatePacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> AoAWorldEventManager.syncFromServer(this.eventType, this.eventId, this.data));
	}
}
