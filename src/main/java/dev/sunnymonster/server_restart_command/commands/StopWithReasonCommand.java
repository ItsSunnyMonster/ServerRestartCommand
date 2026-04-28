package dev.sunnymonster.server_restart_command.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.sunnymonster.server_restart_command.util.ServerSay;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import static net.minecraft.commands.Commands.argument;

public class StopWithReasonCommand {
    public static boolean WaitingToStop = false;

    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (WaitingToStop) {
                var players = server.getPlayerList().getPlayers();
                // If only one player remains, and it is the player quitting
                if (server.getPlayerCount() == 0 || (server.getPlayerCount() == 1 && players.getFirst().equals(handler.player))) {
                    ServerSay.say(server, "Server empty, stopping. See chat log for reason.");
                    server.halt(false);
                }
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> {
            dispatcher.register(Commands.literal("stopr")
                    .requires(source -> source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                    .then(argument("wait_for_empty", BoolArgumentType.bool())
                            .then(argument("reason", StringArgumentType.greedyString())
                                    .executes(context -> {
                                        MinecraftServer server = context.getSource().getServer();

                                        boolean waitForEmpty = context.getArgument("wait_for_empty", Boolean.class);
                                        String reason = context.getArgument("reason", String.class);

                                        if (waitForEmpty && RestartCommand.WaitingToStop) {
                                            context.getSource().sendFailure(Component.literal("The server is already scheduled to restart when empty."));
                                            return 0;
                                        }

                                        if (waitForEmpty && server.getPlayerCount() != 0) {
                                            ServerSay.say(server, "Server will stop after all players are offline. Reason: " + reason + ". You do not need to take action immediately.");
                                            WaitingToStop = true;
                                            return 1;
                                        }

                                        ServerSay.say(server, "Server stopping. Reason: " + reason + ".");
                                        server.halt(false);
                                        return 1;
                                    })
                            )));
        });
    }
}
