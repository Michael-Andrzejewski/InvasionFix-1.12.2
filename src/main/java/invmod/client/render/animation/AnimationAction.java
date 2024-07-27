// `^`^`^`
// ```java
// /**
//  * AnimationAction Enum Summary:
//  *
//  * The AnimationAction enum defines a set of constants representing different animation actions for an entity within a client render animation system. These actions are used to control the visual state and transitions of animated entities in a game or simulation environment.
//  *
//  * Enum Constants:
//  * - STAND: Represents the entity in a standing still position.
//  * - STAND_TO_RUN: Represents the transition from standing to running.
//  * - RUN: Represents the entity in a running motion.
//  * - WINGFLAP: Represents the flapping motion of an entity's wings.
//  * - WINGGLIDE: Represents the gliding motion of an entity's wings.
//  * - WINGTUCK: Represents the tucking motion of an entity's wings.
//  * - WINGSPREAD: Represents the spreading motion of an entity's wings.
//  * - LEGS_RETRACT: Represents the retraction of an entity's legs.
//  * - LEGS_UNRETRACT: Represents the unretraction of an entity's legs.
//  * - LEGS_CLAW_ATTACK_P1: Represents the first phase of a leg claw attack.
//  * - LEGS_CLAW_ATTACK_P2: Represents the second phase of a leg claw attack.
//  * - MOUTH_OPEN: Represents the opening of an entity's mouth.
//  * - MOUTH_CLOSE: Represents the closing of an entity's mouth.
//  *
//  * This enum is typically used in conjunction with an animation system to define the specific actions that can be animated for an entity. It serves as a centralized reference for animation states, ensuring consistency and ease of use when programming entity animations.
//  */
// package invmod.client.render.animation;
// 
// public enum AnimationAction {
//     // Enum constants representing various animation actions.
//     STAND, STAND_TO_RUN, RUN, WINGFLAP, WINGGLIDE, WINGTUCK, WINGSPREAD, LEGS_RETRACT, LEGS_UNRETRACT,
//     LEGS_CLAW_ATTACK_P1, LEGS_CLAW_ATTACK_P2, MOUTH_OPEN, MOUTH_CLOSE;
// }
// ```
// `^`^`^`

package invmod.client.render.animation;

public enum AnimationAction {
	STAND, STAND_TO_RUN, RUN, WINGFLAP, WINGGLIDE, WINGTUCK, WINGSPREAD, LEGS_RETRACT, LEGS_UNRETRACT,
	LEGS_CLAW_ATTACK_P1, LEGS_CLAW_ATTACK_P2, MOUTH_OPEN, MOUTH_CLOSE;
}