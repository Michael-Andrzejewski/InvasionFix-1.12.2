// `^`^`^`
// ```java
// /**
//  * This class represents a custom mob entity for the mod 'invmod', extending the basic creature functionalities of Minecraft.
//  * The EntityIMMob2 is a specialized entity with additional properties such as tier and texture, which are managed through
//  * Minecraft's data serialization system. This entity is associated with a TileEntityNexus, which may influence its behavior or properties.
//  *
//  * Constructors:
//  * - EntityIMMob2(World worldIn): Basic constructor that initializes the entity within the provided world.
//  * - EntityIMMob2(World worldIn, TileEntityNexus nexus): Overloaded constructor that also associates the entity with a TileEntityNexus.
//  *
//  * Methods:
//  * - entityInit(): Initializes the entity's data parameters, setting default values for tier and texture.
//  * - initEntityAI(): Placeholder for initializing the entity's AI behaviors (currently empty and inherits behavior from superclass).
//  * - getNexus(): Returns the associated TileEntityNexus object.
//  * - setTier(int tier): Sets the tier of the entity, which may affect its abilities or stats.
//  * - getTier(): Retrieves the current tier of the entity.
//  * - getTextureID(): Retrieves the texture identifier for the entity, which can be used to apply different textures.
//  *
//  * Data Parameters:
//  * - TIER: An integer representing the tier level of the entity.
//  * - TEXTURE: An integer representing the texture variant of the entity.
//  *
//  * Note: This class is part of the 'invmod' mod and is designed to work within the Minecraft modding ecosystem.
//  */
// package invmod.entity.monster;
// 
// // ... (imports and class definition)
// ```
// `^`^`^`

package invmod.entity.monster;

import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.EntityCreature;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityIMMob2 extends EntityCreature {

	private static final DataParameter<Integer> TIER = EntityDataManager.createKey(EntityIMMob2.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer> TEXTURE = EntityDataManager.createKey(EntityIMMob2.class,
			DataSerializers.VARINT);

	private TileEntityNexus nexus;

	public EntityIMMob2(World worldIn) {
		super(worldIn);
	}

	public EntityIMMob2(World worldIn, TileEntityNexus nexus) {
		super(worldIn);
		this.nexus = nexus;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(TIER, 1);
		this.getDataManager().register(TEXTURE, 0);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();

	}

	public TileEntityNexus getNexus() {
		return this.nexus;
	}

	public void setTier(int tier) {
		this.getDataManager().set(TIER, tier);
	}

	public int getTier() {
		return this.getDataManager().get(TIER);
	}

	public int getTextureID() {
		return this.getDataManager().get(TEXTURE);
	}

}
