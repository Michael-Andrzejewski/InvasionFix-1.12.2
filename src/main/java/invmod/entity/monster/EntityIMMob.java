// `^`^`^`
// ```java
// /**
//  * This abstract class, EntityIMMob, extends EntityIMLiving and implements the IMob and SparrowAPI interfaces,
//  * providing a framework for creating custom mob entities for the "Invasion Mod" in Minecraft. It includes various
//  * attributes and behaviors that these mobs can exhibit, such as climbing, jumping, attacking, and responding to
//  * environmental conditions like water and lava.
//  *
//  * Methods:
//  * - EntityIMMob(World world): Constructor that initializes the mob in the given world.
//  * - EntityIMMob(World world, TileEntityNexus nexus): Overloaded constructor that also binds the mob to a Nexus.
//  * - entityInit(): Registers data parameters and initializes entity properties.
//  * - onUpdate(): Called each tick to update the entity's state and synchronize client-server data.
//  * - onEntityUpdate(): Handles air management and checks for adjacent climbable blocks.
//  * - onLivingUpdate(): Manages sunlight damage and other living entity updates.
//  * - attackEntityFrom(DamageSource damagesource, float damage): Processes incoming damage and sets the attacker.
//  * - stunEntity(int ticks): Stuns the entity, preventing movement for a specified number of ticks.
//  * - attackEntityAsMob(Entity entity): Attacks the specified entity using the mob's attack strength.
//  * - attackEntityAsMob(Entity entity, int damageOverride): Overloaded attack method allowing for a custom damage value.
//  * - moveRelative(float strafe, float up, float forward, float friction): Handles movement while swimming or flying.
//  *
//  * The class also includes various attributes for managing the mob's health, attack power, movement, and environmental
//  * interactions. It is designed to be extended by specific mob classes that will define their unique behaviors and
//  * characteristics.
//  */
// ```
// This code appears to be part of an entity class for a modded Minecraft game, specifically for a creature with various behaviors and interactions with the game world. The code manages the entity's movement, health, interactions with blocks, detection of other entities, and saving/loading of entity data. Here's a summary of the methods within the code:
// 
// - `moveFlying`: Adjusts the entity's flying movement based on strafe and forward movement inputs, applying a movement factor and adjusting for the entity's yaw rotation.
// - `onBlockRemoved`: Handles the event when a block is removed near the entity, playing a sound effect and applying a throttle to limit the frequency of sound playback.
// - `canEntityBeDetected`: Determines if another entity can be detected by this entity based on distance and visibility.
// - `findDistanceToNexus`: Calculates the distance from the entity to a 'nexus' point, which is likely a significant location or objective for the entity.
// - `writeEntityToNBT` and `readEntityFromNBT`: Methods for saving and loading the entity's data to and from NBT (Named Binary Tag) format, which is used for storing Minecraft game data.
// - Getter methods (`getPrevRotationRoll`, `getRotationRoll`, etc.): Retrieve various properties of the entity, such as rotation angles and attack range.
// - `setMaxHealth` and `setMaxHealthAndHealth`: Set the maximum health of the entity and optionally set the current health to the same value.
// - `getCanSpawnHere`: Determines if the entity can spawn in the current location based on light levels, ground conditions, and whether the entity is inside a block.
// - `isEntityInOpaqueBlockBeforeSpawn`: Checks if the entity is inside an opaque block before spawning.
// - `getBlockStrength`: Calculates the strength of a block at a given position, which may be used for determining how easily the entity can break it.
// - `getCanClimb`, `getCanDigDown`, `getAggroRange`, `getSenseRange`: Return boolean values or ranges for the entity's abilities, such as climbing, digging, and detection ranges.
// - `getBlockPathWeight`: Used in pathfinding, returns the cost of a path through a block, which may be influenced by light levels.
// - `getAIGoal` and `getPrevAIGoal`: Return the current and previous AI goals of the entity.
// - `getBlockPathCost` and `getPathOptionsFromNode`: Methods used in pathfinding to calculate the cost of a path and to generate path options from a node.
// - `getRenderLabel`: Returns a label used for rendering the entity, possibly for debugging purposes.
// - `getDebugMode`: Returns the debug mode state of the entity.
// - `isHostile`, `isNeutral`, `isThreatTo`, `getAttackingTarget`, `isStupidToAttack`, `doNotVaporize`, `isPredator`, `isPeaceful`, `isPrey`, `isUnkillable`, `isFriendOf`: A series of methods that define the entity's behavior and interactions with other entities, such as whether it is hostile, neutral, or a predator.
// 
// Overall, the code is designed to manage a complex entity's behavior in a Minecraft mod, including movement, health, interactions with the environment, and AI goals.
// ```plaintext
// This code appears to be part of an entity class for a modded Minecraft game, specifically for entities with advanced AI and pathfinding capabilities. The entity has various behaviors and states, such as climbing, swimming, jumping, and interacting with a "nexus" object. Below is a summary of the key methods and their purposes:
// 
// - isNPC(), isPet(), getName(), getGender(), getPetOwner(), getSize(), customStringAndResponse(), getSimplyID(): These methods provide basic entity information and behaviors, such as whether the entity is an NPC or a pet, its name, gender, owner, size, and ID.
// 
// - isNexusBound(), isOnLadder(), isAdjacentClimbBlock(), checkForAdjacentClimbBlock(): These methods determine whether the entity is bound to a nexus, on a ladder, or adjacent to a climbable block, and check for climbable blocks in the entity's vicinity.
// 
// - canSwimHorizontal(), canSwimVertical(), shouldRenderLabel(): These methods define the entity's swimming capabilities and whether its label should be rendered.
// 
// - acquiredByNexus(TileEntityNexus nexus), setDead(), setEntityIndependent(), setBurnsInDay(), setAggroRange(), setSenseRange(), setJumping(), setAdjacentClimbBlock(), setRenderLabel(), setShouldRenderLabel(), setDebugMode(): These methods manage the entity's interactions with a nexus, its life state, independence, day burning behavior, aggression and sensing ranges, jumping state, adjacent climbable block state, label rendering, and debug mode.
// 
// - updateAITasks(), updateAITick(): These methods update the entity's AI tasks and tick, handling navigation, movement, and goal-setting based on the entity's current target or nexus.
// 
// - canDespawn(): Determines if the entity can despawn based on its nexus binding.
// 
// - setRotationRoll(), setRotationYawHeadIM(), setRotationPitchHead(), setAttackRange(): These methods set the entity's rotation and attack range.
// 
// - sunlightDamageTick(), dealFireDamage(), dropFewItems(): These methods handle the entity's damage from sunlight, fire, and item dropping behavior upon death.
// 
// - calcBlockPathCost(), calcPathOptions(), calcPathOptionsVertical(): These methods calculate path costs and options for the entity's movement through the terrain, considering factors like block resistance, swimming, climbing, and digging.
// 
// The code is designed to provide a complex and dynamic behavior pattern for entities, allowing them to navigate the world with a variety of actions and respond to different environmental challenges and player interactions.
// ```
// 
// This code appears to be part of an AI system for a game entity, possibly within a Minecraft-like environment, where the entity navigates through a block-based terrain. The code includes methods for pathfinding, movement, and interaction with the game world.
// 
// - `addAdjacent`: Adds adjacent nodes to the pathfinder if the entity can either climb or if the adjacent block is a ladder, indicating possible paths the entity can take.
// - `getNextLowestSafeYOffset`: Determines the next lowest vertical position the entity can safely move to, considering whether the entity can stand on a block or swim in liquid.
// - `canStandOnBlock`: Checks if a block at a given position is solid and not avoidable, indicating whether the entity can stand on it.
// - `getLightLevelBelow8`: Determines if the light level at the entity's position is below a certain threshold, which could be used for spawning conditions or stealth mechanics.
// - `setAIGoal`, `setPrevAIGoal`, `transitionAIGoal`: Methods for managing the entity's current and previous AI goals, allowing for behavior transitions.
// - `setDestructiveness`: Sets a destructiveness level for the entity, which could influence how it interacts with the environment (e.g., breaking blocks).
// - `setGravity`, `setGroundFriction`: Configures the entity's gravity and ground friction, affecting its movement physics.
// - `setCanClimb`, `setJumpHeight`: Sets the entity's ability to climb and its jump height, modifying its navigation capabilities.
// - `setName`, `setGender`: Assigns a name and gender to the entity, likely for identification or role-playing elements.
// - `onDebugChange`: A placeholder for a method that would be called when a debug-related change occurs.
// - `getBlockStrength`: A static method that calculates the strength of a block based on its explosion resistance and the presence of the same type of blocks adjacent to it.
// 
// Overall, the code is designed to give an entity the ability to navigate a 3D block-based world with various movement capabilities and environmental interactions. It includes methods for pathfinding, adjusting physical properties, and responding to the game world's state.
// `^`^`^`

