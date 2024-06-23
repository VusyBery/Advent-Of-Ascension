package net.tslat.aoa3.mixin.client.patch;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin { // Fix vanilla not accounting for modded bows when modifying FOV for drawing
    @WrapOperation(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean aoa3$fixModdedBowFOV(ItemStack stack, Item bow, Operation<Boolean> original) {
        return stack.is(Tags.Items.TOOLS_BOW) && stack.getItem().getUseAnimation(stack) == UseAnim.BOW;
    }
}
