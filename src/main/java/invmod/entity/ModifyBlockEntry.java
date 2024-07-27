// `^`^`^`
// ```java
// /**
//  * This class represents an entry for modifying a block within a Minecraft world. It is used to store information about block transformations, including the position of the block, the state of the block before and after modification, and the cost associated with the modification.
//  *
//  * Constructors:
//  * - ModifyBlockEntry(BlockPos pos, Block newBlock): Initializes an entry with the position and the new block state using the default state of the block.
//  * - ModifyBlockEntry(BlockPos pos, IBlockState newBlock): Initializes an entry with the position and the new block state directly.
//  * - ModifyBlockEntry(BlockPos pos, Block newBlock, int cost): Initializes an entry with the position, the new block state using the default state of the block, and the cost of modification.
//  * - ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost): Initializes an entry with the position, the new block state, and the cost of modification.
//  * - ModifyBlockEntry(BlockPos pos, Block newBlock, int cost, Block oldBlock): Initializes an entry with the position, the new block state using the default state of the block, the cost, and the old block state using the default state of the old block.
//  * - ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost, IBlockState oldBlock): Initializes an entry with the position, the new block state, the cost, and the old block state.
//  *
//  * Methods:
//  * - getPos(): Returns the position of the block to be modified.
//  * - getNewBlock(): Returns the new state of the block after modification.
//  * - getCost(): Returns the cost associated with the block modification.
//  * - getOldBlock(): Returns the original state of the block before modification.
//  * - setOldBlock(IBlockState state): Sets the original state of the block before modification.
//  *
//  * Note: Some commented-out methods and fields suggest potential extensions for block metadata handling and position information retrieval.
//  */
// package invmod.entity;
// 
// // ... (rest of the imports and class code)
// ```
// `^`^`^`

package invmod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class ModifyBlockEntry
/* implements IPosition */ {
	private BlockPos pos;
	private IBlockState oldBlock;
	private IBlockState newBlock;
	// private IBlockState newBlockMeta;
	private int cost;

	public ModifyBlockEntry(BlockPos pos, Block newBlock) {
		this(pos, newBlock.getDefaultState(), 0, null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock) {
		this(pos, newBlock, 0, null);
	}

	public ModifyBlockEntry(BlockPos pos, Block newBlock, int cost) {
		this(pos, newBlock != null ? newBlock.getDefaultState() : null, cost, null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost) {
		this(pos, newBlock, cost, null);
	}

	public ModifyBlockEntry(BlockPos pos, Block newBlock, int cost, Block oldBlock) {
		this(pos, newBlock.getDefaultState(), cost, oldBlock != null ? oldBlock.getDefaultState() : null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost,
			/* IBlockState newBlockMeta, */ IBlockState oldBlock) {
		this.pos = pos;
		this.newBlock = newBlock;
		this.cost = cost;
		// this.newBlockMeta = newBlockMeta;
		this.oldBlock = oldBlock;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	/*
	 * @Override public int getXCoord(){ return this.xCoord; }
	 * 
	 * @Override public int getYCoord(){ return this.yCoord; }
	 * 
	 * @Override public int getZCoord(){ return this.zCoord; }
	 */

	public IBlockState getNewBlock() {
		return this.newBlock;
	}

	/*
	 * public IBlockState getNewBlockMeta() { return this.newBlockMeta; }
	 */

	public int getCost() {
		return this.cost;
	}

	public IBlockState getOldBlock() {
		return this.oldBlock;
	}

	public void setOldBlock(IBlockState state) {
		this.oldBlock = state;
	}

}