// `^`^`^`
// ```java
// /**
//  * Interface: IPathfindable
//  * 
//  * Purpose:
//  * The IPathfindable interface defines the structure for objects that can perform pathfinding operations within the game Minecraft. It is part of the 'invmod' package, which likely stands for "Invasion Mod," suggesting that this interface is used for customizing the behavior of entities during invasions or similar events.
//  * 
//  * Methods:
//  * - getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap):
//  *   This method calculates the cost of moving from one block to another during pathfinding. It takes into account the previous node (prevNode), the current node (node), and the terrain map (terrainMap) to determine the cost. The cost influences the pathfinding algorithm's decision-making process, favoring paths with lower costs.
//  * 
//  * - getPathOptionsFromNode(IBlockAccess paramIBlockAccess, PathNode paramPathNode, PathfinderIM paramPathfinderIM):
//  *   This method is responsible for evaluating the available path options from a given node within the game world. It uses the terrain information provided by IBlockAccess, the current node's position (paramPathNode), and the pathfinder instance (paramPathfinderIM) to determine possible moves. This is essential for the pathfinder to explore different routes and find an optimal path to the target.
//  * 
//  * Usage:
//  * Implementing this interface allows for the creation of entities that can navigate the Minecraft world by calculating paths dynamically, taking into account the terrain and other obstacles. It is a crucial component for AI behavior that requires movement across the game map.
//  */
// package invmod;
// 
// import invmod.entity.ai.navigator.PathNode;
// import invmod.entity.ai.navigator.PathfinderIM;
// import net.minecraft.world.IBlockAccess;
// 
// public abstract interface IPathfindable {
//     public abstract float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap);
//     public abstract void getPathOptionsFromNode(IBlockAccess paramIBlockAccess, PathNode paramPathNode, PathfinderIM paramPathfinderIM);
// }
// ```
// `^`^`^`

package invmod;

import invmod.entity.ai.navigator.PathNode;
import invmod.entity.ai.navigator.PathfinderIM;
import net.minecraft.world.IBlockAccess;

public abstract interface IPathfindable {
	public abstract float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap);

	public abstract void getPathOptionsFromNode(IBlockAccess paramIBlockAccess, PathNode paramPathNode,
			PathfinderIM paramPathfinderIM);
}