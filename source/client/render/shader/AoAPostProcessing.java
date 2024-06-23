package net.tslat.aoa3.client.render.shader;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.advent.Logging;
import net.tslat.aoa3.client.ClientOperations;
import net.tslat.aoa3.common.registration.item.AoAArmour;
import net.tslat.aoa3.common.registration.worldgen.AoADimensions;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.Map;

public final class AoAPostProcessing {
    private static final Map<ResourceLocation, PostChain> ACTIVE_EFFECTS = new Object2ObjectArrayMap<>();

    private static final ResourceLocation DUSTOPIA_DESATURATE = AdventOfAscension.id("shaders/post/dustopia_desaturate.json");
    private static final ResourceLocation NIGHT_VISION_GOGGLES = AdventOfAscension.id("shaders/post/night_vision_goggles.json");

    public static void init() {
        final IEventBus eventBus = NeoForge.EVENT_BUS;

        eventBus.addListener(AoAPostProcessing::playerLogout);
        eventBus.addListener(AoAPostProcessing::playerLogin);
        eventBus.addListener(AoAPostProcessing::playerRespawn);
        eventBus.addListener(AoAPostProcessing::clientTick);
    }

    public static boolean activateEffect(ResourceLocation shader) {
        if (ACTIVE_EFFECTS.containsKey(shader))
            return false;

        try {
            final Minecraft mc = Minecraft.getInstance();
            final PostChain effect = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), shader);

            effect.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            ACTIVE_EFFECTS.put(shader, effect);
        }
        catch (IOException ex) {
            Logging.logMessage(Level.WARN, "Failed to load shader: " + shader, ex);
        }
        catch (JsonSyntaxException ex) {
            Logging.logMessage(Level.WARN, "Failed to parse shader: " + shader, ex);
        }

        return true;
    }

    public static void deactivateEffect(ResourceLocation shader) {
        if (!ACTIVE_EFFECTS.containsKey(shader))
            return;

        ACTIVE_EFFECTS.remove(shader).close();
    }

    public static void resizeShaders(int width, int height) {
        for (PostChain shader : ACTIVE_EFFECTS.values()) {
            shader.resize(width, height);
        }
    }

    public static void shutdownShaders() {
        for (PostChain shader : ACTIVE_EFFECTS.values()) {
            shader.close();
        }

        ACTIVE_EFFECTS.clear();
    }

    public static void doShaderProcessing(float partialTick) {
        if (ACTIVE_EFFECTS.isEmpty())
            return;

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        for (PostChain shader : ACTIVE_EFFECTS.values()) {
            shader.process(partialTick);
        }
    }

    private static void clientTick(final ClientTickEvent.Pre ev) {
        checkNightVisionGogglesShader(ClientOperations.getPlayer());
    }

    private static void playerLogin(final ClientPlayerNetworkEvent.LoggingIn ev) {
        checkDustopiaShader(ClientOperations.getPlayer());
    }

    private static void playerLogout(final ClientPlayerNetworkEvent.LoggingOut ev) {
        shutdownShaders();
    }

    private static void playerRespawn(final ClientPlayerNetworkEvent.Clone ev) {
        checkDustopiaShader(ClientOperations.getPlayer());
    }

    private static void checkDustopiaShader(Player player) {
        if (player.level().dimension() == AoADimensions.DUSTOPIA) {
            activateEffect(DUSTOPIA_DESATURATE);
        }
        else {
            deactivateEffect(DUSTOPIA_DESATURATE);
        }
    }

    private static void checkNightVisionGogglesShader(Player player) {
        if (player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(AoAArmour.NIGHT_VISION_GOGGLES)) {
            activateEffect(NIGHT_VISION_GOGGLES);
        }
        else {
            deactivateEffect(NIGHT_VISION_GOGGLES);
        }
    }
}
