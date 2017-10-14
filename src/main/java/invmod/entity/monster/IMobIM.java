package invmod.entity.monster;

import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.BlockPos;


public interface IMobIM extends IMob
{

	public void setNexus(TileEntityNexus nexus);

	public TileEntityNexus getNexus();

	public int getTier();

	public int getTextureID();

	public boolean isBlockDestructible(BlockPos pos);

	public boolean avoidsBlock(BlockPos pos);

	public boolean ignoresBlock(BlockPos pos);

}
