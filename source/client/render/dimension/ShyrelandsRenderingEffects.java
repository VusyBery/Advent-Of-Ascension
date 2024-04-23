package net.tslat.aoa3.client.render.dimension;

import net.minecraft.resources.ResourceLocation;
import net.tslat.aoa3.advent.AdventOfAscension;

public class ShyrelandsRenderingEffects extends AoADimensionEffectsRenderer {
    public static final ResourceLocation ID = AdventOfAscension.id("shyrelands");

    ShyrelandsRenderingEffects() {
        super(40, true, SkyType.NORMAL, false, true);
    }
}