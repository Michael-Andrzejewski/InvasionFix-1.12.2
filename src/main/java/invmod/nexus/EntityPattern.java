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