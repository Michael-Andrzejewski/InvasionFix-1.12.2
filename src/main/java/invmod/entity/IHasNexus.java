// `^`^`^`
// ```java
// /**
//  * Interface: IHasNexus
//  * 
//  * Purpose:
//  * This interface is designed to be implemented by entities that can be associated with a Nexus in the 'invmod' mod. The Nexus is a central block/entity in the mod that interacts with various entities. This interface ensures that any entity that can be linked to a Nexus will have the necessary methods to get the associated Nexus and to be acquired by a Nexus.
//  * 
//  * Methods:
//  * - getNexus(): This method is intended to return the TileEntityNexus object that the implementing entity is associated with. It serves as a getter to retrieve the Nexus linked to the entity.
//  * 
//  * - acquiredByNexus(TileEntityNexus nexus): This method is meant to be called when the Nexus acquires the entity. It serves as a setter to associate the implementing entity with the provided Nexus instance.
//  * 
//  * Usage:
//  * Any entity class within the 'invmod' mod that needs to interact with a Nexus should implement this interface. This ensures a consistent API for the Nexus to interact with its associated entities and vice versa.
//  */
// package invmod.entity;
// 
// import invmod.tileentity.TileEntityNexus;
// 
// public interface IHasNexus {
//     public TileEntityNexus getNexus();
//     public void acquiredByNexus(TileEntityNexus nexus);
// }
// ```
// `^`^`^`

package invmod.entity;

import invmod.tileentity.TileEntityNexus;

public interface IHasNexus {
	public TileEntityNexus getNexus();

	public void acquiredByNexus(TileEntityNexus nexus);
}