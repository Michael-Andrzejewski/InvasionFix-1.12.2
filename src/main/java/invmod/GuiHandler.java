package invmod;

import invmod.client.gui.GuiNexus;
import invmod.inventory.container.ContainerNexus;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		if (id == Config.NEXUS_GUI_ID)
			return new GuiNexus(player.inventory, (TileEntityNexus) world.getTileEntity(new BlockPos(x, y, z)));

		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == Config.NEXUS_GUI_ID)
			return new ContainerNexus(player.inventory, (TileEntityNexus) world.getTileEntity(new BlockPos(x, y, z)));

		return null;
	}
}