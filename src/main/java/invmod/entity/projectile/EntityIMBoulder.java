// `^`^`^`
// ```java
// /**
//  * This code defines the EntityIMBoulder class, which is a custom projectile entity in a Minecraft mod.
//  * The class extends the base Entity class and represents a boulder that can be thrown or launched in the game.
//  *
//  * Constructors:
//  * - EntityIMBoulder(World world): Initializes a new boulder entity with default parameters in the specified world.
//  * - EntityIMBoulder(World world, double x, double y, double z): Initializes a new boulder entity at the given coordinates.
//  * - EntityIMBoulder(World world, EntityLivingBase shooter, float velocity): Initializes a new boulder entity with a specified shooter and velocity.
//  *
//  * Methods:
//  * - entityInit(): Empty method from the base class, typically used for initializing entity data parameters.
//  * - setBoulderHeading(double x, double y, double z, float speed, float variance): Sets the boulder's trajectory with the given direction, speed, and variance.
//  * - setVelocity(double x, double y, double z): Sets the boulder's velocity and updates its rotation based on the new velocity vector.
//  * - onUpdate(): Called each tick to update the boulder's position, handle collisions, and check for impacts with blocks or entities.
//  *
//  * The boulder has properties such as position, inTile (the block it's currently in), inGround (whether it's stuck in the ground), life (ticks until it despawns),
//  * and arrowCritical (whether it will deal critical damage). The boulder can collide with entities, deal damage, and interact with specific blocks like the Nexus block.
//  */
// package invmod.entity.projectile;
// 
// // ... (imports)
// 
// public class EntityIMBoulder extends Entity {
//     // ... (class members and methods)
// }
// ```
// This summary provides an overview of the EntityIMBoulder class, its constructors, methods, and functionality within the context of a Minecraft mod. It describes the purpose of the entity as a projectile boulder and outlines its interactions with the game world, including collision handling and entity impact.
// ```java
// /**
//  * This code represents the behavior and state management of an arrow entity within a game, likely a Minecraft-like environment. It includes methods for handling arrow physics, interactions, and serialization.
//  *
//  * - The code checks the 'mobGriefing' game rule to determine if the arrow can cause an explosion that alters the environment.
//  * - It handles the arrow's critical effect, spawning critical hit particles if the arrow is critical.
//  * - The arrow's position and rotation are updated based on its motion, with adjustments for yaw and pitch to ensure smooth transitions.
//  * - Water interaction is simulated, reducing the arrow's speed and spawning water bubble particles.
//  * - Air resistance and gravity are applied to the arrow's motion.
//  * - The arrow's position is updated based on its motion.
//  * - Serialization methods `writeEntityToNBT` and `readEntityFromNBT` are provided to save and load the arrow's state to and from NBT (Named Binary Tag) format, which is commonly used in Minecraft for storing entity and block data.
//  * - The `onCollideWithPlayer` method is stubbed out, indicating a placeholder for future player collision handling.
//  * - The `getFlightTime` method returns the duration the arrow has been in the air.
//  *
//  * The commented-out code suggests previous versions of the code handled block destruction and item dropping, which have been replaced by an explosion mechanic. The `getShadowSize` method is also commented out, indicating that the arrow may not cast a shadow in this version of the code.
//  */
// ```
// `^`^`^`

package invmod.entity.projectile;

import java.util.List;

