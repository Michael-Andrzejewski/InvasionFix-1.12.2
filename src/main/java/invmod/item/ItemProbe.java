// `^`^`^`
// ```java
// /**
//  * This code defines an item class named ItemProbe within the 'invmod' mod, which is designed to interact with the game environment in Minecraft.
//  * The ItemProbe has two primary functions based on its subtype: adjusting the Nexus spawn range and displaying block strength.
//  *
//  * Class: ItemProbe
//  * - Extends ModItem, a custom item class from the 'invmod' mod.
//  * - Contains a static array of probe names for different subtypes.
//  * - Constructor sets up the item with a name, subtype handling, damage, and stack size.
//  * - Overrides isFull3D to ensure the item is rendered properly in 3D.
//  * - Overrides onItemUseFirst to handle the item's interaction with blocks in the world:
//  *   - When used on a Nexus block, it adjusts the Nexus's spawn radius by increments of 8, cycling between 32 and 128.
//  *   - When used with the material probe subtype (damage value 1), it displays the strength of the block being interacted with.
//  * - Overrides getUnlocalizedName to provide a unique name for each subtype based on the damage value.
//  * - Overrides getItemEnchantability to set the enchantability level to 14.
//  * - Overrides getSubItems to add both subtypes of the probe to the specified creative tab.
//  *
//  * Note: Some commented-out code and placeholders (e.g., /* BlocksAndItems.blockNexus */) suggest that parts of the code may be intended for future updates or require additional context from the mod's other components.
//  */
// ```
// `^`^`^`

package invmod.item;

import invmod.ModBlocks;
import invmod.mod_invasion;
import invmod.entity.monster.EntityIMMob;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemProbe extends ModItem {

	public static final String[] probeNames = { "nexusadjuster", "materialprobe" };

	public ItemProbe() {
		super("probe");
		this.setHasSubtypes(true);
		this.setMaxDamage(1);
		this.setMaxStackSize(1);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	/*
	 * @Override public EnumActionResult onItemUseFirst(ItemStack itemstack,
	 * EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float
	 * hitX, float hitY, float hitZ, EnumHand hand) {
	 */
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {

		ItemStack itemstack = player.getHeldItem(hand);
		// if (world.isRemote) return EnumActionResult.FAIL;
		Block block = world.getBlockState(pos).getBlock();

		// Change Nexus spawn range
		if (block == /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK) {
			TileEntityNexus nexus = (TileEntityNexus) world.getTileEntity(pos);
			int newRange = nexus.getSpawnRadius();

			// check if the player wants to increase or decrease the range
			newRange += player.isSneaking() ? -8 : 8;

			if (newRange < 32)
				newRange = 128;
			if (newRange > 128)
				newRange = 32;
			nexus.setSpawnRadius(newRange);
			mod_invasion.sendMessageToPlayer(player, "Nexus range changed to: " + nexus.getSpawnRadius());
			return EnumActionResult.SUCCESS;
		}

		// Display block strength; material probe only
		if (itemstack.getItemDamage() == 1) {
			float blockStrength = EntityIMMob.getBlockStrength(pos, block, world);
			mod_invasion.sendMessageToPlayer(player,
					"Block strength: " + (int) ((blockStrength + 0.005D) * 100.0D) / 100.0D);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if (itemstack.getItemDamage() < probeNames.length) {
			return super.getUnlocalizedName() + "." + probeNames[itemstack.getItemDamage()];
		}
		return super.getUnlocalizedName();
	}

	@Override
	public int getItemEnchantability() {
		return 14;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

}