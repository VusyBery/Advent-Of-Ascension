package net.tslat.aoa3.common.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.client.gui.hud.XpParticlesRenderer;
import net.tslat.aoa3.common.registration.AoAConfigs;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.player.skill.AoASkill;

public record XpGainPacket(ResourceLocation skill, float xp, boolean levelUp) implements AoAPacket {
	public static final ResourceLocation ID = AdventOfAscension.id("xp_gain");
	public static final Type<XpGainPacket> TYPE = new Type<>(AdventOfAscension.id("xp_gain"));
	public static final StreamCodec<RegistryFriendlyByteBuf, XpGainPacket> CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			XpGainPacket::skill,
			ByteBufCodecs.FLOAT,
			XpGainPacket::xp,
			ByteBufCodecs.BOOL,
			XpGainPacket::levelUp,
			XpGainPacket::new);

	@Override
	public Type<? extends XpGainPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		if (AoAConfigs.CLIENT.showXpParticles.get()) {
			AoASkill skill = AoASkills.getSkill(this.skill);

			if (skill != null)
				XpParticlesRenderer.addXpParticle(skill, this.xp, this.levelUp);
		}
	}
}
