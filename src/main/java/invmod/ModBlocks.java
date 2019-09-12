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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModBlocks {

	public static final BlockNexus NEXUS_BLOCK = Null();
	
	
	@EventBusSubscriber(modid = Reference.MODID)
	public static class BlockRegistrationHandler {

		public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<ItemBlock>();

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final Block[] blocks = { setBlockName(new BlockNexus(), "blockNexus")
					};

			IForgeRegistry<Block> registry = event.getRegistry();

			for (Block block : blocks) {
				registry.register(block);
			}
		}

		private static Block setBlockName(Block block, String name) {
			return setBlockNameAndTab(block, name, mod_Invasion.tabInvmod);
		}

		private static Block setBlockNameAndTab(Block block, String name, @Nullable CreativeTabs tab) {
			Block b = block.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
			return b;
		}

		@SubscribeEvent
		public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
			final ItemBlock[] itemBlocks = {
					createItemBlock(NEXUS_BLOCK)};

			IForgeRegistry<Item> registry = event.getRegistry();

			for (ItemBlock itemBlock : itemBlocks) {
				registry.register(itemBlock);
				ITEM_BLOCKS.add(itemBlock);
			}
		}

		private static ItemBlock createItemBlock(Block block) {
			return (ItemBlock) new ItemBlock(block).setUnlocalizedName(block.getLocalizedName())
					.setRegistryName(block.getRegistryName());
		}

	}

}
