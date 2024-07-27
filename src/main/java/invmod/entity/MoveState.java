// `^`^`^`
// ```java
// /**
//  * This code defines an enumeration named MoveState within the invmod.entity package. The purpose of this enumeration is to represent the various movement states that an entity can have within a game or simulation. The MoveState enum provides a standardized way of describing the movement behavior of entities, making the code more readable and maintainable.
// 
//  * Enumerations:
//  * - STANDING: Represents an entity that is standing still.
//  * - RUNNING: Represents an entity that is running.
//  * - NONE: Represents an entity with no movement state specified.
//  * - CLIMBING: Represents an entity that is climbing.
//  * - FLYING: Represents an entity that is flying.
//  * - JUMPING: Represents an entity that is jumping.
// 
//  * The MoveState enum can be used to control animations, trigger events, or influence game logic based on the movement state of entities. It is a simple yet powerful tool for developers to manage and understand entity behaviors within their game or simulation environment.
//  */
// package invmod.entity;
// 
// public enum MoveState {
//     STANDING, RUNNING, NONE, CLIMBING, FLYING, JUMPING;
// }
// ```
// `^`^`^`

package invmod.entity;

public enum MoveState {
	STANDING, RUNNING, NONE, CLIMBING, FLYING, JUMPING;
}