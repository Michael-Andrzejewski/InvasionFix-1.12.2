package invmod.item;

import net.minecraft.item.Item;


public class ItemStrongDampingAgent extends Item
{

	private final String name = "strongdampingagent";

	public ItemStrongDampingAgent()
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
