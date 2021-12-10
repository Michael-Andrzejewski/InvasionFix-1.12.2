package invmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRiftFlux extends Item {

	private final String name = "riftFlux";

	public ItemRiftFlux() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		this.setMaxDamage(0);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(64);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	/*
	 * @Override public EnumActionResult onItemUseFirst(ItemStack itemstack,
	 * EntityPlayer entityplayer, World world, BlockPos blockPos, EnumFacing side,
	 * float hitX, float hitY, float hitZ, EnumHand hand) { return
	 * EnumActionResult.FAIL; }
	 */
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.FAIL;
	}

	public String getName() {
		return this.name;
	}
}