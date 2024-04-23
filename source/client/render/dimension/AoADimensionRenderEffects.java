package net.tslat.aoa3.client.render.dimension;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.tslat.aoa3.advent.AdventOfAscension;

public final class AoADimensionRenderEffects {
	public static void init() {
		AdventOfAscension.getModEventBus().addListener(EventPriority.NORMAL, false, RegisterDimensionSpecialEffectsEvent.class, ev -> {
			final LunalusRenderingEffects lunalusRenderer = new LunalusRenderingEffects();

			ev.register(VoidSkyRenderingEffects.ID, new VoidSkyRenderingEffects());
			ev.register(AbyssRenderingEffects.ID, new AbyssRenderingEffects());
			ev.register(BarathosRenderingEffects.ID, new BarathosRenderingEffects());
			ev.register(ShyrelandsRenderingEffects.ID, new ShyrelandsRenderingEffects());
			ev.register(LBoreanRenderingEffects.ID, new LBoreanRenderingEffects());
			ev.register(LunalusRenderingEffects.ID, lunalusRenderer);
			ev.register(NowhereRenderingEffects.ID, new NowhereRenderingEffects(lunalusRenderer));
		});
	}
}
