package invmod.item.trap;

import invmod.entity.block.trap.EntityIMTrap;
import invmod.item.ModItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPoisonTrap extends ModItem {

	public ItemPoisonTrap() {
		super("poisonTrap");
		this.setMaxStackSize(64);
	}

	// @Override
	// public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer
	// entityplayer, World world, BlockPos blockPos,
	// EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	// {
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote)
			return EnumActionResult.FAIL;
		if (side == EnumFacing.UP) {
			EntityIMTrap trap = new EntityIMTrap(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 3);

			if ((trap.isValidPlacement())
					&& (world.getEntitiesWithinAABB(EntityIMTrap.class, trap.getEntityBoundingBox()).size() == 0)) {
				world.spawnEntity(trap);

				// players in creative mode won't lose the item
				if (!player.capabilities.isCreativeMode) {
					ItemStack item = player.getHeldItem(hand);
					// itemstack.stackSize -= 1;
					item.shrink(1);
				}
			}
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.FAIL;
	}

}