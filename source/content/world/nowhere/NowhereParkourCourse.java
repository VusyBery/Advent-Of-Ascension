package net.tslat.aoa3.content.world.nowhere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.advent.AoAResourceCaching;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.event.dimension.NowhereEvents;
import net.tslat.aoa3.util.AdvancementUtil;
import net.tslat.aoa3.util.InventoryUtil;
import net.tslat.aoa3.util.LootUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class NowhereParkourCourse {
	public static final Codec<NowhereParkourCourse> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ResourceLocation.CODEC.fieldOf("structure_id").forGetter(course -> course.structureId),
			BlockPos.CODEC.fieldOf("structure_pos").forGetter(course -> course.structurePos),
			Vec3.CODEC.fieldOf("player_start").forGetter(course -> course.playerStart),
			Codec.INT.fieldOf("tier").forGetter(course -> course.tier),
			ResourceLocation.CODEC.optionalFieldOf("reward_table").forGetter(course -> course.rewardLootTable)
	).apply(builder, NowhereParkourCourse::new));
	
	private static final Int2ObjectMap<List<NowhereParkourCourse>> COURSES = new Int2ObjectOpenHashMap<>();

	static {
		AoAResourceCaching.onDataReload(registryAccess -> {
			COURSES.clear();
			registryAccess.registryOrThrow(AoARegistries.NOWHERE_PARKOUR_COURSES_REGISTRY_KEY).stream().forEach(course -> COURSES.computeIfAbsent(course.getTier(), key -> new ObjectArrayList<>()).add(course));
		});
		AoAResourceCaching.onClientLogout(COURSES::clear);
	}

	public final ResourceLocation structureId;
	private final BlockPos structurePos;
	private final Vec3 playerStart;
	private final int tier;
	private final Optional<ResourceLocation> rewardLootTable;

	private Structure structure = null;
	private StructureStart structureStart = null;
	private AABB structureBounds = null;

	public NowhereParkourCourse(ResourceLocation structureId, BlockPos structurePos, Vec3 playerStart, int tier, Optional<ResourceLocation> rewardTable) {
		this.structureId = structureId;
		this.structurePos = structurePos;
		this.playerStart = playerStart;
		this.tier = tier;
		this.rewardLootTable = rewardTable;
	}

	@Nullable
	public static NowhereParkourCourse getCourseForPosition(ServerLevel level, Vec3 pos) {
		if (!NowhereEvents.isInParkourRegion(BlockPos.containing(pos)))
			return null;

		int tier = ((int)pos.x - 500) / 500;

		if (!COURSES.containsKey(tier))
			return null;
		
		for (NowhereParkourCourse course : COURSES.get(tier)) {
			if (course.isOnCourse(level, pos))
				return course;
		}

		return null;
	}

	@Nullable
	public static NowhereParkourCourse getNextCourse(NowhereParkourCourse currentCourse) {
		List<NowhereParkourCourse> sameTierCourses = COURSES.get(currentCourse.getTier());

		for (int i = 0; i < sameTierCourses.size(); i++) {
			if (sameTierCourses.get(i) == currentCourse)
				return i == sameTierCourses.size() - 1 ? null : sameTierCourses.get(i + 1);
		}

		return null;
	}

	@Nullable
	public static NowhereParkourCourse getFirstCourseForTier(int tier) {
		return COURSES.containsKey(tier) ? COURSES.get(tier).getFirst() : null;
	}

	public int getTier() {
		return this.tier;
	}

	@Nullable
	private Structure getStructure(ServerLevel level) {
		if (this.structure != null)
			return this.structure;

		this.structure = level.registryAccess().registry(Registries.STRUCTURE).get().get(structureId);

		return this.structure;
	}

	@Nullable
	private StructureStart getStructureStart(ServerLevel level) {
		if (this.structureStart != null)
			return this.structureStart;

		Structure structure = getStructure(level);

		if (structure == null)
			return null;

		this.structureStart = level.structureManager().getStructureAt(structurePos, structure);

		return this.structureStart;
	}

	@Nullable
	public AABB getStructureBounds(ServerLevel level) {
		if (this.structureBounds != null)
			return this.structureBounds;

		StructureStart structureStart = getStructureStart(level);

		if (structureStart == null)
			return null;

		this.structureBounds = AABB.of(structureStart.getBoundingBox());

		return this.structureBounds;
	}

	public boolean isOnCourse(ServerLevel level, Vec3 position) {
		AABB bounds = getStructureBounds(level);

		return bounds != null && bounds.contains(position);
	}

	public void grantRewards(ServerPlayer player) {
		this.rewardLootTable.ifPresent(tableId -> InventoryUtil.giveItemsTo(player, LootUtil.generateLoot(tableId, LootUtil.getGiftParameters(player.serverLevel(), player.position(), player.getLuck(), player))));
		AdvancementUtil.grantCriterion(player, AdventOfAscension.id("nowhere/tier_" + this.tier + "_acrobat"), "complete_course");
	}

	public void teleportPlayerToCourse(ServerPlayer player) {
		player.connection.teleport(playerStart.x, playerStart.y, playerStart.z, player.getYRot(), player.getXRot());
	}
}