package net.tslat.aoa3.common.networking.packets.adventplayer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.AoAPacket;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.PlayerUtil;

public record SyncAoAAbilityDataPacket(AoASkill skill, String abilityUniqueId, String data) implements AoAPacket {
	public static final Type<SyncAoAAbilityDataPacket> TYPE = new Type<>(AdventOfAscension.id("sync_aoa_ability_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncAoAAbilityDataPacket> CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY),
			SyncAoAAbilityDataPacket::skill,
			ByteBufCodecs.STRING_UTF8,
			SyncAoAAbilityDataPacket::abilityUniqueId,
			ByteBufCodecs.STRING_UTF8,
			SyncAoAAbilityDataPacket::data,
			SyncAoAAbilityDataPacket::new);

	@Override
	public Type<? extends SyncAoAAbilityDataPacket> type() {
		return TYPE;
	}

	public SyncAoAAbilityDataPacket(AoAAbility.Instance ability, String data) {
		this(ability.getSkill().type(), ability.getUniqueIdentifier(), data);
	}

	@Override
	public void receiveMessage(IPayloadContext context) {
		context.enqueueWork(() -> {
			AoASkill.Instance skillInstance = PlayerUtil.getAdventPlayer(context.player()).getSkill(this.skill);
			AoAAbility.Instance abilityInstance = skillInstance.getAbilityMap().get(this.abilityUniqueId);

			abilityInstance.receiveInteractionDataFromClient(this.data);
		});
	}
}
