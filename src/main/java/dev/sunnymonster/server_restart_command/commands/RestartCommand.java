package dev.sunnymonster.server_restart_command.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.sunnymonster.server_restart_command.ServerRestartCommand;
import dev.sunnymonster.server_restart_command.util.AdminPing;
import dev.sunnymonster.server_restart_command.util.ServerSay;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import static net.minecraft.commands.Commands.argument;

public class RestartCommand {
    public static boolean Called = false;
    public static boolean WaitingToStop = false;

    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (WaitingToStop) {
                var players = server.getPlayerList().getPlayers();
                // If only one player remains, and it is the player quitting
                if (server.getPlayerCount() == 0 || (server.getPlayerCount() == 1 && players.getFirst().equals(handler.player))) {
                    ServerSay.say(server, AdminPing.getPingText() + "Server empty, restarting. See chat log for reason.");
                    Called = true;
                    server.halt(false);
                }
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> {
            dispatcher.register(Commands.literal("restart")
                    .requires(source ->
                            source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                    .then(argument("wait_for_empty", BoolArgumentType.bool())
                            .then(argument("reason", StringArgumentType.greedyString())
                                    .executes(context -> {
                                        MinecraftServer server = context.getSource().getServer();

                                        boolean waitForEmpty = context.getArgument("wait_for_empty", Boolean.class);
                                        String reason = context.getArgument("reason", String.class);

                                        if (waitForEmpty && StopWithReasonCommand.WaitingToStop) {
                                            context.getSource().sendFailure(Component.literal("The server is already scheduled to stop when empty."));
                                            return 0;
                                        }

                                        if (waitForEmpty && server.getPlayerCount() != 0) {
                                            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "say Server will restart after all players are offline. Reason: " + reason + ". You do not need to take action immediately.");
                                            WaitingToStop = true;
                                            return 1;
                                        }

                                        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "say Server restarting. Reason: " + reason + ".");
                                        context.getSource().sendSuccess(() -> Component.literal("Exiting server with non-0 exit code."), true);
                                        Called = true;
                                        server.halt(false);
                                        return 1;
                                    }))));
        });
    }
}