import invmod.ModBlocks;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityIMBoulder extends Entity {
	private int xTile;
	private int yTile;
	private int zTile;
	private Block inTile;
	private int inData;
	private boolean inGround;
	private int life;
	public boolean doesArrowBelongToPlayer;
	public int arrowShake;
	public EntityLivingBase shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	public boolean arrowCritical;

	public EntityIMBoulder(World world) {
		super(world);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = Blocks.AIR;
		this.inData = 0;
		this.life = 60;
		this.inGround = false;
		this.doesArrowBelongToPlayer = false;
		this.arrowShake = 0;
		this.ticksInAir = 0;
		this.arrowCritical = false;
		this.setSize(0.5F, 0.5F);
	}

	public EntityIMBoulder(World world, double d, double d1, double d2) {
		super(world);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = Blocks.AIR;
		this.inData = 0;
		this.life = 60;
		this.inGround = false;
		this.doesArrowBelongToPlayer = false;
		this.arrowShake = 0;
		this.ticksInAir = 0;
		this.arrowCritical = false;
		this.setSize(0.5F, 0.5F);
		this.setPosition(d, d1, d2);
	}

	public EntityIMBoulder(World world, EntityLivingBase entityliving, float f) {
		super(world);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.inTile = Blocks.AIR;
		this.inData = 0;
		this.life = 60;
		this.inGround = false;
		this.doesArrowBelongToPlayer = false;
		this.arrowShake = 0;
		this.ticksInAir = 0;
		this.arrowCritical = false;
		this.shootingEntity = entityliving;
		this.doesArrowBelongToPlayer = (entityliving instanceof EntityPlayer);
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ,
				entityliving.rotationYaw, entityliving.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		this.posY -= 0.1D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
		this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F)
				* MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F));
		this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F));
		this.setBoulderHeading(this.motionX, this.motionY, this.motionZ, f, 1.0F);
	}

	@Override
	protected void entityInit() {
	}

	public void setBoulderHeading(double x, double y, double z, float speed, float variance) {
		float distance = MathHelper.sqrt(x * x + y * y + z * z);
		x /= distance;
		y /= distance;
		z /= distance;

		x += this.rand.nextGaussian() * variance;
		y += this.rand.nextGaussian() * variance;
		z += this.rand.nextGaussian() * variance;
		x *= speed;
		y *= speed;
		z *= speed;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float xzDistance = MathHelper.sqrt(x * x + z * z);
		this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / 3.141592653589793D));
		this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(y, xzDistance) * 180.0D
				/ 3.141592653589793D));
		this.ticksInGround = 0;
	}

	@Override
	public void setVelocity(double d, double d1, double d2) {
		this.motionX = d;
		this.motionY = d1;
		this.motionZ = d2;
		if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F)) {
			float f = MathHelper.sqrt(d * d + d2 * d2);
			this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(d, d2) * 180.0D / 3.141592741012573D));
			this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(d1, f) * 180.0D / 3.141592741012573D));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F)) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
					/ Math.PI));
			this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI));
		}

		IBlockState blockState = this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile));
		if (blockState.getBlock() != Blocks.AIR) {
			// block.setBlockBoundsBasedOnState(this.world, new BlockPos(this.xTile,
			// this.yTile, this.zTile));
			// AxisAlignedBB axisalignedbb =
			// block.getSelectedBoundingBox(this.world.getBlockState(new
			// BlockPos(this.xTile, this.yTile, this.zTile)),this.world, new
			// BlockPos(this.xTile, this.yTile, this.zTile));
			AxisAlignedBB axisalignedbb = blockState.getBlock().getBoundingBox(blockState, this.world,
					new BlockPos(this.xTile, this.yTile, this.zTile));
			if ((axisalignedbb != null) && (axisalignedbb.contains(new Vec3d(this.posX, this.posY, this.posZ)))) {
				this.inGround = true;
			}

		}

		if ((this.inGround) || (this.life-- <= 0)) {
			this.setDead();
			return;
		}

		this.ticksInAir += 1;

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		// after update required one less vec, chose false, could also be true
		RayTraceResult rtr0 = this.world.rayTraceBlocks(vec3d, vec3d1, false);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (rtr0 != null)
			vec3d1 = new Vec3d(rtr0.hitVec.x, rtr0.hitVec.y, rtr0.hitVec.z);

		Entity entity = null;
		List list = this.world.getEntitiesWithinAABBExcludingEntity(this,
				this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;
		for (int l = 0; l < list.size(); l++) {
			Entity entity1 = (Entity) list.get(l);
			if ((entity1.canBeCollidedWith()) && ((entity1 != this.shootingEntity) || (this.ticksInAir >= 5))) {
				float f5 = 0.3F;
				AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f5, f5, f5);
				RayTraceResult rtr = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
				if (rtr != null) {
					double d1 = vec3d.distanceTo(rtr.hitVec);
					if ((d1 < d) || (d == 0.0D)) {
						entity = entity1;
						d = d1;
					}
				}
			}
		}
		if (entity != null)
			rtr0 = new RayTraceResult(entity);
		if (rtr0 != null) {
			if (rtr0.entityHit != null) {
				int damage = (int) (Math.max(this.ticksInAir / 20.0F, 1.0F) * 6.0F);
				if (damage > 14)
					damage = 14;
				if (rtr0.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), damage)) {
					if ((rtr0.entityHit instanceof EntityLiving)) {
						if (!this.world.isRemote) {
							EntityLiving entityLiving = (EntityLiving) rtr0.entityHit;
							entityLiving.setArrowCountInEntity(entityLiving.getArrowCountInEntity() + 1);
						}
					}
					// this.world.playSoundAtEntity(this, "random.explode", 1.0F, 0.9F /
					// (this.rand.nextFloat() * 0.2F + 0.9F));
					this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f,
							0.9f / (this.rand.nextFloat() * 0.2f + 0.9f));
					this.setDead();
				}
			} else {
				this.xTile = rtr0.getBlockPos().getX();
				this.yTile = rtr0.getBlockPos().getY();
				this.zTile = rtr0.getBlockPos().getZ();
				this.inTile = this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock();
//				this.inData = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
				this.motionX = ((float) (rtr0.hitVec.x - this.posX));
				this.motionY = ((float) (rtr0.hitVec.y - this.posY));
				this.motionZ = ((float) (rtr0.hitVec.z - this.posZ));
				float f2 = MathHelper
						.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / f2 * 0.05D;
				this.posY -= this.motionY / f2 * 0.05D;
				this.posZ -= this.motionZ / f2 * 0.05D;
				// this.world.playSoundAtEntity(this, "random.explode", 1.0F, 0.9F /
				// (this.rand.nextFloat() * 0.2F + 0.9F));
				this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1f, 0.9f / (this.rand.nextFloat() * 0.2f + 0.9f));
				this.inGround = true;
				this.arrowCritical = false;

				Block block2 = this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock();
				if (block2 == /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK) {
					TileEntityNexus tileEntityNexus = (TileEntityNexus) this.world
							.getTileEntity(new BlockPos(this.xTile, this.yTile, this.zTile));
					if (tileEntityNexus != null) {
						tileEntityNexus.attackNexus(2);
					}
				} else if (block2 != Blocks.BEDROCK) {
					if ((block2 != null) && (block2 != /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK)
							&& (block2 != Blocks.CHEST)) {
						// check if mobgriefing is enabled
						boolean mobgriefing = this.world.getGameRules().getBoolean("mobGriefing");

						// int meta = this.world.getBlockMetadata(this.xTile,
						// this.yTile, this.zTile);
						// block=Blocks.air;
						// block.onBlockDestroyedByPlayer(this.world,
						// this.xTile, this.yTile, this.zTile, meta);
						// block.dropBlockAsItem(this.world, this.xTile,
						// this.yTile, this.zTile, meta, 0);
						if (!this.world.isRemote) {
							// this.world.createExplosion(null, this.xTile,
							// this.yTile, this.zTile, 0.5F, true);

							Explosion explosion = new Explosion(this.world, this, this.xTile, this.yTile, this.zTile,
									2.0F, false, mobgriefing);

							explosion.doExplosionA();
							explosion.doExplosionB(false);
						}

					}

				}
			}

		}

		if (this.arrowCritical) {
			for (int i1 = 0; i1 < 4; i1++) {
				this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * i1 / 4.0D,
						this.posY + this.motionY * i1 / 4.0D, this.posZ + this.motionZ * i1 / 4.0D, -this.motionX,
						-this.motionY + 0.2D, -this.motionZ);
			}

		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;

		float xyVelocity = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = ((float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
		for (this.rotationPitch = ((float) (Math.atan2(this.motionY, xyVelocity) * 180.0D
				/ 3.141592653589793D)); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			;
		while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			this.prevRotationPitch += 360.0F;
		while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			this.prevRotationYaw -= 360.0F;
		while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			this.prevRotationYaw += 360.0F;
		this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
		this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
		float airResistance = 1.0F;
		float gravityAcel = 0.025F;
		if (this.isInWater()) {
			for (int k1 = 0; k1 < 4; k1++) {
				float f7 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f7,
						this.posY - this.motionY * f7, this.posZ - this.motionZ * f7, this.motionX, this.motionY,
						this.motionZ);
			}

			airResistance = 0.8F;
		}
		this.motionX *= airResistance;
		this.motionY *= airResistance;
		this.motionZ *= airResistance;
		this.motionY -= gravityAcel;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.xTile);
		nbttagcompound.setShort("yTile", (short) this.yTile);
		nbttagcompound.setShort("zTile", (short) this.zTile);
		nbttagcompound.setByte("inTile", (byte) (Block.getIdFromBlock(this.inTile)));
		nbttagcompound.setByte("inData", (byte) this.inData);
		nbttagcompound.setByte("shake", (byte) this.arrowShake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		nbttagcompound.setBoolean("player", this.doesArrowBelongToPlayer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		this.xTile = nbttagcompound.getShort("xTile");
		this.yTile = nbttagcompound.getShort("yTile");
		this.zTile = nbttagcompound.getShort("zTile");
		this.inTile = Block.getBlockById((nbttagcompound.getByte("inTile") & 0xFF));
		this.inData = (nbttagcompound.getByte("inData") & 0xFF);
		this.arrowShake = (nbttagcompound.getByte("shake") & 0xFF);
		this.inGround = (nbttagcompound.getByte("inGround") == 1);
		this.doesArrowBelongToPlayer = nbttagcompound.getBoolean("player");
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityplayer) {
		if (this.world.isRemote)
			;
	}

//	@Override
//	public float getShadowSize() {
//		return 0.0F;
//	}

	public int getFlightTime() {
		return this.ticksInAir;
	}
}