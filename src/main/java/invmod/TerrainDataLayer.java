// `^`^`^`
// ```java
// /**
//  * This class, TerrainDataLayer, serves as an extended block access layer for managing additional data on top of the existing Minecraft world data.
//  * It implements the IBlockAccessExtended interface, allowing it to interact with Minecraft's world data and add custom data handling.
//  *
//  * Methods:
//  * - TerrainDataLayer(IBlockAccess world): Constructor that initializes the TerrainDataLayer with a reference to the existing world.
//  * - setData(double x, double y, double z, Integer data): Stores custom data at the specified coordinates.
//  * - getLayeredData(int x, int y, int z): Retrieves custom data stored at the specified coordinates.
//  * - setAllData(IntHashMap data): Replaces the current data layer with the provided IntHashMap data.
//  * - getBlockState(BlockPos blockPos): Delegates to the underlying world to get the block state at the specified position.
//  * - getCombinedLight(BlockPos blockPos, int meta): Delegates to the underlying world to get the light level at the specified position.
//  * - isAirBlock(BlockPos blockPos): Delegates to the underlying world to check if the block at the specified position is air.
//  * - getBiome(BlockPos blockPos): Delegates to the underlying world to get the biome at the specified position.
//  * - getTileEntity(BlockPos blockPos): Delegates to the underlying world to get the tile entity at the specified position.
//  * - isSideSolid(BlockPos blockPos, EnumFacing side, boolean _default): Checks if the block at the specified position has a solid side.
//  * - getStrongPower(BlockPos pos, EnumFacing direction): Delegates to the underlying world to get the redstone power at the specified position.
//  * - getWorldType(): Returns the world type of the underlying world.
//  *
//  * The class provides a way to store and retrieve additional data for blocks in the world, which can be used for custom game mechanics or AI behaviors.
//  */
// ```
// `^`^`^`

package invmod;

import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathNode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class TerrainDataLayer implements IBlockAccessExtended {
	public static final int EXT_DATA_SCAFFOLD_METAPOSITION = 16384;
	private IBlockAccess world;
	private IntHashMap dataLayer;

	public TerrainDataLayer(IBlockAccess world) {
		this.world = world;
		this.dataLayer = new IntHashMap();
	}

	@Override
	public void setData(double x, double y, double z, Integer data) {
		this.dataLayer.addKey(PathNode.makeHash(x, y, z, PathAction.NONE), data);
	}

	@Override
	public int getLayeredData(int x, int y, int z) {
		int key = PathNode.makeHash(x, y, z, PathAction.NONE);
		if (this.dataLayer.containsItem(key)) {
			return ((Integer) this.dataLayer.lookup(key)).intValue();
		}
		return 0;
	}

	public void setAllData(IntHashMap data) {
		this.dataLayer = data;
	}

	@Override
	public IBlockState getBlockState(BlockPos blockPos) {
		return this.world.getBlockState(blockPos);
	}

	@Override
	public int getCombinedLight(BlockPos blockPos, int meta) {
		return this.world.getCombinedLight(blockPos, meta);
	}

	@Override
	public boolean isAirBlock(BlockPos blockPos) {
		return this.world.isAirBlock(blockPos);
	}

	@Override
	public Biome getBiome(BlockPos blockPos) {
		return this.world.getBiome(blockPos);
	}

	/*
	 * @Override public boolean extendedLevelsInChunkCache() { return
	 * this.world.extendedLevelsInChunkCache(); }
	 */

	@Override
	public TileEntity getTileEntity(BlockPos blockPos) {
		return this.world.getTileEntity(blockPos);
	}

	@Override
	public boolean isSideSolid(BlockPos blockPos, EnumFacing side, boolean _default) {
		// return this.world.getBlockState(blockPos).getBlock().getMaterial().isSolid();
		IBlockState blockState = this.world.getBlockState(blockPos);
		return blockState.getBlock().getMaterial(blockState).isSolid(); // DarthXenon: Redundant, in my opinion.
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return this.world.getStrongPower(pos, direction);
	}

	@Override
	public WorldType getWorldType() {
		return this.world.getWorldType();
	}
}