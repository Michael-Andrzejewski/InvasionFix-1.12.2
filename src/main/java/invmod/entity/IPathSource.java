// `^`^`^`
// ```java
// /**
//  * Interface: IPathSource
//  * 
//  * Purpose:
//  * This interface defines the contract for pathfinding capabilities within the invmod (Invasion Mod) for Minecraft. It provides methods for creating paths for entities, both living and generic, within the game world. The paths are calculated based on various parameters such as starting and ending positions, entity characteristics, and the world's block access information.
//  * 
//  * Methods:
//  * - createPath(IPathfindable, Vec3d, Vec3d, float, float, IBlockAccess): Generates a path between two Vec3d positions for a pathfindable entity.
//  * - createPath(EntityIMLiving, Entity, float, float, IBlockAccess): Creates a path for a living entity to reach another entity.
//  * - createPath(EntityIMLiving, Vec3d, float, float, IBlockAccess): Generates a path for a living entity to a specific Vec3d position.
//  * - createPath(IPathResult, IPathfindable, BlockPos, BlockPos, float, IBlockAccess): Asynchronously creates a path between two BlockPos positions for a pathfindable entity and stores the result in IPathResult.
//  * - createPath(IPathResult, EntityIMLiving, Entity, float, IBlockAccess): Asynchronously creates a path for a living entity to reach another entity and stores the result in IPathResult.
//  * - createPath(IPathResult, EntityIMLiving, BlockPos, float, IBlockAccess): Asynchronously creates a path for a living entity to a specific BlockPos position and stores the result in IPathResult.
//  * - getSearchDepth(): Retrieves the current search depth for pathfinding.
//  * - getQuickFailDepth(): Retrieves the current quick fail depth for pathfinding.
//  * - setSearchDepth(int): Sets the search depth for pathfinding.
//  * - setQuickFailDepth(int): Sets the quick fail depth for pathfinding.
//  * - canPathfindNice(PathPriority, float, int, int): Determines if pathfinding can be performed nicely based on priority, speed, and depth parameters.
//  * 
//  * Enums:
//  * - PathPriority: Defines the priority levels (LOW, MEDIUM, HIGH) for pathfinding requests.
//  */
// ```
// `^`^`^`

package invmod.entity;

import invmod.IPathfindable;
import invmod.entity.ai.navigator.Path;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public interface IPathSource {

	public Path createPath(IPathfindable paramIPathfindable, Vec3d pos0In, Vec3d pos1In, float paramFloat1,
			float paramFloat2, IBlockAccess paramIBlockAccess);

	public Path createPath(EntityIMLiving paramEntityIMLiving, Entity paramEntity, float paramFloat1, float paramFloat2,
			IBlockAccess paramIBlockAccess);

	public Path createPath(EntityIMLiving paramEntityIMLiving, Vec3d vec, float paramFloat1, float paramFloat2,
			IBlockAccess paramIBlockAccess);

	public void createPath(IPathResult paramIPathResult, IPathfindable paramIPathfindable, BlockPos pos0, BlockPos pos1,
			float paramFloat, IBlockAccess paramIBlockAccess);

	public void createPath(IPathResult paramIPathResult, EntityIMLiving paramEntityIMLiving, Entity paramEntity,
			float paramFloat, IBlockAccess paramIBlockAccess);

	public void createPath(IPathResult paramIPathResult, EntityIMLiving paramEntityIMLiving, BlockPos pos,
			float paramFloat, IBlockAccess paramIBlockAccess);

	public int getSearchDepth();

	public int getQuickFailDepth();

	public void setSearchDepth(int paramInt);

	public void setQuickFailDepth(int paramInt);

	public boolean canPathfindNice(PathPriority paramPathPriority, float paramFloat, int paramInt1, int paramInt2);

	public static enum PathPriority {
		LOW, MEDIUM, HIGH;
	}

}