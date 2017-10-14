package invmod.item;

import invmod.Reference;
import invmod.mod_Invasion;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCatalystMixture extends ModItem {
	
	public ItemCatalystMixture() {
		super("catalystMixture");
		this.setMaxStackSize(1);
	}
	
	public String getName(){
		return name;
	}
}
