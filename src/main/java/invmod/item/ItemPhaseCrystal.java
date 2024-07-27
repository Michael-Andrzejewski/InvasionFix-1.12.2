// `^`^`^`
// ```java
// /**
//  * This class represents a custom item in a Minecraft mod, specifically a "Phase Crystal" item.
//  * The class extends the base Item class from Minecraft's net.minecraft.item package, indicating
//  * that it is an item that can be used within the game.
//  *
//  * Public Methods:
//  * - ItemPhaseCrystal(): Constructor method that initializes the item with specific properties.
//  *   It sets the maximum stack size for the item to 1, meaning only one item can be held in a
//  *   single inventory slot. The commented-out lines suggest that there were previous intentions
//  *   to set the registry name, register the item, set its unlocalized name, and assign it to a
//  *   creative tab, but these lines are currently inactive.
//  *
//  * - getName(): This method returns the name of the item, which is "phaseCrystal". This can be
//  *   used to retrieve the item's name for display or identification purposes within the game or
//  *   mod code.
//  *
//  * Note: The commented-out code indicates that there may be additional setup required for full
//  * integration into the game, such as registering the item and setting its display properties.
//  * These steps are essential for the item to appear and function correctly in the game but are
//  * not currently active in this code snippet.
//  */
// package invmod.item;
// 
// import net.minecraft.item.Item;
// 
// public class ItemPhaseCrystal extends Item {
// 
//     private final String name = "phaseCrystal";
// 
//     public ItemPhaseCrystal() {
//         // this.setRegistryName(this.name);
//         // GameRegistry.register(this);
//         // this.setUnlocalizedName(Reference.MODID + "_" + this.name);
//         this.setMaxStackSize(1);
//         // this.setCreativeTab(mod_Invasion.tabInvmod);
//     }
// 
//     public String getName() {
//         return this.name;
//     }
// }
// ```
// `^`^`^`

package invmod.item;

import net.minecraft.item.Item;

public class ItemPhaseCrystal extends Item {

	private final String name = "phaseCrystal";

	public ItemPhaseCrystal() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(1);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	public String getName() {
		return this.name;
	}
}
