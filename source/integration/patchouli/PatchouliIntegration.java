package net.tslat.aoa3.integration.patchouli;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Optional;


public class PatchouliIntegration {
	public static void preInit() {
		if (FMLEnvironment.dist == Dist.CLIENT)
			PatchouliClientIntegration.init();
	}

	public static boolean isBookLoaded(ResourceLocation id) {
		return false;
		//return BookRegistry.INSTANCE.books.containsKey(id);
	}

	public static ItemStack getBook(ResourceLocation id) {
		return ItemStack.EMPTY;
		//return BookRegistry.INSTANCE.books.get(id).getBookItem();
	}

	public static void openBook(ResourceLocation id) {
		//PatchouliAPI.get().openBookGUI(id);
	}

	public static Optional<ResourceLocation> getBookIdFromScreen(Screen screen) {
		return Optional.empty();
		//return Optional.ofNullable(screen instanceof GuiBook bookGui ? bookGui.book.id : null);
	}
}
