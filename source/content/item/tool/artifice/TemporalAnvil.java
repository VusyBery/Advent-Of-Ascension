package net.tslat.aoa3.content.item.tool.artifice;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.aoa3.library.builder.SoundBuilder;
import net.tslat.aoa3.util.LocaleUtil;

public class TemporalAnvil extends ArtificeItem {
    public TemporalAnvil() {
        super(new Item.Properties().stacksTo(1));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide)
            player.openMenu(new SimpleMenuProvider((containerId, inventory, plInventory) -> new AnvilMenu(containerId, inventory) {
                @Override
                protected void onTake(Player player, ItemStack stack) {
                    super.onTake(player, stack);
                    new SoundBuilder(SoundEvents.ANVIL_USE).atPos(level, player.position()).include(player).pitch(0.9f + player.getRandom().nextFloat() * 0.1f).execute();
                }
            }, Component.translatable(LocaleUtil.createContainerLocaleKey("temporal_anvil"))));

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
    }
}