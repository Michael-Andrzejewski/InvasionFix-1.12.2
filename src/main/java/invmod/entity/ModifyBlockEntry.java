package invmod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;


public class ModifyBlockEntry
/*implements IPosition*/ {
	private BlockPos pos;
	private IBlockState oldBlock;
	private IBlockState newBlock;
	//private IBlockState newBlockMeta;
	private int cost;

	public ModifyBlockEntry(BlockPos pos, Block newBlock)
	{
		this(pos, newBlock.getDefaultState(), 0, null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock)
	{
		this(pos, newBlock, 0, null);
	}

	public ModifyBlockEntry(BlockPos pos, Block newBlock, int cost)
	{
		this(pos, newBlock != null ? newBlock.getDefaultState() : null, cost, null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost)
	{
		this(pos, newBlock, cost, null);
	}

	public ModifyBlockEntry(BlockPos pos, Block newBlock, int cost, Block oldBlock)
	{
		this(pos, newBlock.getDefaultState(), cost, oldBlock != null ? oldBlock.getDefaultState() : null);
	}

	public ModifyBlockEntry(BlockPos pos, IBlockState newBlock, int cost, /*IBlockState newBlockMeta,*/ IBlockState oldBlock)
	{
		this.pos = pos;
		this.newBlock = newBlock;
		this.cost = cost;
		//this.newBlockMeta = newBlockMeta;
		this.oldBlock = oldBlock;
	}

	public BlockPos getPos()
	{
		return this.pos;
	}

	/*@Override
	public int getXCoord(){
		return this.xCoord;
	}
	
	@Override
	public int getYCoord(){
		return this.yCoord;
	}
	
	@Override
	public int getZCoord(){
		return this.zCoord;
	}*/

	public IBlockState getNewBlock()
	{
		return this.newBlock;
	}

	/*public IBlockState getNewBlockMeta()
	{
		return this.newBlockMeta;
	}*/

	public int getCost()
	{
		return this.cost;
	}

	public IBlockState getOldBlock()
	{
		return this.oldBlock;
	}

	public void setOldBlock(IBlockState state)
	{
		this.oldBlock = state;
	}

}