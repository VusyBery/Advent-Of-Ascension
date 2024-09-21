package net.tslat.aoa3.content.capability;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.block.AoABlockEntities;

public final class AoACapabilityAttachments {
    public static void init() {
        AdventOfAscension.getModEventBus().addListener(AoACapabilityAttachments::attachCapabilities);
    }

    public static void attachCapabilities(final RegisterCapabilitiesEvent ev) {
        ev.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AoABlockEntities.INFUSED_PRESS.get(), (blockEntity, side) -> switch (side) {
            case null -> new RangedWrapper(blockEntity.getItemHandler(), 0, 9);
            case UP, NORTH, SOUTH, EAST, WEST -> new RangedWrapper(blockEntity.getItemHandler(), 0, 9);
            case DOWN -> new RangedWrapper(blockEntity.getItemHandler(), 9, 10);
        });
    }
}
