// `^`^`^`
// ```java
// /**
//  * This class, NavigatorEngy, extends the NavigatorIM class and is designed to provide navigation capabilities
//  * specifically for the EntityIMPigEngy entity within the invmod mod. It handles pathfinding and instructs the
//  * entity on how to interact with the environment, particularly in building structures necessary for navigation.
//  *
//  * Constructor:
//  * - NavigatorEngy(EntityIMPigEngy entity, IPathSource pathSource): Initializes the navigator with the pig entity
//  *   and a path source for pathfinding.
//  *
//  * Methods:
//  * - createPath(EntityIMLiving entity, Vec3d vec, float targetRadius): Attempts to create a path to a target
//  *   location, considering the terrain and any structures like the Nexus that may influence pathfinding.
//  *
//  * - handlePathAction(): Determines the action to take based on the current path node. This includes building
//  *   ladders, bridges, and scaffolds as needed to navigate the terrain. It uses the pig entity's TerrainBuildEngy
//  *   capabilities to execute these tasks and updates the navigation status accordingly.
//  *
//  * The class leverages the IBlockAccessExtended interface to interact with the game world's blocks and the
//  * TileEntityNexus to potentially influence pathfinding. It also uses the Distance utility class to calculate
//  * distances for pathfinding purposes. The class is part of the invmod.entity.ai.navigator package, which
//  * suggests it is part of a larger AI system for entities in the mod.
//  */
// package invmod.entity.ai.navigator;
// 
// // ... (rest of the imports and class code)
// ```
// This summary provides an overview of the NavigatorEngy class, its constructor, and its methods, explaining the purpose and functionality within the context of the invmod mod's AI system for the EntityIMPigEngy.
// `^`^`^`

package invmod.entity.ai.navigator;

import invmod.IBlockAccessExtended;
import invmod.entity.EntityIMLiving;
import invmod.entity.IPathSource;
import invmod.entity.monster.EntityIMPigEngy;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Distance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class NavigatorEngy extends NavigatorIM {

	private final EntityIMPigEngy pigEntity;

	public NavigatorEngy(EntityIMPigEngy entity, IPathSource pathSource) {
		super(entity, pathSource);
		this.pigEntity = entity;
		this.setNoMaintainPos();
	}

	@Override
	protected Path createPath(EntityIMLiving entity, Vec3d vec, float targetRadius) {
		IBlockAccess terrainCache = this.getChunkCache(entity.getPosition(), new BlockPos(vec), 16.0F);
		TileEntityNexus nexus = this.pigEntity.getNexus();
		if (nexus != null) {
			IBlockAccessExtended terrainCacheExt = nexus.getAttackerAI().wrapEntityData(terrainCache);

			nexus.getAttackerAI().addScaffoldDataTo(terrainCacheExt);
			terrainCache = terrainCacheExt;
		}
		float maxSearchRange = 12.0F + (float) Distance.distanceBetween(entity.getPosition(), vec);
		if (this.pathSource.canPathfindNice(IPathSource.PathPriority.HIGH, maxSearchRange,
				this.pathSource.getSearchDepth(), this.pathSource.getQuickFailDepth())) {
			return this.pathSource.createPath(entity, vec, targetRadius, maxSearchRange, terrainCache);
		}
		return null;
	}

	@Override
	protected boolean handlePathAction() {
		if (!this.actionCleared) {
			this.resetStatus();
			if (this.getLastActionResult() != 0)
				return false;
			return true;
		}

		if ((this.activeNode.action == PathAction.LADDER_UP_PX) || (this.activeNode.action == PathAction.LADDER_UP_NX)
				|| (this.activeNode.action == PathAction.LADDER_UP_PZ)
				|| (this.activeNode.action == PathAction.LADDER_UP_NZ)) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildLadder(this.activeNode.pos, this))
				return this.setDoingTaskAndHold();
		} else if (this.activeNode.action == PathAction.BRIDGE) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildBridge(this.activeNode.pos, this))
				return this.setDoingTaskAndHold();
		} else if (this.activeNode.action == PathAction.SCAFFOLD_UP) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildScaffoldLayer(this.activeNode.pos, this))
				return this.setDoingTaskAndHoldOnPoint();
		} else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_PX) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode.pos, 0, 1, this))
				return this.setDoingTaskAndHold();
		} else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_NX) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode.pos, 1, 1, this))
				return this.setDoingTaskAndHold();
		} else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_PZ) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode.pos, 2, 1, this))
				return this.setDoingTaskAndHold();
		} else if (this.activeNode.action == PathAction.LADDER_TOWER_UP_NZ) {
			if (this.pigEntity.getTerrainBuildEngy().askBuildLadderTower(this.activeNode.pos, 3, 1, this)) {
				return this.setDoingTaskAndHold();
			}
		}
		this.nodeActionFinished = true;
		return true;
	}

}