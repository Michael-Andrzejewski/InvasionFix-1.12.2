// `^`^`^`
// ```java
// /**
//  * This code is part of the invmod utility package and is intended to integrate with the Waila mod, providing additional information about the in-game block "BlockNexus" when it is looked at by the player.
//  *
//  * The IMWailaProvider class implements the IWailaDataProvider interface, which defines methods that Waila will call to get information about blocks.
//  *
//  * Methods:
//  * - getWailaStack: Returns the ItemStack that Waila will display for the block. Currently, it returns null, indicating no change to the default behavior.
//  * - getWailaHead: Returns the list of strings to be displayed at the head of the tooltip. This method does not modify the default behavior and returns the current tooltip.
//  * - getWailaBody: Returns the list of strings to be displayed in the body of the tooltip. It adds information about the activation status of the TileEntityNexus and the current wave number if the nexus is active.
//  * - getWailaTail: Returns the list of strings to be displayed at the tail of the tooltip. This method does not modify the default behavior and returns the current tooltip.
//  * - callbackRegister: A static method that is called to register the IMWailaProvider as a body provider for the BlockNexus class, allowing it to provide custom body text for the tooltip.
//  *
//  * The code uses the TileEntityNexus class to determine the status of the nexus and the current wave number, which are then added to the tooltip displayed to the player.
//  */
// ```
// `^`^`^`

package invmod.util;

//package invmod.common.util;
//
//import invmod.common.nexus.BlockNexus;
//import invmod.common.nexus.TileEntityNexus;
//
//import java.util.List;
//
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.StatCollector;
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import mcp.mobius.waila.api.IWailaRegistrar;
//
//public class IMWailaProvider implements IWailaDataProvider{
//	
//	@Override
//	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) { return null; }
//
//	@Override
//	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//		return currenttip;
//	}
//	@Override
//	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip,IWailaDataAccessor accessor, IWailaConfigHandler config) {
//		TileEntityNexus teNexus = (TileEntityNexus)accessor.getTileEntity();
//		if (teNexus != null){
//				if(teNexus.isActive()){
//					int waveNumber=teNexus.getCurrentWave();
//					currenttip.add(StatCollector.translateToLocal("invmod.waila.status.active"));
//					currenttip.add(StatCollector.translateToLocal("invmod.waila.wavenumber")+waveNumber);
//					
//				}else{
//					currenttip.add(StatCollector.translateToLocal("invmod.waila.status.deactivated"));
//					
//				}
//		}
//		
//		return currenttip;
//	}
//
//	@Override
//	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,	IWailaConfigHandler config) {
//		return currenttip;
//	}
//	
//	public static void callbackRegister(IWailaRegistrar registrar){
//		registrar.registerBodyProvider(new IMWailaProvider(), BlockNexus.class);
//	}
//
//}
