package net.tslat.aoa3.common.toast;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.custom.AoAToastTypes;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.ToastUtil;

public record AbilityUnlockToastData(AoASkill skill, AoAAbility ability) implements CustomToastData {
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityUnlockToastData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY),
            AbilityUnlockToastData::skill,
            ByteBufCodecs.registry(AoARegistries.ABILITIES_REGISTRY_KEY),
            AbilityUnlockToastData::ability,
            AbilityUnlockToastData::new);

    @Override
    public Type<AbilityUnlockToastData> type() {
        return AoAToastTypes.ABILITY_UNLOCK.get();
    }

    public static void sendToastPopupTo(ServerPlayer pl, AoAAbility.Instance ability) {
        new AbilityUnlockToastData(ability.getSkill().type(), ability.type()).sendToPlayer(pl);
    }

    @Override
    public void handleOnClient() {
        ToastUtil.addConfigOptionalToast(() -> ToastUtil.addAbilityUnlockToast(this), () -> LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("abilityUnlocked"), ChatFormatting.GREEN, this.skill.getName(), this.ability.getName()));
    }
}
