package net.tslat.aoa3.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class AdvancementUtil {
	public static Optional<AdvancementHolder> getAdvancement(ServerLevel level, ResourceLocation id) {
		return Optional.ofNullable(level.getServer().getAdvancements().get(id));
	}

	public static boolean grantCriterion(ServerPlayer player, ResourceLocation id, String criterion) {
		return getAdvancement(player.serverLevel(), id).map(adv -> player.getAdvancements().award(adv, criterion)).orElse(false);
	}

	public static boolean revokeCriterion(ServerPlayer player, ResourceLocation id, String criterion) {
		return getAdvancement(player.serverLevel(), id).map(adv -> player.getAdvancements().revoke(adv, criterion)).orElse(false);
	}

	public static boolean completeAdvancement(ServerPlayer player, ResourceLocation id) {
		return getAdvancement(player.serverLevel(), id).map(adv -> {
			PlayerAdvancements advancements = player.getAdvancements();
			boolean granted = false;

			for (String criterion : advancements.getOrStartProgress(adv).getRemainingCriteria()) {
				granted |= advancements.award(adv, criterion);
			}

			return granted;
		}).orElse(false);
	}

	public static boolean isAdvancementCompleted(ServerPlayer player, ResourceLocation id) {
		return getAdvancement(player.serverLevel(), id).map(adv -> player.getAdvancements().getOrStartProgress(adv).isDone()).orElse(false);
	}
}
