package net.tslat.aoa3.mixin.client.patch;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.tslat.aoa3.advent.AdventOfAscension;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin { // Removes the log warnings for unknown recipe types. Why is this error even a thing?
	@WrapWithCondition(method = "getCategory", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    private static boolean aoa3$skipUncategorisedRecipes(Logger logger, String message, Object recipeTypeId, Object recipeId, @Local(argsOnly = true, index = 0) RecipeHolder<?> recipe) {
		return !AdventOfAscension.isAoA(recipe.id());
	}
}
