// `^`^`^`
// ```java
// /**
//  * This code is part of the 'invmod' package and is responsible for handling events related to player entities within the game, specifically when a player logs into the game world. The code is designed to work with the Minecraft Forge modding API and is part of a mod called 'mod_invasion'.
// 
//  * The PlayerEvents class listens for the EntityJoinWorldEvent, which is triggered when an entity, including players, joins a world. The playerLoginEvent method is annotated with @SubscribeEvent, indicating that it is an event handler.
// 
//  * playerLoginEvent(EntityJoinWorldEvent): This method is called when any entity joins the world. It checks if the entity is a player and if the player's name is in the 'deathList' map maintained by the mod_invasion class. If the player is listed, it inflicts a large amount of magic damage to the player, effectively killing them, removes their name from the deathList, and broadcasts a message to all players indicating that the "Nexus energies" have caught up to the deceased player.
// 
//  * The code utilizes the ModLogger utility to log debug information and the DimensionManager from Forge to access all loaded worlds. It iterates through each world to find the player entity by name and applies the specified effects if conditions are met.
// 
//  * Note: This code assumes the existence of the mod_invasion class and its static members deathList and broadcastToAll, as well as the ModLogger utility class. It is designed to be part of a larger mod that likely involves additional gameplay mechanics such as "Nexus energies".
//  */
// ```
// `^`^`^`

package invmod.event;

import java.util.Map;

import invmod.mod_invasion;
import invmod.util.ModLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerEvents {

	@SubscribeEvent
	public void playerLoginEvent(EntityJoinWorldEvent entityJoinWorldEvent) {
		ModLogger.logDebug("Player logged in.");

		for (Map.Entry entry : mod_invasion.deathList.entrySet()) {
			for (World world : DimensionManager.getWorlds()) {
				EntityPlayer player = world.getPlayerEntityByName((String) entry.getKey());
				if (player != null) {
					player.attackEntityFrom(DamageSource.MAGIC, 500.0F);
					player.setDead();
					mod_invasion.deathList.remove(player.getDisplayName());
					mod_invasion.broadcastToAll("Nexus energies caught up to " + player.getDisplayName());
				}
			}
		}

	}
}
