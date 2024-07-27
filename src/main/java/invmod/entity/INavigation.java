// `^`^`^`
// ```plaintext
// This code defines the INavigation interface, which is a part of the invmod package, specifically for entities. The interface outlines methods for navigation tasks within a Minecraft mod, allowing entities to move and navigate the game world. The interface extends INotifyTask, indicating that it includes notification functionality for task completion.
// 
// Methods:
// 
// - getCurrentWorkingAction(): Returns the current action the navigation system is processing.
// - setSpeed(float paramFloat): Sets the movement speed for the navigation.
// - getPathToXYZ(double x, double y, double z, float paramFloat): Calculates a path to the specified coordinates.
// - tryMoveToXYZ(double x, double y, double z, float speed, float paramFloat2): Attempts to move the entity to the specified coordinates at a given speed.
// - getPathTowardsXZ(double x, double z, int paramInt1, int paramInt2, int paramInt3): Calculates a path towards the specified X and Z coordinates with additional parameters.
// - tryMoveTowardsXZ(double x, double z, int paramInt1, int paramInt2, int paramInt3, float speed): Attempts to move the entity towards the specified X and Z coordinates with a given speed.
// - getPathToEntity(Entity paramEntity, float paramFloat): Calculates a path to the specified entity.
// - tryMoveToEntity(Entity paramEntity, float speed, float paramFloat2): Attempts to move the entity towards another entity at a given speed.
// - autoPathToEntity(Entity paramEntity): Automatically generates a path to the specified entity.
// - setPath(Path paramPath, float speed): Sets the current path for the entity to follow at a specified speed.
// - isWaitingForTask(): Checks if the navigation system is waiting for a new task.
// - getPath(): Retrieves the current path the entity is following.
// - onUpdateNavigation(): Updates the navigation system, typically called each tick.
// - getLastActionResult(): Returns the result of the last navigation action.
// - noPath(): Checks if there is no current path.
// - getStuckTime(): Retrieves the amount of time the entity has been stuck.
// - getLastPathDistanceToTarget(): Gets the distance from the last point on the path to the target.
// - clearPath(): Clears the current path.
// - haltForTick(): Pauses navigation for a tick.
// - getTargetEntity(): Retrieves the entity that is the current target of the navigation.
// - getStatus(): Provides a status report of the navigation system.
// 
// Overall, this interface is crucial for mod developers to implement complex navigation logic for entities within the Minecraft game environment.
// ```
// `^`^`^`

package invmod.entity;

import invmod.INotifyTask;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathAction;
import net.minecraft.entity.Entity;

public interface INavigation extends INotifyTask {

	public PathAction getCurrentWorkingAction();

	public void setSpeed(float paramFloat);

	public Path getPathToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat);

	public boolean tryMoveToXYZ(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1,
			float paramFloat2);

	public Path getPathTowardsXZ(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, int paramInt3);

	public boolean tryMoveTowardsXZ(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2,
			int paramInt3, float paramFloat);

	public Path getPathToEntity(Entity paramEntity, float paramFloat);

	public boolean tryMoveToEntity(Entity paramEntity, float paramFloat1, float paramFloat2);

	public void autoPathToEntity(Entity paramEntity);

	public boolean setPath(Path paramPath, float paramFloat);

	public boolean isWaitingForTask();

	public Path getPath();

	public void onUpdateNavigation();

	public int getLastActionResult();

	public boolean noPath();

	public int getStuckTime();

	public float getLastPathDistanceToTarget();

	public void clearPath();

	public void haltForTick();

	public Entity getTargetEntity();

	public String getStatus();

}