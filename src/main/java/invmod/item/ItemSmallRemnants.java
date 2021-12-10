package invmod.item;

import net.minecraft.item.Item;

public class ItemSmallRemnants extends Item {

	private final String name = "smallRemnants";

	public ItemSmallRemnants() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		this.setMaxDamage(0);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(64);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	public String getName() {
		return this.name;
	}
}
