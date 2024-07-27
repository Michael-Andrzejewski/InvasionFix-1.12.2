// `^`^`^`
// ```java
// /**
//  * This class represents a finite selection pool for generic items of type T. It allows for random selection of items
//  * with a finite number of occurrences for each item. The pool can be regenerated to its original state and cloned.
//  *
//  * Public Methods:
//  * - addEntry(T entry, int amount): Adds a single item with a specified number of occurrences to the pool.
//  * - addEntry(ISelect<T> entry, int amount): Adds an item selection interface with a specified number of occurrences.
//  * - selectNext(): Randomly selects and returns an item from the pool, decrementing its occurrence count.
//  * - clone(): Creates a deep copy of the current FiniteSelectionPool with the original occurrence counts.
//  * - reset(): Resets the pool to its original state with all initial occurrence counts.
//  * - toString(): Provides a string representation of the pool with details about each entry and its amount.
//  *
//  * Private Method:
//  * - regeneratePool(): Resets the occurrence counts of all items in the pool to their original numbers.
//  *
//  * The pool maintains a list of pairs, each containing an item selection interface and its occurrence count. It also
//  * tracks the total number of items available for selection and the original total for regeneration purposes.
//  */
// package invmod.util;
// 
// // ... (rest of the code) ...
// ```
// 
// `^`^`^`

package invmod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FiniteSelectionPool<T> implements ISelect<T> {
	private List<Pair<ISelect<T>, Integer>> currentPool;
	private List<Integer> originalPool;
	private int totalAmount;
	private int originalAmount;
	private Random rand;

	public FiniteSelectionPool() {
		this.currentPool = new ArrayList();
		this.originalPool = new ArrayList();
		this.totalAmount = 0;
		this.rand = new Random();
	}

	public void addEntry(T entry, int amount) {
		SingleSelection selection = new SingleSelection(entry);
		this.addEntry(selection, amount);
	}

	public void addEntry(ISelect<T> entry, int amount) {
		this.currentPool.add(new Pair(entry, Integer.valueOf(amount)));
		this.originalPool.add(Integer.valueOf(amount));
		this.originalAmount = (this.totalAmount += amount);
	}

	@Override
	public T selectNext() {
		if (this.totalAmount < 1) {
			this.regeneratePool();
		}
		float r = this.rand.nextInt(this.totalAmount);
		for (Pair entry : this.currentPool) {
			int amountLeft = ((Integer) entry.getVal2()).intValue();
			if (r < amountLeft) {
				entry.setVal2(Integer.valueOf(amountLeft - 1));
				this.totalAmount -= 1;
				return (T) ((ISelect) entry.getVal1()).selectNext();
			}

			r -= amountLeft;
		}

		return null;
	}

	@Override
	public FiniteSelectionPool<T> clone() {
		FiniteSelectionPool clone = new FiniteSelectionPool();
		for (int i = 0; i < this.currentPool.size(); i++) {
			clone.addEntry((ISelect) ((Pair) this.currentPool.get(i)).getVal1(), this.originalPool.get(i).intValue());
		}

		return clone;
	}

	@Override
	public void reset() {
		this.regeneratePool();
	}

	@Override
	public String toString() {
		String s = "FiniteSelectionPool@" + Integer.toHexString(this.hashCode()) + "#Size=" + this.currentPool.size();
		for (int i = 0; i < this.currentPool.size(); i++) {
			s = s + "\n\tEntry " + i + "   Amount: " + this.originalPool.get(i);
			s = s + "\n\t" + ((ISelect) ((Pair) this.currentPool.get(i)).getVal1()).toString();
		}
		return s;
	}

	private void regeneratePool() {
		this.totalAmount = this.originalAmount;
		for (int i = 0; i < this.currentPool.size(); i++) {
			((Pair) this.currentPool.get(i)).setVal2(this.originalPool.get(i));
		}
	}
}