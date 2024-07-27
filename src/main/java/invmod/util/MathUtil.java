// `^`^`^`
// ```java
// /**
//  * This class provides utility methods for mathematical operations, particularly for angle manipulation,
//  * floating-point comparison, and binary packing/unpacking of numerical values.
//  *
//  * Methods:
//  * - floatEquals(float f1, float f2, float tolerance): Compares two floating-point numbers within a specified tolerance.
//  * - boundAnglePiRad(double angle): Normalizes an angle in radians to the range [-π, π].
//  * - boundAngle180Deg(double angle): Normalizes an angle in degrees to the range [-180, 180].
//  * - interpRotationRad(float rot1, float rot2, float t): Interpolates between two angles in radians.
//  * - interpRotationDeg(float rot1, float rot2, float t): Interpolates between two angles in degrees.
//  * - interpWrapped(float val1, float val2, float t, float min, float max): Interpolates between two wrapped values.
//  * - unpackFloat(int i): Converts an integer bit representation back into a floating-point number.
//  * - packFloat(float f): Converts a floating-point number into its integer bit representation.
//  * - packAnglesDeg(float a1, float a2, float a3, float a4): Packs four angles in degrees into a single integer.
//  * - unpackAnglesDeg_1(int i): Unpacks the first angle from a packed integer.
//  * - unpackAnglesDeg_2(int i): Unpacks the second angle from a packed integer.
//  * - unpackAnglesDeg_3(int i): Unpacks the third angle from a packed integer.
//  * - unpackAnglesDeg_4(int i): Unpacks the fourth angle from a packed integer.
//  * - packBytes(int i1, int i2, int i3, int i4): Packs four bytes into a single integer.
//  * - unpackBytes_1(int i): Unpacks the first byte from a packed integer.
//  * - unpackBytes_2(int i): Unpacks the second byte from a packed integer.
//  * - unpackBytes_3(int i): Unpacks the third byte from a packed integer.
//  * - unpackBytes_4(int i): Unpacks the fourth byte from a packed integer.
//  * - packShorts(int i1, int i2): Packs two shorts into a single integer.
//  * - unpackShorts_1(int i): Unpacks the first short from a packed integer.
//  * - unpackShorts_2(int i): Unpacks the second short from a packed integer.
//  *
//  * These methods are useful for game development, simulations, and other applications where angle calculations
//  * and efficient data packing are commonly required.
//  */
// package invmod.util;
// 
// public class MathUtil {
//     // ... (methods implementation)
// }
// ```
// `^`^`^`

package invmod.util;

public class MathUtil {
	public static boolean floatEquals(float f1, float f2, float tolerance) {
		float diff = f1 - f2;
		if (diff >= 0.0F) {
			return diff < tolerance;
		}
		return -diff < tolerance;
	}

	public static double boundAnglePiRad(double angle) {
		angle %= 6.283185307179586D;
		if (angle >= 3.141592653589793D)
			angle -= 6.283185307179586D;
		else if (angle < -3.141592653589793D) {
			angle += 6.283185307179586D;
		}
		return angle;
	}

	public static double boundAngle180Deg(double angle) {
		angle %= 360.0D;
		if (angle >= 180.0D)
			angle -= 360.0D;
		else if (angle < -180.0D) {
			angle += 360.0D;
		}
		return angle;
	}

	public static float interpRotationRad(float rot1, float rot2, float t) {
		return interpWrapped(rot1, rot2, t, -3.141593F, 3.141593F);
	}

	public static float interpRotationDeg(float rot1, float rot2, float t) {
		return interpWrapped(rot1, rot2, t, -180.0F, 180.0F);
	}

	public static float interpWrapped(float val1, float val2, float t, float min, float max) {
		float dVal = val2 - val1;
		while (dVal < min) {
			dVal += max - min;
		}
		while (dVal >= max) {
			dVal -= max - min;
		}
		return val1 + t * dVal;
	}

	public static float unpackFloat(int i) {
		return Float.intBitsToFloat(i);
	}

	public static int packFloat(float f) {
		return Float.floatToIntBits(f);
	}

	public static int packAnglesDeg(float a1, float a2, float a3, float a4) {
		return packBytes((byte) (int) (a1 / 360.0F * 256.0F), (byte) (int) (a2 / 360.0F * 256.0F),
				(byte) (int) (a3 / 360.0F * 256.0F), (byte) (int) (a4 / 360.0F * 256.0F));
	}

	public static float unpackAnglesDeg_1(int i) {
		return unpackBytes_1(i) * 360.0F / 256.0F;
	}

	public static float unpackAnglesDeg_2(int i) {
		return unpackBytes_2(i) * 360.0F / 256.0F;
	}

	public static float unpackAnglesDeg_3(int i) {
		return unpackBytes_3(i) * 360.0F / 256.0F;
	}

	public static float unpackAnglesDeg_4(int i) {
		return unpackBytes_4(i) * 360.0F / 256.0F;
	}

	public static int packBytes(int i1, int i2, int i3, int i4) {
		return i1 << 24 & 0xFF000000 | i2 << 16 & 0xFF0000 | i3 << 8 & 0xFF00 | i4 & 0xFF;
	}

	public static byte unpackBytes_1(int i) {
		return (byte) (i >>> 24);
	}

	public static byte unpackBytes_2(int i) {
		return (byte) (i >>> 16 & 0xFF);
	}

	public static byte unpackBytes_3(int i) {
		return (byte) (i >>> 8 & 0xFF);
	}

	public static byte unpackBytes_4(int i) {
		return (byte) (i & 0xFF);
	}

	public static int packShorts(int i1, int i2) {
		return i1 << 16 | i2 & 0xFFFF;
	}

	public static short unhopackSrts_1(int i) {
		return (short) (i >>> 16);
	}

	public static int unpackShorts_2(int i) {
		return (short) (i & 0xFFFF);
	}
}