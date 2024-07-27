// `^`^`^`
// ```java
// /**
//  * This code defines an abstract class EntityIMLiving, which extends EntityCreature and implements IHasNexus and IPathfindable interfaces.
//  * It is part of a mod for Minecraft that introduces custom entities with advanced pathfinding capabilities.
//  *
//  * Class Summary:
//  * - EntityIMLiving: An abstract class representing a living entity with advanced movement and navigation abilities.
//  *
//  * Key Data Parameters:
//  * - IS_HOLDING_ONTO_LADDER: Boolean indicating if the entity is holding onto a ladder.
//  * - MOVE_STATE: Integer representing the current movement state of the entity (e.g., standing, running).
//  * - TIER: Integer representing the tier level of the entity, which may affect its abilities or stats.
//  * - TEXTURE: Integer representing the texture ID used for rendering the entity.
//  *
//  * Key Components:
//  * - pathSource: A PathCreator object used to generate paths for navigation.
//  * - imNavigator: A NavigatorIM object that handles the entity's navigation.
//  * - oldNavAdapter: A PathNavigateAdapter object for compatibility with older navigation systems.
//  * - moveHelperIM: An IMMoveHelper object that assists with the entity's movement.
//  * - targetNexus: A TileEntityNexus object representing the entity's target or home base.
//  * - unDestructableBlocks: A list of blocks that the entity cannot destroy.
//  *
//  * Constructor(s):
//  * - EntityIMLiving(World worldIn): Initializes the entity in the given world.
//  * - EntityIMLiving(World worldIn, TileEntityNexus nexus): Initializes the entity with a reference to a Nexus.
//  *
//  * Key Methods:
//  * - entityInit(): Registers data parameters and initializes the entity.
//  * - onUpdate(): Updates the entity's state each tick, handling movement states.
//  * - onFollowingEntity(Entity entity): Called when the entity is following another entity.
//  * - onPathSet(): Called when a path has been set for the entity.
//  * - onPathBlocked(Path path, INotifyTask asker): Called when the entity's path is blocked.
//  * - rally(Entity leader): Sets the entity to rally mode, following a leader.
//  * - readyToRally(): Checks if the entity is ready to rally.
//  * - setTier(int tier): Sets the entity's tier level.
//  * - getTier(): Retrieves the entity's tier level.
//  * - setTexture(int textureId): Sets the entity's texture ID.
//  * - getTextureId(): Retrieves the entity's texture ID.
//  * - writeEntityToNBT(NBTTagCompound tag): Saves the entity's data to NBT.
//  * - readEntityFromNBT(NBTTagCompound tag): Reads the entity's data from NBT.
//  * - updateAITick(): Updates AI tasks each tick.
//  * - setCurrentTargetPos(BlockPos pos): Sets the entity's current target position.
//  * - canStandAt(IBlockAccess terrainMap, BlockPos pos): Checks if the entity can stand at a given position.
//  * - setSize(float width, float height): Sets the size of the entity.
//  * - setIsHoldingIntoLadder(boolean flag): Sets whether the entity is holding onto a ladder.
//  * - canStandAtAndIsValid(IBlockAccess terrainMap, BlockPos pos): Checks if the entity can stand at and is valid at a given position.
//  * - isAdjacentSolidBlock(IBlockAccess terrainMap, BlockPos pos): Checks for solid blocks adjacent to the given position.
//  * - getCollide(IBlockAccess terrainMap, Vec3d vec): Checks for collisions at a given vector position.
//  * - getCollide(IBlockAccess terrainMap, BlockPos pos): Checks for collisions at a given block position.
//  * - avoidsBlock(Block block): Checks if the entity avoids a specific block.
//  * - ignoresBlock(Block block): Checks if the entity ignores a specific block.
//  *
//  * This class is designed to be extended by specific entity types that will define their own behaviors and attributes.
//  */
// ```
// This code appears to be part of an AI system for an entity within a Minecraft-like game, where entities interact with the game world and its rules. The code defines methods for determining the destructibility of blocks, managing movement speed and state, checking for ladders, and handling navigation.
// 
// - `isBlockDestructible`: Checks if a block at a given position is destructible, considering the game's mob griefing rules and whether the block is in a list of undestructible blocks or has a ladder attached.
// 
// - `blockHasLadder`: Determines if there is a ladder on any of the four sides of the given block position.
// 
// - `setBaseMoveSpeedStat` and `setMoveSpeedStat`: These methods set the base and current movement speed of the entity, respectively. The base speed is a reference speed, while the current speed can be adjusted dynamically.
// 
// - `resetMoveSpeed`: Resets the entity's movement speed to the base value.
// 
// - `setMoveState`: Sets the movement state of the entity (e.g., walking, idle) and synchronizes this state with the game's data manager if the game is not in a remote context.
// 
// - `setTurnRate`: Sets the rate at which the entity can turn.
// 
// - `isHoldingOntoLadder`: Checks if the entity is currently holding onto a ladder.
// 
// - `getNavigatorNew`: Retrieves the entity's navigation system.
// 
// - `getPathSource`: Provides access to the entity's pathfinding source.
// 
// - `getCollideSize`: Returns the size of the entity for collision purposes.
// 
// - `getBlockRemovalOrder`: Determines the order in which blocks should be removed when the entity is moving through them, based on its position and collision size.
// 
// - `getMoveSpeedStat` and `getBaseMoveSpeedStat`: Return the current and base movement speed of the entity.
// 
// - `getMoveHelper`: Provides access to the entity's movement helper, which likely assists in movement calculations.
// 
// - `getMoveState`: Retrieves the current movement state of the entity.
// 
// - `getCurrentTargetPos`: Gets the position of the entity's current target.
// 
// - `getTurnRate`: Returns the entity's turn rate.
// 
// - `getNexus`: Provides access to the entity's target nexus, which could be a specific goal or home base within the game world.
// 
// Overall, this code is designed to manage an entity's interactions with the environment, particularly its movement and interactions with blocks, and to maintain the state necessary for its AI to make decisions.
// `^`^`^`

