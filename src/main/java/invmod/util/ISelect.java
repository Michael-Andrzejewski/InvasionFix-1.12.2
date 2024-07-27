// `^`^`^`
// ```java
// /**
//  * This interface defines a selection mechanism for elements of type T. It is designed to be implemented by classes that
//  * provide a way to sequentially select elements from a collection or sequence, with the ability to reset the selection process.
//  *
//  * Methods:
//  * - selectNext(): This abstract method is intended to be implemented to provide the logic for selecting the next element in the sequence.
//  *   The method should return an element of type T that represents the next selected item. The specific selection criteria and
//  *   sequence order are determined by the implementing class.
//  *
//  * - reset(): This abstract method should be implemented to reset the selection process to its initial state. This is useful when
//  *   the selection needs to be restarted or when the underlying collection has been modified and a fresh selection is required.
//  *   Implementing this method ensures that the selector can be reused without the need to create a new instance.
//  *
//  * Usage:
//  * An implementing class would define the specific selection logic and state management within these methods, allowing for
//  * various selection strategies (e.g., random, round-robin, priority-based) to be encapsulated within different selector implementations.
//  */
// package invmod.util;
// 
// public abstract interface ISelect<T> {
//     public abstract T selectNext();
//     public abstract void reset();
// }
// ```
// `^`^`^`

package invmod.util;

public abstract interface ISelect<T> {
	public abstract T selectNext();

	public abstract void reset();
}