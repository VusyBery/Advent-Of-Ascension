package net.tslat.aoa3.player.skill;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.tslat.aoa3.common.registration.AoATags;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.event.custom.events.PlayerSkillsLootModificationEvent;
import net.tslat.aoa3.event.dynamic.DynamicEventSubscriber;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.util.PlayerUtil;

import java.util.List;

public class ExtractionSkill extends AoASkill.Instance {
	private final List<DynamicEventSubscriber<?>> eventSubscribers = List.of(
			listener(PlayerSkillsLootModificationEvent.class, serverOnly(this::handleLootModification)),
			listener(PlayerEvent.ItemSmeltedEvent.class, serverOnly(this::handleItemSmelted)));

	public ExtractionSkill(ServerPlayerDataManager plData, JsonObject jsonData) {
		super(AoASkills.EXTRACTION.get(), plData, jsonData);
	}

	public ExtractionSkill(CompoundTag nbtData) {
		super(AoASkills.EXTRACTION.get(), nbtData);
	}

	@Override
	public List<DynamicEventSubscriber<?>> getEventSubscribers() {
		return this.eventSubscribers;
	}

	private void handleLootModification(final PlayerSkillsLootModificationEvent ev) {
		if (!canGainXp(true))
			return;

		final LootContext context = ev.getLootContext();
		final BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

		if (state == null)
			return;

		Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

		if (origin == null)
			return;

		ServerLevel world = context.getLevel();
		BlockPos pos = BlockPos.containing(origin);
		Block block = state.getBlock();

		if (!Block.isShapeFullBlock(state.getCollisionShape(context.getLevel(), pos)))
			return;

		if (!isApplicableBlock(state))
			return;

		float hardness = state.getDestroySpeed(world, pos);
		float xp = PlayerUtil.getTimeBasedXpForLevel(getLevel(true), 2 * hardness);

		for (ItemStack item : ev.getGeneratedLoot()) {
			if (item.getItem() != block.asItem()) {
				xp *= 2f;

				break;
			}
		}

		if (ev.getGeneratedLoot().size() > 2)
			xp *= 1.5f;

		adjustXp(xp, false, false);
	}

	private void handleItemSmelted(final PlayerEvent.ItemSmeltedEvent ev) {
		if (ev.getEntity().level().isClientSide() || ev.getSmelting().getCount() <= 0)
			return;

		ItemStack smelting = ev.getSmelting();

		if (smelting.getFoodProperties(ev.getEntity()) == null && canGainXp(true)) {
			float xp = PlayerUtil.getTimeBasedXpForLevel(getLevel(true), 40)/* * ev.getRemovedCount()*/; // TODO when Neoforge fixes #1367

			if (smelting.is(Tags.Items.NUGGETS)) {
				xp *= 1.5f;
			}
			else if (smelting.is(Tags.Items.INGOTS)) {
				xp *= 2f;
			}
			else if (smelting.is(Tags.Items.GEMS)) {
				xp *= 2.5f;
			}

			adjustXp(xp, false, false);
		}
	}

	public static boolean isApplicableBlock(BlockState block) {
		return block.is(AoATags.Blocks.EXTRACTION_TRAINABLE);
	}
}
