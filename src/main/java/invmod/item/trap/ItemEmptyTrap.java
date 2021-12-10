package invmod.item.trap;

import net.minecraft.item.Item;

public class ItemEmptyTrap extends Item {

	private final String name = "emptyTrap";

	public ItemEmptyTrap() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		this.setMaxStackSize(64);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	public String getName() {
		return this.name;
	}

}
