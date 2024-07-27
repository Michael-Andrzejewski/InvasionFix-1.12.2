// `^`^`^`
// ```java
// /**
//  * This class serves as a GUI (Graphical User Interface) handler for the Nexus block within the Invasion Mod.
//  * It is responsible for providing the correct GUI elements to the client and server when a Nexus block is interacted with.
//  * 
//  * Methods:
//  * - getClientGuiElement: This method is called on the client side to open the GUI for the Nexus block. It checks if the
//  *   provided ID matches the Nexus GUI ID and, if so, creates a new GuiNexus instance with the player's inventory and
//  *   the TileEntityNexus at the specified position.
//  * 
//  * - getServerGuiElement: This method is called on the server side to handle the container for the Nexus block. It checks
//  *   if the provided ID matches the Nexus GUI ID and, if so, creates a new ContainerNexus instance with the player's
//  *   inventory and the TileEntityNexus at the specified position.
//  * 
//  * Dependencies:
//  * - invmod.client.gui.GuiNexus: The GUI class for the Nexus block.
//  * - invmod.inventory.container.ContainerNexus: The container class for the Nexus block that handles item storage and manipulation.
//  * - invmod.tileentity.TileEntityNexus: The TileEntity class for the Nexus block that stores all the data and state information.
//  * - invmod.util.config.Config: A configuration class that holds various settings, including the Nexus GUI ID.
//  * - net.minecraft and net.minecraftforge: Minecraft Forge classes used for player, world, and GUI handling.
//  */
// package invmod;
// 
// // ... (imports and class definition)
// ```
// This summary provides an overview of the `GuiHandler` class, its purpose within the mod, and a description of its methods and dependencies.
// `^`^`^`

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