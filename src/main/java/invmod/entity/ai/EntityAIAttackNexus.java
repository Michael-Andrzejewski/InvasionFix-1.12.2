package invmod.entity.ai;

import invmod.BlocksAndItems;
import invmod.entity.Goal;
import invmod.entity.monster.EntityIMMob;
import invmod.entity.monster.EntityIMZombie;
import invmod.nexus.INexusAccess;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;


public class EntityAIAttackNexus extends EntityAIBase
{
	private EntityIMMob theEntity;
	private boolean attacked;

	public EntityAIAttackNexus(EntityIMMob par1EntityLiving)
	{
		this.theEntity = par1EntityLiving;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute()
	{
		if ((this.theEntity.getAIGoal() == Goal.BREAK_NEXUS) && (this.theEntity.findDistanceToNexus() > 2.0D))
		{
			return false;
		}

		return this.isNexusInRange();
	}

	@Override
	public void startExecuting()
	{
	}

	@Override
	public boolean continueExecuting()
	{
		return !this.attacked;
	}

	@Override
	public void updateTask()
	{
		if (this.isNexusInRange())
		{
			if (this.theEntity instanceof EntityIMZombie)
			{
				((EntityIMZombie)this.theEntity).updateAnimation(true);
			}
			this.theEntity.getNexus().attackNexus(2);
		}
		this.attacked = true;
	}

	@Override
	public void resetTask()
	{
		this.attacked = false;
	}

	private boolean isNexusInRange()
	{
		BlockPos size = this.theEntity.getCollideSize();
		int x = this.theEntity.getPosition().getX();
		int y = this.theEntity.getPosition().getY();
		int z = this.theEntity.getPosition().getZ();
		for (int i = 0; i < size.getY(); i++)
		{
			for (int j = 0; j < size.getX(); j++)
			{
				if (this.theEntity.world.getBlockState(new BlockPos(x + j, y, z - 1)).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x + j, y, z - 1))
					{
						return true;
					}
				}
				if (this.theEntity.world.getBlockState(new BlockPos(x + j, y, z + 1 + size.getZ())).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x + j, y, z + 1 + size.getZ()))
					{
						return true;
					}
				}
			}

			for (int j = 0; j < size.getZ(); j++)
			{
				if (this.theEntity.world.getBlockState(new BlockPos(x - 1, y, z + j)).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x - 1, y, z + j))
					{
						return true;
					}
				}
				if (this.theEntity.world.getBlockState(new BlockPos(x + 1 + size.getX(), y, z + j)).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x + 1 + size.getX(), y, z + j))
					{
						return true;
					}
				}
			}
		}

		for (int i = 0; i < size.getX(); i++)
		{
			for (int j = 0; j < size.getZ(); j++)
			{
				if (this.theEntity.world.getBlockState(new BlockPos(x + i, y + 1 + size.getY(), z + j)).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x + i, y + 1 + size.getY(), z + j))
					{
						return true;
					}
				}
				if (this.theEntity.world.getBlockState(new BlockPos(x + i, y - 1, z + j)).getBlock() == BlocksAndItems.blockNexus)
				{
					if (this.isCorrectNexus(x + i, y - 1, z + j))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isCorrectNexus(int x, int y, int z)
	{
		INexusAccess nexus = (TileEntityNexus)this.theEntity.world.getTileEntity(new BlockPos(x, y, z));
		if ((nexus != null) && (nexus == this.theEntity.getNexus()))
		{
			return true;
		}
		return false;
	}
}