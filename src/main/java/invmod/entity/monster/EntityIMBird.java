// `^`^`^`
// ```java
// /**
//  * This class represents a custom bird entity within the 'invmod' mod, which is an extension to the base flying entity class 'EntityIMFlying'.
//  * The bird entity features specialized animations and behaviors, such as wing flapping, leg movement, and beak opening.
//  *
//  * Key Methods:
//  * - EntityIMBird(World world, TileEntityNexus nexus): Constructor that initializes the bird entity with animations and properties.
//  * - doScreech(), doMeleeSound(), doHurtSound(), doDeathSound(): Methods for playing various sounds, currently empty and can be overridden.
//  * - getWingAnimationState(), getLegSweepProgress(), getLegAnimationState(), getBeakAnimationState(): Accessor methods for retrieving animation states.
//  * - onUpdate(): Overrides the base update method to handle animation updates and synchronization of animation flags between server and client.
//  * - getSpecies(): Returns the species name of the entity.
//  * - getClawsForward(), isAttackingWithWings(), isBeakOpen(): Boolean methods to check the state of the bird's claws, wings, and beak.
//  * - getCarriedEntityYawOffset(): Returns the yaw offset for any entity being carried by the bird.
//  * - attackEntityFrom(DamageSource par1DamageSource, float par2): Handles the bird's response to taking damage, including playing hurt sounds and death logic.
//  * - setBeakState(int timeOpen): Sets the state of the bird's beak for a specified duration.
//  * - onPickedUpEntity(Entity entity): Updates the yaw offset when the bird picks up another entity.
//  * - setClawsForward(boolean flag), setAttackingWithWings(boolean flag), setBeakOpen(boolean flag): Methods to set the state of the bird's claws, wings, and beak.
//  * - updateAITick(): Empty method for future AI updates.
//  * - updateFlapAnimation(), updateLegAnimation(), updateBeakAnimation(): Methods to update the respective animations.
//  * - getTier(): Returns the tier of the bird entity.
//  * - toString(): Provides a string representation of the bird entity, including its tier.
//  *
//  * The class also manages an internal set of animation flags to track the state of the bird's animations and behaviors.
//  */
// ```
// `^`^`^`

package invmod.entity.monster;

