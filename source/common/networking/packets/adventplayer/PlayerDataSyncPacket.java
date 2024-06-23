package net.tslat.aoa3.common.networking.packets.adventplayer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.client.player.ClientPlayerDataManager;

public record PlayerDataSyncPacket(CompoundTag data) implements AoAPacket {
    public static final Type<PlayerDataSyncPacket> TYPE = new Type<>(AdventOfAscension.id("player_data_sync"));
    public static final StreamCodec<FriendlyByteBuf, PlayerDataSyncPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, PlayerDataSyncPacket::data,
            PlayerDataSyncPacket::new);

    @Override
    public Type<? extends PlayerDataSyncPacket> type() {
        return TYPE;
    }

    @Override
    public void receiveMessage(IPayloadContext context) {
        ClientPlayerDataManager.get().loadFromNbt(this.data);
    }
}
