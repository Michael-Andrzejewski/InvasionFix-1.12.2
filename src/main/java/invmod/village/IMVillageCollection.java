package invmod.village;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;


public class IMVillageCollection extends WorldSavedData
{

	private World world;
	private int tickCount = 0;

	public IMVillageCollection(String name)
	{
		super(name);
	}

	public IMVillageCollection(World worldIn)
	{
		this(fileNameForProvider(worldIn.provider));
		this.world = worldIn;
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound p_189551_1_)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public static String fileNameForProvider(WorldProvider provider)
	{
		return "villages_im_" + provider.getDimensionType().getSuffix();
	}

}
