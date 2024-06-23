package net.tslat.aoa3.common.toast;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.ToastPopupPacket;

public interface CustomToastData {
    Type<? extends CustomToastData> type();
    void handleOnClient();
    default void sendToPlayer(ServerPlayer pl) {
        AoANetworking.sendToPlayer(pl, new ToastPopupPacket(this));
    }

    @FunctionalInterface
     interface Type<D extends CustomToastData> {
        StreamCodec<RegistryFriendlyByteBuf, D> streamCodec();
    }
}