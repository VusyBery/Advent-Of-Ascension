package net.tslat.aoa3.common.networking.configtask;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import net.tslat.aoa3.advent.AdventOfAscension;
import net.tslat.aoa3.common.networking.packets.setup.SkillReqLoginSyncPacket;
import net.tslat.aoa3.data.server.AoASkillReqReloadListener;

import java.util.function.Consumer;

public record SkillReqsHandshakeTask(ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {
	public static final Type TYPE = new Type(AdventOfAscension.id("skill_reqs_sync"));

	@Override
	public Type type() {
		return TYPE;
	}

	@Override
	public void run(Consumer<CustomPacketPayload> sender) {
		if (this.listener.getConnectionType().isNeoForge())
			sender.accept(new SkillReqLoginSyncPacket(AoASkillReqReloadListener.getParsedReqData()));
	}
}