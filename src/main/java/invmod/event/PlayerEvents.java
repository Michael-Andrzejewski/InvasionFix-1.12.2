package invmod.event;

import java.util.Map;
import invmod.mod_Invasion;
import invmod.util.ModLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class PlayerEvents
{

	@SubscribeEvent
	public void playerLoginEvent(EntityJoinWorldEvent entityJoinWorldEvent)
	{
		ModLogger.logDebug("Player logged in.");

		for (Map.Entry entry : mod_Invasion.deathList.entrySet())
		{
			for (World world : DimensionManager.getWorlds())
			{
				EntityPlayer player = world
					.getPlayerEntityByName((String)entry.getKey());
				if (player != null)
				{
					player.attackEntityFrom(DamageSource.magic, 500.0F);
					player.setDead();
					mod_Invasion.deathList.remove(player.getDisplayName());
					mod_Invasion.broadcastToAll("Nexus energies caught up to "
						+ player.getDisplayName());
				}
			}
		}

	}
}
