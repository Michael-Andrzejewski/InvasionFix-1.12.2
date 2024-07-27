// `^`^`^`
// ```java
// /**
//  * Interface: IBlockAccessExtended
//  * Package: invmod
//  * Dependencies: net.minecraft.util.math.MathHelper, net.minecraft.util.math.Vec3d, net.minecraft.world.IBlockAccess
//  *
//  * Purpose:
//  * This interface extends the IBlockAccess interface from Minecraft, providing additional functionality
//  * for accessing and manipulating layered data within a Minecraft world. It is designed to be used in
//  * scenarios where blocks or entities have additional data that needs to be retrieved or updated beyond
//  * the standard block properties.
//  *
//  * Methods:
//  * - getLayeredData(int paramInt1, int paramInt2, int paramInt3): 
//  *   Retrieves the layered data at the specified block coordinates.
//  *
//  * - getLayeredData(double x, double y, double z): 
//  *   Overloaded method that retrieves the layered data at the specified coordinates by converting
//  *   the double values to integer block coordinates using the MathHelper's floor method.
//  *
//  * - getLayeredData(Vec3d vec): 
//  *   Overloaded method that retrieves the layered data using a Vec3d object, which encapsulates the
//  *   x, y, and z coordinates, by delegating to the getLayeredData(double x, double y, double z) method.
//  *
//  * - setData(double x, double y, double z, Integer paramInteger): 
//  *   Sets or updates the layered data at the specified coordinates with the provided Integer value.
//  *
//  * Note:
//  * This interface is intended for use within the Minecraft modding community and assumes familiarity
//  * with Minecraft mod development concepts and the Minecraft Forge API.
//  */
// ```
// 
// This executive documentation provides a concise summary of the `IBlockAccessExtended` interface, its purpose, and descriptions of its methods, tailored for developers working on Minecraft mods.
// `^`^`^`

package invmod;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public interface IBlockAccessExtended extends IBlockAccess {

	public int getLayeredData(int paramInt1, int paramInt2, int paramInt3);

	public default int getLayeredData(double x, double y, double z) {
		return this.getLayeredData(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}

	public default int getLayeredData(Vec3d vec) {
		return this.getLayeredData(vec.x, vec.y, vec.z);
	}

	public void setData(double x, double y, double z, Integer paramInteger);

}