package invmod.item;

import net.minecraft.item.Item;


public class ItemEngyHammer extends Item
{

	private final String name = "engyHammer";

	public ItemEngyHammer()
	{
		//this.setRegistryName(this.name);
		//GameRegistry.register(this);
		//this.setCreativeTab(null);
		//this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	public String getName()
	{
		return this.name;
	}
}
