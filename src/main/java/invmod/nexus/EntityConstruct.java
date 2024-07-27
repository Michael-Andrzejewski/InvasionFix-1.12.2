// `^`^`^`
// ```java
// /**
//  * This class represents a template for creating entity instances within the game's nexus module.
//  * It encapsulates the properties necessary to define the visual and behavioral aspects of an entity.
//  *
//  * Class: EntityConstruct
//  * Package: invmod.nexus
//  *
//  * Constructor:
//  * EntityConstruct(IMEntityType mobType, int tier, int texture, int flavour, float scaling, int minAngle, int maxAngle)
//  * - Initializes an entity construct with specified type, tier, texture, flavor, scaling, and angle constraints.
//  *
//  * Methods:
//  * - getMobType(): Returns the type of the entity as an IMEntityType.
//  * - getTexture(): Returns the texture identifier for the entity.
//  * - getTier(): Returns the tier level of the entity, which may affect its strength or abilities.
//  * - getFlavour(): Returns an integer representing a variant or subtype of the entity.
//  * - getScaling(): Returns the scaling factor for the entity's size.
//  * - getMinAngle(): Returns the minimum angle constraint for the entity's orientation or movement.
//  * - getMaxAngle(): Returns the maximum angle constraint for the entity's orientation or movement.
//  *
//  * The class is designed to be used as a blueprint for creating entities with specific characteristics,
//  * such as appearance and behavior, which can be further utilized in the game's spawning system or for
//  * custom entity generation.
//  */
// package invmod.nexus;
// 
// public class EntityConstruct {
//     // Class fields and methods are defined here.
// }
// ```
// `^`^`^`

package invmod.nexus;

public class EntityConstruct {
	private IMEntityType entityType;
	private int texture;
	private int tier;
	private int flavour;
	private int minAngle;
	private int maxAngle;
	private float scaling;

	public EntityConstruct(IMEntityType mobType, int tier, int texture, int flavour, float scaling, int minAngle,
			int maxAngle) {
		this.entityType = mobType;
		this.texture = texture;
		this.tier = tier;
		this.flavour = flavour;
		this.scaling = scaling;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}

	public IMEntityType getMobType() {
		return this.entityType;
	}

	public int getTexture() {
		return this.texture;
	}

	public int getTier() {
		return this.tier;
	}

	public int getFlavour() {
		return this.flavour;
	}

	public float getScaling() {
		return this.scaling;
	}

	public int getMinAngle() {
		return this.minAngle;
	}

	public int getMaxAngle() {
		return this.maxAngle;
	}
}