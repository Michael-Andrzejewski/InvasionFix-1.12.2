// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for an EntityIMMob to lay eggs within the game.
//  * It extends the EntityAIBase class from Minecraft, providing specific functionality
//  * for egg-laying entities.
//  *
//  * Methods:
//  * - EntityAILayEgg(EntityIMMob entity, int initialEggCount): Constructor that initializes the AI with the parent entity and initial egg count.
//  * - addEggs(int eggs): Adds a specified number of eggs to the current egg count and logs the new total.
//  * - shouldExecute(): Determines if the AI should start executing, based on cooldown, goal status, egg count, and visibility of the target.
//  * - startExecuting(): Prepares the entity to start laying eggs by setting an initial delay and logging the start of the process.
//  * - updateTask(): Manages the countdown for egg-laying, toggles the laying state, and calls layEgg() when it's time to lay an egg.
//  * - layEgg(): Creates an egg entity, potentially with offspring inside, and spawns it into the world, then sets a cooldown until the next egg can be laid.
//  *
//  * Constants:
//  * - EGG_LAY_TIME: The time it takes to lay an egg.
//  * - INITIAL_EGG_DELAY: The initial delay before starting to lay eggs.
//  * - NEXT_EGG_DELAY: The delay between laying eggs.
//  * - EGG_HATCH_TIME: The time it takes for an egg to hatch.
//  *
//  * Fields:
//  * - parentEntity: The entity that is laying the eggs.
//  * - eggLayingTimer: A countdown timer for egg-laying events.
//  * - isLaying: A flag indicating whether the entity is currently laying an egg.
//  * - eggCount: The number of eggs the entity has left to lay.
//  * - cooldownTimer: A timer to manage the cooldown period between laying eggs.
//  */
// ```
// `^`^`^`

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