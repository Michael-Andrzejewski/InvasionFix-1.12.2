// `^`^`^`
// ```java
// /**
//  * This class represents the information necessary to define custom spawn eggs in Minecraft.
//  * Spawn eggs are items that can be used to spawn a particular type of entity (mob) in the game.
//  *
//  * Fields:
//  * - eggID: A short representing the unique identifier for the custom spawn egg.
//  * - mobID: A String representing the unique string identifier for the type of mob to spawn.
//  * - displayName: A String for the custom name displayed for the spawn egg (optional).
//  * - spawnData: An NBTTagCompound containing additional data to apply to the mob when spawned.
//  * - primaryColor: An int representing the primary color of the spawn egg.
//  * - secondaryColor: An int representing the secondary color of the spawn egg.
//  *
//  * Constructors:
//  * - SpawnEggInfo(short, String, String, NBTTagCompound, int, int): Initializes a new instance with all fields.
//  *   If displayName is not provided, it can be set to null.
//  * - SpawnEggInfo(short, String, NBTTagCompound, int, int): Initializes a new instance without a displayName.
//  *   This constructor calls the first one, passing null for the displayName.
//  *
//  * This class is a simple data holder and does not contain any methods for behavior.
//  */
// package invmod.util.spawneggs;
// 
// import net.minecraft.nbt.NBTTagCompound;
// 
// public class SpawnEggInfo {
//     // Class fields and constructors are defined here.
// }
// ```
// `^`^`^`

package invmod.util.spawneggs;

import net.minecraft.nbt.NBTTagCompound;

public class SpawnEggInfo {

	public final short eggID;
	public final String mobID;
	public final String displayName;
	public final NBTTagCompound spawnData;
	public final int primaryColor;
	public final int secondaryColor;

	public SpawnEggInfo(short eggID, String mobID, String displayName, NBTTagCompound spawnData, int primaryColor,
			int secondaryColor) {
		this.eggID = eggID;
		this.mobID = mobID;
		this.displayName = displayName;
		this.spawnData = spawnData;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	public SpawnEggInfo(short eggID, String mobID, NBTTagCompound compound, int primaryColor, int secondaryColor) {
		this(eggID, mobID, null, compound, primaryColor, secondaryColor);
	}

}