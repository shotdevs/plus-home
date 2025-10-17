# PlusHome

A modern, user-friendly home system for SMP servers – with GUI support, teleport delay, and movement cancelation. This plugin provides a clean and intuitive way for players to manage their homes.

## Features
- **/home command** to open a beautiful, modern GUI.
- **Set, rename, and delete homes** easily through commands and a settings menu.
- **Per-home settings** accessible via right-click in the GUI.
- **Language selection menu** to change the plugin's language.
- **Configurable teleport delay** for fair gameplay.
- **Movement during the delay cancels teleportation**.
- **Supports multiple homes per player**, with limits configurable via permissions.

## Commands
- `/home` - Opens the home GUI for managing and teleporting to homes.
- `/sethome <name>` - Sets a home at your current location.
- `/delhome <name>` - Deletes a home.

## GUI
The `/home` command opens a GUI where players can:
- **View all their homes:** Each home is represented by a blue bed.
- **Teleport to a home:** Simply left-click on a home to start the teleportation.
- **Open home settings:** Right-click on a home to open a settings menu.
- **Open language settings:** Click the comparator in the bottom-right corner to open the language selection menu.

### Home Settings
The home settings menu allows players to:
- **Rename a home:** Click the name tag to start the renaming process in chat.
- **Delete a home:** Click the barrier to open a confirmation menu to delete the home.

## Permissions
- `plushomes.use` - Allows a player to use the `/home`, `/sethome`, and `/delhome` commands. (Default: `true`)
- `plushomes.set.multiple.<number>` - Allows a player to set up to `<number>` homes. For example, `plushomes.set.multiple.10` allows a player to set 10 homes. (Default: `op`)

## Configuration
The `config.yml` file allows you to configure the following:
- `teleport-delay`: The delay in seconds before a player is teleported. (Default: `5`)
- `default-max-homes`: The default number of homes a player can set without special permissions. (Default: `5`)

## Installation
1. Download the latest release from the [releases page](<link-to-releases>).
2. Place the `plushome.jar` file in your server's `plugins` folder.
3. Restart your server.

## Building from Source
This project uses Gradle. To build the plugin from source, run the following command in the project's root directory:
```bash
./gradlew shadowJar
```
The compiled JAR file will be located in `build/libs/`.

## Author
This plugin was created by **shotdes**.