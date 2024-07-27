// `^`^`^`
// ```java
// /**
//  * This class represents a spider egg entity within the game, which is a non-hostile, neutral entity that can hatch into other entities.
//  * 
//  * - EntityIMEgg(World world): Constructor that initializes a new egg entity in the specified world.
//  * - EntityIMEgg(Entity parent, Entity[] contents, int hatchTime): Overloaded constructor for creating an egg with a parent entity, entities to hatch, and a hatch time.
//  * - getSpecies(): Returns the species identifier for the egg entity.
//  * - getTier(): Returns the tier of the egg entity, which is 0.
//  * - isHostile(): Indicates that the egg entity is not hostile.
//  * - isNeutral(): Indicates that the egg entity is neutral.
//  * - isThreatTo(Entity entity): Determines if the egg entity is a threat to the specified entity, which is true for players.
//  * - getAttackingTarget(): Returns the current target the egg entity is attacking, which is always null.
//  * - onEntityUpdate(): Called each tick to handle egg hatching and entity updates.
//  * - hatch(): Handles the hatching of the egg and spawning of contained entities.
//  * - playHatchSound(): Plays the sound associated with the egg hatching.
//  * - writeEntityToNBT(NBTTagCompound compound): Saves the egg entity's data to NBT.
//  * - readEntityFromNBT(NBTTagCompound compound): Reads and sets the egg entity's data from NBT.
//  * - toString(): Provides a string representation of the egg entity.
//  * 
//  * The class also defines several constants for data parameters, hatch time, and egg dimensions, and maintains state related to the hatching process.
//  */
// ```
// 
// This summary provides an overview of the `EntityIMEgg` class, which is part of a mod for the game Minecraft. The class extends `EntityIMMob` and represents a spider egg entity with specific behaviors and properties, such as hatching into other entities after a certain period. The class includes constructors for creating the egg entity, methods to define its behavior and interactions, and methods to handle its lifecycle and serialization.
// `^`^`^`

package invmod.entity.block;

import invmod.SoundHandler;
import invmod.mod_invasion;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityIMEgg extends EntityIMMob {
    private static final DataParameter<Byte> META_HATCHED = EntityDataManager.<Byte>createKey(EntityIMEgg.class, DataSerializers.BYTE);
    private static final int POST_HATCH_LIFETIME = 40;
    private static final float EGG_WIDTH = 0.5F;
    private static final float EGG_HEIGHT = 0.8F;

    private int hatchTime;
    private int ticks;
    private boolean hatched;
    private Entity parent;
    private Entity[] contents;

    public EntityIMEgg(World world) {
        super(world);
        this.dataManager.register(META_HATCHED, (byte) 0);
    }

    public EntityIMEgg(Entity parent, Entity[] contents, int hatchTime) {
        super(parent.world);
        this.parent = parent;
        this.contents = contents;
        this.hatchTime = hatchTime;
        this.setBurnsInDay(false);
        this.hatched = false;
        this.ticks = 0;
        this.setBaseMoveSpeedStat(0.01F);

        this.dataManager.register(META_HATCHED, (byte) 0);

        this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
        this.setName("Spider Egg");
        this.setGender(0);
        this.setPosition(parent.posX, parent.posY, parent.posZ);
        this.setSize(EGG_WIDTH, EGG_HEIGHT);
    }

    @Override
    public String getSpecies() {
        return "SpiderEgg";
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public boolean isHostile() {
        return false;
    }

    @Override
    public boolean isNeutral() {
        return true;
    }

    @Override
    public boolean isThreatTo(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    @Override
    public Entity getAttackingTarget() {
        return null;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if (!this.world.isRemote) {
            this.ticks++;
            if (this.hatched) {
                if (this.ticks > this.hatchTime + POST_HATCH_LIFETIME)
                    this.setDead();
            } else if (this.ticks > this.hatchTime) {
                this.hatch();
            }
        } else if (!this.hatched && this.dataManager.get(META_HATCHED) == 1) {
            this.playHatchSound();
            this.hatched = true;
        }
    }

    private void hatch() {
        this.playHatchSound();
        this.hatched = true;
        if (!this.world.isRemote) {
            this.dataManager.set(META_HATCHED, (byte) 1);
            if (this.contents != null) {
                for (Entity entity : this.contents) {
                    if (entity != null) {
                        entity.setPosition(this.posX, this.posY, this.posZ);
                        this.world.spawnEntity(entity);
                    }
                }
            }
        }
    }

    private void playHatchSound() {
        SoundEvent hatchSound = SoundHandler.egghatch1;
        if (hatchSound != null) {
            this.playSound(hatchSound, 1.0F, 1.0F);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("HatchTime", this.hatchTime);
        compound.setInteger("Ticks", this.ticks);
        compound.setBoolean("Hatched", this.hatched);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.hatchTime = compound.getInteger("HatchTime");
        this.ticks = compound.getInteger("Ticks");
        this.hatched = compound.getBoolean("Hatched");
    }

    @Override
    public String toString() {
        return "IMSpider-egg";
    }
}