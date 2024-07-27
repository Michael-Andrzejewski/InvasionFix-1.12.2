// `^`^`^`
// ```java
// /**
//  * This class represents a proxy entity for spawning night mobs in the game Minecraft, as part of the 'invmod' mod.
//  * It extends EntityLiving, meaning it is a living entity within the game world.
//  *
//  * Constructor:
//  * - EntityIMSpawnProxy(World world): Initializes the proxy entity with the provided world.
//  *
//  * Overridden Methods:
//  * - onEntityUpdate(): Called each tick to update the entity's state. It spawns a set of night mobs at its location
//  *   and then marks itself for removal.
//  * - writeEntityToNBT(NBTTagCompound nbttagcompound): Intentionally left blank, as this proxy entity does not need
//  *   to save any additional data.
//  * - readEntityFromNBT(NBTTagCompound nbttagcompound): Intentionally left blank, as this proxy entity does not need
//  *   to load any additional data.
//  * - getCanSpawnHere(): Determines if the proxy entity can spawn based on darkness conditions and path weight.
//  *
//  * Methods:
//  * - getBlockPathWeight(int i, int j, int k): Returns a weight based on the light brightness at the specified block
//  *   position, influencing mob spawn rates.
//  * - darkEnoughToSpawn(): Checks if the current conditions are dark enough for night mobs to spawn.
//  *
//  * This entity is used to control the spawning of night mobs, ensuring they only appear under the correct conditions
//  * and in appropriate locations. It leverages the game's light level mechanics to make these determinations.
//  */
// package invmod.entity;
// 
// // ... (rest of the imports and class code)
// ```
// `^`^`^`

package invmod.entity;

import invmod.mod_invasion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityIMSpawnProxy extends EntityLiving {

	public EntityIMSpawnProxy(World world) {
		super(world);
	}

	@Override
	public void onEntityUpdate() {
		if (this.world != null) {
			Entity[] entities = mod_invasion.getNightMobSpawns1(this.world);
			for (Entity entity : entities) {
				entity.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				this.world.spawnEntity(entity);
			}
		}
		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	public float getBlockPathWeight(int i, int j, int k) {
		return 0.5F - this.world.getLightBrightness(new BlockPos(i, j, k));
	}

	protected boolean darkEnoughToSpawn() {
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		if (this.world.getLightFor(EnumSkyBlock.SKY, new BlockPos(i, j, k)) > this.rand.nextInt(32)) {
			return false;
		}
		int l = this.world.getBlockLightOpacity(new BlockPos(i, j, k));
		if (this.world.isThundering()) {
			int i1 = this.world.getSkylightSubtracted();
			this.world.setSkylightSubtracted(10);
			l = this.world.getBlockLightOpacity(new BlockPos(i, j, k));
			this.world.setSkylightSubtracted(i1);
		}
		return l <= this.rand.nextInt(8);
	}

	@Override
	public boolean getCanSpawnHere() {
		int i = MathHelper.floor(this.posX);
		int j = MathHelper.floor(this.getEntityBoundingBox().minY);
		int k = MathHelper.floor(this.posZ);
		return (this.darkEnoughToSpawn()) && (super.getCanSpawnHere()) && (this.getBlockPathWeight(i, j, k) >= 0.0F);
	}
}