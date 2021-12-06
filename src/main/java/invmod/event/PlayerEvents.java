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


public class PlayerEvents
{

	@SubscribeEvent
	public void playerLoginEvent(EntityJoinWorldEvent entityJoinWorldEvent)
	{
		ModLogger.logDebug("Player logged in.");

		for (Map.Entry entry : mod_invasion.deathList.entrySet())
		{
			for (World world : DimensionManager.getWorlds())
			{
				EntityPlayer player = world
					.getPlayerEntityByName((String)entry.getKey());
				if (player != null)
				{
					player.attackEntityFrom(DamageSource.MAGIC, 500.0F);
					player.setDead();
					mod_invasion.deathList.remove(player.getDisplayName());
					mod_invasion.broadcastToAll("Nexus energies caught up to "
						+ player.getDisplayName());
				}
			}
		}

	}
}
