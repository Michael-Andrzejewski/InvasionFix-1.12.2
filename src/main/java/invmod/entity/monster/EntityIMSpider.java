// `^`^`^`
// ```java
// /**
//  * This code defines the EntityIMSpider class, which is a custom entity class for a modded Minecraft game. The class extends EntityIMMob and implements ISpawnsOffspring, indicating that this entity can spawn offspring.
//  *
//  * EntityIMSpider(World world): Constructor that initializes a spider entity in the given world without a nexus reference.
//  * EntityIMSpider(World world, TileEntityNexus nexus): Overloaded constructor that initializes a spider entity with a nexus reference.
//  * initEntityAI(): Initializes the entity's AI tasks, including swimming, attacking players and the nexus, wandering, and targeting.
//  * onUpdate(): Called every tick to update the entity's state, including syncing metadata changes from the server to the client.
//  * moveRelative(float x, float up, float z, float friction): Handles the entity's movement logic, including swimming, climbing, and flying.
//  * getMoveHelper(): Returns the IMMoveHelper instance associated with this entity, which aids in movement.
//  * jump(): Causes the entity to jump, setting the motionY to give an upward velocity.
//  * setTier(int tier): Sets the tier of the spider and updates its attributes based on the tier.
//  * setFlavour(int flavour): Sets the flavor of the spider, which can affect its appearance and abilities.
//  * toString(): Provides a string representation of the entity, including its tier and name.
//  * getMountedYOffset(): Returns the Y-offset for mounting this entity.
//  * writeEntityToNBT(NBTTagCompound nbttagcompound): Writes the entity's data to NBT for saving.
//  * readEntityFromNBT(NBTTagCompound nbttagcompound): Reads the entity's data from NBT.
//  * avoidsBlock(int id): Determines if the spider should avoid certain blocks.
//  * spiderScaleAmount(): Returns the scale factor for the spider's size based on its tier and flavor.
//  * getOffspring(Entity partner): Returns an array of Entity objects representing the offspring of this spider when paired with a partner.
//  *
//  * The class also includes several static DataParameter objects for tracking metadata changes and various private fields related to the spider's behavior and attributes.
//  */
// ```
// ```plaintext
// This code appears to be part of a larger class that defines the behavior and attributes of a spider entity within a game, likely a mod for Minecraft given the context and naming conventions. The class includes methods for creating spider entities, managing their sounds, handling their movement and interactions with the environment, and defining their attributes based on different types (or "flavours") of spiders.
// 
// - The constructor `EntityConstruct` initializes a new spider entity with specified parameters such as type, tier, and flavor.
// - The method `getAirborneTime` returns the time the spider has been airborne.
// - The method `canBePushed` determines if the spider can be pushed, based on whether it is on a ladder.
// - The method `getCreatureAttribute` returns the creature attribute, which is ARTHROPOD for spiders.
// - The method `checkForAdjacentClimbBlock` checks if there is a block adjacent that the spider can climb.
// - The method `getSpecies` returns the species name of the entity, which is "Spider".
// - The method `setAirborneTime` sets the time the spider has been airborne.
// - The method `canTriggerWalking` determines if walking should be triggered, which is false for spiders.
// - The method `getLivingSound` returns the sound made by the spider when it is alive.
// - The method `getHurtSound` returns the sound made by the spider when it is hurt.
// - The method `getDeathSound` returns the sound made by the spider when it dies.
// - The method `fall` handles the spider's behavior when falling, including playing sounds based on the block it lands on.
// - The method `dropFewItems` controls the items dropped by the spider upon death.
// - The private method `setAttributes` sets various attributes of the spider, such as size, health, and attack strength, based on its tier and flavor.
// 
// Overall, this code is responsible for defining the behavior and characteristics of spider entities in the game, providing a more immersive and dynamic experience for players.
// ```
// `^`^`^`

package invmod.entity.monster;

