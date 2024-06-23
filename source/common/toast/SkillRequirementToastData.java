package net.tslat.aoa3.common.toast;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.custom.AoAToastTypes;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.ToastUtil;

public record SkillRequirementToastData(AoASkill skill, int level) implements CustomToastData {
    public static final StreamCodec<RegistryFriendlyByteBuf, SkillRequirementToastData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(AoARegistries.SKILLS_REGISTRY_KEY),
            SkillRequirementToastData::skill,
            ByteBufCodecs.VAR_INT,
            SkillRequirementToastData::level,
            SkillRequirementToastData::new);

    @Override
    public Type<SkillRequirementToastData> type() {
        return AoAToastTypes.SKILL_REQUIREMENT.get();
    }

    public static void sendToastPopupTo(ServerPlayer pl, AoASkill skill, int level) {
        new SkillRequirementToastData(skill, level).sendToPlayer(pl);
    }

    @Override
    public void handleOnClient() {
        ToastUtil.addConfigOptionalToast(() -> ToastUtil.addSkillRequirementToast(this), () -> LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("insufficientLevels"), ChatFormatting.RED, this.skill.getName(), LocaleUtil.numToComponent(this.level)));
    }
}
