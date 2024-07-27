// `^`^`^`
// ```java
// /**
//  * This class defines a custom command for managing invasions in a Minecraft mod.
//  * The command, named 'invasion', provides various subcommands to control and debug invasion events linked to a Nexus.
//  *
//  * Methods:
//  * - execute: Processes the 'invasion' command and its subcommands. It identifies the nearest Nexus to the player,
//  *   validates the command syntax, and delegates to specific methods based on the subcommand.
//  * - sendHelp: Sends help information to the player about the 'invasion' command usage, split across multiple pages.
//  * - changeRadius: Changes the spawn radius of the Nexus if the Nexus is not currently active and the radius is within valid bounds.
//  * - spawnBolt: Spawns a lightning bolt at a specified location for a given duration, relative to the Nexus position.
//  * - spawnDebugMob: Spawns a debug mob (either EntityIMLiving or EntityIMWolf) at the player's location and binds it to the Nexus.
//  * - sendMessage: Overloaded method to send a formatted text message to the player.
//  * - getName: Returns the name of the command ('invasion').
//  * - getUsage: Returns the usage string for the command, which is empty in this implementation.
//  *
//  * The command supports various operations such as starting or ending an invasion, changing mob spawn radius, checking Nexus status,
//  * spawning lightning bolts, and spawning debug mobs for testing purposes.
//  */
// package invmod.command;
// 
// // ... (import statements)
// 
// public class InvasionCommand extends CommandBase {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.command;

import java.lang.reflect.Constructor;

