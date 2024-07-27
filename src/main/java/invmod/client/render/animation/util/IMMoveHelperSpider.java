// `^`^`^`
// ```java
// /**
//  * This class is part of a mod for Minecraft that provides custom movement behavior for spider-like entities.
//  * It extends the IMMoveHelper class, adding the ability for the entities to climb solid blocks.
//  *
//  * Public Methods:
//  * - IMMoveHelperSpider(EntityIMMob par1EntityLiving): Constructor that initializes the helper with a specific entity.
//  *
//  * Protected Methods:
//  * - getClimbFace(BlockPos blockPos): Determines the direction an entity should climb based on its current path and the solidity of surrounding blocks.
//  *
//  * The getClimbFace method performs several key operations:
//  * 1. It calculates the entity's position based on its width and the provided block position.
//  * 2. It retrieves the entity's current path and determines the direction of the next path point relative to the current one.
//  * 3. It iterates through adjacent block positions in a loop, checking for a solid block that the entity can climb.
//  * 4. It returns the EnumFacing direction that corresponds to a solid block face or null if no climbable face is found.
//  *
//  * Note: The method contains a TODO comment indicating that a section of the code needs further attention or correction.
//  */
// ```
// 
// This summary provides an overview of the class's purpose, its constructor, and the key method it contains. It also highlights the main steps taken by the `getClimbFace` method to determine the climbable direction for the entity and notes where the code may require additional work.
// `^`^`^`

package invmod.client.render.animation.util;

import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.monster.EntityIMMob;
import invmod.util.Coords;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class IMMoveHelperSpider extends IMMoveHelper {
	public IMMoveHelperSpider(EntityIMMob par1EntityLiving) {
		super(par1EntityLiving);
	}

	@Override
	protected EnumFacing getClimbFace(BlockPos blockPos) {
		int mobX = MathHelper.floor(blockPos.getX() - this.entity.width / 2.0F);
		int mobY = MathHelper.floor(blockPos.getY());
		int mobZ = MathHelper.floor(blockPos.getZ() - this.entity.width / 2.0F);

		int index = 0;
		Path path = this.entity.getNavigatorNew().getPath();
		if ((path != null) && (!path.isFinished())) {
			PathNode currentPoint = path.getPathPointFromIndex(path.getCurrentPathIndex());
			int pathLength = path.getCurrentPathLength();
			for (int i = path.getCurrentPathIndex(); i < pathLength; i++) {
				PathNode point = path.getPathPointFromIndex(i);
				if (point.pos.x > currentPoint.pos.x)
					break;
				if (point.pos.x < currentPoint.pos.x) {
					index = 2;
					break;
				}
				if (point.pos.z > currentPoint.pos.z) {
					index = 4;
					break;
				}
				if (point.pos.z < currentPoint.pos.z) {
					index = 6;
					break;
				}
			}

		}

		for (int count = 0; count < 8; count++) {
			BlockPos pos = new BlockPos(mobX + Coords.offsetAdj2X[index], mobY, mobZ + Coords.offsetAdj2Z[index]);
			IBlockState blockState = this.entity.world.getBlockState(pos);
			boolean isSolidBlock = true;
			for (EnumFacing side : EnumFacing.values()) {
				if (!blockState.getBlock().isSideSolid(blockState, this.entity.world, pos, side)) {
					isSolidBlock = false;
					break;
				}
			}
			// if (blockState.getBlock().isSideSolid(blockState, this.entity.world, pos,
			// side)/*.isSolidFullCube()*/) {
			if (isSolidBlock) {
				// TODO: Fix this, I just typed something random here
				return EnumFacing.getFront(index / 2);
				// return index / 2;
			}
			index++;
			if (index > 7)
				index = 0;
		}
		return null;
	}
}