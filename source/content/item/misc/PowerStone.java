package net.tslat.aoa3.content.item.misc;

import net.minecraft.world.item.Item;
import net.tslat.aoa3.common.registration.entity.variant.PixonVariant;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class PowerStone extends Item {
    private final int enchantLevel;
    private final IntSupplier colour;

    public PowerStone(int enchantLevel, Supplier<PixonVariant> pixonVariant) {
        super(new Item.Properties());

        this.enchantLevel = enchantLevel;
        this.colour = () -> pixonVariant.get().primaryColour();
    }

    public int getEnchantLevel() {
        return this.enchantLevel;
    }

    public int getColour() {
        return this.colour.getAsInt();
    }
}
