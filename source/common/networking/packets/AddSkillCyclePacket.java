package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.PlayerUtil;

public record AddSkillCyclePacket(AoASkill skill) implements AoAPacket {
	public static final Type<AddSkillCyclePacket> TYPE = new Type<>(AdventOfAscension.id("add_skill_cycle"));
	public static final StreamCodec<RegistryFriendlyByteBuf, AddSkillCyclePacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY), AddSkillCyclePacket::skill,
			AddSkillCyclePacket::new);

	@Override
	public Type<? extends AddSkillCyclePacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> PlayerUtil.getAdventPlayer(context.player()).getSkill(this.skill).addCycle());
	}
}
