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
				if (point.pos.xCoord > currentPoint.pos.xCoord) break;
				if (point.pos.xCoord < currentPoint.pos.xCoord){index = 2; break;}
				if (point.pos.zCoord > currentPoint.pos.zCoord){index = 4; break;}
				if (point.pos.zCoord < currentPoint.pos.zCoord){index = 6; break;}
			}

		}

		for (int count = 0; count < 8; count++) {
			BlockPos pos = new BlockPos(mobX + Coords.offsetAdj2X[index], mobY, mobZ + Coords.offsetAdj2Z[index]);
			IBlockState blockState = this.entity.world.getBlockState(pos);
			boolean isSolidBlock = true;
			for(EnumFacing side : EnumFacing.values()){
				if(!blockState.getBlock().isSideSolid(blockState, this.entity.world, pos, side)){isSolidBlock = false; break;}
			}
			//if (blockState.getBlock().isSideSolid(blockState, this.entity.world, pos, side)/*.isSolidFullCube()*/) {
			if(isSolidBlock){
				// TODO: Fix this, I just typed something random here
				return EnumFacing.getFront(index / 2);
				//return index / 2;
			}
			index++;
			if (index > 7) index = 0;
		}
		return null;
	}
}