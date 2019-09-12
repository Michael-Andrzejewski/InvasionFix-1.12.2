package invmod.block;

import java.util.Random;

import invmod.BlocksAndItems;
import invmod.mod_Invasion;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockNexus extends BlockContainer
{

	public final String name = "blockNexus";
	public final ItemBlock itemBlock;

	public BlockNexus()
	{
		super(Material.ROCK);
		this.setUnlocalizedName(this.name);
		this.setRegistryName(this.name);
		this.setResistance(6000000.0F);
		this.setHardness(3.0F);
		//this.setStepSound(Blocks.glass.stepSound);
		this.setSoundType(Blocks.GLASS.getSoundType());
		this.itemBlock = new ItemBlock(this);
		this.itemBlock.setRegistryName(this.name);
		//GameRegistry.register(this);
		//GameRegistry.register(this.itemBlock);
		this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	//@Override
	//public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Item item = null;
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if (heldItem != null) item = heldItem.getItem();

		if (worldIn.isRemote) return true;

		if ((item != BlocksAndItems.itemProbe) && ((!Config.DEBUG) || (item != BlocksAndItems.itemDebugWand)))
		{
			TileEntityNexus tileEntityNexus = (TileEntityNexus)worldIn.getTileEntity(pos);
			if (tileEntityNexus != null)
			{
				playerIn.openGui(mod_Invasion.instance(), Config.NEXUS_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			return true;
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState blockState, World worldIn, BlockPos pos, Random rand)
	{
		TileEntityNexus nexus = (TileEntityNexus)worldIn.getTileEntity(pos);
		int numberOfParticles = nexus != null ? (nexus.isActive() ? 6 : 0) : 0;

		for (int i = 0; i < numberOfParticles; i++)
		{

			//Copied from BlockEnderChest
			int j = rand.nextInt(2) * 2 - 1;
			int k = rand.nextInt(2) * 2 - 1;
			double d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
			double d1 = (double)((float)pos.getY() + rand.nextFloat());
			double d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)k;
			double d3 = (double)(rand.nextFloat() * (float)j);
			double d4 = ((double)rand.nextFloat() - 0.5D) * 0.125D;
			double d5 = (double)(rand.nextFloat() * (float)k);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);

			/*double y1 = blockPos.getY() + random.nextFloat();
			double y2 = (random.nextFloat() - 0.5D) * 0.5D;
			
			int direction = random.nextInt(2) * 2 - 1;
			double x2;
			double x1;
			double z1;
			double z2;
			if (random.nextInt(2) == 0) {
				z1 = blockPos.getZ() + 0.5D + 0.25D * direction;
				z2 = random.nextFloat() * 2.0F * direction;
			
				x1 = blockPos.getX() + random.nextFloat();
				x2 = (random.nextFloat() - 0.5D) * 0.5D;
			} else {
				x1 = blockPos.getX() + 0.5D + 0.25D * direction;
				x2 = random.nextFloat() * 2.0F * direction;
				z1 = blockPos.getZ() + random.nextFloat();
				z2 = (random.nextFloat() - 0.5D) * 0.5D;
			}
			
			world.spawnParticle(EnumParticleTypes.PORTAL, x1, y1, z1, x2, y2, z2);*/
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEntityNexus(world);
	}

	public static void setBlockView(boolean active, World worldIn, BlockPos blockPos)
	{
		// IBlockState iblockstate = worldIn.getBlockState(blockPos);
		if (blockPos != null && worldIn != null)
		{
			TileEntity tileentity = worldIn.getTileEntity(blockPos);
			if (active)
			{
				System.out.println("This cant be true!!!!!!");
			}
			else
			{
				worldIn.setBlockState(blockPos, BlocksAndItems.blockNexus.getDefaultState(), 3);
			}
			if (tileentity != null)
			{
				tileentity.validate();
				worldIn.setTileEntity(blockPos, tileentity);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos blockPos)
	{
		TileEntityNexus tile = (TileEntityNexus)world.getTileEntity(blockPos);
		if (tile.isActive())
		{
			return -1.0F;
		}
		else
		{
			return super.getPlayerRelativeBlockHardness(state, player, world, blockPos);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

}