// `^`^`^`
// This code defines the `EntityIMThrower` class, which is a custom mob entity for a Minecraft mod. The entity represents a specialized monster with throwing abilities, likely part of an invasion mod where entities attack a central nexus or the player.
// 
// **Class Overview:**
// - `EntityIMThrower` extends `EntityIMMob`, indicating it is a type of mob within the mod.
// - It includes various AI tasks and behaviors, such as swimming, attacking the nexus, throwing boulders, wandering, and targeting players.
// - The class manages its own AI tasks (`tasksIM`) and target tasks (`targetTasksIM`) for behavior control.
// - It has a tier system that adjusts its stats and behaviors based on the tier level.
// 
// **Key Methods:**
// - `initEntityAI()`: Initializes the entity's AI tasks and target tasks.
// - `updateAITick()`: Updates the entity's AI each tick, managing the throw timer and clearing points if necessary.
// - `knockBack(...)`: Handles the knockback effect when the entity is hit, with special behavior for tier 2 entities.
// - `canThrow()`: Checks if the entity is ready to throw again based on the throw timer.
// - `onPathBlocked(...)`: Called when the entity's path is blocked, potentially clearing the obstruction.
// - `setTier(int tier)`: Sets the entity's tier, adjusting its stats and size accordingly.
// - `getSpecies()`, `getGender()`: Returns the species and gender of the entity.
// - `getAmbientSound()`, `getHurtSound(...)`, `getDeathSound()`: Sound event handlers for various entity states.
// - `clearPoint()`: Attempts to clear a blockage in the entity's path.
// - `tryDestroyBlock(...)`: Helper methods to destroy blocks at specified positions.
// 
// The entity is designed to interact with the game world by attacking players and the nexus, clearing its path, and responding to being attacked. The tier system provides a way to scale the difficulty and abilities of the entity. The code also includes sound event handlers for a more immersive experience. Overall, the `EntityIMThrower` class adds a complex mob with specific behaviors to the mod, enhancing the challenge and dynamics of an invasion scenario.
// ```plaintext
// This code appears to be part of a larger file, likely a class for a modded entity in a Minecraft-like game. The entity has specialized behaviors for interacting with blocks and other entities, including attacking and throwing projectiles.
// 
// - The first block of code handles the destruction of a block at a specific position (`pos`). It retrieves the block state, sets the block to air (effectively removing it), and then calls a method to simulate the block being destroyed by a player. If configured, it drops the block as an item in the world. Additionally, it plays an explosion sound effect with a throttle to prevent sound spamming.
// 
// - The `attackEntityAsMob` method allows the entity to attack another entity. If certain conditions are met (a cooldown is not active and the attack strength is above a threshold), it throws a boulder at the target entity. Otherwise, it falls back to the superclass's attack method.
// 
// - The `throwBoulder` method is overloaded with two versions. Both calculate the trajectory for throwing a boulder projectile at a target position, but one version includes a `forced` parameter that alters the behavior if the calculated angle is not suitable. The method creates a new boulder entity, sets its heading, and spawns it in the world.
// 
// - The `throwTNT` method is similar to `throwBoulder`, but it creates and throws a TNT projectile instead.
// 
// - The `dropFewItems` method is overridden to add custom item drops for the entity. It calls the superclass method and then drops an additional custom item defined in the mod.
// 
// - The `toString` method provides a string representation of the entity, including its type and tier.
// 
// Overall, this code is designed to give a modded entity in a game the ability to interact with the environment and other entities by destroying blocks, throwing projectiles, and dropping custom items upon death.
// ```
// `^`^`^`

package invmod.entity.monster;

