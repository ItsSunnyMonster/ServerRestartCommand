package dev.sunnymonster.server_restart_command.commands;

import dev.sunnymonster.server_restart_command.util.ServerSay;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import static net.minecraft.commands.Commands.literal;

public class CancelCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> {
            dispatcher.register(literal("cancel_stop")
                    .requires(source ->
                            source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.OWNERS)))
                    .executes(context -> {
                        if (RestartCommand.WaitingToStop) {
                            RestartCommand.WaitingToStop = false;
                            ServerSay.say(context.getSource().getServer(), "Server restart cancelled.");
                        } else if (StopWithReasonCommand.WaitingToStop) {
                            StopWithReasonCommand.WaitingToStop = false;
                            ServerSay.say(context.getSource().getServer(), "Server shutdown cancelled.");
                        } else {
                            context.getSource().sendFailure(Component.literal("Nothing to cancel."));
                            return 0;
                        }

                        return 1;
                    })
            );
        });
    }
}
