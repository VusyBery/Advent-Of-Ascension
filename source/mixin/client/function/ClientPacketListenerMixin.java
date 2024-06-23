package net.tslat.aoa3.mixin.client.function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.tslat.aoa3.client.gui.adventgui.AdventMainGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleAwardStats", at = @At("TAIL"))
    public void aoa3$injectBestiaryStatsReceiver(ClientboundAwardStatsPacket packet, CallbackInfo callback) {
        if (Minecraft.getInstance().screen instanceof AdventMainGui adventGui)
            adventGui.onStatsUpdated();
    }
}
