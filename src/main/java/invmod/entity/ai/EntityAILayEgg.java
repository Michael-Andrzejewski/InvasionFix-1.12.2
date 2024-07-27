package invmod.entity.ai;

import invmod.entity.Goal;
import invmod.entity.ISpawnsOffspring;
import invmod.entity.block.EntityIMEgg;
import invmod.entity.monster.EntityIMMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAILayEgg extends EntityAIBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int EGG_LAY_TIME = 45;
    private static final int INITIAL_EGG_DELAY = 25;
    private static final int NEXT_EGG_DELAY = 230;
    private static final int EGG_HATCH_TIME = 125;

    private final EntityIMMob parentEntity;
    private int eggLayingTimer;
    private boolean isLaying;
    private int eggCount;
    private int cooldownTimer;

    public EntityAILayEgg(EntityIMMob entity, int initialEggCount) {
        this.parentEntity = entity;
        this.eggCount = initialEggCount;
        this.isLaying = false;
        this.cooldownTimer = 0;
    }

    public void addEggs(int eggs) {
        this.eggCount += eggs;
        LOGGER.debug("Added {} eggs. New egg count: {}", eggs, this.eggCount);
    }

    @Override
    public boolean shouldExecute() {
        if (cooldownTimer > 0) {
            cooldownTimer--;
            return false;
        }

        return parentEntity.getAIGoal() == Goal.TARGET_ENTITY 
            && eggCount > 0 
            && parentEntity.getAttackTarget() != null 
            && parentEntity.getEntitySenses().canSee(parentEntity.getAttackTarget());
    }

    @Override
    public void startExecuting() {
        this.eggLayingTimer = INITIAL_EGG_DELAY;
        LOGGER.debug("Starting egg laying process");
    }

    @Override
    public void updateTask() {
        this.eggLayingTimer--;
        if (this.eggLayingTimer <= 0) {
            if (!this.isLaying) {
                this.isLaying = true;
                this.eggLayingTimer = EGG_LAY_TIME;
                this.setMutexBits(1);
                LOGGER.debug("Egg laying started");
            } else {
                this.isLaying = false;
                this.eggCount--;
                this.eggLayingTimer = NEXT_EGG_DELAY;
                this.setMutexBits(0);
                this.layEgg();
                LOGGER.debug("Egg laid. Remaining eggs: {}", this.eggCount);
            }
        }
    }

    private void layEgg() {
        Entity[] contents = null;
        if (this.parentEntity instanceof ISpawnsOffspring) {
            contents = ((ISpawnsOffspring) this.parentEntity).getOffspring(null);
        }
        EntityIMEgg egg = new EntityIMEgg(this.parentEntity, contents, EGG_HATCH_TIME);
        this.parentEntity.world.spawnEntity(egg);
        this.cooldownTimer = NEXT_EGG_DELAY;
    }
}