// `^`^`^`
// ```java
// /**
//  * Enumerated Type: Goal
//  * 
//  * Purpose:
//  * This code defines an enumerated type (enum) named Goal within the invmod.entity package. The Goal enum represents various objectives or behaviors that an entity within the game can pursue. It is likely used to manage AI decision-making processes, allowing entities to switch between different goals based on their current situation or state.
//  * 
//  * Enum Values:
//  * - TARGET_ENTITY: Represents the goal of targeting another entity.
//  * - GOTO_ENTITY: Represents the goal of moving towards another entity.
//  * - NONE: Represents the absence of a goal.
//  * - BREAK_NEXUS: Represents the goal of breaking a nexus point or objective.
//  * - CHILL: Represents a passive or idle state.
//  * - FLYING_TARGET_ENTITY: Represents the goal of targeting another entity while flying.
//  * - STAY_AT_RANGE: Represents the goal of maintaining a specific distance from a target.
//  * - FIND_ATTACK_OPPORTUNITY: Represents the goal of looking for a chance to attack.
//  * - SWOOP: Represents the goal of swooping down, likely in a flying attack.
//  * - FLYING_STRIKE: Represents the goal of performing a flying strike.
//  * - SWITCH_TARGET: Represents the goal of changing the current target.
//  * - PICK_UP_TARGET: Represents the goal of picking up the current target.
//  * - TACKLE_TARGET: Represents the goal of tackling the target.
//  * - MELEE_TARGET: Represents the goal of engaging in melee combat with the target.
//  * - LEAVE_MELEE: Represents the goal of disengaging from melee combat.
//  * - STABILISE: Represents the goal of stabilizing oneself, possibly after being hit or during flight.
//  * 
//  * Usage:
//  * This enum is likely used in conjunction with AI routines to dictate the current focus of an entity's behavior, allowing for a structured and varied response to the game's dynamics.
//  */
// ```
// `^`^`^`

package invmod.entity;

public enum Goal {
	TARGET_ENTITY, GOTO_ENTITY, NONE, BREAK_NEXUS, CHILL, FLYING_TARGET_ENTITY, STAY_AT_RANGE, FIND_ATTACK_OPPORTUNITY,
	SWOOP, FLYING_STRIKE, SWITCH_TARGET, PICK_UP_TARGET, TACKLE_TARGET, MELEE_TARGET, LEAVE_MELEE, STABILISE;
}