import invmod.mod_invasion;
import invmod.client.render.AnimationRegistry;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.client.render.animation.util.FlyState;
import invmod.client.render.animation.util.LegController;
import invmod.client.render.animation.util.MouthController;
import invmod.client.render.animation.util.WingController;
import invmod.entity.MoveState;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityIMBird extends EntityIMFlying {

	// private static final int META_ANIMATION_FLAGS = 26;
	private static final DataParameter<Integer> ANIMATION_FLAGS = EntityDataManager
			.<Integer>createKey(EntityIMBird.class, DataSerializers.VARINT);
	private AnimationState animationRun;
	private AnimationState animationFlap;
	private AnimationState animationBeak;
	private WingController wingController;
	private LegController legController;
	private MouthController beakController;
	private int animationFlags;
	private float carriedEntityYawOffset;
	private int tier;

	public EntityIMBird(World world) {
		this(world, null);
	}

	public EntityIMBird(World world, TileEntityNexus nexus) {
		super(world, nexus);
		this.animationRun = new AnimationState(AnimationRegistry.instance().getAnimation("bird_run"));
		this.animationFlap = new AnimationState(AnimationRegistry.instance().getAnimation("wing_flap_2_piece"));
		this.animationBeak = new AnimationState(AnimationRegistry.instance().getAnimation("bird_beak"));
		this.animationRun.setNewAction(AnimationAction.STAND);
		this.animationFlap.setNewAction(AnimationAction.WINGTUCK);
		this.animationBeak.setNewAction(AnimationAction.MOUTH_CLOSE);
		this.wingController = new WingController(this, this.animationFlap);
		this.legController = new LegController(this, this.animationRun);
		this.beakController = new MouthController(this, this.animationBeak);
		this.setName("Bird");
		this.setGender(2);
		this.setBaseMoveSpeedStat(1.0F);
		this.attackStrength = 1;
		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
		this.animationFlags = 0;
		this.carriedEntityYawOffset = 0.0F;
		this.setGravity(0.025F);
		this.setThrust(0.1F);
		this.setMaxPoweredFlightSpeed(0.5F);
		this.setLiftFactor(0.35F);
		this.setThrustComponentRatioMin(0.0F);
		this.setThrustComponentRatioMax(0.5F);
		this.setMaxTurnForce(this.getGravity() * 8.0F);
		this.setMoveState(MoveState.STANDING);
		this.setFlyState(FlyState.GROUNDED);
		this.tier = 1;

		// this.dataWatcher.addObject(26, Integer.valueOf(0));
		this.getDataManager().register(ANIMATION_FLAGS, Integer.valueOf(0));
	}

	public void doScreech() {
	}

	public void doMeleeSound() {
	}

	protected void doHurtSound() {
	}

	protected void doDeathSound() {
	}

	public AnimationState getWingAnimationState() {
		return this.animationFlap;
	}

	public float getLegSweepProgress() {
		return 1.0F;
	}

	public AnimationState getLegAnimationState() {
		return this.animationRun;
	}

	public AnimationState getBeakAnimationState() {
		return this.animationBeak;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote) {
			this.updateFlapAnimation();
			this.updateLegAnimation();
			this.updateBeakAnimation();
			// this.animationFlags = this.dataWatcher.getWatchableObjectInt(26);
			this.animationFlags = this.getDataManager().get(ANIMATION_FLAGS);
		} else {
			// this.dataWatcher.updateObject(26, Integer.valueOf(this.animationFlags));
			this.getDataManager().set(ANIMATION_FLAGS, this.animationFlags);
		}
	}

	@Override
	public String getSpecies() {
		return "Bird";
	}

	public boolean getClawsForward() {
		return (this.animationFlags & 0x1) > 0;
	}

	public boolean isAttackingWithWings() {
		return (this.animationFlags & 0x2) > 0;
	}

	public boolean isBeakOpen() {
		return (this.animationFlags & 0x4) > 0;
	}

	public float getCarriedEntityYawOffset() {
		return this.carriedEntityYawOffset;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (ForgeHooks.onLivingAttack(this, par1DamageSource, par2))
			return false;
		if (this.isUnkillable())
			return false;
		if (this.world.isRemote)
			return false;
		this.ticksExisted = 0;
		if (this.getHealth() <= 0.0F)
			return false;
		if ((par1DamageSource.isFireDamage()) && (this.isPotionActive(MobEffects.FIRE_RESISTANCE)))
			return false;

//		if (((par1DamageSource == DamageSource.anvil) || (par1DamageSource == DamageSource.fallingBlock)) && (getCurrentItemOrArmor(4) != null))
//		{
//			//getCurrentItemOrArmor(4).damageItem((int)(par2 * 4.0F + this.rand.nextFloat() * par2 * 2.0F), this);
//			par2 *= 0.75F;
//		}

		this.limbSwingAmount = 1.5F;
		boolean flag = true;

		if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
			if (par2 <= this.lastDamage)
				return false;

			this.damageEntity(par1DamageSource, par2 - this.lastDamage);
			this.lastDamage = par2;
			flag = false;
		} else {
			this.lastDamage = par2;
//			this.prevHealth = getHealth();
			this.hurtResistantTime = this.maxHurtResistantTime;
			this.damageEntity(par1DamageSource, par2);
			this.hurtTime = (this.maxHurtTime = 10);
		}

		this.attackedAtYaw = 0.0F;
		Entity entity = par1DamageSource.getTrueSource();

		if (entity != null) {
			if ((entity instanceof EntityLivingBase))
				this.setRevengeTarget((EntityLivingBase) entity);

			if ((entity instanceof EntityPlayer)) {
				this.recentlyHit = 100;
				this.attackingPlayer = ((EntityPlayer) entity);
			} else if ((entity instanceof EntityWolf)) {
				EntityWolf entitywolf = (EntityWolf) entity;

				if (entitywolf.isTamed()) {
					this.recentlyHit = 100;
					this.attackingPlayer = null;
				}
			}
		}

		if (flag) {
			this.world.setEntityState(this, (byte) 2);
			// if (par1DamageSource != DamageSource.DROWN) this.setBeenAttacked();
			if (entity != null) {
				double d0 = entity.posX - this.posX;
				double d1 = entity.posZ - this.posZ;
				for (d1 = entity.posZ - this.posZ; d0 * d0 + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random())
						* 0.01D) {
					d0 = (Math.random() - Math.random()) * 0.01D;
				}
				this.attackedAtYaw = ((float) (Math.atan2(d1, d0) * 180.0D / 3.141592653589793D) - this.rotationYaw);
				this.knockBack(entity, par2, d0, d1);
			} else {
				this.attackedAtYaw = ((int) (Math.random() * 2.0D) * 180);
			}
		}

		if (this.getHealth() <= 0.0F) {
			if (flag)
				this.doDeathSound();
			this.onDeath(par1DamageSource);
		} else if (flag) {
			this.doHurtSound();
		}

		return true;
	}

	protected void setBeakState(int timeOpen) {
		this.beakController.setMouthState(timeOpen);
	}

	protected void onPickedUpEntity(Entity entity) {
		this.carriedEntityYawOffset = (entity.rotationYaw - entity.rotationYaw);
	}

	public void setClawsForward(boolean flag) {
		if ((flag ? 1 : 0) != (this.animationFlags & 0x1))
			this.animationFlags ^= 1;
	}

	public void setAttackingWithWings(boolean flag) {
		if ((flag ? 1 : 0) != (this.animationFlags & 0x2))
			this.animationFlags ^= 2;
	}

	protected void setBeakOpen(boolean flag) {
		if ((flag ? 1 : 0) != (this.animationFlags & 0x4))
			this.animationFlags ^= 4;
	}

	@Override
	protected void updateAITick() {
	}

	protected void updateFlapAnimation() {
		this.wingController.update();
	}

	protected void updateLegAnimation() {
		this.legController.update();
	}

	protected void updateBeakAnimation() {
		this.beakController.update();
	}

	@Override
	public int getTier() {
		return this.tier;
	}

	@Override
	public String toString() {
		return "IMBird T" + this.getTier();
	}
}