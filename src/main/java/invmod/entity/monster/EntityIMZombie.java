package invmod.entity.monster;

import java.util.List;
import invmod.IBlockAccessExtended;
import invmod.INotifyTask;
import invmod.SoundHandler;
import invmod.mod_Invasion;
import invmod.entity.ICanDig;
import invmod.entity.ITerrainDig;
import invmod.entity.TerrainDigger;
import invmod.entity.TerrainModifier;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIKillEntity;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAISprint;
import invmod.entity.ai.EntityAIStoop;
import invmod.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.entity.ai.EntityAITargetRetaliate;
import invmod.entity.ai.EntityAIWaitForEngy;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathNode;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class EntityIMZombie extends EntityIMMob implements ICanDig
{

	private static final DataParameter<Boolean> META_CHANGED = EntityDataManager.createKey(EntityIMZombie.class, DataSerializers.BOOLEAN); //29
	private static final DataParameter<Integer> FLAVOUR = EntityDataManager.createKey(EntityIMZombie.class, DataSerializers.VARINT); //28
	private static final DataParameter<Boolean> IS_SWINGING = EntityDataManager.createKey(EntityIMZombie.class, DataSerializers.BOOLEAN); //27

	private TerrainModifier terrainModifier;
	private TerrainDigger terrainDigger;
	private boolean metaChanged;
	//private int tier;
	private int flavour;
	//private ItemStack defaultHeldItem;
	private Item itemDrop;
	private float dropChance;
	private int swingTimer;

	public EntityIMZombie(World world)
	{
		this(world, null);
	}

	public EntityIMZombie(World world, TileEntityNexus nexus)
	{
		super(world, nexus);
		this.terrainModifier = new TerrainModifier(this, 2.0F);
		this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0F);
		this.dropChance = 0.0F;

		this.setAttributes(this.getTier(), this.flavour);
		this.floatsInWater = true;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(META_CHANGED, this.metaChanged = this.world == null ? true : this.world.isRemote);
		//this.getDataManager().register(TIER, this.tier = 1);
		//this.getDataManager().register(TEXTURE, 0);
		this.getDataManager().register(FLAVOUR, this.flavour = 0);
		this.getDataManager().register(IS_SWINGING, false);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.metaChanged != this.getDataManager().get(META_CHANGED))
		{
			this.metaChanged = this.getDataManager().get(META_CHANGED);
			//this.setTexture(this.getDataManager().get(TEXTURE));
			//if(this.tier != this.getDataManager().get(TIER)) this.setTier(this.getDataManager().get(TIER));
			if (this.flavour != this.getDataManager().get(FLAVOUR)) this.setFlavour(this.getDataManager().get(FLAVOUR));
		}
		if ((!this.world.isRemote) && (this.flammability >= 20) && (this.isBurning())) this.doFireball();
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.updateAnimation();
		this.updateSound();
	}

	@Override
	public void onPathSet()
	{
		this.terrainModifier.cancelTask();
	}

	@Override
	protected void initEntityAI()
	{
		//added entityaiswimming and increased all other tasksordernumers with 1
		this.tasksIM = new EntityAITasks(this.world.theProfiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 40));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityGolem.class, 30));
		this.tasksIM.addTask(2, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(3, new EntityAIWaitForEngy(this, 4.0F, true));
		this.tasksIM.addTask(4, new EntityAIKillEntity(this, EntityLiving.class, 40));
		this.tasksIM.addTask(5, new EntityAIGoToNexus(this));
		//this.tasks.addTask(5, new EntityAIMoveTowardsNexus(this));
		this.tasksIM.addTask(6, new EntityAIWanderIM(this));
		this.tasksIM.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasksIM.addTask(8, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
		this.tasksIM.addTask(8, new EntityAILookIdle(this));


		this.targetTasksIM = new EntityAITasks(this.world.theProfiler);
		this.targetTasksIM.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, Config.NIGHTSPAWNS_MOB_SENSERANGE));
		this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, Config.NIGHTSPAWNS_MOB_SIGHTRANGE, true));
		this.targetTasksIM.addTask(5, new EntityAIHurtByTarget(this, false));

		if (this.getTier() == 3)
		{
			this.tasksIM.addTask(4, new EntityAIStoop(this));
			this.tasksIM.addTask(3, new EntityAISprint(this));
		}
		else
		{
			//track players from sensing them
			this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, Config.NIGHTSPAWNS_MOB_SENSERANGE, false));
			this.targetTasksIM.addTask(3, new EntityAITargetOnNoNexusPath(this, EntityIMPigEngy.class, 3.5F));
		}
	}

	@Override
	public String toString()
	{
		return "IMZombie-T" + this.getTier();
	}

	@Override
	public IBlockAccess getTerrain()
	{
		return this.world;
	}

	//TODO: Removed Override annotation
	/*public ItemStack getHeldItem() {
		return this.defaultHeldItem;
		
	}*/

	@Override
	public boolean avoidsBlock(Block block)
	{
		if ((this.isImmuneToFire) && ((block == Blocks.FIRE) || (block == Blocks.FLOWING_LAVA) || (block == Blocks.LAVA)))
		{
			return false;
		}
		return super.avoidsBlock(block);
	}

	@Override
	public float getBlockRemovalCost(BlockPos pos)
	{
		return this.getBlockStrength(pos) * 20.0F;
	}

	@Override
	public boolean canClearBlock(BlockPos pos)
	{
		IBlockState state = this.world.getBlockState(pos);
		return (state.getBlock() == Blocks.AIR) || (this.isBlockDestructible(this.world, pos, state));
	}

	@Override
	public boolean onPathBlocked(Path path, INotifyTask notifee)
	{
		if ((!path.isFinished()) && ((this.isNexusBound()) || (this.getAttackTarget() != null)))
		{

			if ((path.getFinalPathPoint().distanceTo(path.getIntendedTarget()) > 2.2D) && (path.getCurrentPathIndex() + 2 >= path.getCurrentPathLength() / 2))
			{
				return false;
			}
			PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());

			if (this.terrainDigger.askClearPosition(new BlockPos(node.pos), notifee, 1.0F))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isBigRenderTempHack()
	{
		return this.getTier() == 3;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		return (this.getTier() == 3) && (this.isSprinting()) ? this.chargeAttack(entity) : super.attackEntityAsMob(entity);
	}

	@Override
	public boolean canBePushed()
	{
		return this.getTier() != 3;
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5)
	{
		if (this.getTier() == 3) return;
		this.isAirBorne = true;
		float f = MathHelper.sqrt(par3 * par3 + par5 * par5);
		float f1 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= par3 / f * f1;
		this.motionY += f1;
		this.motionZ -= par5 / f * f1;

		if (this.motionY > 0.4000000059604645D) this.motionY = 0.4000000059604645D;
	}

	@Override
	public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap)
	{
		if ((this.getTier() == 2) && (this.flavour == 2) && (node.action == PathAction.SWIM))
		{
			float multiplier = 1.0F;
			if ((terrainMap instanceof IBlockAccessExtended))
			{
				int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.pos) & 0x7;
				multiplier += mobDensity * 3;
			}

			if ((node.pos.yCoord > prevNode.pos.yCoord) && (this.getCollide(terrainMap, node.pos) == 2))
			{
				multiplier += 2.0F;
			}

			return prevNode.distanceTo(node) * 1.2F * multiplier;
		}

		return super.getBlockPathCost(prevNode, node, terrainMap);
	}

	@Override
	public boolean canBreatheUnderwater()
	{
		return (this.getTier() == 2) && (this.flavour == 2);
	}

	@Override
	public boolean isBlockDestructible(IBlockAccess terrainMap, BlockPos pos, IBlockState state)
	{
		if (this.getDestructiveness() == 0) return false;

		BlockPos position = this.getCurrentTargetPos();
		int dY = position.getY() - pos.getY();
		boolean isTooSteep = false;
		if (dY > 0)
		{
			dY += 8;
			int dX = position.getX() - pos.getX();
			int dZ = position.getZ() - pos.getZ();
			double dXZ = Math.sqrt(dX * dX + dZ * dZ) + 1.E-005D;
			isTooSteep = dY / dXZ > 2.144D;
		}

		return (!isTooSteep) && (super.isBlockDestructible(terrainMap, pos, state));
	}

	@Override
	public void onFollowingEntity(Entity entity)
	{
		if (entity == null)
		{
			this.setDestructiveness(1);
		}
		else if (((entity instanceof EntityIMPigEngy)) || ((entity instanceof EntityIMCreeper)))
		{
			this.setDestructiveness(0);
		}
		else
		{
			this.setDestructiveness(1);
		}
	}

	public float scaleAmount()
	{
		if (this.getTier() == 2) return 1.12F;
		if (this.getTier() == 3) return 1.21F;
		return 1.0F;
	}

	@Override
	public String getSpecies()
	{
		return "Zombie";
	}

	/*@Override
	public int getTier() {
		return this.tier < 3 ? 2 : 3;
	}*/

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setInteger("flavour", this.flavour);
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
		this.setTexture(nbttagcompound.getInteger("textureId"));
		this.flavour = nbttagcompound.getInteger("flavour");
		//this.tier = nbttagcompound.getInteger("tier");
		if (this.getTier() == 0) this.setTier(1);
		this.setFlavour(this.flavour);
		//setTier(this.tier);
		//super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	public void setTier(int tier)
	{
		super.setTier(tier);
		this.setAttributes(tier, this.flavour);
		if (this.getTextureId() == 0)
		{
			if (tier == 1)
			{
				this.setTexture(this.rand.nextBoolean() ? 0 : 1);
			}
			else if (tier == 2)
			{
				if (this.flavour == 2)
				{
					this.setTexture(5);
				}
				else if (this.flavour == 3)
				{
					this.setTexture(3);
				}
				else
				{
					this.setTexture(this.rand.nextBoolean() ? 2 : 4);
				}
			}
			else if (tier == 3)
			{
				this.setTexture(6);
			}
		}
	}

	public void setFlavour(int flavour)
	{
		this.getDataManager().set(FLAVOUR, flavour);
		this.flavour = flavour;
		this.setAttributes(this.getTier(), flavour);
	}

	@Override
	protected void sunlightDamageTick()
	{
		if ((this.getTier() == 2) && (this.flavour == 2))
		{
			this.damageEntity(DamageSource.generic, 3.0F);
		}
		else
		{
			super.sunlightDamageTick();
		}
	}

	protected void updateAnimation()
	{
		this.updateAnimation(false);
	}

	public void updateAnimation(boolean override)
	{
		if ((!this.world.isRemote) && ((this.terrainModifier.isBusy()) || override)) this.setSwinging(true);

		int swingSpeed = this.getSwingSpeed();
		if (this.isSwinging())
		{
			this.swingTimer += 1;
			if (this.swingTimer >= swingSpeed)
			{
				this.swingTimer = 0;
				this.setSwinging(false);
			}
		}
		else
		{
			this.swingTimer = 0;
		}
		this.swingProgress = (float)this.swingTimer / (float)swingSpeed;
	}

	protected boolean isSwinging()
	{
		return this.getDataManager().get(IS_SWINGING);
	}

	protected void setSwinging(boolean flag)
	{
		//this.isSwingInProgress=flag;
		//getDataWatcher().updateObject(27, Byte.valueOf((byte) (flag == true ? 1 : 0)));
		this.getDataManager().set(IS_SWINGING, flag);
	}

	protected void updateSound()
	{
		if (this.terrainModifier.isBusy())
		{
			if (--this.throttled2 <= 0)
			{
				//this.world.playSoundAtEntity(this, "invmod:scrape", 0.85F, 1.0F / (this.rand.nextFloat() * 0.5F + 1.0F));
				this.playSound(SoundHandler.scrape1, 0.85F, 1.0F / (this.rand.nextFloat() * 0.5F + 1.0F));
				this.throttled2 = (45 + this.rand.nextInt(20));
			}
		}
	}

	protected int getSwingSpeed()
	{
		return 10;
	}

	protected boolean chargeAttack(Entity entity)
	{
		int knockback = 4;
		entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength + 3);
		entity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F, 0.4D, MathHelper.cos(this.rotationYaw * 3.141593F / 180.0F) * knockback * 0.5F);
		this.setSprinting(false);
		//this.world.playSoundAtEntity(entity, "damage.fallbig", 1.0F, 1.0F);
		this.playSound(SoundEvents.ENTITY_GENERIC_BIG_FALL, 1f, 1f);
		return true;
	}

	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();
		this.terrainModifier.onUpdate();
	}

	protected ITerrainDig getTerrainDig()
	{
		return this.terrainDigger;
	}

	//TODO: Removed Override annotation
	/*protected String getLivingSound() {
		if (this.getTier() == 3) {
			return this.rand.nextInt(3) == 0 ? "invmod:bigzombie1" : null;
		}
	
		return "mob.zombie.say";
	}*/

	@Override
	protected SoundEvent getAmbientSound()
	{
		if (this.getTier() == 3) return this.rand.nextInt(3) == 0 ? SoundHandler.bigzombie1 : null;
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	@Override
	protected Item getDropItem()
	{
		return Items.ROTTEN_FLESH;
	}

	@Override
	protected void dropFewItems(boolean flag, int bonus)
	{
		super.dropFewItems(flag, bonus);
		if (this.rand.nextFloat() < 0.35F)
		{
			this.dropItem(Items.ROTTEN_FLESH, 1);
		}

		if ((this.itemDrop != null) && (this.rand.nextFloat() < this.dropChance))
		{
			this.entityDropItem(new ItemStack(this.itemDrop, 1, 0), 0.0F);
		}
	}

	private void setAttributes(int tier, int flavour)
	{
		this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
		this.setGender(1);
		if (tier == 1)
		{
			//this.tier = 1;
			this.setName("Zombie");
			this.setBaseMoveSpeedStat(0.19F);
			this.selfDamage = 3;
			this.maxSelfDamage = 6;
			this.flammability = 3;
			if (flavour == 0)
			{
				this.attackStrength = 4;
				this.maxDestructiveness = 2;
				this.setDestructiveness(2);
			}
			else if (flavour == 1)
			{
				this.attackStrength = 6;
				this.maxDestructiveness = 0;
				//this.defaultHeldItem = new ItemStack(Items.WOODEN_SWORD, 1);
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.WOODEN_SWORD));
				this.itemDrop = Items.WOODEN_SWORD;
				this.dropChance = 0.2F;
				this.setDestructiveness(0);
			}
		}
		else if (tier == 2)
		{
			//this.tier = 2;
			if (flavour == 0)
			{
				this.setName("Zombie");
				this.setBaseMoveSpeedStat(0.19F);
				this.attackStrength = 7;
				this.selfDamage = 4;
				this.maxSelfDamage = 12;
				this.maxDestructiveness = 2;
				this.flammability = 4;
				this.itemDrop = Items.IRON_CHESTPLATE;
				this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
				this.dropChance = 0.25F;
				this.setDestructiveness(2);
			}
			else if (flavour == 1)
			{
				this.setName("Zombie Marauder");
				this.setBaseMoveSpeedStat(0.19F);
				this.attackStrength = 10;
				this.selfDamage = 3;
				this.maxSelfDamage = 9;
				this.maxDestructiveness = 0;
				//this.itemDrop = Items.IRON_SWORD;
				this.dropChance = 0.25F;
				//this.defaultHeldItem = new ItemStack(Items.IRON_SWORD, 1);
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(this.rand.nextBoolean() ? Items.IRON_SWORD : Items.IRON_AXE));
				this.setHeldItem(EnumHand.OFF_HAND, new ItemStack(this.rand.nextBoolean() ? Items.IRON_SWORD : Items.IRON_AXE));
				this.itemDrop = this.getHeldItem(this.rand.nextBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND).getItem();
				this.setDestructiveness(0);
			}
			else if (flavour == 2)
			{
				this.setName("Tar Zombie");
				this.setBaseMoveSpeedStat(0.19F);
				this.attackStrength = 5;
				this.selfDamage = 3;
				this.maxSelfDamage = 9;
				this.maxDestructiveness = 2;
				this.flammability = 30;
				this.floatsInWater = false;
				this.setDestructiveness(2);
			}
			else if (flavour == 3)
			{
				this.setName("Zombie Pigman");
				this.setBaseMoveSpeedStat(0.25F);
				this.attackStrength = 8;
				this.maxDestructiveness = 2;
				this.isImmuneToFire = true;
				//this.defaultHeldItem = new ItemStack(Items.GOLDEN_SWORD, 1);
				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
				this.setDestructiveness(2);
			}
		}
		else if (tier == 3)
		{
			//this.tier = 3;
			if (flavour == 0)
			{
				this.setName("Zombie Brute");
				this.setBaseMoveSpeedStat(0.17F);
				this.attackStrength = 18;
				this.selfDamage = 4;
				this.maxSelfDamage = 20;
				this.maxDestructiveness = 2;
				this.flammability = 4;
				this.dropChance = 0.0F;
				this.setDestructiveness(2);
			}
		}
	}

	private void doFireball()
	{
		BlockPos pos = new BlockPos(this.getPositionVector());
		for (int xOffset = -1; xOffset < 2; xOffset++)
			for (int yOffset = -1; yOffset < 2; yOffset++)
				for (int zOffset = -1; zOffset < 2; zOffset++)
				{
					if ((this.world.isAirBlock(pos.add(xOffset, yOffset, zOffset)) || (this.world.getBlockState(pos.add(xOffset, yOffset, zOffset)).getMaterial().getCanBurn())))
					{
						this.world.setBlockState(pos.add(xOffset, yOffset, zOffset), Blocks.FIRE.getDefaultState());
					}
				}

		List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(1.5D, 1.5D, 1.5D));
		for (int el = entities.size() - 1; el >= 0; el--)
		{
			entities.get(el).setFire(8);
		}
		this.attackEntityFrom(DamageSource.inFire, 500.0F);
	}

	@Override
	public void onBlockRemoved(BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub

	}

}
