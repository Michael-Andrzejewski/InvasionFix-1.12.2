package invmod;

import invmod.block.BlockNexus;
import invmod.item.ItemDebugWand;
import invmod.item.ItemInfusedSword;
import invmod.item.ItemProbe;
import invmod.item.ItemSearingBow;
import invmod.item.ItemStrangeBone;
import invmod.item.ModItem;
import invmod.item.trap.ItemFlameTrap;
import invmod.item.trap.ItemPoisonTrap;
import invmod.item.trap.ItemRiftTrap;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import invmod.util.spawneggs.ItemSpawnEgg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;


public class BlocksAndItems
{

	// The almighty Nexus Block Declaration
	public static BlockNexus blockNexus;

	// Item Declarations
	public static ModItem itemPhaseCrystal;
	public static ModItem itemRiftFlux;
	public static ModItem itemSmallRemnants;
	public static ModItem itemNexusCatalyst;
	public static ItemInfusedSword itemInfusedSword;
	public static ItemSearingBow itemSearingBow;
	public static ModItem itemCatalystMixture;
	public static ModItem itemStableCatalystMixture;
	public static ModItem itemStableNexusCatalyst;
	public static ModItem itemDampingAgent;
	public static ModItem itemStrongDampingAgent;
	public static ModItem itemStrangeBone;
	public static ItemProbe itemProbe;
	public static ModItem itemStrongCatalystMixture;
	public static ModItem itemStrongCatalyst;
	public static ModItem itemEngyHammer;
	public static ItemDebugWand itemDebugWand;
	public static ModItem itemEmptyTrap;
	public static ModItem itemFlameTrap;
	public static ModItem itemRiftTrap;
	public static ModItem itemPoisonTrap;

	public static ItemSpawnEgg itemSpawnEgg;

	//Load Blocks
	static void loadBlocks()
	{
		blockNexus = new BlockNexus();
		GameRegistry.registerTileEntity(TileEntityNexus.class, "Nexus");
	}

	//Load Items
	static void loadItems()
	{

		itemPhaseCrystal = new ModItem("phaseCrystal").setMaxStackSize(1);
		itemRiftFlux = new ModItem("riftFlux")
		{
			@Override
			public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
				return EnumActionResult.FAIL;
			};
			
			/*@Override
			public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
			{
				return EnumActionResult.FAIL;
			}*/
		}.setMaxDamage(0).setMaxStackSize(64);
		itemSmallRemnants = new ModItem("smallRemnants").setMaxDamage(0).setMaxStackSize(64);
		itemNexusCatalyst = new ModItem("nexusCatalyst").setMaxStackSize(1);
		itemInfusedSword = new ItemInfusedSword();
		itemSearingBow = new ItemSearingBow();
		itemCatalystMixture = new ModItem("catalystMixture").setMaxStackSize(1);
		itemStableCatalystMixture = new ModItem("stableCatalystMixture").setMaxStackSize(1);
		itemStableNexusCatalyst = new ModItem("stableNexusCatalyst").setMaxStackSize(1);
		itemDampingAgent = new ModItem("dampingAgent").setMaxStackSize(1);
		itemStrongDampingAgent = new ModItem("strongDampingAgent").setMaxStackSize(1);
		itemStrangeBone = new ItemStrangeBone();
		itemStrongCatalystMixture = new ModItem("strongCatalystMixture").setMaxStackSize(1);
		itemStrongCatalyst = new ModItem("strongCatalyst").setMaxStackSize(1);
		itemEngyHammer = new ModItem("engyHammer").setCreativeTab(null).setFull3D().setMaxStackSize(1);
		itemProbe = new ItemProbe();

		itemEmptyTrap = new ModItem("emptyTrap").setMaxStackSize(64);
		itemFlameTrap = new ItemFlameTrap();
		itemRiftTrap = new ItemRiftTrap();
		itemPoisonTrap = new ItemPoisonTrap();

		itemSpawnEgg = new ItemSpawnEgg();

		itemDebugWand = Config.DEBUG ? new ItemDebugWand() : null;

	}

	static void registerItems(FMLInitializationEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			ItemModelMesher renderItem = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

			registerItem(itemPhaseCrystal);
			registerItem(itemRiftFlux);
			registerItem(itemSmallRemnants);
			registerItem(itemNexusCatalyst);
			registerItem(itemCatalystMixture);
			registerItem(itemStableCatalystMixture);
			registerItem(itemStableNexusCatalyst);
			registerItem(itemDampingAgent);
			registerItem(itemStrangeBone);
			registerItem(itemStrongCatalystMixture);
			registerItem(itemStrongCatalyst);
			registerItem(itemStrongDampingAgent);
			registerItem(itemEngyHammer);
			registerItem(itemEmptyTrap);
			registerItem(itemFlameTrap);
			registerItem(itemRiftTrap);
			registerItem(itemPoisonTrap);

			renderItem.register(itemInfusedSword, 0, new ModelResourceLocation(Reference.MODID + ":" + itemInfusedSword.name, "inventory"));
			renderItem.register(itemSearingBow, 0, new ModelResourceLocation(Reference.MODID + ":" + itemSearingBow.name, "inventory"));
			renderItem.register(itemProbe, 0, new ModelResourceLocation(Reference.MODID + ":" + itemProbe.name, "inventory"));
			renderItem.register(itemProbe, 1, new ModelResourceLocation(Reference.MODID + ":" + itemProbe.name, "inventory"));

			renderItem.register(itemSpawnEgg, new ItemMeshDefinition()
			{
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack)
				{
					return new ModelResourceLocation("spawn_egg", "inventory");
				}
			});

			renderItem.register(blockNexus.itemBlock, 0, new ModelResourceLocation(Reference.MODID + ":" + blockNexus.name, "inventory"));

		}

	}

	private static void registerItem(ModItem item)
	{
		ItemModelMesher renderItem = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation resource = new ModelResourceLocation(Reference.MODID + ":" + item.name, "inventory");
		renderItem.register(item, 0, resource);
	}

}
