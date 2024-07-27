// `^`^`^`
// ```java
// /**
//  * Executive Summary:
//  * This code defines an enumeration, PathAction, within the package invmod.entity.ai.navigator, which represents various actions that an entity can take while navigating a path. The enum is designed to be used in the context of AI pathfinding, where entities need to decide how to interact with the environment to reach their destination.
//  *
//  * Enum Values:
//  * - NONE: Represents no action or default state.
//  * - LADDER_UP: Indicates the entity should climb up a ladder.
//  * - BRIDGE: Suggests the entity should build or cross a bridge.
//  * - SWIM: Implies the entity should swim through water.
//  * - DIG: Signifies the entity should dig through a barrier.
//  * - LADDER_UP_PX/NX/PZ/NZ: Specifies climbing up a ladder in positive/negative X/Z direction.
//  * - LADDER_TOWER_UP_PX/NX/PZ/NZ: Represents climbing up a ladder tower in the specified direction.
//  * - SCAFFOLD_UP: Indicates the entity should climb up a scaffold.
//  *
//  * Static Arrays:
//  * - ladderTowerIndexOrient: An array holding the ladder tower climbing actions in different orientations.
//  * - ladderIndexOrient: An array containing the basic ladder climbing actions in different orientations.
//  *
//  * Purpose:
//  * The primary purpose of this enumeration is to categorize the types of actions an entity can take when navigating complex paths that involve vertical movement or obstacle interaction. It is likely used by AI algorithms to make decisions about path traversal and to execute the corresponding actions.
//  */
// ```
// `^`^`^`

package invmod.entity.ai.navigator;

public enum PathAction {
	NONE, LADDER_UP, BRIDGE, SWIM, DIG, LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ, LADDER_UP_NZ, LADDER_TOWER_UP_PX,
	LADDER_TOWER_UP_NX, LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ, SCAFFOLD_UP;

	public static final PathAction[] ladderTowerIndexOrient = { LADDER_TOWER_UP_PX, LADDER_TOWER_UP_NX,
			LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ };
	public static final PathAction[] ladderIndexOrient = { LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ, LADDER_UP_NZ };
}