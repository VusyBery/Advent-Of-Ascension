package net.tslat.aoa3.common.networking.packets.leaderboard;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.leaderboard.task.LeaderboardActions;
import net.tslat.aoa3.player.skill.AoASkill;

import java.util.Optional;

public record LeaderboardPageDataRequestPacket(Optional<AoASkill> skill, int page) implements AoAPacket {
	public static final Type<LeaderboardPageDataRequestPacket> TYPE = new Type<>(AdventOfAscension.id("leaderboard_page_data_request"));
	public static final StreamCodec<RegistryFriendlyByteBuf, LeaderboardPageDataRequestPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.optional(ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY)),
			LeaderboardPageDataRequestPacket::skill,
			ByteBufCodecs.VAR_INT,
			LeaderboardPageDataRequestPacket::page,
			LeaderboardPageDataRequestPacket::new);

	@Override
	public Type<? extends LeaderboardPageDataRequestPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		LeaderboardActions.getPlayerData(this.skill, this.page, (ServerPlayer)context.player());
	}
}
