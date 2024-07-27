// `^`^`^`
// ```java
// /**
//  * This class represents a simple selection mechanism for a single object of generic type T.
//  * It implements the ISelect interface, providing a basic framework for selecting an object
//  * and resetting the selection process, if necessary. This class is useful when there is a need
//  * to handle a single, unchanging selection within a system that uses the ISelect interface.
//  *
//  * Public Methods:
//  * - SingleSelection(T object): Constructs a SingleSelection instance with the provided object.
//  * - selectNext(): Returns the selected object. Since this is a single selection, the same object
//  *   is always returned.
//  * - reset(): A no-operation in this implementation, as there is only one object and no state to reset.
//  * - toString(): Provides a string representation of the selected object by calling its toString method.
//  *
//  * Usage:
//  * This class is typically used when an ISelect implementation is required, but the selection
//  * is known to be constant and does not change over time. It simplifies the handling of such
//  * cases by providing a straightforward implementation with minimal overhead.
//  */
// package invmod.util;
// 
// public class SingleSelection<T> implements ISelect<T> {
//     // Class implementation...
// }
// ```
// `^`^`^`

package invmod.util;

public class SingleSelection<T> implements ISelect<T> {
	private T object;

	public SingleSelection(T object) {
		this.object = object;
	}

	@Override
	public T selectNext() {
		return this.object;
	}

	@Override
	public void reset() {
	}

	@Override
	public String toString() {
		return this.object.toString();
	}
}