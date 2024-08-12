package net.tslat.aoa3.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.tslat.aoa3.common.registration.AoARegistries;
import net.tslat.aoa3.content.world.event.AoAWorldEvent;
import net.tslat.aoa3.content.world.event.AoAWorldEventManager;
import net.tslat.aoa3.util.StringUtil;

public class WorldEventCommand implements Command<CommandSourceStack> {
    private static final WorldEventCommand CMD = new WorldEventCommand();
    private static final SuggestionProvider<CommandSourceStack> EVENT_SUGGESTIONS = (context, builder) -> {
        ReloadableServerRegistries.Holder reloadableRegistries = context.getSource().getServer().reloadableRegistries();

        return SharedSuggestionProvider.suggestResource(reloadableRegistries.getKeys(AoARegistries.WORLD_EVENTS_REGISTRY_KEY).stream().filter(id -> AoAWorldEventManager.getEventById(context.getSource().getLevel(), id) != null).toList(), builder);
    };

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("worldevent").requires(command -> command.hasPermission(4)).executes(CMD);

        builder.then(Commands.argument("level", DimensionArgument.dimension())
                        .then(Commands.argument("event", ResourceLocationArgument.id())
                                .suggests(EVENT_SUGGESTIONS)
                                .then(Commands.literal("check")
                                        .executes(cmd -> checkEvent(cmd, DimensionArgument.getDimension(cmd, "level"))))
                                .then(Commands.literal("start")
                                        .executes(cmd -> startEvent(cmd, DimensionArgument.getDimension(cmd, "level"))))
                                .then(Commands.literal("stop")
                                        .executes(cmd -> stopEvent(cmd, DimensionArgument.getDimension(cmd, "level"))))));

        return builder;
    }

    private static int checkEvent(CommandContext<CommandSourceStack> context, ServerLevel level) throws CommandSyntaxException {
        ResourceLocation eventId = ResourceLocationArgument.getId(context, "event");
        AoAWorldEvent event = AoAWorldEventManager.getEventById(level, eventId);

        if (event == null) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.noEvent", AoACommand.CommandFeedbackType.WARN, Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())), Component.literal(eventId.toString()));

            return 0;
        }

        if (event.isActive()) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.active", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

            return 1;
        }

        AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.inactive", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

        return 0;
    }

    private static int startEvent(CommandContext<CommandSourceStack> context, ServerLevel level) throws CommandSyntaxException {
        ResourceLocation eventId = ResourceLocationArgument.getId(context, "event");
        AoAWorldEvent event = AoAWorldEventManager.getEventById(level, eventId);

        if (event == null) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.noEvent", AoACommand.CommandFeedbackType.WARN, Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())), Component.literal(eventId.toString()));

            return 0;
        }

        if (event.isActive()) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.alreadyActive", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

            return 0;
        }

        event.start(level);
        AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.start", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

        return 1;
    }

    private static int stopEvent(CommandContext<CommandSourceStack> context, ServerLevel level) throws CommandSyntaxException {
        ResourceLocation eventId = ResourceLocationArgument.getId(context, "event");
        AoAWorldEvent event = AoAWorldEventManager.getEventById(level, eventId);

        if (event == null) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.noEvent", AoACommand.CommandFeedbackType.WARN, Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())), Component.literal(eventId.toString()));

            return 0;
        }

        if (!event.isActive()) {
            AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.alreadyInactive", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

            return 0;
        }

        event.stop(level);
        AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.stop", AoACommand.CommandFeedbackType.INFO, Component.literal(eventId.toString()), Component.literal(StringUtil.toTitleCase(level.dimension().location().getPath())));

        return 1;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        AoACommand.feedback(context.getSource(), "WorldEvent", "command.aoa.worldevent.desc", AoACommand.CommandFeedbackType.INFO);

        return 1;
    }
}
