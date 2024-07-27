// `^`^`^`
// ```java
// /**
//  * Interface: ITerrainDig
//  * 
//  * Purpose:
//  * The ITerrainDig interface defines a contract for entities in the 'invmod' mod that are capable of modifying the terrain by removing blocks or clearing positions. It is used within the context of Minecraft, a game where terrain manipulation is a core mechanic. This interface ensures that entities that can dig or clear terrain do so in a controlled and notified manner.
//  * 
//  * Methods:
//  * - askRemoveBlock(BlockPos pos, INotifyTask onFinished, float costMultiplier):
//  *   Requests the removal of a block at the specified BlockPos. It notifies the task handler upon completion and applies a cost multiplier to the action.
//  * 
//  * - askClearPosition(BlockPos pos, INotifyTask onFinished, float costMultiplier):
//  *   Requests clearing the terrain at the specified BlockPos. Similar to block removal, it notifies upon completion and applies a cost multiplier.
//  * 
//  * - askRemoveBlock(Vec3d vec, INotifyTask onFinished, float costMultiplier) [default]:
//  *   An overloaded version of askRemoveBlock that takes a Vec3d instead of a BlockPos, converting it internally to BlockPos.
//  * 
//  * - askClearPosition(Vec3d vec, INotifyTask onFinished, float costMultiplier) [default]:
//  *   An overloaded version of askClearPosition that takes a Vec3d instead of a BlockPos, converting it internally to BlockPos.
//  * 
//  * Usage:
//  * Entities implementing this interface can perform terrain alteration tasks and are expected to handle the completion notification and cost application as defined by the methods.
//  */
// package invmod.entity;
// 
// // ... (imports and interface definition)
// ```
// This summary provides an overview of the ITerrainDig interface, describing its purpose within the game mod, the functionality of each method, and the expected usage by implementing entities.
// `^`^`^`

package invmod.entity;

import invmod.INotifyTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface ITerrainDig {

	public boolean askRemoveBlock(BlockPos pos, INotifyTask onFinished, float costMultiplier);

	public boolean askClearPosition(BlockPos pos, INotifyTask onFinished, float costMultiplier);

	public default boolean askRemoveBlock(Vec3d vec, INotifyTask onFinished, float costMultiplier) {
		return this.askRemoveBlock(new BlockPos(vec), onFinished, costMultiplier);
	}

	public default boolean askClearPosition(Vec3d vec, INotifyTask onFinished, float costMultiplier) {
		return this.askClearPosition(new BlockPos(vec), onFinished, costMultiplier);
	}

}