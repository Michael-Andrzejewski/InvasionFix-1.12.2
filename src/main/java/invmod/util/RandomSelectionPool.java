// `^`^`^`
// ```java
// /**
//  * This class represents a weighted random selection pool for entities of type EntityIMLiving.
//  * It allows for the addition of entities with associated weights and provides a method to randomly select an entity based on the defined weights.
//  *
//  * Public Methods:
//  * - RandomSelectionPool(): Constructor that initializes the selection pool, total weight, and random number generator.
//  * - addEntry(EntityIMLiving entry, float weight): Adds a single entity to the pool with the specified weight.
//  * - addEntry(ISelect<EntityIMLiving> entry, float weight): Adds an entry that can select entities to the pool with the specified weight.
//  * - selectNext(): Randomly selects and returns an entity from the pool based on the weights of the entries.
//  * - clone(): Creates and returns a copy of the current RandomSelectionPool with the same entries and weights.
//  * - reset(): Currently, this method is empty and does not perform any action.
//  * - toString(): Provides a string representation of the RandomSelectionPool, including the size of the pool and details of each entry.
//  *
//  * The class uses a list of Pair objects to store entries and their associated weights. The totalWeight variable keeps track of the sum of all weights.
//  * The selectNext method uses a random float to determine which entity to select, simulating a weighted probability distribution.
//  * If an error occurs during selection, a safe fail mechanism returns the first entity in the pool.
//  * The clone method allows for the duplication of the pool, preserving the state of the original.
//  * The toString method is useful for debugging and logging the state of the pool.
//  */
// package invmod.util;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSelectionPool<EntityIMLiving> implements ISelect<EntityIMLiving> {
	private List<Pair<ISelect<EntityIMLiving>, Float>> pool;
	private float totalWeight;
	private Random rand;

	public RandomSelectionPool() {
		this.pool = new ArrayList();
		this.totalWeight = 0.0F;
		this.rand = new Random();
	}

	public void addEntry(EntityIMLiving entry, float weight) {
		SingleSelection selection = new SingleSelection(entry);
		this.addEntry(selection, weight);
	}

	public void addEntry(ISelect<EntityIMLiving> entry, float weight) {
		this.pool.add(new Pair(entry, Float.valueOf(weight)));
		this.totalWeight += weight;
	}

	@Override
	public EntityIMLiving selectNext() {
		float r = this.rand.nextFloat() * this.totalWeight;
		for (Pair entry : this.pool) {
			if (r < ((Float) entry.getVal2()).floatValue()) {
				return (EntityIMLiving) ((ISelect) entry.getVal1()).selectNext();
			}

			r -= ((Float) entry.getVal2()).floatValue();
		}

		if (this.pool.size() > 0) {
			ModLogger.logFatal("RandomSelectionPool invalid setup or rounding error. Failing safe.");
			return (EntityIMLiving) ((ISelect) ((Pair) this.pool.get(0)).getVal1()).selectNext();
		}
		return null;
	}

	@Override
	public RandomSelectionPool<EntityIMLiving> clone() {
		RandomSelectionPool clone = new RandomSelectionPool();
		for (Pair entry : this.pool) {
			clone.addEntry((ISelect) entry.getVal1(), ((Float) entry.getVal2()).floatValue());
		}

		return clone;
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		String s = "RandomSelectionPool@" + Integer.toHexString(this.hashCode()) + "#Size=" + this.pool.size();
		for (int i = 0; i < this.pool.size(); i++) {
			s = s + "\n\tEntry " + i + "   Weight: " + ((Pair) this.pool.get(i)).getVal2();
			s = s + "\n\t" + ((ISelect) ((Pair) this.pool.get(i)).getVal1()).toString();
		}
		return s;
	}
}