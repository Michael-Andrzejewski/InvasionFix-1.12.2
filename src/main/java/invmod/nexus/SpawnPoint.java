package invmod.nexus;

import invmod.util.IPolarAngle;
import net.minecraft.util.math.BlockPos;


public class SpawnPoint
	implements IPolarAngle, Comparable<IPolarAngle>
{
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int spawnAngle;
	private SpawnType spawnType;

	public SpawnPoint(BlockPos pos, int angle, SpawnType type)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), angle, type);
	}

	public SpawnPoint(int x, int y, int z, int angle, SpawnType type)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.spawnAngle = angle;
		this.spawnType = type;
	}

	/*@Override
	public int getXCoord()
	{
	return this.xCoord;
	}
	
	@Override
	public int getYCoord()
	{
	return this.yCoord;
	}
	
	@Override
	public int getZCoord()
	{
	return this.zCoord;
	}*/

	public BlockPos getPos()
	{
		return new BlockPos(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public int getAngle()
	{
		return this.spawnAngle;
	}

	public SpawnType getType()
	{
		return this.spawnType;
	}

	@Override
	public int compareTo(IPolarAngle polarAngle)
	{
		if (this.spawnAngle < polarAngle.getAngle())
		{
			return -1;
		}
		if (this.spawnAngle > polarAngle.getAngle())
		{
			return 1;
		}

		return 0;
	}

	@Override
	public String toString()
	{
		return "Spawn#" + this.spawnType + "#" + this.xCoord + "," + this.yCoord + "," + this.zCoord + "#" + this.spawnAngle;
	}
}