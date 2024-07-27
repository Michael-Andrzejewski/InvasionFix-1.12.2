// `^`^`^`
// ```java
// /**
//  * This class extends the BehaviorDefaultDispenseItem to provide custom behavior for dispensing spawn eggs from a dispenser block in Minecraft.
//  * It overrides the dispenseStack method to handle the creation and placement of entities in the game world when a spawn egg is dispensed.
//  *
//  * Methods:
//  * - dispenseStack(IBlockSource blockSource, ItemStack stack): This method is called when a spawn egg is dispensed from a dispenser.
//  *   It calculates the position where the entity should be spawned based on the dispenser's location and facing direction.
//  *   It then calls ItemSpawnEgg.spawnCreature to create the entity at the calculated position.
//  *   If the spawned entity is an instance of EntityLiving and the spawn egg item stack has a custom display name,
//  *   the entity is given this custom name. Finally, the method decrements the stack size of the spawn egg by one and returns the modified stack.
//  *
//  * Note: The commented-out lines involving EnumFacing suggest that there was an alternative or previous method to determine the direction
//  * in which the entity would be spawned based on the dispenser's orientation. This has been replaced with the current IPosition-based logic.
//  */
// package invmod.util.spawneggs;
// 
// // ... (imports)
// 
// public class DispenserBehaviorSpawnEgg extends BehaviorDefaultDispenseItem {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.util.spawneggs;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class DispenserBehaviorSpawnEgg extends BehaviorDefaultDispenseItem {

	@Override
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
		// EnumFacing enumfacing =
		// BlockDispenser.getFacing(blockSource.getBlockMetadata());
		IPosition direction = BlockDispenser.getDispensePosition(blockSource);
		double x = blockSource.getX() + direction.getX(); // enumfacing.getFrontOffsetX();
		double y = (float) blockSource.getY() + 0.2F;
		double z = blockSource.getZ() + direction.getZ(); // enumfacing.getFrontOffsetZ();
		Entity entity = ItemSpawnEgg.spawnCreature(blockSource.getWorld(), stack, new BlockPos(x, y, z));
		if (entity instanceof EntityLiving && stack.hasDisplayName())
			((EntityLiving) entity).setCustomNameTag(stack.getDisplayName());

		stack.splitStack(1);

		return stack;
	}
}