import invmod.INotifyTask;
import invmod.ModBlocks;
import invmod.ModItems;
import invmod.mod_invasion;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIRandomBoulder;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAIThrowerKillEntity;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.projectile.EntityIMBoulder;
import invmod.entity.projectile.EntityIMPrimedTNT;
import invmod.tileentity.TileEntityNexus;
import invmod.util.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityIMThrower extends EntityIMMob {

	private int throwTime;
	private int punchTimer;
	private boolean clearingPoint;
	private BlockPos pointToClear;
	private INotifyTask clearPointNotifee;

	public EntityIMThrower(World world) {
		this(world, null);
	}

	public EntityIMThrower(World world, TileEntityNexus nexus) {
		super(world, nexus);
		// this.setBaseMoveSpeedStat(0.13F);
		this.attackStrength = 10;
		this.selfDamage = 0;
		this.maxSelfDamage = 0;
		this.experienceValue = 20;
		this.clearingPoint = false;
		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
		this.setName("Thrower");
		this.setDestructiveness(2);
		this.setSize(1.8F, 1.95F);
	}

	@Override
	protected void initEntityAI() {
		this.tasksIM = new EntityAITasks(this.world.profiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		if (this.getTier() == 1) {
			this.tasksIM.addTask(1, new EntityAIThrowerKillEntity(this, EntityPlayer.class, 55, 60.0F, 1.0F));
			this.tasksIM.addTask(1, new EntityAIThrowerKillEntity(this, EntityPlayerMP.class, 55, 60.0F, 1.0F));
		} else {
			this.tasksIM.addTask(1, new EntityAIThrowerKillEntity(this, EntityPlayer.class, 60, 90.0F, 1.5F));
			this.tasksIM.addTask(1, new EntityAIThrowerKillEntity(this, EntityPlayerMP.class, 60, 90.0F, 1.5F));
		}
		this.tasksIM.addTask(2, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(3, new EntityAIRandomBoulder(this, 3));
		this.tasksIM.addTask(4, new EntityAIGoToNexus(this));
		this.tasksIM.addTask(7, new EntityAIWanderIM(this));
		this.tasksIM.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasksIM.addTask(9, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
		this.tasksIM.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
		this.tasksIM.addTask(10, new EntityAILookIdle(this));

		this.targetTasksIM = new EntityAITasks(this.world.profiler);
		this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
		this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		this.targetTasksIM.addTask(3, new EntityAIHurtByTarget(this, false));
	}

	@Override
	public void updateAITick() {
		super.updateAITick();
		this.throwTime -= 1;
		if (this.clearingPoint) {
			if (this.clearPoint()) {
				this.clearingPoint = false;
				if (this.clearPointNotifee != null)
					this.clearPointNotifee.notifyTask(0);
			}
		}
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
		if (this.getTier() == 2)
			return;
		this.isAirBorne = true;
		float f = MathHelper.sqrt(par3 * par3 + par5 * par5);
		float f1 = 0.2F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= par3 / f * f1;
		this.motionY += f1;
		this.motionZ -= par5 / f * f1;

		if (this.motionY > 0.4000000059604645D)
			this.motionY = 0.4000000059604645D;
	}

	public boolean canThrow() {
		return this.throwTime <= 0;
	}

	@Override
	public boolean onPathBlocked(Path path, INotifyTask notifee) {
		if (!path.isFinished()) {
			PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
			this.clearingPoint = true;
			this.clearPointNotifee = notifee;
			this.pointToClear = new BlockPos(node.pos);
			return true;
		}
		return false;
	}

	@Override
	public void setTier(int tier) {
		super.setTier(tier);
		this.selfDamage = 0;
		this.maxSelfDamage = 0;
		this.clearingPoint = false;
		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));

		if (tier == 1) {
			// this.setBaseMoveSpeedStat(0.13F);
			this.attackStrength = 10;
			this.experienceValue = 20;
			this.setName("Thrower");
			this.setDestructiveness(2);
			this.setSize(1.8F, 1.95F);
		} else if (tier == 2) {
			// this.setBaseMoveSpeedStat(0.23F);
			this.attackStrength = 15;
			this.experienceValue = 25;
			this.setName("Big Thrower");
			this.setDestructiveness(4);
			this.setSize(2F, 2F);
		}

		if (this.getTextureId() == 1) {
			this.setTexture(tier);
		}
	}

	/*
	 * @Override public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	 * nbttagcompound.setInteger("tier", this.tier);
	 * super.writeEntityToNBT(nbttagcompound); }
	 * 
	 * @Override public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	 * super.readEntityFromNBT(nbttagcompound);
	 * setTexture(nbttagcompound.getInteger("tier")); this.tier =
	 * nbttagcompound.getInteger("tier"); setTier(this.tier); }
	 */

	@Override
	public String getSpecies() {
		return "Zombie";
	}

	@Override
	public int getGender() {
		return 1;
	}

	// TODO: Removed Override annotation
	/*
	 * protected String getLivingSound() { return "mob.zombie.say"; }
	 */

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	protected boolean clearPoint() {
		if (--this.punchTimer <= 0) {
			// this is a cheat, I should fix it where it get's the point to clear
			int x = this.pointToClear.getX() + 1;
			int y = this.pointToClear.getY();
			int z = this.pointToClear.getZ();
			int mobX = MathHelper.floor(this.posX);
			int mobZ = MathHelper.floor(this.posZ);
			int xOffsetR = 0;
			int zOffsetR = 0;
			int axisX = 0;
			int axisZ = 0;

			float facing = this.rotationYaw % 360.0F;
			if (facing < 0.0F) {
				facing += 360.0F;
			}
			if ((facing >= 45.0F) && (facing < 135.0F)) {
				zOffsetR = -1;
				axisX = -1;
			} else if ((facing >= 135.0F) && (facing < 225.0F)) {
				xOffsetR = -1;
				axisZ = -1;
			} else if ((facing >= 225.0F) && (facing < 315.0F)) {
				zOffsetR = -1;
				axisX = 1;
			} else {
				xOffsetR = -1;
				axisZ = 1;
			}
			IBlockState blockState = this.world.getBlockState(new BlockPos(x, y, z));
			IBlockState blockState0 = this.world.getBlockState(new BlockPos(x, y + 1, z));
			IBlockState blockState1 = this.world.getBlockState(new BlockPos(x + xOffsetR, y, z + zOffsetR));
			IBlockState blockState2 = this.world.getBlockState(new BlockPos(x + xOffsetR, y + 1, z + zOffsetR));

			IBlockState blockState3 = this.world.getBlockState(new BlockPos(x - axisX, y + 1, z - axisZ));
			IBlockState blockState4 = this.world
					.getBlockState(new BlockPos(x - axisX + xOffsetR, y + 1, z - axisZ + zOffsetR));

			IBlockState blockState5 = this.world.getBlockState(new BlockPos(x - 2 * axisX, y + 1, z - 2 * axisZ));
			IBlockState blockState6 = this.world
					.getBlockState(new BlockPos(x - 2 * axisX + xOffsetR, y + 1, z - 2 * axisZ + zOffsetR));

			if (((blockState.getBlock() != null) && (blockState.getMaterial().isSolid()))
					|| ((blockState0.getBlock() != null) && (blockState0.getMaterial().isSolid()))
					|| ((blockState1.getBlock() != null) && (blockState1.getMaterial().isSolid()))
					|| ((blockState2.getBlock() != null) && (blockState2.getMaterial().isSolid()))) {
				this.tryDestroyBlock(x, y, z);
				this.tryDestroyBlock(x, y + 1, z);
				this.tryDestroyBlock(x + xOffsetR, y, z + zOffsetR);
				this.tryDestroyBlock(x + xOffsetR, y + 1, z + zOffsetR);
				this.punchTimer = 160;
			} else if (((blockState3.getBlock() != null) && (blockState3.getMaterial().isSolid()))
					|| ((blockState4.getBlock() != null) && (blockState4.getMaterial().isSolid()))) {
				this.tryDestroyBlock(x - axisX, y + 1, z - axisZ);
				this.tryDestroyBlock(x - axisX + xOffsetR, y + 1, z - axisZ + zOffsetR);
				this.punchTimer = 160;
			} else if (((blockState5.getBlock() != null) && (blockState5.getMaterial().isSolid()))
					|| ((blockState6.getBlock() != null) && (blockState6.getMaterial().isSolid()))) {
				this.tryDestroyBlock(x - 2 * axisX, y + 1, z - 2 * axisZ);
				this.tryDestroyBlock(x - 2 * axisX + xOffsetR, y + 1, z - 2 * axisZ + zOffsetR);
				this.punchTimer = 160;
			} else {
				return true;
			}
		}
		return false;
	}

	protected void tryDestroyBlock(int x, int y, int z) {
		this.tryDestroyBlock(new BlockPos(x, y, z));
	}

	protected void tryDestroyBlock(BlockPos pos) {
		Block block = this.world.getBlockState(pos).getBlock();
		// if ((block != null) && ((isNexusBound()) || (this.j != null))) {
		if ((block != null) || (this.entity != null)) {
			if ((block == /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK)
					&& (pos.equals(this.targetNexus.getPos()))) {
				this.targetNexus.attackNexus(5);
			} else if (block != /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK) {
				IBlockState blockState = this.world.getBlockState(pos);
				this.world.setBlockToAir(pos);
				block.onBlockDestroyedByPlayer(this.world, pos, blockState);

				if (Config.DROP_DESTRUCTED_BLOCKS) {
					block.dropBlockAsItem(this.world, pos, blockState, 0);
				}
				if (this.throttled == 0) {
					this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 4f);
					this.throttled = 5;
				}
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity, int f) {
		if ((this.throwTime <= 0) && (f > 4.0F)) {
			this.throwTime = 120;
			// f is the throwdistance
			if (f < 50.0F) {
				this.throwBoulder(entity.posX, entity.posY + entity.getEyeHeight() - 0.7D, entity.posZ, false);
			}
			return true;
		} else {
			return super.attackEntityAsMob(entity, f);
		}

	}

	protected void throwBoulder(double entityX, double entityY, double entityZ, boolean forced) {
		float launchSpeed = 1.0F;
		double dX = entityX - this.posX;
		double dZ = entityZ - this.posZ;
		double dXY = MathHelper.sqrt(dX * dX + dZ * dZ);

		if ((0.025D * dXY / (launchSpeed * launchSpeed) <= 1.0D)) {
			EntityIMBoulder entityBoulder = new EntityIMBoulder(this.world, this, launchSpeed);
			double dY = entityY - entityBoulder.posY;
			double angle = 0.5D * Math.asin(0.025D * dXY / (launchSpeed * launchSpeed));
			dY += dXY * Math.tan(angle);
			entityBoulder.setBoulderHeading(dX, dY, dZ, launchSpeed, 0.05F);
			this.world.spawnEntity(entityBoulder);
		} else if (forced) {
			EntityIMBoulder entityBoulder = new EntityIMBoulder(this.world, this, launchSpeed);
			double dY = entityY - entityBoulder.posY;
			dY += dXY * Math.tan(0.7853981633974483D);
			entityBoulder.setBoulderHeading(dX, dY, dZ, launchSpeed, 0.05F);
			this.world.spawnEntity(entityBoulder);
		}

	}

	public void throwBoulder(double entityX, double entityY, double entityZ) {
		this.throwTime = 40;
		float launchSpeed = 1.0F;
		double dX = entityX - this.posX;
		double dZ = entityZ - this.posZ;
		double dXY = MathHelper.sqrt(dX * dX + dZ * dZ);
		double p = 0.025D * dXY / (launchSpeed * launchSpeed);
		double angle = p <= 1.0D ? 0.5D * p : 0.7853981633974483D;
		EntityIMBoulder entityBoulder = new EntityIMBoulder(this.world, this, launchSpeed);
		double dY = entityY - entityBoulder.posY;
		dY += dXY * Math.tan(angle);
		entityBoulder.setBoulderHeading(dX, dY, dZ, launchSpeed, 0.05F);
		this.world.spawnEntity(entityBoulder);
	}

	public void throwTNT(double entityX, double entityY, double entityZ) {
		this.throwTime = 40;
		float launchSpeed = 1.0F;
		double dX = entityX - this.posX;
		double dZ = entityZ - this.posZ;
		double dXY = MathHelper.sqrt(dX * dX + dZ * dZ);
		double p = 0.025D * dXY / (launchSpeed * launchSpeed);
		double angle = p <= 1.0D ? 0.5D * p : 0.7853981633974483D;
		EntityIMPrimedTNT entityTNT = new EntityIMPrimedTNT(this.world, this, launchSpeed);
		double dY = entityY - entityTNT.posY;
		dY += dXY * Math.tan(angle);
		entityTNT.setBoulderHeading(dX, dY, dZ, launchSpeed, 0.05F);
		this.world.spawnEntity(entityTNT);
	}

	@Override
	protected void dropFewItems(boolean flag, int bonus) {
		super.dropFewItems(flag, bonus);
		this.entityDropItem(new ItemStack(/* BlocksAndItems.itemSmallRemnants */ModItems.SMALL_REMNANTS, 1), 0.0F);
	}

	@Override
	public String toString() {
		return "IMThrower-T" + this.getTier();
	}

}