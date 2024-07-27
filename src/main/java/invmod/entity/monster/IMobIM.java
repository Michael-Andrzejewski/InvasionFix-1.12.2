// `^`^`^`
// ```java
// /**
//  * Interface: IMobIM
//  * Package: invmod.entity.monster
//  * 
//  * This interface extends the IMob interface from Minecraft and is part of the 'invmod' mod, specifically for entities that interact with a Nexus block. It defines additional behaviors and properties for monsters that are part of the Invasion Mod, allowing them to interact with the Nexus in various ways.
//  * 
//  * Methods:
//  * - setNexus(TileEntityNexus nexus): Assigns a Nexus to the monster, establishing a link between the monster and the Nexus block.
//  * - getNexus(): Retrieves the Nexus associated with the monster, allowing for interaction with the Nexus's properties or status.
//  * - getTier(): Returns the tier of the monster, which may affect its strength or abilities within the mod's context.
//  * - getTextureID(): Provides an identifier for the monster's texture, which can be used to apply different textures to the monster.
//  * - isBlockDestructible(BlockPos pos): Determines if the monster can destroy a specific block at the given position.
//  * - avoidsBlock(BlockPos pos): Checks if the monster will naturally avoid a block at the specified position, influencing its pathfinding.
//  * - ignoresBlock(BlockPos pos): Indicates whether the monster will ignore a block during its movement, potentially allowing it to pass through certain blocks.
//  * 
//  * This interface is crucial for defining the behavior of monsters within the Invasion Mod, particularly how they interact with the Nexus and the environment.
//  */
// package invmod.entity.monster;
// 
// // ... (imports and interface declaration)
// ```
// This summary provides an overview of the purpose and functionality of the `IMobIM` interface within the context of the Invasion Mod for Minecraft, detailing each method and its role in defining the behavior of Nexus-linked monsters.
// `^`^`^`

package invmod.entity.monster;

import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;

public interface IMobIM extends IMob {

	public void setNexus(TileEntityNexus nexus);

	public TileEntityNexus getNexus();

	public int getTier();

	public int getTextureID();

	public boolean isBlockDestructible(BlockPos pos);

	public boolean avoidsBlock(BlockPos pos);

	public boolean ignoresBlock(BlockPos pos);

}
