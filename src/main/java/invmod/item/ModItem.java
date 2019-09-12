package invmod.item;

import invmod.mod_Invasion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


public class ModItem extends Item
{

	public final String name;

	public ModItem(String name)
	{
		this.name = name;
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setCreativeTab(mod_Invasion.tabInvmod);
		//GameRegistry.register(this);
	}

	@Override
	public ModItem setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public ModItem setMaxStackSize(int maxStackSize)
	{
		super.setMaxStackSize(maxStackSize);
		return this;
	}

	@Override
	public ModItem setMaxDamage(int maxDamageIn)
	{
		super.setMaxDamage(maxDamageIn);
		return this;
	}

	@Override
	public ModItem setFull3D()
	{
		super.setFull3D();
		return this;
	}

}
