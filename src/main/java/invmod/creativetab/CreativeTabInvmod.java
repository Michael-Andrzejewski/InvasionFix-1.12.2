package invmod.creativetab;

import invmod.BlocksAndItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(BlocksAndItems.blockNexus);
	}

}
