// `^`^`^`
// ```java
// /**
//  * Interface: ITerrainModify
//  * 
//  * Purpose:
//  * The ITerrainModify interface defines a contract for entities that modify terrain within the game. It ensures that implementing classes can check for task readiness, request tasks for modifying blocks, and retrieve the last block that was modified.
//  * 
//  * Methods:
//  * - boolean isReadyForTask(INotifyTask paramINotifyTask):
//  *   Checks if the entity is ready to undertake a new terrain modification task, based on the provided INotifyTask instance.
//  * 
//  * - boolean requestTask(ModifyBlockEntry[] paramArrayOfModifyBlockEntry, INotifyTask paramINotifyTask1, INotifyTask paramINotifyTask2):
//  *   Requests a new terrain modification task, providing an array of ModifyBlockEntry objects that represent the blocks to be modified, and two INotifyTask instances for callback purposes upon task initiation and completion.
//  * 
//  * - ModifyBlockEntry getLastBlockModified():
//  *   Retrieves the last block entry that was modified by the entity, encapsulated in a ModifyBlockEntry object.
//  * 
//  * Usage:
//  * This interface is used within the invmod package and is intended to be implemented by entities that are capable of altering the game's terrain. It provides a structured way to manage terrain modification tasks and callbacks.
//  */
// package invmod.entity;
// 
// import invmod.INotifyTask;
// 
// public interface ITerrainModify {
//     public boolean isReadyForTask(INotifyTask paramINotifyTask);
//     public boolean requestTask(ModifyBlockEntry[] paramArrayOfModifyBlockEntry, INotifyTask paramINotifyTask1, INotifyTask paramINotifyTask2);
//     public ModifyBlockEntry getLastBlockModified();
// }
// ```
// `^`^`^`

package invmod.entity;

import invmod.INotifyTask;

public interface ITerrainModify {
	public boolean isReadyForTask(INotifyTask paramINotifyTask);

	public boolean requestTask(ModifyBlockEntry[] paramArrayOfModifyBlockEntry, INotifyTask paramINotifyTask1,
			INotifyTask paramINotifyTask2);

	public ModifyBlockEntry getLastBlockModified();
}