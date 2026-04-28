package dev.sunnymonster.server_restart_command;

import dev.sunnymonster.server_restart_command.commands.CancelCommand;
import dev.sunnymonster.server_restart_command.commands.RestartCommand;
import dev.sunnymonster.server_restart_command.commands.StopWithReasonCommand;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRestartCommand implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("server_restart_command");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing server restart command...");
        RestartCommand.register();
        StopWithReasonCommand.register();
        CancelCommand.register();
    }
}
