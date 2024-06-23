package net.tslat.aoa3.integration.patchouli;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.gui.adventgui.AdventGuiTabLore;
import net.tslat.aoa3.integration.IntegrationManager;

public final class PatchouliClientIntegration {
	public static void init() {
		NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Opening.class, PatchouliClientIntegration::onBookOpen);
	}

	private static void onBookOpen(final ScreenEvent.Opening ev) {
		if (!IntegrationManager.isPatchouliActive())
			return;

		PatchouliIntegration.getBookIdFromScreen(ev.getScreen())
				.filter(AdventOfAscension::isAoA)
				.ifPresent(AdventGuiTabLore::bookOpened);
	}
}
