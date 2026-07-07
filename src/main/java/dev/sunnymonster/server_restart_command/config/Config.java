package dev.sunnymonster.server_restart_command.config;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public List<String> adminIds;
    public Boolean pingAdminsWhenStopped;

    public static class Default {
        public static Config get() {
            Config config = new Config();

            config.adminIds = new ArrayList<>();
            config.pingAdminsWhenStopped = true;

            return config;
        }
    }

    public boolean normalize() {
        Config d = Config.Default.get();
        boolean changed = false;

        if (adminIds == null) {
            adminIds = d.adminIds;
            changed = true;
        }
        if (pingAdminsWhenStopped == null) {
            pingAdminsWhenStopped = d.pingAdminsWhenStopped;
            changed = true;
        }

        return changed;
    }
}
