# Server Restart Command

A simple mod that allows Minecraft server admins to schedule server restarts without waiting for everyone to log off.

## Commands

* `/restart <wait_for_empty> <reason>`, shuts down the server with a non-zero exit code. You should run your server with
  a script which relaunches the server when the exit code is not 0.
  * `<wait_for_empty>`: `boolean`, `false` to restart the server immediately, `true` to wait until server becomes empty.
  * `<reason>`: `greedy string`, reason for the restart, will be announced in chat.
* `/stopr <wait_for_empty> <reason>`, shuts down the server normally.
  * `<wait_for_empty>`: `boolean`, see above.
  * `<reason>`: `greedy string`, see above.
* `/cancel`, cancels currently scheduled restarts or shutdowns.