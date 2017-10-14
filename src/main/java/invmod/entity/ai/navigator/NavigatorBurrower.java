package invmod.entity.ai.navigator;

import invmod.entity.IPathSource;
import invmod.entity.monster.EntityIMBurrower;
import invmod.util.PosRotate3D;

public class NavigatorBurrower extends NavigatorParametric
{
	protected PathNode nextNode;
	protected PathNode prevNode;
	protected PathNode[] prevSegmentNodes;
	protected PathNode[] activeSegmentNodes;
	protected PathNode[] nextSegmentNodes;
	protected int[] segmentPathIndices;
	protected int[] segmentTime;
	protected int[] segmentOffsets;
	protected float timePerTick;
	protected Path lastPath;
	protected boolean nodeChanged;

	public NavigatorBurrower(EntityIMBurrower entity, IPathSource pathSource, int segments, int offset)
	{
		super(entity, pathSource);
		this.timePerTick = 0.05F;
		this.prevSegmentNodes = new PathNode[segments];
		this.activeSegmentNodes = new PathNode[segments];
		this.nextSegmentNodes = new PathNode[segments];
		this.segmentPathIndices = new int[segments];
		this.segmentTime = new int[segments];
		this.segmentOffsets = new int[segments];
		this.nodeChanged = false;

		for (int i = 0; i < this.segmentOffsets.length; i++)
			this.segmentOffsets[i] = ((i + 1) * offset);
	}

	@Override
protected PosRotate3D entityPositionAtParam(int param)
	{
		return calcAbsolutePositionAndRotation(param * this.timePerTick, this.prevNode, this.activeNode, this.nextNode);
	}

	protected PosRotate3D positionAtTime(int tick, PathNode start, PathNode middle, PathNode end)
	{
		PosRotate3D pos = calcPositionAndRotation(tick * this.timePerTick, start, middle, end);
		pos.setPosX(pos.getPosX() + middle.pos.xCoord);
		pos.setPosY(pos.getPosY() + middle.pos.yCoord);
		pos.setPosZ(pos.getPosZ() + middle.pos.zCoord);
		return pos;
	}

	@Override
protected boolean isReadyForNextNode(int ticks)
	{
		return ticks * this.timePerTick >= 1.0D;
	}

	@Override
protected void pathFollow(int time)
	{
		int nextFrontIndex = this.path.getCurrentPathIndex() + 2;
		if (isReadyForNextNode(time))
		{
			if (nextFrontIndex < this.path.getCurrentPathLength())
			{
				this.timeParam = 0;
				this.path.setCurrentPathIndex(nextFrontIndex - 1);
				this.prevNode = this.activeNode;
				this.activeNode = this.nextNode;
				this.nextNode = this.path.getPathPointFromIndex(nextFrontIndex);
				this.nodeChanged = true;
			}
		}
		else
		{
			this.timeParam = time;
		}
	}

	protected void doSegmentFollowTo(int ticks, int segmentIndex)
	{
		ticks += this.segmentOffsets[segmentIndex];
		while (ticks <= 0) ticks += 20;

		int nextFrontIndex = this.segmentPathIndices[segmentIndex] + 2;
		if (isReadyForNextNode(ticks))
		{
			if (nextFrontIndex < this.path.getCurrentPathLength())
			{
				this.segmentPathIndices[segmentIndex] = (nextFrontIndex - 1);
				this.prevSegmentNodes[segmentIndex] = this.activeSegmentNodes[segmentIndex];
				this.activeSegmentNodes[segmentIndex] = this.nextSegmentNodes[segmentIndex];
				if (this.segmentPathIndices[segmentIndex] >= 0)
					this.nextSegmentNodes[segmentIndex] = this.path.getPathPointFromIndex(nextFrontIndex);
				else {
					this.nextSegmentNodes[segmentIndex] = this.path.getPathPointFromIndex(0);
				}
				this.segmentTime[segmentIndex] = 0;
			}
		}
		else
		{
			this.segmentTime[segmentIndex] = ticks;
		}

		if (this.segmentPathIndices[segmentIndex] >= 0)
		{
			PosRotate3D pos = positionAtTime(this.segmentTime[segmentIndex], this.prevSegmentNodes[segmentIndex], this.activeSegmentNodes[segmentIndex], this.nextSegmentNodes[segmentIndex]);
			((EntityIMBurrower)this.theEntity).setSegment(segmentIndex, pos);
			if (this.segmentTime[segmentIndex] == 0)
				((EntityIMBurrower)this.theEntity).setSegment(segmentIndex, pos);
		}
	}

