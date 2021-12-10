package invmod.item;

import net.minecraft.item.Item;

public class ItemStrongCatalystMixture extends Item {

	private final String name = "strongCatalystMixture";

	public ItemStrongCatalystMixture() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(1);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	public String getName() {
		return this.name;
	}
}
