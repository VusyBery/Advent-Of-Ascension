package net.tslat.aoa3.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.tslat.aoa3.player.ability.AoAAbility;
import net.tslat.aoa3.player.resource.AoAResource;
import net.tslat.aoa3.player.skill.AoASkill;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface PlayerDataManager extends INBTSerializable<CompoundTag> {
	Player getPlayer();

	boolean isLegitimate();

	int getTotalLevel();

	Collection<AoASkill.Instance> getSkills();

	@NotNull
	AoASkill.Instance getSkill(AoASkill skill);

	AoAAbility.Instance getAbility(String abilityId);

	Collection<AoAResource.Instance> getResources();

	@NotNull
	AoAResource.Instance getResource(AoAResource resource);

	void addEventListener(AoAPlayerEventListener listener);
}