	@Override
protected void doMovementTo(int time)
	{
		PosRotate3D movePos = entityPositionAtParam(time);
		this.theEntity.moveEntity(movePos.getPosX() - this.theEntity.posX, movePos.getPosY() - this.theEntity.posY, movePos.getPosZ() - this.theEntity.posZ);
		((EntityIMBurrower)this.theEntity).setHeadRotation(movePos);

		if (this.nodeChanged)
		{
			((EntityIMBurrower)this.theEntity).setHeadRotation(movePos);
			this.nodeChanged = false;
		}

		if (Math.abs(this.theEntity.getDistanceSq(movePos.getPosX(), movePos.getPosY(), movePos.getPosZ())) < this.minMoveToleranceSq)
		{
			for (int segmentIndex = 0; segmentIndex < this.segmentPathIndices.length; segmentIndex++) {
				doSegmentFollowTo(time, segmentIndex);
			}
			this.timeParam = time;
			this.ticksStuck -= 1;
		}
		else
		{
			this.ticksStuck += 1;
		}
	}

	@Override
public boolean noPath()
	{
		return (this.path == null) || (this.path.getCurrentPathIndex() >= this.path.getCurrentPathLength() - 2);
	}

	@Override
public boolean setPath(Path newPath, float speed)
	{
		if ((newPath == null) || (newPath.getCurrentPathLength() < 2))
		{
			this.path = null;
			return false;
		}

		if (this.path == null)
		{
			this.path = newPath;
			this.activeNode = this.path.getPathPointFromIndex(0);
			this.prevNode = this.activeNode;
			this.nextNode = this.path.getPathPointFromIndex(1);
			if (this.activeNode.action != PathAction.NONE) {
				this.nodeActionFinished = false;
			}
			for (int i = 0; i < this.segmentPathIndices.length; i++)
			{
				if (this.activeSegmentNodes[i] == null)
				{
					this.activeSegmentNodes[i] = this.activeNode;
					this.nextSegmentNodes[i] = this.activeNode;
					this.segmentPathIndices[i] = 0;
					this.segmentTime[i] = this.segmentOffsets[i];
					while (this.segmentTime[i] < 0)
					{
						this.segmentTime[i] += 20;
						this.segmentPathIndices[i] -= 1;
					}
				}
			}
		}

		int mainIndex = this.path.getCurrentPathIndex();
		if (newPath.getPathPointFromIndex(0).equals(this.activeNode))
		{
			if (this.segmentPathIndices.length > 0)
			{
				int lowestIndex = this.segmentPathIndices[(this.segmentPathIndices.length - 1)];
				if (lowestIndex < 0)
					lowestIndex = 0;
				this.path = extendPath(this.path, newPath, lowestIndex, mainIndex);
				mainIndex -= lowestIndex;
				this.path.setCurrentPathIndex(mainIndex);
				this.nextNode = this.path.getPathPointFromIndex(mainIndex + 1);
				for (int i = 0; i < this.segmentPathIndices.length; i++)
				{
					this.segmentPathIndices[i] -= lowestIndex;
					if (this.segmentPathIndices[i] == mainIndex)
						this.nextSegmentNodes[i] = this.nextNode;
				}
			}
			else
			{
				this.path = newPath;
				this.path.setCurrentPathIndex(0);
				this.nextNode = this.path.getPathPointFromIndex(1);
			}
		}
		else
		{
			this.path = newPath;
			this.activeNode = this.path.getPathPointFromIndex(0);
			this.prevNode = this.activeNode;
			this.nextNode = this.path.getPathPointFromIndex(1);
			if (this.activeNode.action != PathAction.NONE) {
				this.nodeActionFinished = false;
			}
			for (int i = 0; i < this.segmentPathIndices.length; i++)
			{
				if (this.activeSegmentNodes[i] == null)
				{
					this.activeSegmentNodes[i] = this.activeNode;
					this.nextSegmentNodes[i] = this.activeNode;
					this.segmentPathIndices[i] = 0;
					this.segmentTime[i] = this.segmentOffsets[i];
					while (this.segmentTime[i] < 0)
					{
						this.segmentTime[i] += 20;
						this.segmentPathIndices[i] -= 1;
					}
				}
			}
		}

		this.ticksStuck = 0;

		if (this.noSunPathfind)
		{
			removeSunnyPath();
		}

		return true;
	}

	private PosRotate3D calcAbsolutePositionAndRotation(float time, PathNode start, PathNode middle, PathNode end)
	{
		PosRotate3D pos = calcPositionAndRotation(time, start, middle, end);
		pos.setPosX(pos.getPosX() + middle.pos.xCoord);
		pos.setPosY(pos.getPosY() + middle.pos.yCoord);
		pos.setPosZ(pos.getPosZ() + middle.pos.zCoord);
		return pos;
	}

