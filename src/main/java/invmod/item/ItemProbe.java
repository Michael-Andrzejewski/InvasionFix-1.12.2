package invmod.item;

import java.util.List;
import invmod.BlocksAndItems;
import invmod.mod_Invasion;
import invmod.entity.monster.EntityIMMob;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemProbe extends ModItem
{

	public static final String[] probeNames = { "nexusAdjuster", "materialProbe" };

	public ItemProbe()
	{
		super("probe");
		this.setHasSubtypes(true);
		this.setMaxDamage(1);
		this.setMaxStackSize(1);
		this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack itemstack,
		EntityPlayer player, World world, BlockPos blockPos,
		EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{

		//if (world.isRemote) return EnumActionResult.FAIL;
		Block block = world.getBlockState(blockPos).getBlock();

		//Change Nexus spawn range
		if (block == BlocksAndItems.blockNexus)
		{
			TileEntityNexus nexus = (TileEntityNexus)world.getTileEntity(blockPos);
			int newRange = nexus.getSpawnRadius();

			// check if the player wants to increase or decrease the range
			newRange += player.isSneaking() ? -8 : 8;

			if (newRange < 32) newRange = 128;
			if (newRange > 128) newRange = 32;
			nexus.setSpawnRadius(newRange);
			mod_Invasion.sendMessageToPlayer(player, "Nexus range changed to: " + nexus.getSpawnRadius());
			return EnumActionResult.SUCCESS;
		}

		//Display block strength; material probe only
		if (itemstack.getItemDamage() == 1)
		{
			float blockStrength = EntityIMMob.getBlockStrength(blockPos, block, world);
			mod_Invasion.sendMessageToPlayer(player, "Block strength: " + (int)((blockStrength + 0.005D) * 100.0D) / 100.0D);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		if (itemstack.getItemDamage() < probeNames.length)
		{
			return super.getUnlocalizedName() + "." + probeNames[itemstack.getItemDamage()];
		}
		return super.getUnlocalizedName();
	}

	@Override
	public int getItemEnchantability()
	{
		return 14;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}

}