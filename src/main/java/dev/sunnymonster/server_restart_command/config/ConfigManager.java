package dev.sunnymonster.server_restart_command.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import dev.sunnymonster.server_restart_command.ServerRestartCommand;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Jankson JANKSON = Jankson.builder().build();

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("server_restart_command.json5");

    private static Config config;

    public static void load() {
        ServerRestartCommand.LOGGER.info("Loading configuration from {}", CONFIG_PATH.toString());
        try {
            if (Files.exists(CONFIG_PATH)) {
                JsonObject json = JANKSON.load(CONFIG_PATH.toFile());
                config = JANKSON.fromJson(json, Config.class);
                if (config.normalize()) {
                    save();
                }
            } else {
                config = Config.Default.get();
                save();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static void save() {
        ServerRestartCommand.LOGGER.info("Saving configuration to {}", CONFIG_PATH);
        try {
            JsonElement json = JANKSON.toJson(config);

            Files.writeString(CONFIG_PATH, json.toJson(true, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    public static Config get() {
        return config;
    }
}