	private PosRotate3D calcPositionAndRotation(float time, PathNode start, PathNode middle, PathNode end)
	{
		//DarthXenon: Changed to double
		double vX = end.pos.xCoord - start.pos.xCoord;
		double vY = end.pos.yCoord - start.pos.yCoord;
		double vZ = end.pos.zCoord - start.pos.zCoord;
		int hX = middle.pos.xCoord != start.pos.xCoord ? 1 : -1;
		int hY = middle.pos.yCoord != start.pos.yCoord ? 1 : -1;
		int hZ = middle.pos.zCoord != start.pos.zCoord ? 1 : -1;
		int gX = middle.pos.xCoord != end.pos.xCoord ? 1 : -1;
		int gY = middle.pos.yCoord != end.pos.yCoord ? 1 : -1;
		int gZ = middle.pos.zCoord != end.pos.zCoord ? 1 : -1;
		double xOffset = vX * -0.5D * hX;
		double yOffset = vY * -0.5D * hY;
		double zOffset = vZ * -0.5D * hZ;

		double posX = 0.0D; double posY = 0.0D; double posZ = 0.0D;
		float rotX = 0.0F; float rotY = 0.0F; float rotZ = 0.0F;

		if ((hX == 1) && (gX == 1)){
			posX = time * vX * 0.5D + (vX > 0 ? 0 : 1);
			posY = 0.5D;
			posZ = 0.5D;
			rotY = (float) (vX >= 1d ? 0d : Math.PI);
			return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
		}
		if ((hY == 1) && (gY == 1)){
			posY = time * vY * 0.5D + (vY > 0 ? 0 : 1);
			posX = 0.5D;
			posZ = 0.5D;
			return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
		}
		if ((hZ == 1) && (gZ == 1)){
			posZ = time * vZ * 0.5D + (vZ > 0 ? 0 : 1);
			posY = 0.5D;
			posX = 0.5D;
			rotY = (float) (vZ * Math.PI / 4d);
			return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
		}

		if (hX == 1){
			posX = vX * hX * Math.sin(time * 0.5D * 3.141592653589793D) * 0.5D + xOffset;
		} else {
			posX = vX * hX * Math.cos(time * 0.5D * 3.141592653589793D) * 0.5D + xOffset;
		}
		if (hY == 1){
			posY = vY * hY * Math.sin(time * 0.5D * 3.141592653589793D) * 0.5D + yOffset;
		} else {
			posY = vY * hY * Math.cos(time * 0.5D * 3.141592653589793D) * 0.5D + yOffset;
		}
		if (hZ == 1){
			posZ = vZ * hZ * Math.sin(time * 0.5D * 3.141592653589793D) * 0.5D + zOffset;
		} else {
			posZ = vZ * hZ * Math.cos(time * 0.5D * 3.141592653589793D) * 0.5D + zOffset;
		}
		if (hX == 1){
			rotY = vX == 1 ? 0.0F : 180.0F;
			if (gZ == 1)
				rotY += time * vZ * vX * 90.0F;
			else if (gY == 1)
				rotZ = (float) (time * vY * 90d);
		} else if (hY == 1){
			if (gX == 1){
				rotX = vX == 1 ? 0.0F : 180.0F;
				rotZ = (float) (90 * vY + time * vX * -90d);
			} else if (gZ == 1){
				rotX = 90.0F;
				rotY = (float) (vZ * (time * vY * -90d));
				rotZ = -90.0F;
			}
		} else if (hZ == 1){
			if (gX == 1){
				rotY = (float) (vZ * (90d + time * vX * -90d));
			} else if (gY == 1){
				rotX = 90.0F;
				rotY = (float) (-vZ * (-90d + time * vY * -90d));
				rotZ = -90.0F;
			}
		}

		posX += 0.5D;
		posY += 0.5D;
		posZ += 0.5D;

		rotX /= 57.295799F;
		rotY /= 57.295799F;
		rotZ /= 57.295799F;
		return new PosRotate3D(posX, posY, posZ, rotX, rotY, rotZ);
	}

	private PosRotate3D calcStraight(float time, PathNode start, PathNode end){
		PosRotate3D segment = new PosRotate3D();
		segment.setPosX(start.pos.xCoord + 0.5D +	time * (end.pos.xCoord - start.pos.xCoord) * 0.5D);
		segment.setPosY(start.pos.yCoord +			time * (end.pos.yCoord - start.pos.yCoord) * 0.5D);
		segment.setPosZ(start.pos.zCoord + 0.5D +	time * (end.pos.zCoord - start.pos.zCoord * 0.5D));
		return segment;
	}

	private Path extendPath(Path path1, Path path2, int lowerBoundP1, int upperBoundP1){
		int k = upperBoundP1 - lowerBoundP1;
		PathNode[] newPoints = new PathNode[k + path2.getCurrentPathLength()];
		System.arraycopy(path1.points, lowerBoundP1, newPoints, 0, k);
		System.arraycopy(path2.points, 0, newPoints, k, path2.getCurrentPathLength());
		return new Path(newPoints, path2.getIntendedTarget());
	}
}