package invmod.creativetab;

import invmod.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class CreativeTabInvmod extends CreativeTabs
{

	public CreativeTabInvmod()
	{
		super("invasionTab");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem()
	{
		return new ItemStack(Item.getItemFromBlock(/*BlocksAndItems.blockNexus*/ModBlocks.NEXUS_BLOCK),1);
	}

}
