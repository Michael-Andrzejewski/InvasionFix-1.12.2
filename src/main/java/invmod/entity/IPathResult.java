// `^`^`^`
// ```java
// /**
//  * This interface defines a contract for handling the results of pathfinding operations within the 'invmod' package.
//  * Implementing classes are expected to provide concrete behavior for what should occur when a pathfinding operation
//  * is completed and a path is successfully found.
//  *
//  * Methods:
//  * - pathCompleted(Path paramPath): This method is called when a path has been successfully computed. The implementing
//  *   class should define the actions to be taken with the completed path, which is passed as a parameter. The Path
//  *   object contains the route information that the pathfinding algorithm has generated.
//  *
//  * Usage:
//  * - This interface should be implemented by any class that needs to be notified when a pathfinding operation finishes.
//  * - The pathCompleted method provides a way to handle the path result, which could involve navigating an entity along
//  *   the path or performing some other action based on the path's information.
//  *
//  * Note:
//  * - The interface is part of the 'invmod.entity' package, which suggests it is used within the context of entities,
//  *   potentially for mods involving entities that require custom pathfinding behavior.
//  */
// package invmod.entity;
// 
// import invmod.entity.ai.navigator.Path;
// 
// public abstract interface IPathResult {
//     public abstract void pathCompleted(Path paramPath);
// }
// ```
// `^`^`^`

package invmod.entity;

import invmod.entity.ai.navigator.Path;

public abstract interface IPathResult {
	public abstract void pathCompleted(Path paramPath);
}