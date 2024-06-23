package net.tslat.aoa3.player.halo;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.HaloChangePacket;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public record PlayerHaloContainer(EnumSet<HaloTypes> unlocked, AtomicReference<HaloTypes> current) {
    public static final StreamCodec<FriendlyByteBuf, PlayerHaloContainer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(size -> EnumSet.noneOf(HaloTypes.class), NeoForgeStreamCodecs.enumCodec(HaloTypes.class)),
            PlayerHaloContainer::unlocked,
            NeoForgeStreamCodecs.enumCodec(HaloTypes.class).map(AtomicReference::new, AtomicReference::get),
            PlayerHaloContainer::current,
            PlayerHaloContainer::new);

    private PlayerHaloContainer(Set<HaloTypes> unlocked) {
        this(EnumSet.noneOf(HaloTypes.class), new AtomicReference<>());

        this.unlocked.addAll(unlocked);

        if (!this.unlocked.isEmpty()) {
            this.current.set(this.unlocked.iterator().next());
        }
        else {
            this.current.set(HaloTypes.DONATOR);
        }
    }

    private PlayerHaloContainer(HaloTypes selected) {
        this(EnumSet.noneOf(HaloTypes.class), new AtomicReference<>());

        this.current.set(selected);
    }

    public static PlayerHaloContainer forUnlocked(Set<HaloTypes> unlocked) {
        return new PlayerHaloContainer(unlocked);
    }

    public static PlayerHaloContainer defaulted(HaloTypes selected) {
        return new PlayerHaloContainer(selected);
    }

    public PlayerHaloContainer mergeUpdatedMap(PlayerHaloContainer update) {
        this.unlocked.clear();
        this.unlocked.addAll(update.unlocked);

        return this;
    }

    public PlayerHaloContainer updateSelectedHalo(UUID player, HaloTypes selected, boolean sync) {
        if (this.unlocked.contains(selected))
            this.current.set(selected);

        if (sync)
            AoANetworking.sendToAllPlayers(new HaloChangePacket(player, selected));

        return this;
    }

    public HaloTypes getSelected() {
        return this.current.get();
    }

    public boolean isEmpty() {
        return this.unlocked.isEmpty();
    }

    public boolean hasHalo(HaloTypes halo) {
        return this.unlocked.contains(halo);
    }
}
