package net.tslat.aoa3.common.networking.packets.adventplayer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.player.AoAPlayerEventListener;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.PlayerUtil;

public record ToggleAoAAbilityPacket(AoASkill skill, String abilityUniqueId) implements AoAPacket {
	public static final Type<ToggleAoAAbilityPacket> TYPE = new Type<>(AdventOfAscension.id("toggle_aoa_ability"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ToggleAoAAbilityPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY),
			ToggleAoAAbilityPacket::skill,
			ByteBufCodecs.STRING_UTF8,
			ToggleAoAAbilityPacket::abilityUniqueId,
			ToggleAoAAbilityPacket::new);

	public ToggleAoAAbilityPacket(AoASkill skill, AoAAbility.Instance ability) {
		this(skill, ability.getUniqueIdentifier());
	}

	@Override
	public Type<? extends ToggleAoAAbilityPacket> type() {
		return TYPE;
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> {
			AoASkill.Instance skillInstance = PlayerUtil.getAdventPlayer(context.player()).getSkill(this.skill);
			AoAAbility.Instance abilityInstance = skillInstance.getAbilityMap().get(this.abilityUniqueId);

			if (abilityInstance.getListenerState() == AoAPlayerEventListener.ListenerState.ACTIVE) {
				abilityInstance.disable(AoAPlayerEventListener.ListenerState.MANUALLY_DISABLED, false);
			}
			else if (abilityInstance.getListenerState() == AoAPlayerEventListener.ListenerState.MANUALLY_DISABLED) {
				abilityInstance.reenable(false);
			}
		});
	}
}
