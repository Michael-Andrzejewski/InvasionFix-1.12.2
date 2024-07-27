// `^`^`^`
// ```java
// /**
//  * This class represents a terrain manipulation system designed for entities in a Minecraft-like environment.
//  * It allows entities to dig through terrain at a specified rate, removing blocks from the world.
//  *
//  * Public Methods:
//  * - TerrainDigger(ICanDig digger, ITerrainModify modifier, float digRate): Constructor initializing the digger entity, terrain modifier, and dig rate.
//  * - setDigRate(float digRate): Sets the rate at which the digger entity can dig through terrain.
//  * - getDigRate(): Retrieves the current dig rate.
//  * - askClearPosition(BlockPos pos, INotifyTask onFinished, float costMultiplier): Requests to clear a position by removing blocks in a specified order, considering cost and dig rate.
//  * - askRemoveBlock(BlockPos pos, INotifyTask onFinished, float costMultiplier): Requests to remove a single block at a specified position, considering cost and dig rate.
//  * - notifyTask(int result): Callback method that is invoked when a terrain modification task is completed.
//  *
//  * The TerrainDigger uses an ICanDig interface to determine the order of block removal and the cost associated with it.
//  * It also uses an ITerrainModify interface to execute the actual block removal tasks. The dig rate affects the speed of the operation.
//  * The class implements ITerrainDig for digging functionality and INotifyTask for receiving notifications upon task completions.
//  */
// package invmod.entity;
// 
// // ... (imports)
// 
// public class TerrainDigger implements ITerrainDig, INotifyTask {
//     // ... (class implementation)
// }
// ```
// `^`^`^`

package invmod.entity;

import invmod.INotifyTask;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class TerrainDigger implements ITerrainDig, INotifyTask {
	private ICanDig digger;
	private ITerrainModify modifier;
	private float digRate;

	public TerrainDigger(ICanDig digger, ITerrainModify modifier, float digRate) {
		this.digger = digger;
		this.modifier = modifier;
		this.digRate = digRate;
	}

	public void setDigRate(float digRate) {
		this.digRate = digRate;
	}

	public float getDigRate() {
		return this.digRate;
	}

	@Override
	public boolean askClearPosition(BlockPos pos, INotifyTask onFinished, float costMultiplier) {
		BlockPos[] removals = this.digger.getBlockRemovalOrder(pos);
		ModifyBlockEntry[] removalEntries = new ModifyBlockEntry[removals.length];
		int entries = 0;
		for (int i = 0; i < removals.length; i++) {
			Block block = this.digger.getTerrain().getBlockState(removals[i]).getBlock();
			if ((block != Blocks.AIR) && (!block.isPassable(this.digger.getTerrain(), removals[i]))) {
				if (!this.digger.canClearBlock(removals[i])) {
					return false;
				}

				removalEntries[(entries++)] = new ModifyBlockEntry(removals[i], Blocks.AIR,
						(int) (costMultiplier * this.digger.getBlockRemovalCost(removals[i]) / this.digRate));
			}
		}
		ModifyBlockEntry[] finalEntries = new ModifyBlockEntry[entries];
		System.arraycopy(removalEntries, 0, finalEntries, 0, entries);
		return this.modifier.requestTask(finalEntries, onFinished, this);
	}

	@Override
	public boolean askRemoveBlock(BlockPos pos, INotifyTask onFinished, float costMultiplier) {
		if (!this.digger.canClearBlock(pos)) {
			return false;
		}
		ModifyBlockEntry[] removalEntries = { new ModifyBlockEntry(pos, Blocks.AIR,
				(int) (costMultiplier * this.digger.getBlockRemovalCost(pos) / this.digRate)) };
		return this.modifier.requestTask(removalEntries, onFinished, this);
	}

	@Override
	public void notifyTask(int result) {
		if (result == 0) {
			ModifyBlockEntry entry = this.modifier.getLastBlockModified();
			this.digger.onBlockRemoved(entry.getPos(), entry.getOldBlock());
		}
	}

}