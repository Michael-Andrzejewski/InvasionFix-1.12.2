package invmod.item;

import net.minecraft.item.Item;


public class ItemNexusCatalyst extends Item
{

	private final String name = "nexusCatalyst";

	public ItemNexusCatalyst()
	{
		//this.setRegistryName(this.name);
		//GameRegistry.register(this);
		//this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(1);
		//this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	public String getName()
	{
		return this.name;
	}
}
