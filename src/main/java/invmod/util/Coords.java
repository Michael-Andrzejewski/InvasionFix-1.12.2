// `^`^`^`
// ```
// /*
//  * Executive Documentation Summary:
//  * 
//  * The Coords class within the invmod.util package is a utility class that provides predefined arrays of integer and double offsets to represent adjacent and surrounding coordinates in a 2D grid system. These offsets are particularly useful for grid-based operations such as pathfinding, area scanning, or adjacency checks in a game or simulation environment.
//  * 
//  * - offsetAdjX and offsetAdjZ: Represent the immediate horizontal and vertical neighbors (adjacent cells) of a grid cell.
//  * - offsetAdj2X and offsetAdj2Z: Extend the concept of adjacency to include cells two steps away in each cardinal direction.
//  * - offsetRing1X and offsetRing1Z: Define the coordinates of a ring surrounding a central point at a distance of one unit.
//  * - offsetAdjXD, offsetAdjZD, offsetAdj2XD, offsetAdj2ZD, offsetRing1XD, offsetRing1ZD: These arrays are the double precision versions of the above integer arrays, with minimal offsets added to avoid zero values, which may be useful for precise calculations or to avoid issues with floating-point arithmetic.
//  * 
//  * The class does not contain any methods or constructors and serves purely as a repository for constant arrays that can be accessed statically throughout the application.
//  */
// ```
// `^`^`^`

package invmod.util;

public class Coords {

	public static final int[] offsetAdjX = { 1, -1, 0, 0 };
	public static final int[] offsetAdjZ = { 0, 0, 1, -1 };

	public static final int[] offsetAdj2X = { 2, 2, -1, -1, 1, 0, 0, 1 };
	public static final int[] offsetAdj2Z = { 0, 1, 1, 0, 2, 2, -1, -1 };

	public static final int[] offsetRing1X = { 1, 0, -1, -1, -1, 0, 1, 1 };
	public static final int[] offsetRing1Z = { 1, 1, 1, 0, -1, -1, -1, 0 };

	public static final double[] offsetAdjXD = { 0.0001d, -0.0001, 0, 0 };
	public static final double[] offsetAdjZD = { 0, 0, 0.0001d, -0.0001d };

	public static final double[] offsetAdj2XD = { 0.0002d, 0.0002d, -0.0001d, -0.0001d, 0.0001d, 0, 0, 0.0001d };
	public static final double[] offsetAdj2ZD = { 0, 0.0001d, 0.0001d, 0, 0.0002d, 0.0002d, -0.0001d, -0.0001d };

	public static final double[] offsetRing1XD = { 0.0001d, 0, -0.0001d, -0.0001d, -0.0001d, 0, 0.0001d, 0.0001d };
	public static final double[] offsetRing1ZD = { 0.0001d, 0.0001d, 0.0001d, 0, -0.0001d, -0.0001d, -0.0001d, 0 };

}