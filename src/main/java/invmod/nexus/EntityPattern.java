// `^`^`^`
// ```java
// /**
//  * This class represents a pattern for creating entities with randomized attributes.
//  * It is designed to be used within the invmod.nexus package and utilizes the RandomSelectionPool
//  * utility to manage the randomization of entity properties such as tier, texture, and flavour.
//  *
//  * Methods:
//  * - EntityPattern(IMEntityType entityType): Constructor that initializes the entity type and
//  *   creates new RandomSelectionPools for tier, texture, and flavour.
//  * - generateEntityConstruct(): Overloaded method that generates an EntityConstruct with default
//  *   angle range.
//  * - generateEntityConstruct(int minAngle, int maxAngle): Generates an EntityConstruct with
//  *   randomized tier, texture, and flavour within specified angle range.
//  * - addTier(int tier, float weight): Adds a tier option with a specified weight to the tier pool.
//  * - addTexture(int texture, float weight): Adds a texture option with a specified weight to the
//  *   texture pool.
//  * - addFlavour(int flavour, float weight): Adds a flavour option with a specified weight to the
//  *   flavour pool.
//  * - toString(): Provides a string representation of the EntityPattern instance, including its
//  *   unique hash code and entity type.
//  *
//  * Constants:
//  * - DEFAULT_TIER: Default tier value used when no tier is selected.
//  * - DEFAULT_FLAVOUR: Default flavour value used when no flavour is selected.
//  * - OPEN_TEXTURE: Default texture value used when no texture is selected.
//  * - OPEN_SCALING: Default scaling value used in the EntityConstruct.
//  *
//  * The class implements the IEntityIMPattern interface, ensuring that it provides the necessary
//  * methods for entity pattern creation.
//  */
// package invmod.nexus;
// 
// // Class definition and imports...
// ```
// `^`^`^`

package invmod.nexus;

import invmod.util.RandomSelectionPool;

public class EntityPattern implements IEntityIMPattern {
	private IMEntityType entityType;
	private RandomSelectionPool<Integer> tierPool;
	private RandomSelectionPool<Integer> texturePool;
	private RandomSelectionPool<Integer> flavourPool;
	private static final int DEFAULT_TIER = 1;
	private static final int DEFAULT_FLAVOUR = 0;
	private static final int OPEN_TEXTURE = 0;
	private static final int OPEN_SCALING = 0;

	public EntityPattern(IMEntityType entityType) {
		this.entityType = entityType;
		this.tierPool = new RandomSelectionPool();
		this.texturePool = new RandomSelectionPool();
		this.flavourPool = new RandomSelectionPool();
	}

	@Override
	public EntityConstruct generateEntityConstruct() {
		return this.generateEntityConstruct(-180, 180);
	}

	@Override
	public EntityConstruct generateEntityConstruct(int minAngle, int maxAngle) {
		Integer tier = this.tierPool.selectNext();
		if (tier == null) {
			tier = Integer.valueOf(1);
		}
		Integer texture = this.texturePool.selectNext();
		if (texture == null) {
			texture = Integer.valueOf(0);
		}
		Integer flavour = this.flavourPool.selectNext();
		if (flavour == null) {
			flavour = Integer.valueOf(0);
		}
		return new EntityConstruct(this.entityType, tier.intValue(), texture.intValue(), flavour.intValue(), 0.0F,
				minAngle, maxAngle);
	}

	public void addTier(int tier, float weight) {
		this.tierPool.addEntry(Integer.valueOf(tier), weight);
	}

	public void addTexture(int texture, float weight) {
		this.texturePool.addEntry(Integer.valueOf(texture), weight);
	}

	public void addFlavour(int flavour, float weight) {
		this.flavourPool.addEntry(Integer.valueOf(flavour), weight);
	}

	@Override
	public String toString() {
		return "EntityIMPattern@" + Integer.toHexString(this.hashCode()) + "#" + this.entityType;
	}
}