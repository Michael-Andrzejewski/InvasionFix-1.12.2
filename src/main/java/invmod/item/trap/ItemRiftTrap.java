// `^`^`^`
// ```plaintext
// This Java code defines a custom item class `ItemRiftTrap` for a Minecraft mod, which represents a trap item that can be placed in the game world. The class extends `ModItem`, indicating that it is a specialized item within the mod.
// 
// Summary of `ItemRiftTrap` class:
// 
// - Constructor `ItemRiftTrap()`: Sets up the item with the name "riftTrap" and a maximum stack size of 64, allowing players to hold up to 64 of these items in one inventory slot.
// 
// - Method `onItemUseFirst(...)`: Overrides a method from the parent class to define the behavior when a player uses the item. The method checks if the action is performed on the client side (in which case it fails immediately) and only allows placement on the top side of a block (EnumFacing.UP). When a player uses the item, the method attempts to spawn a new `EntityIMTrap` (a trap entity) at the specified location with an offset to center it on the block. It checks for valid placement and absence of other traps in the bounding box area. If the conditions are met, the trap entity is spawned into the world. If the player is not in creative mode, the item is consumed by decreasing the stack size by one. The method returns an `EnumActionResult` indicating the success or failure of the action.
// 
// This class is part of the `invmod.item.trap` package and interacts with the Minecraft API to integrate the custom trap item into the game, providing players with additional gameplay mechanics related to setting traps.
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

public class ItemRiftTrap extends ModItem {

	public ItemRiftTrap() {
		super("riftTrap");
		this.setMaxStackSize(64);
	}

	// @Override
	// public EnumActionResult onItemUseFirst(ItemStack itemstack,
	// EntityPlayer entityplayer, World world, BlockPos blockPos,
	// EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	// {
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		if (world.isRemote)
			return EnumActionResult.FAIL;
		if (side == EnumFacing.UP) {
			EntityIMTrap trap = new EntityIMTrap(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 1);

			if ((trap.isValidPlacement())
					&& (world.getEntitiesWithinAABB(EntityIMTrap.class, trap.getEntityBoundingBox()).size() == 0)) {
				world.spawnEntity(trap);

				// players in creative mode won't lose the item
				if (!player.capabilities.isCreativeMode) {
					ItemStack stack = player.getHeldItem(hand);
					stack.shrink(1);
				}
			}
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.FAIL;
	}

}
