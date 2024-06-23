package net.tslat.aoa3.common.registration;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.tslat.aoa3.player.ServerPlayerDataManager;

import java.util.function.Supplier;

public final class AoADataAttachments {
    public static void init() {}

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ServerPlayerDataManager>> ADVENT_PLAYER = register("advent_player", () -> AttachmentType.serializable(pl -> new ServerPlayerDataManager((ServerPlayer)pl)).build());

    private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String id, Supplier<AttachmentType<T>> attachment) {
        return AoARegistries.DATA_ATTACHMENTS.register(id, attachment);
    }
}
