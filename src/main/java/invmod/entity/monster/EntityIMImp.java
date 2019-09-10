package invmod.entity.monster;

import invmod.mod_Invasion;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIKillEntity;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.entity.ai.EntityAITargetRetaliate;
import invmod.entity.ai.EntityAIWaitForEngy;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;


public class EntityIMImp extends EntityIMMob
{

	public EntityIMImp(World world, TileEntityNexus nexus)
	{
		super(world, nexus);
		this.setBaseMoveSpeedStat(0.3F);
		this.attackStrength = 3;
		this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
		this.setName("Imp");
		this.setGender(1);
		this.setJumpHeight(1);
		this.setCanClimb(true);
	}

	public EntityIMImp(World world)
	{
		this(world, null);
	}

	@Override
	public String getSpecies()
	{
		return "Imp";
	}

	@Override
	protected void initEntityAI()
	{
		//added entityaiswimming and increased all other tasksordernumers with 1
		this.tasksIM = new EntityAITasks(this.world.profiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 40));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
		this.tasksIM.addTask(2, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(3, new EntityAIWaitForEngy(this, 4.0F, true));
		this.tasksIM.addTask(4, new EntityAIKillEntity(this, EntityLiving.class, 40));
		this.tasksIM.addTask(5, new EntityAIGoToNexus(this));
		this.tasksIM.addTask(6, new EntityAIWanderIM(this));
		this.tasksIM.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasksIM.addTask(8, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
		this.tasksIM.addTask(8, new EntityAILookIdle(this));

		this.targetTasksIM = new EntityAITasks(this.world.profiler);
		this.targetTasksIM.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, this.getAggroRange()));
		this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
		this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		this.targetTasksIM.addTask(5, new EntityAIHurtByTarget(this, false));
		this.targetTasksIM.addTask(3, new EntityAITargetOnNoNexusPath(this, EntityIMPigEngy.class, 3.5F));

	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		entity.setFire(3);
		return super.attackEntityAsMob(entity);
	}

	@Override
	public String toString()
	{
		return "IMImp-T" + this.getTier();
	}
}