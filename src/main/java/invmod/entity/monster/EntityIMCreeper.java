package invmod.entity.monster;

import com.google.common.base.Predicate;

import invmod.INotifyTask;
import invmod.ModBlocks;
import invmod.mod_invasion;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAICreeperIMSwell;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIKillEntity;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAITargetRetaliate;
import invmod.entity.ai.EntityAIWaitForEngy;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathNode;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class EntityIMCreeper extends EntityIMMob
{

	private static final DataParameter<Integer> CREEPER_STATE = EntityDataManager.createKey(EntityIMCreeper.class, DataSerializers.VARINT);

	private int timeSinceIgnited;
	private int lastActiveTime;
	private boolean explosionDeath;
	private boolean commitToExplode;
	private int explodeDirection;

	public EntityIMCreeper(World world)
	{
		this(world, null);
	}

	public EntityIMCreeper(World world, TileEntityNexus nexus)
	{
		super(world, nexus);
		this.setName("Creeper");
		this.setGender(0);
		this.setBaseMoveSpeedStat(0.21F);
		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
		this.initEntityAI();
	}

	@Override
	protected void initEntityAI()
	{
		this.tasksIM = new EntityAITasks(this.world.profiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		this.tasksIM.addTask(1, new EntityAICreeperIMSwell(this));
		this.tasksIM.addTask(2, new EntityAIAvoidEntity(this, EntityOcelot.class, new Predicate()
		{
			@Override
			public boolean apply(Object entity)
			{
				return entity instanceof EntityOcelot;
			}
		}, 6.0F, 0.25D, 0.300000011920929D));
		this.tasksIM.addTask(3, new EntityAIKillEntity(this, EntityPlayer.class, 40));
		this.tasksIM.addTask(3, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
		this.tasksIM.addTask(4, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(5, new EntityAIWaitForEngy(this, 4.0F, true));
		this.tasksIM.addTask(6, new EntityAIKillEntity(this, EntityLiving.class, 40));
		this.tasksIM.addTask(7, new EntityAIGoToNexus(this));
		this.tasksIM.addTask(8, new EntityAIWanderIM(this));
		this.tasksIM.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 4.8F));
		this.tasksIM.addTask(9, new EntityAILookIdle(this));

		this.targetTasksIM = new EntityAITasks(this.world.profiler);
		this.targetTasksIM.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, 12.0F));
		if (this.isNexusBound())
		{
			this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, 20.0F, true));
		}
		else
		{
			this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
			this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		}
		this.targetTasksIM.addTask(3, new EntityAIHurtByTarget(this, false));
	}

	@Override
	public void updateAITick()
	{
		super.updateAITick();
	}

//	@Override
//	public boolean isAIEnabled()
//	{
//		return true;
//	}

	@Override
	public boolean onPathBlocked(Path path, INotifyTask notifee)
	{
		if (!path.isFinished())
		{
			PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
			double dX = node.pos.x + 0.5D - this.posX;
			double dZ = node.pos.z + 0.5D - this.posZ;
			float facing = (float)(Math.atan2(dZ, dX) * 180.0D / 3.141592653589793D) - 90.0F;
			if (facing < 0.0F) facing += 360.0F;
			facing %= 360.0F;

			if ((facing >= 45.0F) && (facing < 135.0F))
				this.explodeDirection = 1;
			else if ((facing >= 135.0F) && (facing < 225.0F))
				this.explodeDirection = 3;
			else if ((facing >= 225.0F) && (facing < 315.0F))
				this.explodeDirection = 0;
			else
			{
				this.explodeDirection = 2;
			}
			this.setCreeperState(1);
			this.commitToExplode = true;
		}
		return false;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(CREEPER_STATE, Integer.valueOf(-1));
		//this.getDataManager().register(17, Byte.valueOf((byte)0)); //unused.
	}

	@Override
	public void onUpdate()
	{
		if (this.explosionDeath)
		{
			this.doExplosion();
			this.setDead();
		}
		else if (this.isEntityAlive())
		{
			this.lastActiveTime = this.timeSinceIgnited;
			int state = this.getCreeperState();

			if (state > 0)
			{
				if (this.commitToExplode)
				{
					this.getMoveHelper().setMoveTo(this.posX + invmod.util.Coords.offsetAdjX[this.explodeDirection], this.posY, this.posZ + invmod.util.Coords.offsetAdjZ[this.explodeDirection], 0.0D);
				}
				if (this.timeSinceIgnited == 0)
				{
					//this.world.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
					this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1f, 0.5f);
				}
			}
			this.timeSinceIgnited += state;
			if (this.timeSinceIgnited < 0)
			{
				this.timeSinceIgnited = 0;
			}
			if (this.timeSinceIgnited >= 30)
			{
				this.timeSinceIgnited = 30;
				this.explosionDeath = true;
			}
		}

		super.onUpdate();
	}

//	@Override
//	public boolean isMartyr()
//	{
//		return this.explosionDeath;
//	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_CREEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		//return "mob.creeper.death";
		return SoundEvents.ENTITY_CREEPER_DEATH;
	}

	@Override
	public String getSpecies()
	{
		return "Creeper";
	}

	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

		if ((par1DamageSource.getTrueSource() instanceof EntitySkeleton))
		{
			this.dropItem(Item.getItemById(Item.getIdFromItem(Items.RECORD_13) + this.rand.nextInt(10)), 1);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		return true;
	}

	public float setCreeperFlashTime(float par1)
	{
		return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0F;
	}

	@Override
	public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap)
	{
		Block block = terrainMap.getBlockState(new BlockPos(node.pos)).getBlock();
		if ((block != Blocks.AIR) && (!block.isPassable(terrainMap, new BlockPos(node.pos))) && (block != /*BlocksAndItems.blockNexus*/ModBlocks.NEXUS_BLOCK))
		{
			return prevNode.distanceTo(node) * 12.0F;
		}

		return super.getBlockPathCost(prevNode, node, terrainMap);
	}

	@Override
	public String toString()
	{
		return "IMCreeper-T" + this.getTier();
	}

	@Override
	protected void dropFewItems(boolean flag, int amount)
	{
		this.entityDropItem(new ItemStack(Items.GUNPOWDER), 0.5F);
	}

	protected void doExplosion()
	{
		Explosion explosion = new Explosion(this.world, this, this.posX, this.posY, this.posZ, 2.1F, false, true);
		if (!this.world.isRemote) explosion.doExplosionA();
		explosion.doExplosionB(true);
		//ExplosionUtil.doExplosionB(world,explosion,true);
	}

	public int getCreeperState()
	{
		return this.getDataManager().get(CREEPER_STATE);
	}

	public void setCreeperState(int state)
	{
		if ((this.commitToExplode) && (state != 1)) return;
		this.getDataManager().set(CREEPER_STATE, Integer.valueOf(state));
	}

}