import invmod.mod_invasion;
import invmod.client.render.animation.util.IMMoveHelper;
import invmod.client.render.animation.util.IMMoveHelperSpider;
import invmod.entity.ISpawnsOffspring;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIKillEntity;
import invmod.entity.ai.EntityAILayEgg;
import invmod.entity.ai.EntityAIPounce;
import invmod.entity.ai.EntityAIRallyBehindEntity;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.entity.ai.EntityAITargetRetaliate;
import invmod.entity.ai.EntityAIWaitForEngy;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.nexus.EntityConstruct;
import invmod.nexus.IMEntityType;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityIMSpider extends EntityIMMob implements ISpawnsOffspring {

	private IMMoveHelper i;
	private byte metaChanged;
	private int flavour;
	private int pounceTime;
	private int pounceAbility;
	private int airborneTime;

	public static final DataParameter<Byte> META_CHANGED = EntityDataManager.createKey(EntityIMSpider.class,
			DataSerializers.BYTE); // 29
	public static final DataParameter<Integer> FLAVOUR = EntityDataManager.createKey(EntityIMSpider.class,
			DataSerializers.VARINT); // 28

	public EntityIMSpider(World world) {
		this(world, null);
	}

	public EntityIMSpider(World world, TileEntityNexus nexus) {
		super(world, nexus);
		this.setSize(1.4F, 0.9F);
		this.setCanClimb(true);
		this.airborneTime = 0;
		this.metaChanged = world.isRemote ? (byte) 1 : (byte) 0;
		this.flavour = 0;
		this.setAttributes(this.getTier(), this.flavour);
		// setAI();
		this.i = new IMMoveHelperSpider(this);

		this.getDataManager().register(META_CHANGED, this.metaChanged);
		this.getDataManager().register(FLAVOUR, this.flavour);
	}

	@Override
	protected void initEntityAI() {
		this.tasksIM = new EntityAITasks(this.world.profiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 40));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayerMP.class, 40));
		this.tasksIM.addTask(2, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(3, new EntityAIWaitForEngy(this, 5.0F, false));
		this.tasksIM.addTask(4, new EntityAIKillEntity(this, EntityLiving.class, 40));
		this.tasksIM.addTask(5, new EntityAIGoToNexus(this));
		this.tasksIM.addTask(7, new EntityAIWanderIM(this));
		this.tasksIM.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		this.tasksIM.addTask(9, new EntityAILookIdle(this));

		this.targetTasksIM = new EntityAITasks(this.world.profiler);
		this.targetTasksIM.addTask(0, new EntityAITargetRetaliate(this, EntityLiving.class, 12.0F));
		this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
		this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		this.targetTasksIM.addTask(3, new EntityAITargetOnNoNexusPath(this, EntityIMPigEngy.class, 3.5F));
		this.targetTasksIM.addTask(4, new EntityAIHurtByTarget(this, false));

		this.tasksIM.addTask(1, new EntityAIRallyBehindEntity(this, EntityIMCreeper.class, 4.0F));
		this.tasksIM.addTask(10, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));

		if (this.getTier() == 2) {
			if (this.flavour == 0) {
				this.tasksIM.addTask(3, new EntityAIPounce(this, 0.2F, 1.55F, 18));
			} else if (this.flavour == 1) {
				this.tasksIM.addTask(1, new EntityAILayEgg(this, 1));
			}
		} else {
			if (this.flavour == 1) {
				this.tasksIM.addTask(3, new EntityAIPounce(this, 0.2F, 1.55F, 18));
			}
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote && this.metaChanged != this.getDataManager().get(META_CHANGED)) {
			this.metaChanged = this.getDataManager().get(META_CHANGED);
			// this.setTexture(this.getDataManager().get(TEXTURE));
			// if(this.tier != this.getDataManager().get(TIER))
			// this.setTier(this.getDataManager().get(TIER));
			if (this.flavour != this.getDataManager().get(FLAVOUR))
				this.setFlavour(this.getDataManager().get(FLAVOUR));
		}
	}

	/*
	 * @Override public void moveEntityWithHeading(float x, float z) {
	 */
	@Override
	public void moveRelative(float x, float up, float z, float friction) {
		if (this.isInWater()) {
			double y = this.posY;
			this.moveFlying(x, z, 0.02F);
			this.setVelocity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.8D;
			this.motionY *= 0.8D;
			this.motionZ *= 0.8D;
			this.motionY -= 0.02D;
			if ((this.collidedHorizontally)
					&& (this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
				this.motionY = 0.3D;
		} else if (this.isInLava()) {
			double y = this.posY;
			this.moveFlying(x, z, 0.02F);
			this.setVelocity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if ((this.collidedHorizontally)
					&& (this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6D - this.posY + y, this.motionZ)))
				this.motionY = 0.3D;
		} else {
			float groundFriction = 0.91F;

			if (this.airborneTime == 0) {
				float landMoveSpeed;
				if (this.onGround) {
					groundFriction = 0.546F;
					Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX),
							MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))
							.getBlock();
					if (block != Blocks.AIR) {
						groundFriction = block.slipperiness * 0.91F;
					}
					landMoveSpeed = this.getAIMoveSpeed();
					landMoveSpeed *= 0.162771F / (groundFriction * groundFriction * groundFriction);
				} else {
					landMoveSpeed = this.jumpMovementFactor;
				}

				this.moveFlying(x, z, landMoveSpeed);
			} else {
				groundFriction = 1.0F;
			}

			if (this.isOnLadder()) {
				float maxLadderXZSpeed = 0.15F;
				if (this.motionX < -maxLadderXZSpeed)
					this.motionX = (-maxLadderXZSpeed);
				if (this.motionX > maxLadderXZSpeed)
					this.motionX = maxLadderXZSpeed;
				if (this.motionZ < -maxLadderXZSpeed)
					this.motionZ = (-maxLadderXZSpeed);
				if (this.motionZ > maxLadderXZSpeed) {
					this.motionZ = maxLadderXZSpeed;
				}
				this.fallDistance = 0.0F;
				if (this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}
				if ((this.isSneaking()) && (this.motionY < 0.0D)) {
					this.motionY = 0.0D;
				}
			}
			this.setVelocity(this.motionX, this.motionY, this.motionZ);
			if (((this.collidedHorizontally) || (this.isJumping)) && (this.isOnLadder())) {
				this.motionY = 0.2D;
			}
			float airResistance = 1.0F;
			this.motionY -= this.getGravity();
			this.motionY *= airResistance;
			this.motionX *= groundFriction * airResistance;
			this.motionZ *= groundFriction * airResistance;
		}

		this.prevLimbSwingAmount = this.limbSwingAmount;
		double dX = this.posX - this.prevPosX;
		double dZ = this.posZ - this.prevPosZ;
		float limbEnergy = MathHelper.sqrt(dX * dX + dZ * dZ) * 4.0F;

		if (limbEnergy > 1.0F) {
			limbEnergy = 1.0F;
		}

		this.limbSwingAmount += (limbEnergy - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	@Override
	public IMMoveHelper getMoveHelper() {
		return this.i;
	}

	@Override
	protected void jump() {
		this.motionY = 0.41D;
		this.isAirBorne = true;
	}

	@Override
	public void setTier(int tier) {
		super.setTier(tier);
		this.setAttributes(tier, this.flavour);
		if (this.getTextureId() == 0) {
			if (tier == 1) {
				this.setTexture(0);
			} else if (tier == 2) {
				this.setTexture(this.flavour == 0 ? 1 : 2);
			}
		}
	}

	public void setFlavour(int flavour) {
		this.flavour = flavour;
		this.getDataManager().set(FLAVOUR, flavour);
		this.setAttributes(this.getTier(), flavour);
	}

	@Override
	public String toString() {
		return "IMSpider-T" + this.getTier() + "-" + this.getName();
	}

	@Override
	public double getMountedYOffset() {
		return this.height * 0.75D - 0.5D;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("flavour", this.flavour);
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setFlavour(this.flavour = nbttagcompound.getInteger("flavour"));
	}

	public boolean avoidsBlock(int id) {
		return id == 51 || id == 7;
	}

	public float spiderScaleAmount() {
		if ((this.getTier() == 1) && (this.flavour == 1))
			return 0.35F;
		if ((this.getTier() == 2) && (this.flavour == 1))
			return 1.3F;
		return 1.0F;
	}

	@Override
	public Entity[] getOffspring(Entity partner) {
		if ((this.getTier() == 2) && (this.flavour == 1)) {
			EntityConstruct template = new EntityConstruct(IMEntityType.SPIDER, 1, 0, 1, 1.0F, 0, 0);
			Entity[] offSpring = new Entity[6];
			for (int i = 0; i < offSpring.length; i++) {
				offSpring[i] = mod_invasion.getMobBuilder().createMobFromConstruct(template, this.world,
						this.getNexus());
			}
			return offSpring;
		}

		return null;
	}

	public int getAirborneTime() {
		return this.airborneTime;
	}

	@Override
	public boolean canBePushed() {
		return !this.isOnLadder();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean checkForAdjacentClimbBlock() {
		return this.collidedHorizontally;
	}

	@Override
	public String getSpecies() {
		return "Spider";
	}

	public void setAirborneTime(int time) {
		this.airborneTime = time;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	// TODO: Removed Override annotation
	protected String getLivingSound() {
		return "mob.spider.say";
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		// return "mob.spider.death";
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		int i = (int) Math.ceil(distance - 3.0F);
		if (i > 0) {
			BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY - 0.2D),
					MathHelper.floor(this.posZ));
			IBlockState blockState = this.world.getBlockState(pos);
			if (blockState.getBlock() != Blocks.AIR) {
				// some cheating with sounds, not sure it this will work
				SoundType stepsound = blockState.getBlock().getSoundType(blockState, this.world, pos, this);
				// this.world.playSoundAtEntity(this, stepsound.toString(),
				// stepsound.getVolume() * 0.5F, stepsound.getFrequency() * 0.75F);
				this.playSound(stepsound.getStepSound(), stepsound.getVolume() * 0.5f, stepsound.getPitch() * 0.75f);
			}
		}
	}

	@Override
	protected void dropFewItems(boolean flag, int bonus) {
		if ((this.getTier() == 1) && (this.flavour == 1))
			return;
		super.dropFewItems(flag, bonus);
		if (this.rand.nextFloat() < 0.35F) {
			this.dropItem(Items.STRING, 1);
		}
	}

	private void setAttributes(int tier, int flavour) {
		this.setGravity(0.08F);
		this.setSize(1.4F, 0.9F);
		this.setGender(this.rand.nextInt(2) + 1);
		if (tier == 1) {
			if (flavour == 0) {
				this.setName("Spider");
				// this.setBaseMoveSpeedStat(0.29F);
				this.attackStrength = 3;
				this.pounceTime = 0;
				this.pounceAbility = 0;
				this.maxDestructiveness = 0;
				this.setDestructiveness(0);
				this.setAggroRange(10);
				this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
			} else if (flavour == 1) {
				this.setName("Baby-Spider");
				this.setSize(0.42F, 0.3F);
				// this.setBaseMoveSpeedStat(0.34F);
				this.attackStrength = 1;
				this.pounceTime = 0;
				this.pounceAbility = 1;
				this.maxDestructiveness = 0;
				this.setDestructiveness(0);
				this.setAggroRange(10);
				this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
			}
		} else if (tier == 2) {
			if (flavour == 0) {
				this.setName("Jumping-Spider");
				// this.setBaseMoveSpeedStat(0.3F);
				this.attackStrength = 5;
				this.pounceTime = 0;
				this.pounceAbility = 1;
				this.maxDestructiveness = 0;
				this.setDestructiveness(0);
				this.setAggroRange(18);
				this.setGravity(0.043F);

				this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
			} else if (flavour == 1) {
				this.setName("Mother-Spider");
				this.setGender(2);
				this.setSize(2.8F, 1.8F);
				// this.setBaseMoveSpeedStat(0.22F);
				this.attackStrength = 4;
				this.pounceTime = 0;
				this.pounceAbility = 0;
				this.maxDestructiveness = 0;
				this.setDestructiveness(0);
				this.setAggroRange(18);
				this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
			} else if (flavour == 2) {

			}
		}
	}
}