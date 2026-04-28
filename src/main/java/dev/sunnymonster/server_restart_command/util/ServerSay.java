package dev.sunnymonster.server_restart_command.util;

import net.minecraft.server.MinecraftServer;

public class ServerSay {
    public static void say(MinecraftServer server, String message) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "say " + message);
    }
}
