// `^`^`^`
// ```java
// /**
//  * This code is part of the 'invmod' package and is responsible for defining and registering custom blocks and their corresponding item forms in a Minecraft mod. It utilizes the Forge modding API to interact with Minecraft's block and item systems.
//  *
//  * Classes:
//  * - ModBlocks: Holds the static reference to the custom block 'NEXUS_BLOCK' and contains the inner class 'BlockRegistrationHandler'.
//  * - BlockRegistrationHandler: Handles the registration of blocks and item blocks to the game registry during the mod initialization phase.
//  *
//  * Methods:
//  * - registerBlocks: Subscribed to the Forge 'RegistryEvent.Register<Block>' event with high priority, this method registers all custom blocks defined in the mod.
//  * - setBlockName: Helper method to set the unlocalized name of a block.
//  * - setBlockNameAndTab: Helper method to set both the unlocalized name and the creative tab of a block.
//  * - registerItemBlocks: Subscribed to the Forge 'RegistryEvent.Register<Item>' event, this method registers item forms of the custom blocks so they can be held in the player's inventory.
//  * - createItemBlock: Helper method to create an ItemBlock from a Block, setting its unlocalized name, registry name, and creative tab.
//  *
//  * Usage:
//  * - The 'NEXUS_BLOCK' is initialized as null and later set to an actual instance of 'BlockNexus' during the registration process.
//  * - The 'ITEM_BLOCKS' list is used to keep track of all item blocks created for the custom blocks.
//  *
//  * Note:
//  * - The code contains commented out debug statements and a potential error handling comment indicating a check for null blocks, which suggests that the code may be in a development or debugging stage.
//  */
// ```
// `^`^`^`

package invmod;

import static invmod.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import invmod.block.BlockNexus;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModBlocks {

	@ObjectHolder("blocknexus")
	public static final BlockNexus NEXUS_BLOCK = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class BlockRegistrationHandler {

		public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<ItemBlock>();

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final Block[] blocks = { setBlockName(new BlockNexus(), "blocknexus") };

			IForgeRegistry<Block> registry = event.getRegistry();

			for (Block block : blocks) {
				registry.register(block);
			}
		}

		private static Block setBlockName(Block block, String name) {
			return setBlockNameAndTab(block, name, mod_invasion.tabInvmod);
		}

		private static Block setBlockNameAndTab(Block block, String name, @Nullable CreativeTabs tab) {
			Block b = block.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
			return b;
		}

		@SubscribeEvent
		public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
			final ItemBlock[] itemBlocks = { createItemBlock(NEXUS_BLOCK) };

			IForgeRegistry<Item> registry = event.getRegistry();

			for (ItemBlock itemBlock : itemBlocks) {
				registry.register(itemBlock);
				ITEM_BLOCKS.add(itemBlock);
			}
		}

		private static ItemBlock createItemBlock(Block block) {
			// return (ItemBlock) new
			// ItemBlock(block).setUnlocalizedName(block.getLocalizedName())
			// .setRegistryName(block.getRegistryName());
			ItemBlock retItem = new ItemBlock(block);

			/*
			 * if(block == null) { System.out.println("BLOCK IS NULL"); }
			 */

			// Cause of error: The block is null ?!?!?

			retItem.setUnlocalizedName(block.getUnlocalizedName());
			retItem.setRegistryName(block.getRegistryName());
			retItem.setCreativeTab(mod_invasion.tabInvmod);

			return retItem;
		}

	}

}
