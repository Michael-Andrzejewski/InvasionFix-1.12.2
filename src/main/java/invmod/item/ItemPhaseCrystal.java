package invmod.item;

import net.minecraft.item.Item;


public class ItemPhaseCrystal extends Item
{

	private final String name = "phaseCrystal";

	public ItemPhaseCrystal()
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
