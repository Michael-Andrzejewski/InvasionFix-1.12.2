// `^`^`^`
// ```java
// /**
//  * Interface for entities that can spawn offspring in a Minecraft mod.
//  *
//  * This interface, ISpawnsOffspring, defines the contract for entities within the mod that have the ability to produce offspring. It is intended to be implemented by any entity class that can generate new entities as a result of some in-game action or event.
//  *
//  * Methods:
//  * - getOffspring(Entity paramEntity): This abstract method is intended to be implemented to return an array of Entity objects that represent the offspring of the entity. The method takes a single Entity parameter, which is the parent entity from which the offspring are derived. The specific implementation of this method should handle the logic for creating and initializing the offspring entities based on the parent entity's properties or state.
//  *
//  * Usage:
//  * Any entity class that is capable of spawning offspring should implement this interface and provide a concrete implementation of the getOffspring method. This allows for a consistent approach to handling entity reproduction within the mod, facilitating easier management and interaction with offspring-spawning entities.
//  */
// package invmod.entity;
// 
// import net.minecraft.entity.Entity;
// 
// public abstract interface ISpawnsOffspring {
//     public abstract Entity[] getOffspring(Entity paramEntity);
// }
// ```
// `^`^`^`

package invmod.entity;

import net.minecraft.entity.Entity;

public abstract interface ISpawnsOffspring {
	public abstract Entity[] getOffspring(Entity paramEntity);
}