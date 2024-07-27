// `^`^`^`
// ```java
// /**
//  * Interface: ICanDig
//  * 
//  * Purpose:
//  * The ICanDig interface defines the contract for entities in a Minecraft mod that are capable of digging or removing blocks in the game world. It outlines methods for determining the order in which blocks are removed, the cost associated with removing a block, whether a block can be cleared, actions to take upon block removal, and accessing the terrain.
//  * 
//  * Methods:
//  * - BlockPos[] getBlockRemovalOrder(BlockPos pos):
//  *   Determines and returns an array of BlockPos objects representing the order in which blocks should be removed from a specified starting position.
//  * 
//  * - float getBlockRemovalCost(BlockPos pos):
//  *   Calculates and returns the cost of removing a block at the specified position. This cost could be used to determine the effort or time required to remove the block.
//  * 
//  * - boolean canClearBlock(BlockPos pos):
//  *   Checks and returns a boolean indicating whether the block at the specified position can be cleared by the entity implementing this interface.
//  * 
//  * - void onBlockRemoved(BlockPos pos, IBlockState state):
//  *   Called when a block is removed. Allows the implementing entity to perform any necessary actions or updates after a block is removed, given the block's position and state.
//  * 
//  * - IBlockAccess getTerrain():
//  *   Provides access to the terrain in which the entity is operating. This allows the entity to gather information about the blocks and their states in the world.
//  * 
//  * Usage:
//  * This interface is intended to be implemented by mod entities that need to interact with the game world by digging or removing blocks. It provides a structured approach to handle block removal with consideration for order, cost, and the ability to clear blocks, as well as responding to changes in the terrain.
//  */
// package invmod.entity;
// 
// import net.minecraft.block.state.IBlockState;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.world.IBlockAccess;
// 
// public interface ICanDig {
//     // Interface methods
// }
// ```
// `^`^`^`

package invmod.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ICanDig {

	public BlockPos[] getBlockRemovalOrder(BlockPos pos);

	public float getBlockRemovalCost(BlockPos pos);

	public boolean canClearBlock(BlockPos pos);

	public void onBlockRemoved(BlockPos pos, IBlockState state);

	public IBlockAccess getTerrain();

}