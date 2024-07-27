// `^`^`^`
// ```java
// /**
//  * This code represents a registry for managing spawn eggs within a game or application.
//  * The registry is designed to keep track of different types of spawn eggs, each identified by a unique ID.
//  * It provides functionality to register new spawn eggs, check the validity of spawn egg IDs, retrieve information about specific spawn eggs, and list all registered spawn eggs.
//  *
//  * Methods:
//  * - registerSpawnEgg(SpawnEggInfo info): Registers a new spawn egg with the registry using the provided SpawnEggInfo object. Throws IllegalArgumentException if the info object is null or if the egg ID is already in use.
//  * - isValidSpawnEggID(short id): Checks if the provided spawn egg ID is valid (i.e., not already in use) and returns a boolean result.
//  * - getEggInfo(short id): Retrieves the SpawnEggInfo associated with the given ID, or returns null if no such spawn egg is registered.
//  * - getEggInfoList(): Returns an unmodifiable collection of all registered SpawnEggInfo objects, preventing direct modification of the registry.
//  *
//  * The registry uses a LinkedHashMap to maintain the insertion order of spawn eggs and ensure quick access to the spawn egg information.
//  */
// package invmod.util.spawneggs;
// 
// // ... (rest of the imports and class definition)
// ```
// `^`^`^`

package invmod.util.spawneggs;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpawnEggRegistry {

	private static final Map<Short, SpawnEggInfo> eggs = new LinkedHashMap<Short, SpawnEggInfo>();

	public static void registerSpawnEgg(SpawnEggInfo info) throws IllegalArgumentException {
		if (info == null)
			throw new IllegalArgumentException("SpawnEggInfo cannot be null");
		if (!isValidSpawnEggID(info.eggID))
			throw new IllegalArgumentException("Duplicate spawn egg with id " + info.eggID);
		eggs.put(info.eggID, info);
	}

	public static boolean isValidSpawnEggID(short id) {
		return !eggs.containsKey(id);
	}

	public static SpawnEggInfo getEggInfo(short id) {
		return eggs.get(id);
	}

	public static Collection<SpawnEggInfo> getEggInfoList() {
		return Collections.unmodifiableCollection(eggs.values());
	}
}