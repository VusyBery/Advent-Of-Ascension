package net.tslat.aoa3.player.ability.farming;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.tslat.aoa3.common.registration.block.AoABlocks;
import net.tslat.aoa3.common.registration.custom.AoAAbilities;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.skill.AoASkill;
import net.tslat.aoa3.util.PlayerUtil;
import net.tslat.aoa3.util.WorldUtil;

import java.util.List;

public class FertiliseFarmland extends AoAAbility.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(PlayerInteractEvent.RightClickBlock.class, serverOnly(this::handleBlockInteraction)));

	public FertiliseFarmland(AoASkill.Instance skill, JsonObject data) {
		super(AoAAbilities.FERTILISE_FARMLAND.get(), skill, data);
	}

	public FertiliseFarmland(AoASkill.Instance skill, CompoundTag data) {
		super(AoAAbilities.FERTILISE_FARMLAND.get(), skill, data);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleBlockInteraction(PlayerInteractEvent.RightClickBlock ev) {
		ItemStack stack = ev.getItemStack();

		if (stack.getItem() == Items.BONE_MEAL) {
			Level world = ev.getLevel();
			BlockState state = world.getBlockState(ev.getPos());

			if (state.getBlock() == Blocks.FARMLAND && WorldUtil.canModifyBlock(world, ev.getPos(), ev.getEntity(), stack)) {
				world.setBlock(ev.getPos(), AoABlocks.FERTILISED_FARMLAND.get().defaultBlockState().setValue(FarmBlock.MOISTURE, state.getValue(FarmBlock.MOISTURE)), Block.UPDATE_ALL);

				PlayerUtil.giveXpToPlayer((ServerPlayer)ev.getEntity(), AoASkills.FARMING.get(), PlayerUtil.getTimeBasedXpForLevel(PlayerUtil.getLevel(ev.getEntity(), AoASkills.FARMING.get()), 1), false);

				if (!ev.getEntity().isCreative())
					stack.shrink(1);
			}
		}
	}
}
