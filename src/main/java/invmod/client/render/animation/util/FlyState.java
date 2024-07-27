// `^`^`^`
// ```java
// /**
//  * This enum defines the various states that an entity, such as a bird or an aircraft, can be in during flight-related activities.
//  * It is part of the invmod.client.render.animation.util package, which is likely used for rendering animations in a client application, possibly a game or simulation.
//  *
//  * The FlyState enum includes the following states:
//  * - FLYING: Represents the state of being in flight.
//  * - GROUNDED: Indicates the entity is on the ground and not in flight.
//  * - TAKEOFF: Signifies the transition state from being grounded to flying.
//  * - LANDING: Represents the state where the entity is in the process of landing.
//  * - TOUCHDOWN: Marks the moment the entity makes contact with the ground during landing.
//  * - CRASHING: Indicates an uncontrolled descent or fall, typically leading to a crash.
//  * - SWOOPING_P1: Represents the first phase of a swooping maneuver, which is a rapid descent and ascent.
//  * - SWOOPING_P2: Represents the second phase of a swooping maneuver.
//  * - STRIKING: Signifies an aggressive or targeted movement towards something while in flight.
//  *
//  * Each state is used to control and animate the entity's behavior and visual representation accordingly.
//  */
// package invmod.client.render.animation.util;
// 
// public enum FlyState {
//     FLYING, GROUNDED, TAKEOFF, LANDING, TOUCHDOWN, CRASHING, SWOOPING_P1, SWOOPING_P2, STRIKING;
// }
// ```
// `^`^`^`

package invmod.client.render.animation.util;

public enum FlyState {
	FLYING, GROUNDED, TAKEOFF, LANDING, TOUCHDOWN, CRASHING, SWOOPING_P1, SWOOPING_P2, STRIKING;
}