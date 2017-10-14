package invmod;

import invmod.entity.ai.navigator.PathNode;
import invmod.entity.ai.navigator.PathfinderIM;
import net.minecraft.world.IBlockAccess;

public abstract interface IPathfindable
{
  public abstract float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap);

  public abstract void getPathOptionsFromNode(IBlockAccess paramIBlockAccess, PathNode paramPathNode, PathfinderIM paramPathfinderIM);
}