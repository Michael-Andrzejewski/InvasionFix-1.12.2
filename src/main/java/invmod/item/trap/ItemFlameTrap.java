// `^`^`^`
// ```java
// /**
//  * This class represents the item for a Flame Trap in a Minecraft mod. The Flame Trap is an entity that can be placed in the world by the player to create a trap for other entities.
//  *
//  * Public Methods:
//  * - ItemFlameTrap(): Constructor that sets the item's name and maximum stack size.
//  * - onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand): Handles the item's usage behavior when a player attempts to use it. If the player right-clicks on the top side of a block, it attempts to place a Flame Trap entity at the specified location. The trap is only placed if the location is valid and there are no other traps in the bounding box area. If the player is not in creative mode, using the item decreases its stack size by one. The method returns an EnumActionResult indicating the success or failure of the action.
//  *
//  * The class extends ModItem, which is assumed to be a custom class within the mod that provides a framework for mod items.
//  */
// package invmod.item.trap;
// 
// // ... (imports)
// 
// public class ItemFlameTrap extends ModItem {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.item.trap;

import invmod.entity.block.trap.EntityIMTrap;
import invmod.item.ModItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlameTrap extends ModItem {

	public ItemFlameTrap() {
		super("flameTrap");
		this.setMaxStackSize(64);
	}

	// @Override
	// public EnumActionResult onItemUseFirst(ItemStack itemstack, EntityPlayer
	// entityplayer, World world, BlockPos blockPos,
	// EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	// {
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, net.minecraft.util.EnumHand hand) {
		if (world.isRemote)
			return EnumActionResult.FAIL;
		if (side == EnumFacing.UP) {
			EntityIMTrap trap = new EntityIMTrap(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 2);

			if ((trap.isValidPlacement())
					&& (world.getEntitiesWithinAABB(EntityIMTrap.class, trap.getEntityBoundingBox()).size() == 0)) {
				world.spawnEntity(trap);

				ItemStack itemstack = player.getHeldItem(hand);
				// players in creative mode won't lose the item
				if (!player.capabilities.isCreativeMode)
					itemstack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.FAIL;
	}

}