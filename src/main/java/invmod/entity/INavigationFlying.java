// `^`^`^`
// ```java
// /**
//  * INavigationFlying Interface Summary:
//  * 
//  * The INavigationFlying interface defines the navigation capabilities for flying entities within a Minecraft mod. It extends the INavigation interface, providing additional methods specifically tailored for aerial movement and behavior. This interface is intended to be implemented by entities that require complex flying navigation such as setting paths, circling targets, and adjusting flight speed.
//  * 
//  * Methods:
//  * - setMovementType(MoveType paramMoveType): Specifies the movement preference of the entity (walking, mixed, flying).
//  * - setLandingPath(): Sets a path for the entity to land.
//  * - setCirclingPath(Entity paramEntity, float paramFloat1, float paramFloat2): Defines a path for the entity to circle around a target entity at specified radii.
//  * - setCirclingPath(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2): Overloaded method to set a circling path around a specific location.
//  * - getDistanceToCirclingRadius(): Returns the distance from the entity to the radius at which it is set to circle.
//  * - isCircling(): Checks if the entity is currently circling.
//  * - setFlySpeed(float paramFloat): Sets the flying speed of the entity.
//  * - setPitchBias(float paramFloat1, float paramFloat2): Adjusts the entity's pitch bias, which can affect the steepness of its flight path.
//  * - enableDirectTarget(boolean paramBoolean): Enables or disables direct targeting, potentially altering how the entity approaches its targets.
//  * 
//  * Enums:
//  * - MoveType: Enumerates the different movement preferences (PREFER_WALKING, MIXED, PREFER_FLYING).
//  * 
//  * This interface is crucial for mod developers who wish to create entities with advanced flying behaviors in Minecraft.
//  */
// ```
// `^`^`^`

package invmod.entity;

import net.minecraft.entity.Entity;

public abstract interface INavigationFlying extends INavigation {
	public abstract void setMovementType(MoveType paramMoveType);

	public abstract void setLandingPath();

	public abstract void setCirclingPath(Entity paramEntity, float paramFloat1, float paramFloat2);

	public abstract void setCirclingPath(double paramDouble1, double paramDouble2, double paramDouble3,
			float paramFloat1, float paramFloat2);

	public abstract float getDistanceToCirclingRadius();

	public abstract boolean isCircling();

	public abstract void setFlySpeed(float paramFloat);

	public abstract void setPitchBias(float paramFloat1, float paramFloat2);

	public abstract void enableDirectTarget(boolean paramBoolean);

	public static enum MoveType {
		PREFER_WALKING, MIXED, PREFER_FLYING;
	}
}