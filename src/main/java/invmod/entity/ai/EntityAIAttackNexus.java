// `^`^`^`
// ```java
// /**
//  * This class defines the AI behavior for an in-game mob entity to attack a Nexus block within the game.
//  * The AI is designed for entities of type EntityIMMob and specifically handles the attack logic for EntityIMZombie.
//  *
//  * Methods:
//  * - EntityAIAttackNexus(EntityIMMob entity): Constructor that initializes the AI with the given entity and sets mutex bits for task execution.
//  * - shouldExecute(): Determines if the AI task should start executing by checking attack cooldown and proximity to the Nexus.
//  * - startExecuting(): Resets the attack cooldown and marks the entity as not having attacked yet.
//  * - shouldContinueExecuting(): Checks if the entity should continue attacking the Nexus.
//  * - updateTask(): Handles the attack logic, including animation updates for zombies and dealing damage to the Nexus.
//  * - resetTask(): Resets the attacked flag to allow for future attacks.
//  * - isNexusInRange(): Checks if a Nexus block is within the attack range of the entity.
//  * - isCorrectNexus(BlockPos pos): Validates that the Nexus block at the specified position is the correct target for the entity.
//  *
//  * Constants:
//  * - ATTACK_COOLDOWN: The cooldown period between attacks.
//  * - ATTACK_RANGE: The range within which the entity can attack the Nexus.
//  *
//  * The AI task is executed within the game loop, allowing the entity to periodically attempt to attack the Nexus when conditions are met.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.ModBlocks;
import invmod.entity.Goal;
import invmod.entity.monster.EntityIMMob;
import invmod.entity.monster.EntityIMZombie;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIAttackNexus extends EntityAIBase {
    private EntityIMMob theEntity;
    private boolean attacked;
    private int attackTime;
    private static final int ATTACK_COOLDOWN = 40;
    private static final double ATTACK_RANGE = 4.0D;

    public EntityAIAttackNexus(EntityIMMob entity) {
        this.theEntity = entity;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if (this.attackTime > 0) {
            this.attackTime--;
            return false;
        }

        if (this.theEntity.getAIGoal() != Goal.BREAK_NEXUS || this.theEntity.findDistanceToNexus() > ATTACK_RANGE) {
            return false;
        }

        return isNexusInRange();
    }

    @Override
    public void startExecuting() {
        this.attackTime = ATTACK_COOLDOWN;
        this.attacked = false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.attacked && isNexusInRange();
    }

    @Override
    public void updateTask() {
        if (this.attackTime <= 0 && isNexusInRange()) {
            if (this.theEntity instanceof EntityIMZombie) {
                ((EntityIMZombie) this.theEntity).updateAnimation(true);
            }
            TileEntityNexus nexus = this.theEntity.getNexus();
            if (nexus != null) {
                nexus.attackNexus(this.theEntity.getAttackDamage());
            }
            this.attacked = true;
            this.attackTime = ATTACK_COOLDOWN;
        }
    }

    @Override
    public void resetTask() {
        this.attacked = false;
    }

    private boolean isNexusInRange() {
        BlockPos entityPos = this.theEntity.getPosition();
        BlockPos size = this.theEntity.getCollideSize();
        AxisAlignedBB searchBox = new AxisAlignedBB(
            entityPos.add(-1, -1, -1),
            entityPos.add(size.getX() + 1, size.getY() + 1, size.getZ() + 1)
        );

        for (BlockPos pos : BlockPos.getAllInBox(searchBox.minX, searchBox.minY, searchBox.minZ,
                                                 searchBox.maxX, searchBox.maxY, searchBox.maxZ)) {
            if (this.theEntity.world.getBlockState(pos).getBlock() == ModBlocks.NEXUS_BLOCK) {
                if (isCorrectNexus(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCorrectNexus(BlockPos pos) {
        TileEntityNexus nexus = (TileEntityNexus) this.theEntity.world.getTileEntity(pos);
        return nexus != null && nexus == this.theEntity.getNexus();
    }
}