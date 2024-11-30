package net.tslat.aoa3.common.registration.entity;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.item.AoAItems;

import java.util.function.Supplier;

public final class AoABoatTypes {
    public static final EnumProxy<Boat.Type> BAOBAB = new EnumProxy<>(Boat.Type.class, AoABlocks.BAOBAB_PLANKS.planks, AdventOfAscension.id("baobab").toString(), AoAItems.BAOBAB_BOAT, AoAItems.BAOBAB_CHEST_BOAT, (Supplier<Item>)() -> Items.STICK, false);
    public static final EnumProxy<Boat.Type> LUCALUS = new EnumProxy<>(Boat.Type.class, AoABlocks.LUCALUS_PLANKS.planks, AdventOfAscension.id("lucalus").toString(), AoAItems.LUCALUS_BOAT, AoAItems.LUCALUS_CHEST_BOAT, (Supplier<Item>)() -> Items.STICK, false);
    public static final EnumProxy<Boat.Type> STRANGLEWOOD = new EnumProxy<>(Boat.Type.class, AoABlocks.STRANGLEWOOD_PLANKS.planks, AdventOfAscension.id("stranglewood").toString(), AoAItems.STRANGLEWOOD_BOAT, AoAItems.STRANGLEWOOD_CHEST_BOAT, (Supplier<Item>)() -> Items.STICK, false);
}
