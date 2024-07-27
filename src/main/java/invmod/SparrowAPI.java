// `^`^`^`
// ```java
// /**
//  * Interface: SparrowAPI
//  * Package: invmod
//  * 
//  * The SparrowAPI interface is part of the invmod package and is designed to provide a standardized set of behaviors for entities within a Minecraft mod. This interface defines methods that allow for the determination of various entity states and relationships, such as hostility, peacefulness, and predator/prey dynamics. It also includes methods for obtaining entity-specific information like name, size, species, and ownership details.
//  * 
//  * Methods:
//  * - isStupidToAttack(): Determines if attacking the entity is unwise.
//  * - doNotVaporize(): Indicates if the entity should not be destroyed.
//  * - isPredator(): Checks if the entity is a predator.
//  * - isHostile(): Checks if the entity is hostile.
//  * - isPeaceful(): Checks if the entity is peaceful.
//  * - isPrey(): Checks if the entity is prey.
//  * - isNeutral(): Checks if the entity has a neutral stance.
//  * - isUnkillable(): Determines if the entity cannot be killed.
//  * - isThreatTo(Entity paramEntity): Determines if the entity is a threat to the specified entity.
//  * - isFriendOf(Entity paramEntity): Determines if the entity is a friend of the specified entity.
//  * - isNPC(): Checks if the entity is a Non-Player Character (NPC).
//  * - isPet(): Returns an integer representing the pet status of the entity.
//  * - getPetOwner(): Retrieves the Entity that owns the pet.
//  * - getName(): Returns the name of the entity.
//  * - getAttackingTarget(): Retrieves the Entity that the entity is currently attacking.
//  * - getSize(): Returns the size of the entity.
//  * - getSpecies(): Returns the species of the entity.
//  * - getTier(): Returns the tier of the entity.
//  * - getGender(): Returns the gender of the entity.
//  * - customStringAndResponse(String paramString): Processes a custom string and returns a response.
//  * - getSimplyID(): Returns a simple identifier for the entity.
//  * 
//  * This interface is essential for mod developers to implement entity-specific logic and interactions within the game.
//  */
// ```
// 
// This executive documentation provides a concise summary of the `SparrowAPI` interface, outlining its purpose within the context of a Minecraft mod and describing each method's functionality. It serves as a quick reference for developers working with the invmod package.
// `^`^`^`

package invmod;

import net.minecraft.entity.Entity;

public interface SparrowAPI {

	public boolean isStupidToAttack();

	public boolean doNotVaporize();

	public boolean isPredator();

	public boolean isHostile();

	public boolean isPeaceful();

	public boolean isPrey();

	public boolean isNeutral();

	public boolean isUnkillable();

	public boolean isThreatTo(Entity paramEntity);

	public boolean isFriendOf(Entity paramEntity);

	public boolean isNPC();

	public int isPet();

	public Entity getPetOwner();

	public String getName();

	public Entity getAttackingTarget();

	public float getSize();

	public String getSpecies();

	public int getTier();

	public int getGender();

	public String customStringAndResponse(String paramString);

	public String getSimplyID();

}