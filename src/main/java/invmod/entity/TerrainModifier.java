package invmod.entity;

import invmod.BlocksAndItems;
import invmod.INotifyTask;
import invmod.util.Distance;
import invmod.util.config.Config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class TerrainModifier implements ITerrainModify {
	
	private static final float DEFAULT_REACH = 2.0F;
	private EntityIMLiving theEntity;
	private INotifyTask taskSetter;
	private INotifyTask blockNotify;
	private List<ModifyBlockEntry> modList;
	private ModifyBlockEntry nextEntry;
	private ModifyBlockEntry lastEntry;
	private int entryIndex;
	private int timer;
	private float reach;
	private boolean outOfRangeFlag;
	private boolean terrainFailFlag;

	public TerrainModifier(EntityIMLiving entity, float defaultReach) {
		this.theEntity = entity;
		this.modList = new ArrayList();
		this.entryIndex = 0;
		this.timer = 0;
		this.reach = defaultReach;
	}

	public void onUpdate() {
		this.taskUpdate();
	}

	@Override
	public boolean isReadyForTask(INotifyTask asker) {
		return (this.modList.size() == 0) || (this.taskSetter == asker);
	}

	public void cancelTask() {
		this.endTask();
	}

	public boolean isBusy() {
		return this.timer > 0;
	}

	@Override
	public boolean requestTask(ModifyBlockEntry[] entries, INotifyTask onFinished, INotifyTask onBlockChange) {
		if (this.isReadyForTask(onFinished)) {
			for (ModifyBlockEntry entry : entries) {
				this.modList.add(entry);
			}
			this.taskSetter = onFinished;
			this.blockNotify = onBlockChange;
			return true;
		}
		return false;
	}

	@Override
	public ModifyBlockEntry getLastBlockModified() {
		return this.lastEntry;
	}

	private void taskUpdate() {
		if (this.timer-- > 1) return;
		if (this.timer <= 1 && this.nextEntry != null) {
			this.entryIndex += 1;
			this.timer = 0;
			int result = this.changeBlock(this.nextEntry) ? 0 : 1;
			this.lastEntry = this.nextEntry;
			if (this.blockNotify != null) this.blockNotify.notifyTask(result);
		}

		if (this.modList != null && this.entryIndex < this.modList.size()) {
			this.nextEntry = (this.modList.get(this.entryIndex));
			while (this.isTerrainIdentical(this.nextEntry)) {
				this.entryIndex += 1;
				if (this.entryIndex < this.modList.size()) {
					this.nextEntry = (this.modList.get(this.entryIndex));
				} else {
					this.endTask();
					return;
				}
			}

			this.timer = this.nextEntry.getCost();
			if (this.timer <= 0) this.timer = 1;
		} else if (this.modList.size() > 0) {
			this.endTask();
		}
	}

	private void endTask() {
		this.entryIndex = 0;
		this.timer = 0;
		this.modList.clear();
		if (this.taskSetter != null)
			this.taskSetter.notifyTask(this.outOfRangeFlag ? 1 : this.terrainFailFlag ? 2 : 0);
	}

	private boolean changeBlock(ModifyBlockEntry entry) {
		double dist = Distance.distanceBetween(
				this.theEntity.posX, this.theEntity.posY + this.theEntity.height / 2.0F, this.theEntity.posZ,
				entry.getPos().getX() + 0.5D, entry.getPos().getY() + 0.5D, entry.getPos().getZ() + 0.5D);
		if (dist > this.reach) {
			this.outOfRangeFlag = true;
			return false;
		}

		IBlockState newState = entry.getNewBlock();
		IBlockState oldState = this.theEntity.worldObj.getBlockState(entry.getPos());
		entry.setOldBlock(oldState);
		if (oldState.getBlock() == BlocksAndItems.blockNexus || oldState.getBlock() == newState.getBlock()) {
			this.terrainFailFlag = true;
			return false;
		}

		boolean succeeded = this.theEntity.worldObj.setBlockState(entry.getPos(), newState);
		if (succeeded) {
			if (oldState.getBlock() != Blocks.AIR) {
				oldState.getBlock().onBlockDestroyedByPlayer(this.theEntity.worldObj, entry.getPos(), oldState);

				if (Config.DROP_DESTRUCTED_BLOCKS) {
					oldState.getBlock().dropBlockAsItem(this.theEntity.worldObj, entry.getPos(), oldState, 0);
				}
			}
			/*if (newState.getBlock() == Blocks.LADDER) {
				this.theEntity.worldObj.setBlockState(entry.getPos(), newState);
//TODO: Figure out what this did
//				Blocks.ladder.onPostBlockPlaced(this.theEntity.worldObj,
//						new BlockPos(entry.getXCoord(), entry.getYCoord(),
//						entry.getZCoord()), meta);
			}*/
		} else {
			this.terrainFailFlag = true;
		}
		return succeeded;
	}

	private boolean isTerrainIdentical(ModifyBlockEntry entry) {
		if ((this.theEntity.worldObj.getBlockState(entry.getPos()).getBlock() == entry.getNewBlock().getBlock())
				&& (this.theEntity.worldObj.getBlockState(entry.getPos()) == entry.getNewBlock())) {
			return true;
		}
		return false;
	}
	
}