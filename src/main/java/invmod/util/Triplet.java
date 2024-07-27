// `^`^`^`
// ```
// /*
//  * Executive Documentation Summary:
//  * 
//  * The code defines a generic class named Triplet within the package invmod.util. The purpose of this class is to encapsulate a trio of objects as a single entity. The Triplet class is generic and can hold any types of objects as specified by the type parameters T, U, and V.
//  * 
//  * Methods:
//  * - Triplet(T val1, U val2, V val3): A constructor that initializes the Triplet with three values, allowing users to create a Triplet with specific values for each of its three fields.
//  * - T getVal1(): Returns the first value of the Triplet, allowing users to retrieve the value stored in the first position.
//  * - U getVal2(): Returns the second value of the Triplet, allowing users to retrieve the value stored in the second position.
//  * - V getVal3(): Returns the third value of the Triplet, allowing users to retrieve the value stored in the third position.
//  * - void setVal1(T entry): Sets the first value of the Triplet, allowing users to update the value stored in the first position.
//  * - void setVal2(U value): Sets the second value of the Triplet, allowing users to update the value stored in the second position.
//  * - void setVal3(V value): Sets the third value of the Triplet, allowing users to update the value stored in the third position.
//  * 
//  * This class is useful for storing and passing around a set of three related objects without creating a specific class for them.
//  */
// ```
// `^`^`^`

package invmod.util;

public class Triplet<T, U, V> {
	private T val1;
	private U val2;
	private V val3;

	public Triplet(T val1, U val2, V val3) {
		this.val1 = val1;
		this.val2 = val2;
		this.val3 = val3;
	}

	public T getVal1() {
		return this.val1;
	}

	public U getVal2() {
		return this.val2;
	}

	public V getVal3() {
		return this.val3;
	}

	public void setVal1(T entry) {
		this.val1 = entry;
	}

	public void setVal2(U value) {
		this.val2 = value;
	}

	public void setVal3(V value) {
		this.val3 = value;
	}
}