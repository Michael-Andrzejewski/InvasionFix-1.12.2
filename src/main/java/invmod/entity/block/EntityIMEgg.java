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