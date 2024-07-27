// `^`^`^`
// ```java
// /**
//  * Interface: ISpawnerAccess
//  * 
//  * Purpose:
//  * The ISpawnerAccess interface defines a contract for objects that manage entity spawning within a modded environment, such as a game mod nexus. It outlines methods for attempting to spawn entities, querying the number of available spawn points within a range, sending spawn alerts, and handling scenarios where no spawn points are available.
//  * 
//  * Methods:
//  * - boolean attemptSpawn(EntityConstruct paramEntityConstruct, int paramInt1, int paramInt2):
//  *   Attempts to spawn an entity based on the provided EntityConstruct and coordinates. Returns true if the spawn attempt is successful.
//  * 
//  * - int getNumberOfPointsInRange(int paramInt1, int paramInt2, SpawnType paramSpawnType):
//  *   Returns the number of spawn points within a specified range that are suitable for a given SpawnType.
//  * 
//  * - void sendSpawnAlert(String paramString):
//  *   Sends an alert message regarding the spawning event, which could be used for notifications or logging purposes.
//  * 
//  * - void noSpawnPointNotice():
//  *   Invoked when there are no available spawn points to handle a spawn request, allowing for custom handling of such situations.
//  */
// ```
// 
// This executive documentation provides a concise summary of the `ISpawnerAccess` interface, its purpose, and the responsibilities of each method within the interface. It is intended to be placed at the top of the code file for quick reference and understanding of the interface's role in the entity spawning system.
// `^`^`^`

package invmod.nexus;

public abstract interface ISpawnerAccess {
	public abstract boolean attemptSpawn(EntityConstruct paramEntityConstruct, int paramInt1, int paramInt2);

	public abstract int getNumberOfPointsInRange(int paramInt1, int paramInt2, SpawnType paramSpawnType);

	public abstract void sendSpawnAlert(String paramString);

	public abstract void noSpawnPointNotice();
}