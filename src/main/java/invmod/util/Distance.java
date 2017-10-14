package invmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class Distance
{
	public static double distanceBetween(BlockPos pos1, BlockPos pos2)
	{
		double dX = pos2.getX() - pos1.getX();
		double dY = pos2.getY() - pos1.getY();
		double dZ = pos2.getZ() - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(BlockPos pos1, Vec3d pos2)
	{
		double dX = pos2.xCoord - pos1.getX();
		double dY = pos2.yCoord - pos1.getY();
		double dZ = pos2.zCoord - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(BlockPos pos1, double x2, double y2, double z2)
	{
		double dX = x2 - pos1.getX();
		double dY = y2 - pos1.getY();
		double dZ = z2 - pos1.getZ();
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		double dX = x2 - x1;
		double dY = y2 - y1;
		double dZ = z2 - z1;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(Entity entity, Vec3d pos2)
	{
		double dX = pos2.xCoord - entity.posX;
		double dY = pos2.yCoord - entity.posY;
		double dZ = pos2.zCoord - entity.posZ;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public static double distanceBetween(Vec3d vec0, Vec3d vec1)
	{
		double dX = vec1.xCoord - vec0.xCoord;
		double dY = vec1.yCoord - vec0.yCoord;
		double dZ = vec1.zCoord - vec0.zCoord;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}
}