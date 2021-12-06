package invmod;

import invmod.client.gui.GuiNexus;
import invmod.inventory.container.ContainerNexus;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		System.out.println("getClientGuiElement()");
		if (id == Config.NEXUS_GUI_ID)
		{
			System.out.println(player.inventory);
			TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(new BlockPos(x, y, z));
			System.out.println(nexus);
			if (nexus != null) {
				System.out.println("return guinexus");
				return new GuiNexus(player.inventory, nexus);
			}
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == Config.NEXUS_GUI_ID)
		{
			TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(new BlockPos(x, y, z));
			if (nexus != null) return new ContainerNexus(player.inventory, nexus);
		}
		return null;
	}
}