// `^`^`^`
// ```java
// /**
//  * Interface: ICanBuild
//  * Purpose: Defines the capabilities of entities in the Invasion Mod (IM) to interact with the terrain by building structures, specifically placing ladders.
//  * Usage: Entities that are capable of modifying the terrain by building should implement this interface.
//  * 
//  * Methods:
//  * - getEntity(): Returns the instance of the entity implementing this interface, ensuring it extends EntityIMLiving. If the instance does not extend EntityIMLiving, it returns null.
//  * - canPlaceLadderAt(BlockPos pos): Determines whether the entity can place a ladder at the specified position in the world. This method must be implemented by the class that extends this interface to provide specific logic for ladder placement.
//  * 
//  * Note: This interface is part of the Invasion Mod (IM) version 1.2.6 and is designed to be implemented by entities that extend the EntityIMLiving class from the Minecraft modding framework.
//  * 
//  * Author: DarthXenon
//  * Since: IM 1.2.6
//  */
// package invmod.entity;
// 
// import net.minecraft.entity.Entity;
// import net.minecraft.util.math.BlockPos;
// 
// public interface ICanBuild<T extends EntityIMLiving> {
//     public default T getEntity() {
//         return (this instanceof EntityIMLiving) ? (T) this : null;
//     }
// 
//     public boolean canPlaceLadderAt(BlockPos pos);
// }
// ```
// `^`^`^`

package invmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

/**
 * In order for an entity to use {@link TerrainBuilder}, implement this
 * interface.
 * 
 * @author DarthXenon
 * @param <T> Must extend {@link EntityIMLiving}.
 * @since IM 1.2.6
 */
public interface ICanBuild<T extends EntityIMLiving> {

	/**
	 * Returns the entity, which must extend {@link Entity}.
	 */
	public default T getEntity() {
		return (this instanceof EntityIMLiving) ? (T) this : null;
	}

	public boolean canPlaceLadderAt(BlockPos pos);

}
