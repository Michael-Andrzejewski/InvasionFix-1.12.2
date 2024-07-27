// `^`^`^`
// ```java
// /**
//  * This AI behavior class, EntityAIRallyBehindEntity, is designed for entities in the 'invmod' mod to follow and rally behind a leader entity.
//  * It extends the EntityAIFollowEntity class, inheriting its following behavior and adding conditions for rallying.
//  *
//  * Constructors:
//  * - EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader): Initializes the AI with a default follow distance.
//  * - EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader, float followDistance): Initializes the AI with a specified follow distance.
//  *
//  * Core Methods:
//  * - shouldExecute(): Determines if the AI should start executing, checking if the entity is ready to rally and if the base follow condition is met.
//  * - shouldContinueExecuting(): Checks if the AI should continue executing, ensuring the entity remains ready to rally and the base follow condition persists.
//  * - updateTask(): Updates the AI task, invoking the base update method and performing a rally action if the entity is ready to rally.
//  * - rally(T leader): A protected method intended to define the rallying behavior towards the specified leader entity.
//  *
//  * The class relies on the entity's readiness to rally and delegates to the superclass for following behavior. The actual rallying logic, presumably involving moving towards a leader when they are a martyr, is hinted at but not implemented in the provided code.
//  */
// ```
// `^`^`^`

package invmod.entity.ai;

import invmod.entity.EntityIMLiving;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIRallyBehindEntity<T extends EntityLivingBase, ILeader> extends EntityAIFollowEntity<T> {

	private static final float DEFAULT_FOLLOW_DISTANCE = 5.0F;

	public EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader) {
		this(entity, leader, 5.0F);
	}

	public EntityAIRallyBehindEntity(EntityIMLiving entity, Class<T> leader, float followDistance) {
		super(entity, leader, followDistance);
	}

	@Override
	public boolean shouldExecute() {
		return (this.getEntity().readyToRally()) && (super.shouldExecute());
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (this.getEntity().readyToRally()) && (super.shouldContinueExecuting());
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.getEntity().readyToRally()) {
			EntityLivingBase leader = this.getTarget();
			// if (((ILeader) leader).isMartyr())
			// rally(leader);
		}
	}

	protected void rally(T leader) {
		this.getEntity().rally(leader);
	}
}