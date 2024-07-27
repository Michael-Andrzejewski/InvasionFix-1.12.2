// `^`^`^`
// ```java
// /**
//  * This class represents a polar angle, which is a way of expressing the orientation of a point in a polar coordinate system.
//  * The class implements the IPolarAngle interface, ensuring that it provides the necessary functionality for handling polar angles.
//  *
//  * Class: PolarAngle
//  * Package: invmod.util
//  *
//  * Public Methods:
//  * - PolarAngle(int angle): Constructor that initializes the polar angle with a given integer value.
//  * - getAngle(): Returns the current angle value stored in the object.
//  * - setAngle(int angle): Sets the object's angle to a new integer value.
//  *
//  * The PolarAngle class encapsulates the concept of an angle in a polar coordinate system, providing a simple interface
//  * for setting and retrieving the angle. It is useful in scenarios where angles need to be managed and manipulated as part
//  * of geometric calculations or representations in a two-dimensional space. The angle is stored as an integer, which implies
//  * that the class is likely working with degrees, although the specific unit is not specified and should be clarified for
//  * proper usage.
//  */
// package invmod.util;
// 
// public class PolarAngle implements IPolarAngle {
//     // Class implementation details...
// }
// ```
// `^`^`^`

package invmod.util;

public class PolarAngle implements IPolarAngle {
	private int angle;

	public PolarAngle(int angle) {
		this.angle = angle;
	}

	@Override
	public int getAngle() {
		return this.angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}