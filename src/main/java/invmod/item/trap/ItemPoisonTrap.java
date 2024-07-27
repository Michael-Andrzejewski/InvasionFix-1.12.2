// `^`^`^`
// ```java
// /**
//  * This class represents a custom item for a poison trap in a Minecraft mod.
//  * The item can be used to place poison traps in the game world.
//  *
//  * Class: ItemPoisonTrap
//  * Package: invmod.item.trap
//  * Superclass: ModItem
//  * Dependencies: Minecraft Forge, invmod
//  *
//  * Constructor:
//  * - ItemPoisonTrap(): Sets the registry name of the item and the maximum stack size to 64.
//  *
//  * Methods:
//  * - onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand):
//  *   Called when a player uses the item. It checks if the action is performed on the server side and if the side of the block clicked is the top side.
//  *   If conditions are met, it attempts to place a poison trap entity at the specified location. It ensures the placement is valid and that no other
//  *   poison trap entities are present in the bounding box area. If the player is not in creative mode, it decreases the item stack by one.
//  *   Returns an EnumActionResult indicating the success or failure of the action.
//  *
//  * Usage:
//  * - Players can use this item to place poison traps on the top side of blocks in the game.
//  * - The item is limited to non-creative modes for consumption upon use.
//  * - It ensures that traps are placed without overlapping existing traps.
//  */
// ```
// `^`^`^`

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