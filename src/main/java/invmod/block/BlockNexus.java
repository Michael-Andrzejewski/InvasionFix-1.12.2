// `^`^`^`
// ```java
// /**
//  * This class represents the custom block "Nexus" in the mod 'invmod'. The Nexus block is a central piece in the mod, providing a unique functionality and interaction for players.
//  *
//  * Constructor:
//  * - BlockNexus(): Sets up the block with material properties, resistance, hardness, and associates an ItemBlock with it for inventory representation.
//  *
//  * Methods:
//  * - onBlockActivated(World, BlockPos, IBlockState, EntityPlayer, EnumHand, EnumFacing, float, float, float): Handles block interaction. When a player right-clicks the block, it opens the Nexus GUI if the world is not remote.
//  *
//  * - randomDisplayTick(IBlockState, World, BlockPos, Random): A client-side method that creates particle effects around the Nexus block when it is active. This visual effect is similar to the one seen around Ender Chests.
//  *
//  * - hasTileEntity(IBlockState): Indicates that this block has an associated TileEntity, which is true for the Nexus block.
//  *
//  * - createTileEntity(World, IBlockState): Creates and returns a new TileEntityNexus instance, which is the TileEntity associated with the Nexus block.
//  *
//  * - setBlockView(boolean, World, BlockPos): A static method that updates the block state at the specified position. If the Nexus is active, it prints a message; otherwise, it resets the block state to the default Nexus block state.
//  *
//  * - getPlayerRelativeBlockHardness(IBlockState, EntityPlayer, World, BlockPos): Determines the block's hardness relative to the player. If the Nexus is active, it cannot be broken, indicated by returning -1.0F.
//  *
//  * - getRenderType(IBlockState): Specifies the rendering type for the block, which is set to model to allow for custom block models.
//  *
//  * This class is a key component of the 'invmod' mod, providing both a unique gameplay mechanic and visual effects to enhance player experience.
//  */
// ```
// `^`^`^`

package invmod.block;

import java.util.Random;

import invmod.ModBlocks;
import invmod.mod_invasion;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNexus extends Block {

	public final String name = "blocknexus";
	public final ItemBlock itemBlock;

	public BlockNexus() {
		super(Material.ROCK);
		// this.setUnlocalizedName(this.name);
		// this.setRegistryName(this.name);
		this.setResistance(6000000.0F);
		this.setHardness(3.0F);
		// this.setStepSound(Blocks.glass.stepSound);
		// this.setSoundType(Blocks.GLASS.getSoundType());
		this.itemBlock = new ItemBlock(this);
		this.itemBlock.setRegistryName(this.name);
		// GameRegistry.register(this);
		// GameRegistry.register(this.itemBlock);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	// @Override
	// public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState
	// state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem,
	// EnumFacing side, float hitX, float hitY, float hitZ)
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			playerIn.openGui(mod_invasion.instance, Config.NEXUS_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState blockState, World worldIn, BlockPos pos, Random rand) {
		TileEntityNexus nexus = (TileEntityNexus) worldIn.getTileEntity(pos);
		int numberOfParticles = nexus != null ? (nexus.isActive() ? 6 : 0) : 0;

		for (int i = 0; i < numberOfParticles; i++) {

			// Copied from BlockEnderChest
			int j = rand.nextInt(2) * 2 - 1;
			int k = rand.nextInt(2) * 2 - 1;
			double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
			double d3 = (double) (rand.nextFloat() * (float) j);
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
			double d5 = (double) (rand.nextFloat() * (float) k);
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);

			/*
			 * double y1 = blockPos.getY() + random.nextFloat(); double y2 =
			 * (random.nextFloat() - 0.5D) * 0.5D;
			 * 
			 * int direction = random.nextInt(2) * 2 - 1; double x2; double x1; double z1;
			 * double z2; if (random.nextInt(2) == 0) { z1 = blockPos.getZ() + 0.5D + 0.25D
			 * * direction; z2 = random.nextFloat() * 2.0F * direction;
			 * 
			 * x1 = blockPos.getX() + random.nextFloat(); x2 = (random.nextFloat() - 0.5D) *
			 * 0.5D; } else { x1 = blockPos.getX() + 0.5D + 0.25D * direction; x2 =
			 * random.nextFloat() * 2.0F * direction; z1 = blockPos.getZ() +
			 * random.nextFloat(); z2 = (random.nextFloat() - 0.5D) * 0.5D; }
			 * 
			 * world.spawnParticle(EnumParticleTypes.PORTAL, x1, y1, z1, x2, y2, z2);
			 */
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	// public TileEntity createTileEntity(World world, int metadata)
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityNexus(world);
	}

	public static void setBlockView(boolean active, World worldIn, BlockPos blockPos) {
		// IBlockState iblockstate = worldIn.getBlockState(blockPos);
		if (blockPos != null && worldIn != null) {
			TileEntity tileentity = worldIn.getTileEntity(blockPos);
			if (active) {
				System.out.println("This cant be true!!!!!!");
			} else {
				worldIn.setBlockState(blockPos, /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK.getDefaultState(),
						3);
			}
			if (tileentity != null) {
				tileentity.validate();
				worldIn.setTileEntity(blockPos, tileentity);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world,
			BlockPos blockPos) {
		TileEntityNexus tile = (TileEntityNexus) world.getTileEntity(blockPos);
		if (tile.isActive()) {
			return -1.0F;
		} else {
			return super.getPlayerRelativeBlockHardness(state, player, world, blockPos);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

}