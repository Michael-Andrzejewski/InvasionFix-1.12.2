// `^`^`^`
// ```java
// /**
//  * This class represents a 3D position with rotation, designed for use within a Minecraft mod.
//  * It encapsulates the coordinates (X, Y, Z) and rotation angles (rotX, rotY, rotZ) for an object in 3D space.
//  *
//  * Methods:
//  * - PosRotate3D(): Default constructor initializing position and rotation to zero.
//  * - PosRotate3D(double posX, double posY, double posZ, float rotX, float rotY, float rotZ): Overloaded constructor for setting initial position and rotation.
//  * - getPos(): Returns a Vec3d object representing the current position.
//  * - getPosX(): Returns the X coordinate of the position.
//  * - getPosY(): Returns the Y coordinate of the position.
//  * - getPosZ(): Returns the Z coordinate of the position.
//  * - getRotX(): Returns the rotation angle around the X-axis.
//  * - getRotY(): Returns the rotation angle around the Y-axis.
//  * - getRotZ(): Returns the rotation angle around the Z-axis.
//  * - setPosX(double pos): Sets the X coordinate of the position.
//  * - setPosY(double pos): Sets the Y coordinate of the position.
//  * - setPosZ(double pos): Sets the Z coordinate of the position.
//  * - setRotX(float rot): Sets the rotation angle around the X-axis.
//  * - setRotY(float rot): Sets the rotation angle around the Y-axis.
//  * - setRotZ(float rot): Sets the rotation angle around the Z-axis.
//  *
//  * This class is useful for managing the position and orientation of objects in a Minecraft world.
//  */
// package invmod.util;
// 
// // Class definition and imports omitted for brevity
// ```
// `^`^`^`

package invmod.util;

import net.minecraft.util.math.Vec3d;

public class PosRotate3D {
	private double posX;
	private double posY;
	private double posZ;
	private float rotX;
	private float rotY;
	private float rotZ;

	public PosRotate3D() {
		this(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
	}

	public PosRotate3D(double posX, double posY, double posZ, float rotX, float rotY, float rotZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}

	public Vec3d getPos() {
		return new Vec3d(this.posX, this.posY, this.posZ);
	}

	public double getPosX() {
		return this.posX;
	}

	public double getPosY() {
		return this.posY;
	}

	public double getPosZ() {
		return this.posZ;
	}

	public float getRotX() {
		return this.rotX;
	}

	public float getRotY() {
		return this.rotY;
	}

	public float getRotZ() {
		return this.rotZ;
	}

	public void setPosX(double pos) {
		this.posX = pos;
	}

	public void setPosY(double pos) {
		this.posY = pos;
	}

	public void setPosZ(double pos) {
		this.posZ = pos;
	}

	public void setRotX(float rot) {
		this.rotX = rot;
	}

	public void setRotY(float rot) {
		this.rotY = rot;
	}

	public void setRotZ(float rot) {
		this.rotZ = rot;
	}
}