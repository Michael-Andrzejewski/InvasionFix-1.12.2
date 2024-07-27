// `^`^`^`
// ```java
// /**
//  * This class represents a custom primed TNT entity with projectile-like behavior in Minecraft.
//  * It extends the EntityTNTPrimed class and is designed to be launched by entities, potentially causing damage on impact.
//  *
//  * Constructors:
//  * - EntityIMPrimedTNT(World): Initializes the entity with default parameters in the specified world.
//  * - EntityIMPrimedTNT(World, double, double, double): Initializes the entity at a specific location.
//  * - EntityIMPrimedTNT(World, EntityLivingBase, float): Initializes the entity with a shooting entity and velocity.
//  *
//  * Methods:
//  * - onCollideWithPlayer(EntityPlayer): Handles collision with a player, currently empty.
//  * - writeEntityToNBT(NBTTagCompound): Saves the entity's data to NBT.
//  * - entityInit(): Empty method, required by the superclass.
//  * - setBoulderHeading(double, double, double, float, float): Sets the entity's heading with specified speed and variance.
//  * - setVelocity(double, double, double): Sets the entity's velocity and updates its rotation based on the new velocity.
//  * - onUpdate(): Called each tick to update the entity's state, handling movement, collision detection, and entity impact.
//  *
//  * The class also contains several fields to track the entity's state, such as position, whether it's in the ground,
//  * the entity that shot it, and whether it was shot by a player. The entity has a limited lifespan and will set itself
//  * as dead when its life reaches zero or when it impacts a block or another entity.
//  */
// ```
// ```java
// /**
//  * This code appears to be part of a custom entity class for a projectile, possibly an arrow, in a modded Minecraft game.
//  * The code manages the behavior of the projectile when it hits a target, interacts with blocks, and its physics while in flight.
//  *
//  * Key methods and their purposes:
//  *
//  * - The unnamed method (possibly an overridden method like 'onUpdate' or 'onImpact') handles the projectile's behavior upon collision.
//  *   It sets the projectile as in the ground, checks if it hits a special block (NEXUS_BLOCK), and if so, triggers an attack on the Nexus.
//  *   If it hits a block other than bedrock or a chest, it checks the 'mobGriefing' game rule and creates an explosion at the impact location.
//  *   It also handles the spawning of critical hit particles if the arrow is critical.
//  *
//  * - 'readEntityFromNBT' is used to read the projectile's data from an NBTTagCompound, restoring its state from saved data.
//  *   This includes its position, the block it's in, whether it's in the ground, and if it belongs to a player.
//  *
//  * - 'getFlightTime' returns the number of ticks the projectile has been in the air, which could be used for various calculations,
//  *   such as drop-off or damage scaling.
//  *
//  * The code also includes physics calculations for the projectile's motion, adjusting its position, rotation, and velocity based on air resistance and gravity.
//  * It handles the spawning of water bubble particles if the projectile is underwater, indicating it also accounts for different environments.
//  *
//  * Note: Some parts of the code are commented out, suggesting they may be placeholders or features that are disabled or in development.
//  */
// ```
// `^`^`^`

package invmod.entity.projectile;

import java.util.List;

import invmod.ModBlocks;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
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

public class EntityIMPrimedTNT extends EntityTNTPrimed {

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

	public EntityIMPrimedTNT(World par1World) {
		super(par1World);
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
		this.setSize(1.0F, 1.0F);
	}

	public EntityIMPrimedTNT(World world, double d, double d1, double d2) {
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
		this.setSize(1.0F, 1.0F);
		this.setPosition(d, d1, d2);
	}

	public EntityIMPrimedTNT(World world, EntityLivingBase entityliving, float f) {
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
	public void onCollideWithPlayer(EntityPlayer entityplayer) {
		if (this.world.isRemote)
			;
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
		// super.onUpdate();
		if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F)) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = (this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
					/ Math.PI));
			this.prevRotationPitch = (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI));
		}

		// TODO Fix this
		/*
		 * Block block = this.world.getBlockState(new BlockPos(this.xTile, this.yTile,
		 * this.zTile)).getBlock(); if (block != Blocks.AIR){
		 * block.setBlockBoundsBasedOnState(this.world, new BlockPos(this.xTile,
		 * this.yTile, this.zTile)); AxisAlignedBB axisalignedbb =
		 * block.getSelectedBoundingBox(this.world, new BlockPos(this.xTile, this.yTile,
		 * this.zTile)); if ((axisalignedbb != null) && (axisalignedbb.isVecInside(new
		 * Vec3d(this.posX, this.posY, this.posZ)))){ this.inGround = true; }
		 * 
		 * }
		 */

		if ((this.inGround) || (this.life-- <= 0)) {
			this.setDead();
			return;
		}

		this.ticksInAir += 1;

		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, vec3d1, false);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if (movingobjectposition != null) {
			vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y,
					movingobjectposition.hitVec.z);
		}

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
		if (entity != null) {
			movingobjectposition = new RayTraceResult(entity);
		}
		if (movingobjectposition != null) {
			if (movingobjectposition.entityHit != null) {
				int damage = (int) (Math.max(this.ticksInAir / 20.0F, 1.0F) * 7.0F);
				if (damage > 18)
					damage = 18;
				if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity),
						damage)) {
					if ((movingobjectposition.entityHit instanceof EntityLiving)) {
						if (!this.world.isRemote) {
							EntityLiving entityLiving = (EntityLiving) movingobjectposition.entityHit;
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
				this.xTile = movingobjectposition.getBlockPos().getX();
				this.yTile = movingobjectposition.getBlockPos().getY();
				this.zTile = movingobjectposition.getBlockPos().getZ();
				this.inTile = this.world.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock();
//					this.inData = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
				this.motionX = ((float) (movingobjectposition.hitVec.x - this.posX));
				this.motionY = ((float) (movingobjectposition.hitVec.y - this.posY));
				this.motionZ = ((float) (movingobjectposition.hitVec.z - this.posZ));
				float f2 = MathHelper
						.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / f2 * 0.05D;
				this.posY -= this.motionY / f2 * 0.05D;
				this.posZ -= this.motionZ / f2 * 0.05D;
				// this.world.playSoundAtEntity(this, "random.explode", 1.5F, 0.9F /
				// (this.rand.nextFloat() * 0.2F + 0.9F));
				this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 0.9f / (this.rand.nextFloat() * 0.2f + 0.9f));
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

//					if(!this.world.isRemote)
//					{
						// this.world.createExplosion(null, this.xTile, this.yTile, this.zTile, 1.0F,
						// true);

						Explosion explosion = new Explosion(this.world, this, this.xTile, this.yTile, this.zTile, 4.0F,
								false, mobgriefing);
						explosion.doExplosionA();
						explosion.doExplosionB(true);
						// ExplosionUtil.doExplosionB(this.world,explosion,false);
//					}

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

//		@Override
//		public float getShadowSize()
//		{
//			return 0.0F;
//		}

	public int getFlightTime() {
		return this.ticksInAir;
	}
}