package invmod.entity.monster;

import invmod.IBlockAccessExtended;
import invmod.ModItems;
import invmod.SparrowAPI;
import invmod.mod_invasion;
import invmod.entity.EntityIMLiving;
import invmod.entity.Goal;
import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.ai.navigator.PathfinderIM;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Coords;
import invmod.util.Distance;
import invmod.util.MathUtil;
import invmod.util.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityIMMob extends EntityIMLiving implements IMob, SparrowAPI {

	private static final DataParameter<Boolean> IS_ADJECENT_CLIMB_BLOCK = EntityDataManager.createKey(EntityIMMob.class,
			DataSerializers.BOOLEAN); // 21
	private static final DataParameter<Boolean> IS_JUMPING = EntityDataManager.createKey(EntityIMMob.class,
			DataSerializers.BOOLEAN); // 22
	private static final DataParameter<Integer> ROTATION = EntityDataManager.createKey(EntityIMMob.class,
			DataSerializers.VARINT); // 24
	private static final DataParameter<String> RENDERLABEL = EntityDataManager.createKey(EntityIMMob.class,
			DataSerializers.STRING); // 25

	protected Goal currentGoal = Goal.NONE;
	protected Goal prevGoal = Goal.NONE;
	protected EntityAITasks tasksIM;
	protected EntityAITasks targetTasksIM;
	private float rotationRoll = 0f;

	// DarthXenon: Not sure what these should be initialized as
	private float rotationYawHeadIM = 0f;
	private float rotationPitchHead = 0f;
	private float prevRotationRoll = 0f;
	private float prevRotationYawHeadIM = 0f;
	private float prevRotationPitchHead = 0f;

	private int debugMode;
	private float airResistance = 0.9995F;
	private float groundFriction = 0.546F;
	private float gravityAcel = 0.08F;
	private float pitchRate = 2f;
	private BlockPos lastBreathExtendPos = BlockPos.ORIGIN;
	private String simplyID = "needID";
	private String name;
	private String renderLabel = "";
	private boolean shouldRenderLabel;
	private int gender = 0;
	private boolean isHostile = true;
	private boolean creatureRetaliates = true;
	protected int attackStrength = 2;
	protected float attackRange = 0f;
	private float maxHealth;
	protected int selfDamage = 2;
	protected int maxSelfDamage = 6;
	protected int maxDestructiveness = 0;
	protected float blockRemoveSpeed = 1f;
	protected boolean floatsInWater = true;
	private boolean canClimb = false;
	private boolean canDig = true;
	private boolean nexusBound;
	private boolean alwaysIndependent = false;
	private boolean burnsInDay;
	private int jumpHeight = 1;
	private int aggroRange;
	private int senseRange;
	private int stunTimer;
	protected int throttled = 0;
	protected int throttled2 = 0;
	protected int pathThrottle = 0;
	protected int destructionTimer = 0;
	protected int flammability = 2;
	protected int destructiveness = 0;
	protected Entity entity;
	/*
	 * protected static final int META_CLIMB_STATE = 20; protected static final byte
	 * META_CLIMBABLE_BLOCK = 21; protected static final byte META_JUMPING = 22;
	 * protected static final byte META_MOVESTATE = 23; protected static final byte
	 * META_ROTATION = 24; protected static final byte META_RENDERLABEL = 25;
	 */

	public EntityIMMob(World world) {
		super(world);
	}

	public EntityIMMob(World world, TileEntityNexus nexus) {
		super(world, nexus);
		this.debugMode = Config.DEBUG ? 1 : 0;
		this.shouldRenderLabel = Config.DEBUG;
		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
		this.isImmuneToFire = false;
		this.experienceValue = 5;
		this.nexusBound = nexus != null;
		this.burnsInDay = nexus != null ? false : Config.NIGHTSPAWNS_MOB_BURN_DURING_DAY;
		this.aggroRange = nexus != null ? 12 : Config.NIGHTSPAWNS_MOB_SIGHTRANGE;
		this.senseRange = nexus != null ? 6 : Config.NIGHTSPAWNS_MOB_SENSERANGE;
		// debugTest
		this.setShouldRenderLabel(this.debugMode == 1);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(IS_ADJECENT_CLIMB_BLOCK, false);
		this.getDataManager().register(IS_JUMPING, false);
		this.getDataManager().register(ROTATION,
				MathUtil.packAnglesDeg(this.rotationRoll, this.rotationYawHeadIM, this.rotationPitchHead, 0.0F));
		this.getDataManager().register(RENDERLABEL, "");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevRotationRoll = this.rotationRoll;
		this.prevRotationYawHeadIM = this.rotationYawHeadIM;
		this.prevRotationPitchHead = this.rotationPitchHead;
		if (this.world.isRemote) {
			// this.moveState =
			// MoveState.values()[this.dataWatcher.getWatchableObjectInt(23)];
			int packedAngles = this.getDataManager().get(ROTATION);
			this.rotationRoll = MathUtil.unpackAnglesDeg_1(packedAngles);
			this.rotationYawHeadIM = MathUtil.unpackAnglesDeg_2(packedAngles);
			this.rotationPitchHead = MathUtil.unpackAnglesDeg_3(packedAngles);
			this.renderLabel = this.getDataManager().get(RENDERLABEL);
		} else {
			int packedAngles = MathUtil.packAnglesDeg(this.rotationRoll, this.rotationYawHeadIM, this.rotationPitchHead,
					0.0F);
			if (packedAngles != this.getDataManager().get(ROTATION))
				this.getDataManager().set(ROTATION, Integer.valueOf(packedAngles));
			if (!this.renderLabel.equals(this.getDataManager().get(RENDERLABEL)))
				this.getDataManager().set(RENDERLABEL, this.renderLabel);
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if (this.world.isRemote) {
			this.isJumping = this.getDataManager().get(IS_JUMPING);
		} else {
			this.setAdjacentClimbBlock(this.checkForAdjacentClimbBlock());
		}

		if (this.getAir() == 190) {
			this.lastBreathExtendPos = this.getPosition();
		} else if (this.getAir() == 0) {
			if (Distance.distanceBetween(this.lastBreathExtendPos, this.getPosition()) > 4.0D) {
				this.lastBreathExtendPos = this.getPosition();
				this.setAir(180);
			}
		}

		// DarthXenon: What is this for?
		// if (this.simplyID == "needID");
	}

	@Override
	public void onLivingUpdate() {
		if (!this.nexusBound) {
			float brightness = this.getBrightness();
			if ((brightness > 0.5F) || (this.posY < 55.0D)) {
				this.ticksExisted += 2;
			}
			if ((this.getBurnsInDay()) && (this.world.isDaytime()) && (!this.world.isRemote)) {
				if ((brightness > 0.5F) && (this.world.canBlockSeeSky(this.getPosition()))
						&& (this.rand.nextFloat() * 30.0F < (brightness - 0.4F) * 2.0F)) {
					this.sunlightDamageTick();
				}
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float damage) {
		if (super.attackEntityFrom(damagesource, damage)) {
			Entity entity = damagesource.getTrueSource();
			// if ((this.riddenByEntity == entity) || (this.ridingEntity == entity)) {
			if (this.getPassengers().contains(entity) || this.getRidingEntity() == entity) {
				return true;
			}
			if (entity != this)
				this.entity = entity;
			return true;
		}
		return false;
	}

	public boolean stunEntity(int ticks) {
		if (this.stunTimer < ticks)
			this.stunTimer = ticks;
		this.motionX = 0.0D;
		this.motionZ = 0.0D;
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
	}

	public boolean attackEntityAsMob(Entity entity, int damageOverride) {
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), damageOverride);
	}

	@Override
	public void moveRelative(float strafe, float up, float forward, float friction) {
		/*
		 * // TODO Auto-generated method stub super.moveRelative(strafe, up, forward,
		 * friction); }
		 * 
		 * @Override public void moveEntityWithHeading(float strafe, float forward) {
		 */
		// super.moveEntityWithHeading(strafe, forward);
		super.moveRelative(strafe, up, forward, friction);
		if (this.isInWater()) {
			double y = this.posY;
			this.moveFlying(strafe, forward, 0.04F);
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
			this.moveFlying(strafe, forward, 0.04F);
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
			float landMoveSpeed;
			if (this.onGround) {
				groundFriction = this.getGroundFriction();
				Block block = this.world
						.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1d, this.posZ))
						.getBlock();
				if (block != Blocks.AIR)
					groundFriction = block.slipperiness * 0.91F;
				landMoveSpeed = this.getAIMoveSpeed();

				landMoveSpeed *= 0.162771F / (groundFriction * groundFriction * groundFriction);
			} else {
				landMoveSpeed = this.jumpMovementFactor;
			}

			this.moveFlying(strafe, forward, landMoveSpeed);

			if (this.isOnLadder()) {
				float maxLadderXZSpeed = 0.15F;
				if (this.motionX < -maxLadderXZSpeed)
					this.motionX = (-maxLadderXZSpeed);
				if (this.motionX > maxLadderXZSpeed)
					this.motionX = maxLadderXZSpeed;
				if (this.motionZ < -maxLadderXZSpeed)
					this.motionZ = (-maxLadderXZSpeed);
				if (this.motionZ > maxLadderXZSpeed)
					this.motionZ = maxLadderXZSpeed;

				this.fallDistance = 0.0F;
				if (this.motionY < -0.15D)
					this.motionY = -0.15D;

				if ((this.isHoldingOntoLadder()) || ((this.isSneaking()) && (this.motionY < 0.0D))) {
					this.motionY = 0.0D;
				} else if ((this.world.isRemote) && (this.isJumping)) {
					this.motionY += 0.04D;
				}
			}
			this.setVelocity(this.motionX, this.motionY, this.motionZ);

			if ((this.collidedHorizontally) && (this.isOnLadder()))
				this.motionY = 0.2D;
			this.motionY -= this.getGravity();
			this.motionY *= this.airResistance;
			this.motionX *= groundFriction * this.airResistance;
			this.motionZ *= groundFriction * this.airResistance;
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

	// TODO: Removed Override annotation
	public void moveFlying(float strafeAmount, float forwardAmount, float movementFactor) {
		float unit = MathHelper.sqrt(strafeAmount * strafeAmount + forwardAmount * forwardAmount);

		if (unit < 0.01F)
			return;
		if (unit < 20.0F)
			unit = 1.0F;

		unit = movementFactor / unit;
		strafeAmount *= unit;
		forwardAmount *= unit;

		float com1 = MathHelper.sin(this.rotationYaw * 3.141593F / 180.0F);
		float com2 = MathHelper.cos(this.rotationYaw * 3.141593F / 180.0F);
		this.motionX += strafeAmount * com2 - forwardAmount * com1;
		this.motionZ += forwardAmount * com2 + strafeAmount * com1;
	}

	/*
	 * // not sure why, but this needed to be removed in order to let the mobs swim
	 * // public boolean handleWaterMovement() { // if (this.floatsInWater) { //
	 * return //
	 * this.world.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.
	 * 0D, // -0.4D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this);
	 * // } // // double vX = this.motionX; // double vY = this.motionY; // double
	 * vZ = this.motionZ; // boolean isInWater = //
	 * this.world.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.
	 * 0D, // -0.4D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this);
	 * // this.motionX = vX; // this.motionY = vY; // this.motionZ = vZ; // return
	 * isInWater; // }
	 */

	public void onBlockRemoved(int x, int y, int z, int id) {
		if (this.getHealth() > this.maxHealth - this.maxSelfDamage) {
			this.attackEntityFrom(DamageSource.GENERIC, this.selfDamage);
		}

		if ((this.throttled == 0) && ((id == 3) || (id == 2) || (id == 12) || (id == 13))) {
			this.playSound(SoundEvents.BLOCK_GRAVEL_STEP, 1.4f, 1f / (this.rand.nextFloat() * 0.6f + 1f));
			this.throttled = 5;
		} else {
			this.playSound(SoundEvents.BLOCK_STONE_STEP, 1.4f, 1f / (this.rand.nextFloat() * 0.6f + 1f));
			this.throttled = 5;
		}
	}

	public boolean canEntityBeDetected(Entity entity) {
		float distance = this.getDistance(entity);
		return (distance <= this.getSenseRange())
				|| ((this.canEntityBeSeen(entity)) && (distance <= this.getAggroRange()));
	}

	public double findDistanceToNexus() {
		if (this.targetNexus == null)
			return Double.MAX_VALUE;
		double x = this.targetNexus.getPos().getX() + 0.5D - this.posX;
		double y = this.targetNexus.getPos().getY() - this.posY + this.height * 0.5D;
		double z = this.targetNexus.getPos().getZ() + 0.5D - this.posZ;
		return Math.sqrt(x * x + y * y + z * z);
	}

	/*
	 * // TODO: Fix This // @Override // public Entity findPlayerToAttack() { //
	 * EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity( // this,
	 * getSenseRange()); // if (entityPlayer != null) { // return entityPlayer; // }
	 * // entityPlayer = this.world.getClosestPlayerToEntity(this, //
	 * getAggroRange()); // if ((entityPlayer != null) &&
	 * (canEntityBeSeen(entityPlayer))) { // return entityPlayer; // } // return
	 * null; // }
	 */

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("alwaysIndependent", this.alwaysIndependent);
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.alwaysIndependent = nbttagcompound.getBoolean("alwaysIndependent");
		if (this.alwaysIndependent) {
			this.setBurnsInDay(Config.NIGHTSPAWNS_MOB_BURN_DURING_DAY);
			this.setAggroRange(Config.NIGHTSPAWNS_MOB_SIGHTRANGE);
			this.setSenseRange(Config.NIGHTSPAWNS_MOB_SENSERANGE);
		}
		super.readEntityFromNBT(nbttagcompound);
	}

	public float getPrevRotationRoll() {
		return this.prevRotationRoll;
	}

	public float getRotationRoll() {
		return this.rotationRoll;
	}

	public float getPrevRotationYawHeadIM() {
		return this.prevRotationYawHeadIM;
	}

	public float getRotationYawHeadIM() {
		return this.rotationYawHeadIM;
	}

	public float getPrevRotationPitchHead() {
		return this.prevRotationPitchHead;
	}

	public float getRotationPitchHead() {
		return this.rotationPitchHead;
	}

	public float getAttackRange() {
		return this.attackRange;
	}

	public void setMaxHealth(float health) {
		this.maxHealth = health;
	}

	public void setMaxHealthAndHealth(float health) {
		this.maxHealth = health;
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
		this.setHealth(health);
	}

	// DarthXenon: Boolean flags are kept separate for debug breakpoint purposes
	@Override
	public boolean getCanSpawnHere() {
		boolean lightFlag = ((this.nexusBound) || (this.getLightLevelBelow8()));
		BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY + 0.5D, this.posZ);
		// boolean onGround =
		// WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND,
		// this.world, pos);
		boolean onGround = this.world.isSideSolid(pos.down(), EnumFacing.UP, false);
		boolean inWall = this.isEntityInOpaqueBlockBeforeSpawn();
		boolean flag = (super.getCanSpawnHere()) && (lightFlag) && (onGround && !inWall);
		return flag;
	}

	public boolean isEntityInOpaqueBlockBeforeSpawn() {
		AxisAlignedBB box = this.getEntityBoundingBox();
		BlockPos min = new BlockPos(box.minX, box.minY, box.minZ);
		BlockPos max = new BlockPos(MathHelper.ceil(box.maxX), MathHelper.ceil(box.maxY), MathHelper.ceil(box.maxZ));
		for (int x = min.getX(); x < max.getX(); x++) {
			for (int y = min.getY(); y < max.getY(); y++) {
				for (int z = min.getZ(); z < max.getZ(); z++) {
					if (this.world.isBlockNormalCube(new BlockPos(x, y, z), false))
						return true;
				}
			}
		}
		return false;
	}

	public int getJumpHeight() {
		return this.jumpHeight;
	}

	public float getBlockStrength(BlockPos pos) {
		return this.getBlockStrength(pos, this.world.getBlockState(pos).getBlock());
	}

	public float getBlockStrength(BlockPos pos, Block block) {
		return getBlockStrength(pos, block, this.world);
	}

	public boolean getCanClimb() {
		return this.canClimb;
	}

	public boolean getCanDigDown() {
		return this.canDig;
	}

	public int getAggroRange() {
		return this.aggroRange;
	}

	public int getSenseRange() {
		return this.senseRange;
	}

	// TODO: Used to have override annotation
	public float getBlockPathWeight(int i, int j, int k) {
		if (this.nexusBound)
			return 0.0F;
		return 0.5F - this.world.getLightBrightness(new BlockPos(i, j, k));
	}

	public boolean getBurnsInDay() {
		return this.burnsInDay;
	}

	public int getDestructiveness() {
		return this.destructiveness;
	}

	public float getPitchRate() {
		return this.pitchRate;
	}

	public float getGravity() {
		return this.gravityAcel;
	}

	public float getAirResistance() {
		return this.airResistance;
	}

	public float getGroundFriction() {
		return this.groundFriction;
	}

	public Goal getAIGoal() {
		return this.currentGoal;
	}

	public Goal getPrevAIGoal() {
		return this.prevGoal;
	}

	// TODO Prevents the entity from spawning with egg; conflict with
	// EntityAISwimming
	/*
	 * @Override public PathNavigate getNavigator() { return this.oldNavAdapter; }
	 */

	@Override
	public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
		return this.calcBlockPathCost(prevNode, node, terrainMap);
	}

	@Override
	public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
		this.calcPathOptions(this.world != null ? this.world : terrainMap, currentNode, pathFinder);
	}

	public String getRenderLabel() {
		return this.renderLabel;
	}

	public int getDebugMode() {
		return this.debugMode;
	}

	@Override
	public boolean isHostile() {
		return this.isHostile;
	}

	@Override
	public boolean isNeutral() {
		return this.creatureRetaliates;
	}

	@Override
	public boolean isThreatTo(Entity entity) {
		return this.isHostile && entity instanceof EntityPlayer;
	}

	@Override
	public Entity getAttackingTarget() {
		return this.getAttackTarget();
	}

	@Override
	public boolean isStupidToAttack() {
		return false;
	}

	@Override
	public boolean doNotVaporize() {
		return false;
	}

	@Override
	public boolean isPredator() {
		return false;
	}

	@Override
	public boolean isPeaceful() {
		return false;
	}

	@Override
	public boolean isPrey() {
		return false;
	}

	@Override
	public boolean isUnkillable() {
		return false;
	}

	@Override
	public boolean isFriendOf(Entity par1entity) {
		return false;
	}

	@Override
	public boolean isNPC() {
		return false;
	}

	@Override
	public int isPet() {
		return 0;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getGender() {
		return this.gender;
	}

	@Override
	public Entity getPetOwner() {
		return null;
	}

	@Override
	public float getSize() {
		return this.height * this.width;
	}

	@Override
	public String customStringAndResponse(String s) {
		return null;
	}

	@Override
	public String getSimplyID() {
		return this.simplyID;
	}

	public boolean isNexusBound() {
		return this.nexusBound;
	}

	@Override
	public boolean isOnLadder() {
		return this.isAdjacentClimbBlock();
	}

	public boolean isAdjacentClimbBlock() {
		return this.getDataManager().get(IS_ADJECENT_CLIMB_BLOCK);
	}

	public boolean checkForAdjacentClimbBlock() {
		BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
		IBlockState blockState = this.world.getBlockState(pos);
		if (blockState == null)
			return false;
		return (blockState.getBlock().isLadder(blockState, this.world, pos, this));
	}

	public boolean canSwimHorizontal() {
		return true;
	}

	public boolean canSwimVertical() {
		return true;
	}

	public boolean shouldRenderLabel() {
		return this.shouldRenderLabel;
	}

	@Override
	public void acquiredByNexus(TileEntityNexus nexus) {
		if ((this.targetNexus == null) && (!this.alwaysIndependent)) {
			this.targetNexus = nexus;
			this.nexusBound = true;
		}
	}

	@Override
	public void setDead() {
		super.setDead();
		if ((this.getHealth() <= 0.0F) && (this.targetNexus != null))
			this.targetNexus.registerMobDied();
	}

	public void setEntityIndependent() {
		this.targetNexus = null;
		this.nexusBound = false;
		this.alwaysIndependent = true;
	}

	public void setBurnsInDay(boolean flag) {
		this.burnsInDay = flag;
	}

	public void setAggroRange(int range) {
		this.aggroRange = range;
	}

	public void setSenseRange(int range) {
		this.senseRange = range;
	}

	@Override
	public void setJumping(boolean flag) {
		super.setJumping(flag);
		if (!this.world.isRemote)
			this.getDataManager().set(IS_JUMPING, flag);
	}

	public void setAdjacentClimbBlock(boolean flag) {
		if (!this.world.isRemote)
			this.getDataManager().set(IS_ADJECENT_CLIMB_BLOCK, flag);
	}

	public void setRenderLabel(String label) {
		this.renderLabel = label;
	}

	public void setShouldRenderLabel(boolean flag) {
		this.shouldRenderLabel = flag;
	}

	public void setDebugMode(int mode) {
		this.debugMode = mode;
		this.onDebugChange();
	}

	@Override
	protected void updateAITasks() {
		this.world.profiler.startSection("Entity IM");
		this.ticksExisted++;
		this.despawnEntity();
		this.getEntitySenses().clearSensingCache();
		this.targetTasksIM.onUpdateTasks();
		this.updateAITick();
		this.tasksIM.onUpdateTasks();
		this.getNavigatorNew().onUpdateNavigation();
		this.getLookHelper().onUpdateLook();
		this.getMoveHelper().onUpdateMoveHelper();
		this.getJumpHelper().doJump();
		this.world.profiler.endSection();
	}

	@Override
	protected void updateAITick() {
		super.updateAITick();
		if (this.getAttackTarget() != null) {
			this.currentGoal = Goal.TARGET_ENTITY;
		} else if (this.targetNexus != null) {
			this.currentGoal = Goal.BREAK_NEXUS;
		} else {
			this.currentGoal = Goal.CHILL;
		}
	}

	@Override
	public boolean canDespawn() {
		return !this.nexusBound;
	}

	public void setRotationRoll(float roll) {
		this.rotationRoll = roll;
	}

	public void setRotationYawHeadIM(float yaw) {
		this.rotationYawHeadIM = yaw;
	}

	public void setRotationPitchHead(float pitch) {
		this.rotationPitchHead = pitch;
	}

	public void setAttackRange(float range) {
		this.attackRange = range;
	}

	// TODO: Fix this
	// @Override
	// protected void attackEntity(Entity entity, float f) {
	// if ((this.attackTime <= 0) && (f < 2.0F)
	// && (entity.getEntityBoundingBox().maxY > this.getEntityBoundingBox().minY)
	// && (entity.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY)) {
	// this.attackTime = 38;
	// attackEntityAsMob(entity);
	// }
	// }

	protected void sunlightDamageTick() {
		if (this.isImmuneToFire) {
			this.damageEntity(DamageSource.GENERIC, 3.0F);
		} else {
			this.setFire(8);
		}
	}

	@Override
	protected void dealFireDamage(int i) {
		super.dealFireDamage(i * this.flammability);
	}

	@Override
	protected void dropFewItems(boolean flag, int amount) {
		if (this.rand.nextInt(4) == 0) {
			this.entityDropItem(new ItemStack(/* BlocksAndItems.itemSmallRemnants */ModItems.SMALL_REMNANTS), 0f);
		}
	}

	protected float calcBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
		float multiplier = 1.0F;
		if ((terrainMap instanceof IBlockAccessExtended)) {
			int mobDensity = ((IBlockAccessExtended) terrainMap).getLayeredData(node.pos) & 0x7;
			multiplier += mobDensity * 3;
		}

		if ((node.pos.y > prevNode.pos.y) && (this.getCollide(terrainMap, node.pos) == 2)) {
			multiplier += 2.0F;
		}

		if (this.blockHasLadder(terrainMap, new BlockPos(node.pos))) {
			multiplier += 5.0F;
		}

		if (node.action == PathAction.SWIM) {
			multiplier *= ((node.pos.y <= prevNode.pos.y)
					&& (!terrainMap.isAirBlock(new BlockPos(node.pos.addVector(0d, 1d, 0d)))) ? 3.0F : 1.0F);
			return prevNode.distanceTo(node) * 1.3F * multiplier;
		}

		Block block = terrainMap.getBlockState(new BlockPos(node.pos)).getBlock();
		return prevNode.distanceTo(node) * (block.getExplosionResistance(null)) * multiplier;
	}

	protected void calcPathOptions(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
		if ((currentNode.pos.y <= 0) || (currentNode.pos.y > 255))
			return;

		this.calcPathOptionsVertical(terrainMap, currentNode, pathFinder);

		if ((currentNode.action == PathAction.DIG) && (!this.canStandAt(terrainMap, new BlockPos(currentNode.pos)))) {
			return;
		}

		int height = this.getJumpHeight();
		for (int i = 1; i <= height; i++) {
			if (this.getCollide(terrainMap, currentNode.pos.addVector(0d, i, 0d)) == 0) {
				height = i - 1;
			}
		}

		int maxFall = 8;
		for (int i = 0; i < 4; i++) {
			if (currentNode.action != PathAction.NONE) {
				if ((i == 0) && (currentNode.action == PathAction.LADDER_UP_NX))
					height = 0;
				if ((i == 1) && (currentNode.action == PathAction.LADDER_UP_PX))
					height = 0;
				if ((i == 2) && (currentNode.action == PathAction.LADDER_UP_NZ))
					height = 0;
				if ((i == 3) && (currentNode.action == PathAction.LADDER_UP_PZ))
					height = 0;
			}
			int yOffset = 0;
			int currentY = MathHelper.floor(currentNode.pos.y) + height;
			boolean passedLevel = false;
			do {
				yOffset = this.getNextLowestSafeYOffset(terrainMap,
						new BlockPos(currentNode.pos.x + Coords.offsetAdjX[i], currentY,
								currentNode.pos.z + Coords.offsetAdjZ[i]),
						maxFall + currentY - MathHelper.floor(currentNode.pos.y));
				if (yOffset > 0)
					break;
				if (yOffset > -maxFall) {
					pathFinder.addNode(new Vec3d(currentNode.pos.x + Coords.offsetAdjX[i], currentY + yOffset + 1,
							currentNode.pos.z + Coords.offsetAdjZ[i]), PathAction.NONE);
				}

				currentY += yOffset - 1;

				if ((!passedLevel) && (currentY <= currentNode.pos.y)) {
					passedLevel = true;
					if (currentY != currentNode.pos.y) {
						this.addAdjacent(terrainMap,
								new BlockPos(currentNode.pos.addVector(Coords.offsetAdjX[i], 0, Coords.offsetAdjZ[i])),
								currentNode, pathFinder);
					}

				}

			}

			while (currentY >= currentNode.pos.y);
		}

		if (this.canSwimHorizontal()) {
			for (int i = 0; i < 4; i++) {
				Vec3d vec = currentNode.pos.addVector(Coords.offsetAdjX[i], 0, Coords.offsetAdjZ[i]);
				if (this.getCollide(terrainMap, vec) == -1)
					pathFinder.addNode(vec, PathAction.SWIM);
			}
		}
	}

	protected void calcPathOptionsVertical(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
		Vec3d vecAbove = currentNode.pos.addVector(0d, 1d, 0d);
		Vec3d vecBelow = currentNode.pos.addVector(0d, -1d, 0d);
		BlockPos posAbove = new BlockPos(vecAbove);
		BlockPos posBelow = new BlockPos(vecBelow);
		int collideAbove = this.getCollide(terrainMap, posAbove);
		int collideBelow = this.getCollide(terrainMap, posBelow);

		if (collideAbove > 0) {
			if (terrainMap.getBlockState(posAbove).getBlock() instanceof BlockLadder) {
				IBlockState blockState = terrainMap.getBlockState(posAbove);
				EnumFacing meta = (EnumFacing) blockState.getProperties().get(BlockLadder.FACING);

				PathAction action;
				switch (meta) {
				case EAST:
					action = PathAction.LADDER_UP_PX;
					break;
				case WEST:
					action = PathAction.LADDER_UP_NX;
					break;
				case NORTH:
					action = PathAction.LADDER_UP_PZ;
					break;
				case SOUTH:
					action = PathAction.LADDER_UP_NZ;
					break;
				default:
					action = PathAction.NONE;
				}

				switch (currentNode.action) {
				case NONE:
					pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), action);
					break;
				case LADDER_UP_PX:
				case LADDER_UP_NX:
				case LADDER_UP_PZ:
				case LADDER_UP_NZ:
					if (action == currentNode.action) {
						pathFinder.addNode(vecAbove, action);
					}
					break;
				default:
					pathFinder.addNode(vecAbove, action);
				}
			} else if (this.getCanClimb()) {
				if (this.isAdjacentSolidBlock(terrainMap, posAbove))
					pathFinder.addNode(vecAbove, PathAction.NONE);
			}
		}

		if (this.getCanDigDown()) {
			if (collideBelow == 2) {
				pathFinder.addNode(vecBelow, PathAction.DIG);
			} else if (collideBelow == 1) {
				int maxFall = 5;
				int yOffset = this.getNextLowestSafeYOffset(terrainMap, posBelow, maxFall);
				if (yOffset <= 0)
					pathFinder.addNode(vecBelow, PathAction.NONE);
			}
		}

		if (this.canSwimVertical()) {
			if (collideBelow == -1)
				pathFinder.addNode(currentNode.pos, PathAction.SWIM);
			if (collideAbove == -1)
				pathFinder.addNode(currentNode.pos, PathAction.SWIM);
		}
	}

	protected void addAdjacent(IBlockAccess terrainMap, BlockPos pos, PathNode currentNode, PathfinderIM pathFinder) {
		this.addAdjacent(terrainMap, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), currentNode, pathFinder);
	}

	protected void addAdjacent(IBlockAccess terrainMap, Vec3d pos, PathNode currentNode, PathfinderIM pathFinder) {
		if (this.getCollide(terrainMap, pos) <= 0)
			return;
		if (this.getCanClimb()) {
			if (this.isAdjacentSolidBlock(terrainMap, new BlockPos(pos)))
				pathFinder.addNode(pos, PathAction.NONE);
		} else if (terrainMap.getBlockState(new BlockPos(pos)).getBlock() == Blocks.LADDER) {
			pathFinder.addNode(pos, PathAction.NONE);
		}
	}

	protected int getNextLowestSafeYOffset(IBlockAccess terrainMap, BlockPos pos, int maxOffsetMagnitude) {
		for (int i = 0; (i + pos.getY() > 0) && (i < maxOffsetMagnitude); i--) {
			boolean flag0 = this.canStandAtAndIsValid(this.world != null ? this.world : terrainMap, pos.up(i)); // if
																												// the
																												// entity
																												// can
																												// stand
																												// on
																												// the
																												// block
			boolean flag1 = this.canSwimHorizontal(); // If the entity can swim
			boolean flag2 = this.getCollide(this.world != null ? this.world : terrainMap, pos.up(i)) == -1; // If the
																											// block is
																											// liquid
			if (flag0 || (flag1 && flag2))
				return i;
		}
		return 1;
	}

	protected boolean canStandOnBlock(IBlockAccess terrainMap, int x, int y, int z) {
		Block block = terrainMap.getBlockState(new BlockPos(x, y, z)).getBlock();
		if ((block != Blocks.AIR) && (!block.isPassable(terrainMap, new BlockPos(x, y, z)))
				&& (!this.avoidsBlock(block))) {
			return true;
		}
		return false;
	}

	protected boolean getLightLevelBelow8() {
		BlockPos blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, blockPos) > this.rand.nextInt(32))
			return false;
		int l = this.world.getBlockLightOpacity(blockPos);

		if (this.world.isThundering()) {
			int i1 = this.world.getSkylightSubtracted();
			this.world.setSkylightSubtracted(10);
			l = this.world.getBlockLightOpacity(blockPos);
			this.world.setSkylightSubtracted(i1);
		}
		return l <= this.rand.nextInt(8);
	}

	protected void setAIGoal(Goal goal) {
		this.currentGoal = goal;
	}

	protected void setPrevAIGoal(Goal goal) {
		this.prevGoal = goal;
	}

	public void transitionAIGoal(Goal newGoal) {
		this.prevGoal = this.currentGoal;
		this.currentGoal = newGoal;
	}

	protected void setDestructiveness(int x) {
		this.destructiveness = x;
	}

	protected void setGravity(float acceleration) {
		this.gravityAcel = acceleration;
	}

	public void setGroundFriction(float frictionCoefficient) {
		this.groundFriction = frictionCoefficient;
	}

	protected void setCanClimb(boolean flag) {
		this.canClimb = flag;
	}

	protected void setJumpHeight(int height) {
		this.jumpHeight = height;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setGender(int gender) {
		this.gender = gender;
	}

	protected void onDebugChange() {
	}

	public static float getBlockStrength(BlockPos pos, Block block, World world) {

		int bonus = 0;
		if (world.getBlockState(pos.down()).getBlock() == block)
			bonus++;
		if (world.getBlockState(pos.up()).getBlock() == block)
			bonus++;
		if (world.getBlockState(pos.west()).getBlock() == block)
			bonus++;
		if (world.getBlockState(pos.east()).getBlock() == block)
			bonus++;
		if (world.getBlockState(pos.north()).getBlock() == block)
			bonus++;
		if (world.getBlockState(pos.south()).getBlock() == block)
			bonus++;

		return block.getExplosionResistance(null) * (1.0F + bonus * 0.1F);
	}

}