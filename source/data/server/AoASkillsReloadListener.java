package net.tslat.aoa3.data.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tslat.aoa3.advent.Logging;
import net.tslat.aoa3.common.registration.AoAConfigs;
import net.tslat.aoa3.common.registration.custom.AoASkills;
import net.tslat.aoa3.player.ServerPlayerDataManager;
import net.tslat.aoa3.player.skill.AoASkill;
import org.apache.logging.log4j.Level;

import java.util.Map;

public class AoASkillsReloadListener extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String folder = "player/skills";

	private static final Map<AoASkill, JsonObject> SKILLS_DATA = new Object2ObjectArrayMap<>();

	public AoASkillsReloadListener() {
		super(GSON, folder);
	}

	public static Object2ObjectOpenHashMap<AoASkill, AoASkill.Instance> generateSkillsMap(ServerPlayerDataManager plData) {
		final Object2ObjectOpenHashMap<AoASkill, AoASkill.Instance> skills = new Object2ObjectOpenHashMap<>(SKILLS_DATA.size());

		if (!AoAConfigs.SERVER.disableSkills.get()) {
			for (Map.Entry<AoASkill, JsonObject> skill : SKILLS_DATA.entrySet()) {
				skills.put(skill.getKey(), skill.getKey().buildDefaultInstance(plData, skill.getValue()));
			}
		}

		return skills;
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
		SKILLS_DATA.clear();

		for (Map.Entry<ResourceLocation, JsonElement> entry : jsonMap.entrySet()) {
			ResourceLocation skillId = entry.getKey();
			JsonElement json = entry.getValue();
			AoASkill skill = AoASkills.getSkill(skillId);

			if (skill == null) {
				Logging.logMessage(Level.WARN, "Unable to find registered skill: '" + skillId.toString() + "' from datapack entry.");

				continue;
			}

			if (!json.isJsonObject()) {
				Logging.logMessage(Level.ERROR, "Invalidly formatted skill json '" + skillId.toString() + "' from datapack entry.");

				continue;
			}

			JsonObject obj = json.getAsJsonObject();

			if (obj.isEmpty())
				continue;

			SKILLS_DATA.put(skill, obj);
		}
	}
}
