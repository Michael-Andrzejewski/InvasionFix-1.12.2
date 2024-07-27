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