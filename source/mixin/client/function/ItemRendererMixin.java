package net.tslat.aoa3.mixin.client.function;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.common.registration.item.AoADataComponents;
import net.tslat.aoa3.common.registration.item.AoAItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Because I don't have access to the entity or hand in BEWLRs =/
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow public abstract BakedModel getModel(ItemStack stack, @Nullable Level level, @Nullable LivingEntity entity, int seed);

    @Inject(method = "getModel", at = @At(value = "HEAD"), cancellable = true)
    public void aoa3$recursivelyWrapCompressedItem(ItemStack stack, Level level, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> callback) {
        if (stack.is(AoAItems.COMPRESSED_ITEM.get()) && stack.has(AoADataComponents.COMPRESSED_ITEM_DATA))
            callback.setReturnValue(getModel(stack.get(AoADataComponents.COMPRESSED_ITEM_DATA).compressedStack(), level, entity, seed));
    }
}
