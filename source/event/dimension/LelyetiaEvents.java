package net.tslat.aoa3.event.dimension;

import net.minecraft.world.entity.player.Player;
import net.tslat.aoa3.common.registration.item.AoATools;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.PlayerUtil;

public class LelyetiaEvents {
	public static void doPlayerTick(Player pl) {
		if (pl.getOffhandItem().is(AoATools.DISTORTING_ARTIFACT) || InventoryUtil.hasItemInHotbar(pl, AoATools.DISTORTING_ARTIFACT))
			return;

		if (pl.getY() <= -25 && PlayerUtil.shouldPlayerBeAffected(pl))
			pl.teleportTo(pl.getX(), pl.level().getMaxBuildHeight(), pl.getZ());
	}
}
