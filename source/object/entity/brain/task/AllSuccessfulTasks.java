package net.tslat.aoa3.object.entity.brain.task;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTask;
import net.minecraft.entity.ai.brain.task.Task;

import java.util.List;
import java.util.Map;

public class AllSuccessfulTasks<E extends LivingEntity> extends MultiTask<E>  {
	public AllSuccessfulTasks(List<Pair<Task<? super E>, Integer>> taskList) {
		this(ImmutableMap.of(), taskList);
	}

	public AllSuccessfulTasks(Map<MemoryModuleType<?>, MemoryModuleStatus> memoryRequirements, List<Pair<Task<? super E>, Integer>> taskList) {
		super(memoryRequirements, ImmutableSet.of(), Ordering.ORDERED, RunType.TRY_ALL, taskList);
	}
}
