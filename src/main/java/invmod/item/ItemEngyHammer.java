package invmod.item;

import invmod.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEngyHammer extends Item {

	private final String name = "engyHammer";

	public ItemEngyHammer() {
		this.setRegistryName(this.name);
		GameRegistry.register(this);
		this.setCreativeTab(null);
		this.setUnlocalizedName(Reference.MODID + "_" + name);
		this.setFull3D();
		this.setMaxStackSize(1);
	}

	public String getName() {
		return name;
	}
}
