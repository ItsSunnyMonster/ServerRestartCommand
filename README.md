# Server Restart Command

A simple mod that allows Minecraft server admins to schedule server restarts without waiting for everyone to log off.

## Server Setup
For the restart functionality to work you should run your server in a script which restarts the server if a non-zero exit code is encountered.
This has the side effect of automatic recovery of server crashes.

Example for Unix:

```sh
#!/bin/sh

while true; do
    echo "Starting Fabric server..."
    java -jar server.jar nogui
    exit_code=$?

    if [ "$exit_code" -eq 0 ]; then
        echo "Server stopped normally."
        break
    fi

    echo "Server stopped. Restarting in 10 seconds..."
    sleep 10
done

printf 'Press Enter to continue...'
read -r dummy
```

Example for Windows: (powershell)

```ps
while ($true) {
    Write-Host "Starting Fabric server..."
    java -jar server.jar nogui
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Server stopped normally."
        break
    }

    Write-Host "Server stopped. Restarting in 10 seconds..."
    Start-Sleep -Seconds 10
}

Write-Host -NoNewLine 'Press any key to continue...';
$null = $Host.UI.RawUI.ReadKey('NoEcho,IncludeKeyDown');
```

## Admin Commands

* `/restart <wait_for_empty> <reason>`, shuts down the server with a non-zero exit code. You should run your server with
  a script which relaunches the server when the exit code is not 0.
  * `<wait_for_empty>`: `boolean`, `false` to restart the server immediately, `true` to wait until server becomes empty.
  * `<reason>`: `greedy string`, reason for the restart, will be announced in chat.
* `/stopr <wait_for_empty> <reason>`, shuts down the server normally.
  * `<wait_for_empty>`: `boolean`, see above.
  * `<reason>`: `greedy string`, see above.
* `/cancel`, cancels currently scheduled restarts or shutdowns.

## Interaction with [Discord MC Chat](https://modrinth.com/mod/discord-mc-chat)

All commands output their feedback messages through `/say` as the server, which means all players can see an announcement when the admin triggers a restart,
but Discord MC Chat will also output the message in the Discord channel. There is also a config option (see below) which allows the mod to ping admins
on Discord when all players are logged off and the server is restarting.

## Configuration Options

The config file can be found as `server_restart_command.json5` under the config directory of the server files.

Below are the default values for all config options.

```json5
{
  // A list of Discord user IDs (as strings) to ping when the server restarts or stops.
  "adminIds": [],
  // Whether to ping admins when the server restarts or stops.
  "pingAdminsWhenStopped": true
}
```
