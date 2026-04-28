package dev.sunnymonster.server_restart_command.mixin;

import dev.sunnymonster.server_restart_command.ServerRestartCommand;
import dev.sunnymonster.server_restart_command.commands.RestartCommand;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "runServer", at = @At(value = "RETURN"))
    private static void exitHandler(CallbackInfo info) throws Exception {
        if (RestartCommand.Called) {
            ServerRestartCommand.LOGGER.info("Exiting with 0x43");
            Runtime.getRuntime().halt(0x43);
        }
    }
}
