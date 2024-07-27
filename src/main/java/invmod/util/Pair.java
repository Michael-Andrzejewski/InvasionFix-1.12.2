// `^`^`^`
// ```
// /*
//  * Executive Documentation Summary:
//  * 
//  * The Pair class within the invmod.util package is a generic utility class designed to hold and manage a pair of objects, potentially of different types. This class is a simple data structure that encapsulates two values, allowing them to be treated as a single unit.
//  * 
//  * Methods:
//  * - Pair(T val1, U val2): A constructor that initializes the Pair with two values, val1 and val2, which can be of any types.
//  * - T getVal1(): Returns the first value of the pair, allowing access to the first element of the Pair object.
//  * - U getVal2(): Returns the second value of the pair, allowing access to the second element of the Pair object.
//  * - void setVal1(T entry): Sets or updates the first value of the pair with a new value, allowing modification of the first element.
//  * - void setVal2(U value): Sets or updates the second value of the pair with a new value, allowing modification of the second element.
//  * 
//  * This class can be used in various contexts where a pair of related values need to be passed around or manipulated together, such as in map entries, coordinate pairs, or complex return types.
//  */
// ```
// `^`^`^`

package invmod.util;

public class Pair<T, U> {
	private T val1;
	private U val2;

	public Pair(T val1, U val2) {
		this.val1 = val1;
		this.val2 = val2;
	}

	public T getVal1() {
		return this.val1;
	}

	public U getVal2() {
		return this.val2;
	}

	public void setVal1(T entry) {
		this.val1 = entry;
	}

	public void setVal2(U value) {
		this.val2 = value;
	}
}