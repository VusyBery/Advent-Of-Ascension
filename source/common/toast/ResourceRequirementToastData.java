package net.tslat.aoa3.common.toast;

import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.common.registration.custom.AoAToastTypes;
import net.tslat.aoa3.player.resource.AoAResource;
import net.tslat.aoa3.util.LocaleUtil;
import net.tslat.aoa3.util.NumberUtil;
import net.tslat.aoa3.util.ToastUtil;

public record ResourceRequirementToastData(AoAResource resource, float value) implements CustomToastData {
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceRequirementToastData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(AoARegistries.RESOURCES_REGISTRY_KEY),
            ResourceRequirementToastData::resource,
            ByteBufCodecs.FLOAT,
            ResourceRequirementToastData::value,
            ResourceRequirementToastData::new);

    @Override
    public Type<ResourceRequirementToastData> type() {
        return AoAToastTypes.RESOURCE_REQUIREMENT.get();
    }

    public static void sendToastPopupTo(ServerPlayer pl, AoAResource resource, float value) {
        new ResourceRequirementToastData(resource, value).sendToPlayer(pl);
    }

    @Override
    public void handleOnClient() {
        ToastUtil.addConfigOptionalToast(() -> ToastUtil.addResourceRequirementToast(this), () -> LocaleUtil.getLocaleMessage(LocaleUtil.createFeedbackLocaleKey("insufficientResource"), ChatFormatting.RED, this.resource.getName(), Component.literal(NumberUtil.roundToNthDecimalPlace(this.value, 2))));
    }
}
