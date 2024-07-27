// `^`^`^`
// ```java
// /**
//  * Interface: ITerrainBuild
//  * 
//  * Purpose:
//  * The ITerrainBuild interface defines a contract for terrain construction tasks within a Minecraft mod. It provides methods to request the building of various structures such as scaffolds, ladder towers, ladders, and bridges. Each method accepts a position and a notification callback to handle task completion.
//  * 
//  * Methods:
//  * - askBuildScaffoldLayer(BlockPos pos, INotifyTask paramINotifyTask):
//  *   Requests the construction of a scaffold layer at the specified position. Returns true if the task is accepted.
//  * 
//  * - askBuildLadderTower(BlockPos pos, int height, int direction, INotifyTask paramINotifyTask):
//  *   Requests the construction of a ladder tower at the specified position, with a specified height and direction. Returns true if the task is accepted.
//  * 
//  * - askBuildLadder(BlockPos pos, INotifyTask paramINotifyTask):
//  *   Requests the construction of a ladder at the specified position. Returns true if the task is accepted.
//  * 
//  * - askBuildBridge(BlockPos pos, INotifyTask paramINotifyTask):
//  *   Requests the construction of a bridge at the specified position. Returns true if the task is accepted.
//  * 
//  * Default Methods:
//  * - askBuildScaffoldLayer(Vec3d vec, INotifyTask paramINotifyTask):
//  *   Overloaded method to request scaffold layer construction using Vec3d for position.
//  * 
//  * - askBuildLadderTower(Vec3d vec, int height, int direction, INotifyTask paramINotifyTask):
//  *   Overloaded method to request ladder tower construction using Vec3d for position.
//  * 
//  * - askBuildLadder(Vec3d vec, INotifyTask paramINotifyTask):
//  *   Overloaded method to request ladder construction using Vec3d for position.
//  * 
//  * - askBuildBridge(Vec3d vec, INotifyTask paramINotifyTask):
//  *   Overloaded method to request bridge construction using Vec3d for position.
//  * 
//  * Usage:
//  * Implement this interface in classes that define the specific behavior for terrain building tasks within the game.
//  */
// ```
// This executive documentation provides a concise summary of the ITerrainBuild interface, outlining its purpose, methods, and usage within the context of a Minecraft mod for terrain construction tasks.
// `^`^`^`

package invmod.entity;

import invmod.INotifyTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface ITerrainBuild {

	public boolean askBuildScaffoldLayer(BlockPos pos, INotifyTask paramINotifyTask);

	public boolean askBuildLadderTower(BlockPos pos, int paramInt1, int paramInt2, INotifyTask paramINotifyTask);

	public boolean askBuildLadder(BlockPos pos, INotifyTask paramINotifyTask);

	public boolean askBuildBridge(BlockPos pos, INotifyTask paramINotifyTask);

	public default boolean askBuildScaffoldLayer(Vec3d vec, INotifyTask paramINotifyTask) {
		return this.askBuildScaffoldLayer(new BlockPos(vec), paramINotifyTask);
	}

	public default boolean askBuildLadderTower(Vec3d vec, int paramInt1, int paramInt2, INotifyTask paramINotifyTask) {
		return this.askBuildLadderTower(new BlockPos(vec), paramInt1, paramInt2, paramINotifyTask);
	}

	public default boolean askBuildLadder(Vec3d vec, INotifyTask paramINotifyTask) {
		return this.askBuildLadder(new BlockPos(vec), paramINotifyTask);
	}

	public default boolean askBuildBridge(Vec3d vec, INotifyTask paramINotifyTask) {
		return this.askBuildBridge(new BlockPos(vec), paramINotifyTask);
	}

}