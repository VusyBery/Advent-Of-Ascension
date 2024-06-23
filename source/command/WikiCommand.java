package net.tslat.aoa3.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.tslat.aoa3.advent.Logging;
import net.tslat.aoa3.common.networking.AoANetworking;
import net.tslat.aoa3.common.networking.packets.WikiSearchPacket;
import net.tslat.aoa3.util.StringUtil;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WikiCommand implements Command<CommandSourceStack> {
	private static final WikiCommand CMD = new WikiCommand();

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("wiki").executes(CMD);

		builder.then(Commands.argument("search", StringArgumentType.greedyString()).requires(command -> command.isPlayer() && command.hasPermission(0))
				.executes(WikiCommand::sendPacket));

		return builder;
	}

	private static int sendPacket(CommandContext<CommandSourceStack> cmd) throws CommandSyntaxException {
		AoANetworking.sendToPlayer(cmd.getSource().getPlayer(), new WikiSearchPacket(StringArgumentType.getString(cmd, "search")));

		return 1;
	}

	public static void handleSearchRequest(String search, CommandSource receiver) {
		final String searchUrl = "https://adventofascension.fandom.com/wiki/" +
				(search.equalsIgnoreCase("random") || search.equalsIgnoreCase("?") ?
						"Special:Random" :
						"Special:Search?query=" + URLEncoder.encode(search.replace(" ", "_"), StandardCharsets.UTF_8));
		final URI uri = URI.create(searchUrl);
		URI updatedUri = null;

		try {
			HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();

			connection.setInstanceFollowRedirects(true);
			connection.connect();

			updatedUri = switch (connection.getResponseCode()) {
				case HttpURLConnection.HTTP_MOVED_TEMP, HttpURLConnection.HTTP_MOVED_PERM -> URI.create(connection.getHeaderField("Location"));
				case HttpURLConnection.HTTP_OK -> connection.getURL().toURI();
				default -> null;
			};
		}
		catch (UnsupportedEncodingException | MalformedURLException ex) {
			Logging.logMessage(Level.ERROR, "Failed to encode URL for AoA Wiki search: \"" + search + "\"", ex);
		}
		catch (IOException | URISyntaxException ex) {
			receiver.sendSystemMessage(AoACommand.getCmdPrefix("Wiki").append(Component.translatable("command.aoa.wiki.connectionFail").setStyle(Style.EMPTY.applyFormat(AoACommand.CommandFeedbackType.ERROR.getColour()))));
		}

		final URI finalUri = updatedUri;
		final String uriPath = finalUri != null ? finalUri.getPath() : searchUrl;
		final String pageTitle = StringUtil.toTitleCase(finalUri != null && uriPath.contains("wiki/") ? uriPath.substring(uriPath.lastIndexOf("wiki/") + 5) : search);

		receiver.sendSystemMessage(AoACommand.getCmdPrefix("Wiki").append(Component.translatable("command.aoa.wiki.response", Component.literal(pageTitle).withStyle(style -> style.withUnderlined(true).withColor(ChatFormatting.RED).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, uriPath)))).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY))));
	}

	@Override
	public int run(CommandContext<CommandSourceStack> context) {
		AoACommand.feedback(context.getSource(), "Wiki", "command.aoa.wiki.desc", AoACommand.CommandFeedbackType.INFO);

		return 1;
	}
}