import invmod.mod_invasion;
import invmod.entity.EntityIMLiving;
import invmod.entity.ally.EntityIMWolf;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class InvasionCommand extends CommandBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		String username = sender.getName();
		TileEntityNexus focusNexus = null;
		if (sender instanceof EntityPlayer) {
			focusNexus = TileEntityNexus.getNearest((EntityPlayer) sender, 50);
		}
		if (args.length <= 0 || args.length > 7) {
			this.sendMessage(sender,
					"Command not recognised, use /invasion help for a list of all the available commands",
					TextFormatting.RED);
			return;
		}
		switch (args[0].toLowerCase()) {
		case "help":
			if (args.length == 1)
				this.sendHelp(sender, 1);
			if (args.length == 2) {
				try {
					this.sendHelp(sender, Integer.parseInt(args[1]));
				} catch (NumberFormatException e) {
					this.sendMessage(sender, "Help page argument is not a number", TextFormatting.RED);
				}
			}
			break;
		case "begin":
			if (focusNexus == null) {
				this.sendMessage(sender, "No nexus detected", TextFormatting.RED);
				return;
			}
			if (args.length == 2) {
				int startWave = 1;
				try {
					startWave = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					if (args[1].equalsIgnoreCase("continuous") || args[1].equalsIgnoreCase("cont")
							|| args[1].equalsIgnoreCase("c")) {
						this.sendMessage(sender, "Starting continuous invasion");
						focusNexus.debugStartContinuous();
					} else {
						this.sendMessage(sender, "Unknown start wave", TextFormatting.RED);
						return;
					}
				}
				this.sendMessage(sender, "Starting invasion at wave " + startWave);
				focusNexus.debugStartInvaion(startWave);
			} else {
				this.sendMessage(sender, "No start wave specified, starting at wave 1");
				focusNexus.debugStartInvaion(1);
			}
			break;
		case "end":
			if (focusNexus != null) {
				focusNexus.emergencyStop();
				mod_invasion.broadcastToAll(username + " ended invasion");
			} else {
				this.sendMessage(sender, "No invasion to end", TextFormatting.RED);
			}
			break;
		case "range":
			if (args.length == 2) {
				try {
					int radius = Integer.parseInt(args[1]);
					this.changeRadius(sender, focusNexus, radius);
				} catch (NumberFormatException e) {
					this.sendMessage(sender, "Cannot set range to a value that is not a number", TextFormatting.RED);
				}
			}
			break;
		case "nexusstatus":
			if (focusNexus != null) {
				this.sendMessage(sender, "Nexus status sent to bound players");
				focusNexus.debugStatus();
			} else {
				this.sendMessage(sender, "No nexus detected", TextFormatting.RED);
			}
			break;
		case "bolt":
			this.spawnBolt(sender, focusNexus, args);
			break;
		case "status":
			if (focusNexus != null) {
				this.sendMessage(sender, "Is invasion active: " + focusNexus.isActive());
			} else {
				this.sendMessage(sender, "No nexus detected", TextFormatting.RED);
			}
			break;
		case "debug":
			if (args.length == 2) {
				if (sender instanceof EntityPlayer)
					this.spawnDebugMob((EntityPlayer) sender, focusNexus, args[1]);
			} else {
				this.sendMessage(sender, "No mob specified", TextFormatting.RED);
			}
			break;
		default:
			this.sendMessage(sender,
					"Command not recognised, use /invasion help for a list of all the available commands",
					TextFormatting.RED);
		}
	}

	private void sendHelp(ICommandSender sender, int page) {
		switch (page) {
		case 1:
			this.sendMessage(sender, "--- Showing Invasion help page 1 of 2 ---", TextFormatting.GREEN);
			this.sendMessage(sender, "/invasion begin [x] - Starts an invasion at wave x (default: 1)");
			this.sendMessage(sender, "/invasion begin c - Starts a continuous invasion");
			this.sendMessage(sender, "/invasion end - Ends the invasion, if active");
			this.sendMessage(sender, "/invasion range <x> - Sets the mobs' spawn range (min: 32, max: 128)");
			this.sendMessage(sender,
					"/invasion nexusstatus - Displays debug status to bound players (only displayed if nexus is active)");
			break;
		case 2:
			this.sendMessage(sender, "--- Showing Invasion help page 2 of 2 ---", TextFormatting.GREEN);
			this.sendMessage(sender,
					"/invasion bolt [targX] [targY] [targZ] [durationTicks] - Spawns a lightning bolt to the specified coordinates for the specified amound of time in ticks");
			this.sendMessage(sender, "/invasion status - Returns whether an invasion is active");
			this.sendMessage(sender,
					"/invasion debug <IMEntityName> - Spawns an IM mob and binds it to an available nexus for debugging purposes");
			break;
		default:
			this.sendMessage(sender, "Unknown help page " + page, TextFormatting.RED);
		}
	}

	private void changeRadius(ICommandSender sender, TileEntityNexus focusNexus, int radius) {
		String username = sender.getName();
		if (focusNexus != null) {
			if ((radius >= 32) && (radius <= 128)) {
				if (focusNexus.setSpawnRadius(radius)) {
					this.sendMessage(sender, "Set nexus range to " + radius);
				} else {
					this.sendMessage(sender, username + ": Can't change range while nexus is active",
							TextFormatting.RED);
				}
			} else {
				this.sendMessage(sender, username + ": Range must be between 32 and 128", TextFormatting.RED);
			}
		} else {
			this.sendMessage(sender, username + ": Right-click the nexus first to set target for command",
					TextFormatting.RED);
		}
	}

	private void spawnBolt(ICommandSender sender, TileEntityNexus focusNexus, String[] args) {
		if (focusNexus != null) {
			int x = focusNexus.getPos().getX();
			int y = focusNexus.getPos().getY();
			int z = focusNexus.getPos().getZ();
			int time = 40;
			if (args.length >= 6)
				return;
			try {
				if (args.length >= 5)
					time = Integer.parseInt(args[4]);
				if (args.length >= 4)
					z += Integer.parseInt(args[3]);
				if (args.length >= 3)
					y += Integer.parseInt(args[2]);
				if (args.length >= 2)
					x += Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				this.sendMessage(sender, "Bolt argument(s) must be integer(s)", TextFormatting.RED);
			}
			this.sendMessage(sender, "Spawning bolt at (" + x + ", " + y + ", " + z + ") (time=" + time + ")");
			focusNexus.createBolt(x, y, z, time);
		} else {
			this.sendMessage(sender, "Cannot spawn bolt because no nexus detected", TextFormatting.RED);
		}
	}

	private void spawnDebugMob(EntityPlayer sender, TileEntityNexus focusNexus, String entityName) {
		if (focusNexus != null) {
			try {
				Class<?> clazz = Class.forName(entityName);
				if (EntityIMLiving.class.isAssignableFrom(clazz)) {
					Constructor<? extends EntityIMLiving> c = ((Class<? extends EntityIMLiving>) clazz)
							.getConstructor(World.class, TileEntityNexus.class);
					EntityIMLiving entity = c.newInstance(sender.world, focusNexus);
					entity.setPosition(sender.posX, sender.posY, sender.posZ);
					sender.world.spawnEntity(entity);
				} else if (EntityIMWolf.class.isAssignableFrom(clazz)) {
					Constructor<? extends EntityIMWolf> c = ((Class<? extends EntityIMWolf>) clazz)
							.getConstructor(World.class, TileEntityNexus.class);
					EntityIMWolf entity = c.newInstance(sender.world, focusNexus);
					entity.setPosition(sender.posX, sender.posY, sender.posZ);
					sender.world.spawnEntity(entity);
				} else {
					this.sendMessage(sender, "Entity is not an IM mob", TextFormatting.RED);
				}
			} catch (Exception e) {
				this.sendMessage(sender, "Unable to spawn entity", TextFormatting.RED);
				e.printStackTrace();
			}
		} else {
			this.sendMessage(sender, "No nexus detected", TextFormatting.RED);
		}
	}

	private void sendMessage(ICommandSender sender, String msg) {
		this.sendMessage(sender, msg, null);
	}

	private void sendMessage(ICommandSender sender, String msg, TextFormatting format) {
		TextComponentTranslation s = new TextComponentTranslation(msg);
		if (format != null)
			s.getStyle().setColor(format);
		sender.sendMessage(s);
	}

	@Override
	public String getName() {
		return "invasion";
	}

	@Override
	public String getUsage(ICommandSender icommandsender) {
		return "";
	}
}