package invmod.entity;

import java.util.Arrays;
import java.util.List;

import invmod.INotifyTask;
import invmod.IPathfindable;
import invmod.client.render.animation.util.IMMoveHelper;
import invmod.entity.ai.navigator.NavigatorIM;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathCreator;
import invmod.entity.ai.navigator.PathNavigateAdapter;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Coords;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityIMLiving extends EntityCreature implements IHasNexus, IPathfindable {

	private static final DataParameter<Boolean> IS_HOLDING_ONTO_LADDER = EntityDataManager
			.createKey(EntityIMLiving.class, DataSerializers.BOOLEAN); // 20
	private static final DataParameter<Integer> MOVE_STATE = EntityDataManager.createKey(EntityIMLiving.class,
			DataSerializers.VARINT); // 23
	private static final DataParameter<Integer> TIER = EntityDataManager.createKey(EntityIMLiving.class,
			DataSerializers.VARINT); // 30
	private static final DataParameter<Integer> TEXTURE = EntityDataManager.createKey(EntityIMLiving.class,
			DataSerializers.VARINT); // 31

	private PathCreator pathSource = new PathCreator(700, 50);
	private final NavigatorIM imNavigator = new NavigatorIM(this, this.pathSource);
	private final PathNavigateAdapter oldNavAdapter = new PathNavigateAdapter(this.imNavigator);
	private BlockPos collideSize;
	private BlockPos currentTargetPos = BlockPos.ORIGIN;
	private int rallyCooldown;
	private float turnRate = 30.0F;
	private float moveSpeedBase = 0.5f;
	private float moveSpeed = 0.5f;
	private MoveState moveState;
	private IMMoveHelper moveHelperIM = new IMMoveHelper(this);
	protected TileEntityNexus targetNexus;
	protected static List<Block> unDestructableBlocks = Arrays.asList(Blocks.BEDROCK, Blocks.COMMAND_BLOCK,
			Blocks.END_PORTAL_FRAME, Blocks.LADDER, Blocks.CHEST);

	public EntityIMLiving(World worldIn) {
		this(worldIn, null);
	}

	public EntityIMLiving(World worldIn, TileEntityNexus nexus) {
		super(worldIn);
		this.targetNexus = nexus;
		this.collideSize = new BlockPos(this.width + 1.0F, this.height + 1.0F, this.width + 1.0F);
		this.setTier(1);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.getDataManager().register(IS_HOLDING_ONTO_LADDER, false);
		this.getDataManager().register(MOVE_STATE, (this.moveState = MoveState.STANDING).ordinal());
		this.getDataManager().register(TIER, 1);
		this.getDataManager().register(TEXTURE, 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.moveState = MoveState.values()[this.getDataManager().get(MOVE_STATE)];
		switch (this.moveState) {
		case CLIMBING:
			// this.motionY = 0.1d;
			this.isJumping = true;
			break;
		case FLYING:
			// this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
			this.moveRelative(this.moveStrafing, 0.0F, this.moveForward, 0.0F);
			break;
		case JUMPING:
			this.jump();
			// this.motionY = 0.5d;
			// this.isJumping = true;
			this.setMoveState(MoveState.RUNNING);
			break;
		case NONE:
			break;
		case RUNNING:
			// this.setVelocityToPosition(this.getMoveHelper().getX(),
			// this.getMoveHelper().getY(), this.getMoveHelper().getZ(),
			// this.getMoveHelper().getSpeed());
			// this.moveEntityWithHeading(this.getMoveHelper().getMoveStrafe(),
			// this.getMoveHelper().getMoveForward());
			// this.moveEntityWithHeading(this.moveStrafing,
			// (float)this.getMoveHelper().getSpeed());
			this.moveRelative(this.moveStrafing, 0.0F, (float) this.getMoveHelper().getSpeed(), 0.0F);
			// this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
			// this.moveEntityWithHeading(0f, (float)this.getMoveHelper().getSpeed());
			break;
		case STANDING:
			this.motionX = 0d;
			this.motionZ = 0d;
			break;
		default:
			break;
		}
	}

	public void onFollowingEntity(Entity entity) {
	}

	public void onPathSet() {
	}

	public boolean onPathBlocked(Path path, INotifyTask asker) {
		return false;
	}

	public void rally(Entity leader) {
		this.rallyCooldown = 300;
	}

	public boolean readyToRally() {
		return this.rallyCooldown == 0;
	}

	public void setTier(int tier) {
		this.getDataManager().set(TIER, tier);
		this.initEntityAI();
		this.setTexture(tier);
	}

	public int getTier() {
		return this.getDataManager().get(TIER);
	}

	public void setTexture(int textureId) {
		this.getDataManager().set(TEXTURE, textureId);
	}

	public int getTextureId() {
		return this.getDataManager().get(TEXTURE);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("tier", this.getDataManager().get(TIER));
		tag.setInteger("textureId", this.getDataManager().get(TEXTURE));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		if (!this.world.isRemote) {
			this.setTexture(tag.getInteger("textureId"));
			this.setTier(tag.getInteger("tier"));
		}
	}

	// TODO Removed Override annotation
	protected void updateAITick() {
		if (this.rallyCooldown > 0) {
			this.rallyCooldown--;
		}
	}

	public void setCurrentTargetPos(BlockPos pos) {
		this.currentTargetPos = pos;
	}

	public boolean canStandAt(IBlockAccess terrainMap, BlockPos pos) {
		boolean isSolidBlock = false;
		for (int xOffset = pos.getX(); xOffset < pos.getX() + this.collideSize.getX(); xOffset++) {
			for (int zOffset = pos.getZ(); zOffset < pos.getZ() + this.collideSize.getZ(); zOffset++) {
				BlockPos pos0 = new BlockPos(xOffset, pos.getY(), zOffset);
				Block block = terrainMap.getBlockState(pos0).getBlock();
				if (block != Blocks.AIR) {
					if (!block.isPassable(terrainMap, pos0)) {
						isSolidBlock = true;
					} else if (this.avoidsBlock(block)) {
						return false;
					}
				}
			}
		}
		return isSolidBlock;
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		this.collideSize = new BlockPos(width + 1f, height + 1f, width + 1f);
	}

	public void setIsHoldingIntoLadder(boolean flag) {
		if (!this.world.isRemote)
			this.getDataManager().set(IS_HOLDING_ONTO_LADDER, flag);
	}

	public boolean canStandAtAndIsValid(IBlockAccess terrainMap, BlockPos pos) {
		boolean flag0 = this.getCollide(terrainMap, pos) > 0;
		boolean flag1 = this.canStandAt(terrainMap, pos);
		return (flag0 && flag1);
	}

	protected boolean isAdjacentSolidBlock(IBlockAccess terrainMap, BlockPos pos) {
		if ((this.collideSize.getX() == 1) && (this.collideSize.getZ() == 1)) {
			for (int i = 0; i < 4; i++) {
				IBlockState blockState = terrainMap
						.getBlockState(pos.add(Coords.offsetAdjX[i], 0, Coords.offsetAdjZ[i]));
				if ((blockState.getBlock() != Blocks.AIR) && (blockState.getMaterial().isSolid()))
					return true;
			}
		} else if ((this.collideSize.getX() == 2) && (this.collideSize.getZ() == 2)) {
			for (int i = 0; i < 8; i++) {
				IBlockState blockState = terrainMap
						.getBlockState(pos.add(Coords.offsetAdj2X[i], 0, Coords.offsetAdj2Z[i]));
				if ((blockState.getBlock() != Blocks.AIR) && (blockState.getMaterial().isSolid()))
					return true;
			}
		}
		return false;
	}

	protected int getCollide(IBlockAccess terrainMap, Vec3d vec) {
		return this.getCollide(terrainMap, new BlockPos(vec));
	}

	protected int getCollide(IBlockAccess terrainMap, BlockPos pos) {
		boolean destructibleFlag = false;
		boolean liquidFlag = false;
		for (int xOffset = pos.getX(); xOffset < pos.getX() + this.collideSize.getX(); xOffset++) {
			for (int yOffset = pos.getY(); yOffset < pos.getY() + this.collideSize.getY(); yOffset++) {
				for (int zOffset = pos.getZ(); zOffset < pos.getZ() + this.collideSize.getZ(); zOffset++) {
					IBlockState blockState = terrainMap.getBlockState(new BlockPos(xOffset, yOffset, zOffset));
					IBlockState blockState0 = terrainMap.getBlockState(new BlockPos(xOffset, yOffset - 1, zOffset));
					if (blockState.getBlock() != Blocks.AIR) {
						if ((blockState.getBlock() == Blocks.WATER) || (blockState.getBlock() == Blocks.LAVA)) {
							liquidFlag = true;
						} else if (!blockState.getBlock().isPassable(terrainMap,
								new BlockPos(xOffset, yOffset, zOffset))) {
							if (this.isBlockDestructible(terrainMap, pos, blockState))
								destructibleFlag = true;
							else
								return 0;
						} else if (blockState0.getBlock() instanceof BlockFence) {
							if (this.isBlockDestructible(terrainMap, pos, blockState0))
								return 3;
							return 0;
						}

						if (this.avoidsBlock(blockState.getBlock()))
							return -2;
					}
				}
			}
		}
		if (destructibleFlag)
			return 2;
		if (liquidFlag)
			return -1;
		return 1;
	}

	public boolean avoidsBlock(Block block) {
		return ((block == Blocks.FIRE) || (block == Blocks.BEDROCK) || (block == Blocks.LAVA)
				|| (block == Blocks.FLOWING_LAVA) || (block == Blocks.CACTUS));
	}

	public boolean ignoresBlock(Block block) {
		return block instanceof BlockTallGrass || block instanceof BlockDeadBush || block instanceof BlockFlower
				|| block instanceof BlockBush
				/*
				 * return ((block == Blocks.TALLGRASS) || (block == Blocks.DEADBUSH) || (block
				 * == Blocks.RED_FLOWER) || (block == Blocks.YELLOW_FLOWER) || (block ==
				 * Blocks.BROWN_MUSHROOM) || (block == Blocks.RED_MUSHROOM)
				 */ || (block == Blocks.WOODEN_PRESSURE_PLATE) || (block == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
				|| (block == Blocks.STONE_PRESSURE_PLATE);
	}

	public boolean isBlockDestructible(IBlockAccess terrainMap, BlockPos pos, IBlockState state) {
		// check if mobgriefing is enabled
		boolean mobgriefing = this.world.getGameRules().getBoolean("mobGriefing");
		if (!mobgriefing)
			return false;
		if (unDestructableBlocks.contains(state.getBlock()) || this.blockHasLadder(terrainMap, pos))
			return false;
		return state.getBlock() instanceof BlockDoor || state.getBlock() == Blocks.TRAPDOOR
				|| state.getMaterial().isSolid();
	}

	protected boolean blockHasLadder(IBlockAccess terrainMap, BlockPos pos) {
		for (int i = 0; i < 4; i++) {
			if (terrainMap.getBlockState(pos.add(Coords.offsetAdjX[i], 0, Coords.offsetAdjZ[i]))
					.getBlock() == Blocks.LADDER)
				return true;
		}
		return false;
	}

	protected void setBaseMoveSpeedStat(float speed) {
		this.moveSpeedBase = speed;
		this.moveSpeed = speed;
	}

	public void setMoveSpeedStat(float speed) {
		this.moveSpeed = speed;
		this.getNavigatorNew().setSpeed(speed);
		// this.getMoveHelper().setMoveSpeed(speed);
	}

	public void resetMoveSpeed() {
		this.setMoveSpeedStat(this.moveSpeedBase);
		this.getNavigatorNew().setSpeed(this.moveSpeedBase);
	}

	public void setMoveState(MoveState moveState) {
		this.moveState = moveState;
		if (!this.world.isRemote)
			this.getDataManager().set(MOVE_STATE, Integer.valueOf(moveState.ordinal()));
	}

	public void setTurnRate(float rate) {
		this.turnRate = rate;
	}

	public boolean isHoldingOntoLadder() {
		return this.getDataManager().get(IS_HOLDING_ONTO_LADDER);
	}

	public INavigation getNavigatorNew() {
		return this.imNavigator;
	}

	public IPathSource getPathSource() {
		return this.pathSource;
	}

	public BlockPos getCollideSize() {
		return this.collideSize;
	}

	public BlockPos[] getBlockRemovalOrder(BlockPos pos) {
		if (MathHelper.floor(this.posY) >= pos.getY()) {
			BlockPos[] blocks = new BlockPos[2];
			blocks[1] = pos.up();
			blocks[0] = new BlockPos(pos);
			return blocks;
		}

		BlockPos[] blocks = new BlockPos[3];
		blocks[2] = new BlockPos(pos);
		blocks[1] = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) + this.collideSize.getY(),
				MathHelper.floor(this.posZ));
		blocks[0] = pos.up();
		return blocks;
	}

	public float getMoveSpeedStat() {
		return this.moveSpeed;
	}

	public float getBaseMoveSpeedStat() {
		return this.moveSpeedBase;
	}

	@Override
	public IMMoveHelper getMoveHelper() {
		return this.moveHelperIM;
	}

	public MoveState getMoveState() {
		return this.moveState;
	}

	public BlockPos getCurrentTargetPos() {
		return this.currentTargetPos;
	}

	public float getTurnRate() {
		return this.turnRate;
	}

	@Override
	public TileEntityNexus getNexus() {
		return this.targetNexus;
	}

}