package dev.sunnymonster.server_restart_command.util;

import dev.sunnymonster.server_restart_command.config.Config;
import dev.sunnymonster.server_restart_command.config.ConfigManager;

import java.util.StringJoiner;

public final class AdminPing {
    public static String getPingText() {
        Config c = ConfigManager.get();

        if (!c.pingAdminsWhenStopped) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ", "", " ");

        for (var id: c.adminIds) {
            joiner.add("<@" + id + ">");
        }

        return joiner.toString();
    }
}
