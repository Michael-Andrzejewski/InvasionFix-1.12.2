// `^`^`^`
// ```
// /*
//  * Executive Documentation Summary:
//  * 
//  * The InvMobConstruct class is part of the invmod.nexus package and is designed to represent the construction blueprint for an invasive mob entity within a game or simulation. This class encapsulates the properties that define the visual and categorical aspects of a mob, such as its texture, tier, flavour, and scaling.
//  * 
//  * Constructor:
//  * - InvMobConstruct(int texture, int tier, int flavour, float scaling): Initializes a new instance of InvMobConstruct with specified texture, tier, flavour, and scaling values.
//  * 
//  * Methods:
//  * - getTexture(): Returns the texture identifier of the mob, which is an integer value representing the visual appearance.
//  * - getTier(): Returns the tier of the mob, an integer value that may represent the mob's level, difficulty, or class.
//  * - getFlavour(): Returns the flavour of the mob, an integer that could be used to categorize the mob into different types or factions.
//  * - getScaling(): Returns the scaling factor of the mob as a float, which could be used to adjust the mob's size or impact in the game world.
//  * 
//  * This class serves as a simple data container without any behavior or logic for the mobs themselves. It is likely used by other parts of the system to instantiate mobs with the specified properties.
//  */
// ```
// `^`^`^`

package invmod.nexus;

public class InvMobConstruct {
	private int texture;
	private int tier;
	private int flavour;
	private float scaling;

	public InvMobConstruct(int texture, int tier, int flavour, float scaling) {
		this.texture = texture;
		this.tier = tier;
		this.flavour = flavour;
		this.scaling = scaling;
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
}