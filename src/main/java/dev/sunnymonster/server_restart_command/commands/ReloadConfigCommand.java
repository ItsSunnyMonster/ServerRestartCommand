package dev.sunnymonster.server_restart_command.commands;

import dev.sunnymonster.server_restart_command.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ReloadConfigCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) -> {
            dispatcher
                    .register(Commands.literal("serverrestart")
                            .then(Commands.literal("reload")
                                    .executes(context -> {
                                        ConfigManager.load();
                                        context.getSource().sendSuccess(() -> Component.literal("Reloaded configuration file!"), false);
                                        return 1;
                                    })));
        }));
    }
}
