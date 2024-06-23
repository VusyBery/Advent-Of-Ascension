package net.tslat.aoa3.common.registration.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import net.tslat.aoa3.advent.AdventOfAscension;

public final class AoAJukeboxSongs {
    public static final ResourceKey<JukeboxSong> OUTLAW = key("outlaw");
    public static final ResourceKey<JukeboxSong> CAVERNS = key("caverns");

    private static ResourceKey<JukeboxSong> key(String id) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, AdventOfAscension.id(id));
    }
}
