// `^`^`^`
// ```java
// /**
//  * This code defines a custom item class for a Minecraft mod, named ItemRiftFlux, which extends the base Item class from Minecraft.
//  *
//  * Class: ItemRiftFlux
//  * - Purpose: Represents a custom item within the game with specific properties and behaviors.
//  * - Usage: Can be used to create instances of the riftFlux item in the game with predefined characteristics.
//  *
//  * Constructor: ItemRiftFlux()
//  * - Initializes a new instance of the ItemRiftFlux with predefined settings.
//  * - Sets the maximum stack size for the item to 64, indicating how many of these items can be stacked together in a single inventory slot.
//  * - Sets the maximum damage to 0, implying that the item is not damageable.
//  * - The commented-out lines suggest that there are additional settings for registry name, unlocalized name, and creative tab assignment, which are not active in the current code but can be uncommented and used if needed.
//  *
//  * Method: onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
//  * - Overrides the onItemUseFirst method from the Item class.
//  * - Defines the action to take when the player uses the item for the first time.
//  * - Currently, it is programmed to always return EnumActionResult.FAIL, indicating no action is taken when the item is used.
//  *
//  * Method: getName()
//  * - Provides a way to retrieve the name of the item, which is "riftFlux".
//  *
//  * Note: The code contains several commented-out lines that suggest additional functionality that can be enabled by uncommenting those lines, such as item registration and naming within the game's registry system.
//  */
// ```
// `^`^`^`

package invmod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRiftFlux extends Item {

	private final String name = "riftFlux";

	public ItemRiftFlux() {
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		this.setMaxDamage(0);
		// this.setUnlocalizedName(Reference.MODID + "_" + this.name);
		this.setMaxStackSize(64);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	/*
	 * @Override public EnumActionResult onItemUseFirst(ItemStack itemstack,
	 * EntityPlayer entityplayer, World world, BlockPos blockPos, EnumFacing side,
	 * float hitX, float hitY, float hitZ, EnumHand hand) { return
	 * EnumActionResult.FAIL; }
	 */
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.FAIL;
	}

	public String getName() {
		return this.name;
	}
}