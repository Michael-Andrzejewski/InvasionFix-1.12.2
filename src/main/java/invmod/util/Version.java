// `^`^`^`
// ```java
// /**
//  * This class represents a software version with major, minor, and build components.
//  * It provides functionality to compare versions and parse version strings.
//  *
//  * - Version(int majorNum, int minorNum, int buildNum): Constructs a Version instance with specified major, minor, and build numbers.
//  *
//  * - byte comparedState(Version version): Compares this Version instance with another Version instance.
//  *   Returns -1 if this version is older, 0 if they are the same, and 1 if this version is newer.
//  *
//  * - static Version get(String s): Parses a version string in the format "major.minor.build" and returns a new Version instance.
//  *   If the string is not in the correct format or contains non-numeric characters, it returns null.
//  *
//  * - String toString(): Returns a string representation of the Version instance in the format "major.minor.build".
//  */
// package invmod.util;
// 
// public class Version {
//     // Class fields representing the major, minor, and build components of the version.
//     public int major;
//     public int minor;
//     public int build;
// 
//     // Constructor and methods as described above.
// }
// ```
// `^`^`^`

package invmod.util;

public class Version {
	public int major;
	public int minor;
	public int build;

	public Version(int majorNum, int minorNum, int buildNum) {
		this.major = majorNum;
		this.minor = minorNum;
		this.build = buildNum;
	}

	public byte comparedState(Version version) {
		if (version.major > this.major) {
			return -1;
		} else if (version.major == this.major) {
			if (version.minor > this.minor) {
				return -1;
			} else if (version.minor == this.minor) {
				if (version.build > this.build) {
					return -1;
				} else if (version.build == this.build) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

	public static Version get(String s) {
		String[] parts = s.split("\\.");
		if (parts.length != 3) {
			return null;
		}

		for (String i : parts) {
			for (Character c : i.toCharArray()) {
				if (!Character.isDigit(c)) {
					return null;
				}
			}
		}

		int[] digits = new int[3];

		for (int i = 0; i < 3; i++) {
			digits[i] = Integer.parseInt(parts[i]);
		}

		return new Version(digits[0], digits[1], digits[2]);
	}

	@Override
	public String toString() {
		return this.major + "." + this.minor + "." + this.build;
	}
}
