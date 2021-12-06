package invmod.entity.monster;

import invmod.IBlockAccessExtended;
import invmod.INotifyTask;
import invmod.ModBlocks;
import invmod.ModItems;
import invmod.mod_invasion;
import invmod.entity.EntityIMLiving;
import invmod.entity.Goal;
import invmod.entity.ICanBuild;
import invmod.entity.ICanDig;
import invmod.entity.INavigation;
import invmod.entity.IPathSource;
import invmod.entity.ITerrainBuild;
import invmod.entity.ITerrainDig;
import invmod.entity.TerrainBuilder;
import invmod.entity.TerrainDigger;
import invmod.entity.TerrainModifier;
import invmod.entity.ai.EntityAIAttackNexus;
import invmod.entity.ai.EntityAIGoToNexus;
import invmod.entity.ai.EntityAIKillEntity;
import invmod.entity.ai.EntityAISimpleTarget;
import invmod.entity.ai.EntityAIWanderIM;
import invmod.entity.ai.navigator.NavigatorEngy;
import invmod.entity.ai.navigator.Path;
import invmod.entity.ai.navigator.PathAction;
import invmod.entity.ai.navigator.PathNavigateAdapter;
import invmod.entity.ai.navigator.PathNode;
import invmod.entity.ai.navigator.PathfinderIM;
import invmod.tileentity.TileEntityNexus;
import invmod.util.Coords;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class EntityIMPigEngy extends EntityIMMob implements ICanDig, ICanBuild
{

	//private static final DataParameter<Optional<ItemStack>> HELD_ITEM = EntityDataManager.createKey(EntityIMPigEngy.class, DataSerializers.OPTIONAL_ITEM_STACK);
	private static final DataParameter<Boolean> IS_SWINGING = EntityDataManager.createKey(EntityIMPigEngy.class, DataSerializers.BOOLEAN);

	//private static final int MAX_LADDER_TOWER_HEIGHT = 4;
	//private static final int META_ITEM_ID_HELD = 30;
	//private static final int META_SWINGING = 31;
	private final NavigatorEngy navigatorEngy;
	private final PathNavigateAdapter oldNavAdapter;
	private int swingTimer;
	private int planks;
	private int askForScaffoldTimer;
	//private int tier;
	private float supportThisTick;

	private TerrainModifier terrainModifier;
	private TerrainDigger terrainDigger;
	private TerrainBuilder terrainBuilder = null;
	//private ItemStack currentItem;

	public EntityIMPigEngy(World world, TileEntityNexus nexus)
	{
		super(world, nexus);
		IPathSource pathSource = this.getPathSource();
		pathSource.setSearchDepth(1500);
		pathSource.setQuickFailDepth(1500);
		this.navigatorEngy = new NavigatorEngy(this, pathSource);
		this.oldNavAdapter = new PathNavigateAdapter(this.navigatorEngy);
		pathSource.setSearchDepth(1200);

		if (this.terrainBuilder == null)
		{
			this.terrainModifier = new TerrainModifier(this, 2.8F);
			this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0F);
			this.terrainBuilder = new TerrainBuilder(this, this.terrainModifier, 1.0F);
		}

		this.setBaseMoveSpeedStat(0.23F);
		this.attackStrength = 2;
		this.selfDamage = 0;
		this.maxSelfDamage = 0;
		this.planks = 15;
		//this.tier = 1;
		this.maxDestructiveness = 2;
		this.askForScaffoldTimer = 0;
		this.isImmuneToFire = true;

		this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));

		this.setMaxHealthAndHealth(mod_invasion.getMobHealth(this));
		this.setName("Pigman Engineer");
		this.setGender(1);
		this.setDestructiveness(2);
		this.setJumpHeight(1);
		this.setCanClimb(false);

		int r = this.rand.nextInt(3);
		if (r == 0) this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.LADDER)));
		else if (r == 1) this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
		else this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(/*BlocksAndItems.itemEngyHammer*/ModItems.ENGY_HAMMER));
	}

	public EntityIMPigEngy(World world)
	{
		this(world, null);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(IS_SWINGING, false);
	}

	@Override
	protected void initEntityAI()
	{
		this.tasksIM = new EntityAITasks(this.world.profiler);
		this.tasksIM.addTask(0, new EntityAISwimming(this));
		this.tasksIM.addTask(1, new EntityAIKillEntity(this, EntityPlayer.class, 60));
		this.tasksIM.addTask(2, new EntityAIAttackNexus(this));
		this.tasksIM.addTask(3, new EntityAIGoToNexus(this));
		this.tasksIM.addTask(7, new EntityAIWanderIM(this));
		this.tasksIM.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 7.0F));
		this.tasksIM.addTask(9, new EntityAIWatchClosest(this, EntityIMCreeper.class, 12.0F));
		this.tasksIM.addTask(9, new EntityAILookIdle(this));

		this.targetTasksIM = new EntityAITasks(this.world.profiler);
		if (this.isNexusBound())
		{
			this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, 3.0F, true));
		}
		else
		{
			this.targetTasksIM.addTask(1, new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
			this.targetTasksIM.addTask(2, new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
		}
		this.targetTasksIM.addTask(3, new EntityAIHurtByTarget(this, false));
	}

	@Override
	public void updateAITasks()
	{
		super.updateAITasks();
		this.terrainModifier.onUpdate();
	}

	@Override
	public void updateAITick()
	{
		super.updateAITick();
		this.terrainBuilder.setBuildRate(1.0F + this.supportThisTick * 0.33F);

		this.supportThisTick = 0.0F;

		this.askForScaffoldTimer--;
		if (this.targetNexus != null)
		{
			int weight = 1;
			if (this.targetNexus.getPos().getY() - this.getPosition().getY() > 1)
			{
				weight = Math.max(6000 / this.targetNexus.getPos().getY() - this.getPosition().getY(), 1);
			}
			if ((this.currentGoal == Goal.BREAK_NEXUS)
				&& (((this.getNavigatorNew().getLastPathDistanceToTarget() > 2.0F) &&
					(this.askForScaffoldTimer <= 0)) ||
					(this.rand.nextInt(weight) == 0)))
			{
				if (this.targetNexus.getAttackerAI().askGenerateScaffolds(this))
				{
					this.getNavigatorNew().clearPath();
					this.askForScaffoldTimer = 60;
				}
				else
				{
					this.askForScaffoldTimer = 140;
				}
			}
		}
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.updateAnimation();
	}

	@Override
	public void onPathSet()
	{
		this.terrainModifier.cancelTask();
	}

	//TODO Prevents spawning with egg due to conflict with EntityAISwimming
	/*@Override
	public PathNavigateAdapter getNavigator() {
		return this.oldNavAdapter;
	}*/

	@Override
	public INavigation getNavigatorNew()
	{
		return this.navigatorEngy;
	}

	@Override
	public IBlockAccess getTerrain()
	{
		return this.world;
	}

	@Override
	public boolean onPathBlocked(Path path, INotifyTask notifee)
	{
		if (!path.isFinished())
		{
			PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
			return this.terrainDigger.askClearPosition(new BlockPos(node.pos), notifee, 1.0F);
		}
		return false;
	}

	public ITerrainBuild getTerrainBuildEngy()
	{
		return this.terrainBuilder;
	}

	protected ITerrainDig getTerrainDig()
	{
		return this.terrainDigger;
	}

	@Override
	public void setTier(int tier)
	{
		super.setTier(tier);
		this.terrainModifier = new TerrainModifier(this, 2.8f);
		this.terrainDigger = new TerrainDigger(this, this.terrainModifier, tier <= 1 ? 1f : ((float)tier) * 0.75f);
		this.terrainBuilder = null;
		if (tier == 2) this.terrainBuilder = new TerrainBuilder(this, this.terrainModifier, 1f, TerrainBuilder.LADDER_COST, TerrainBuilder.COBBLE_COST, Blocks.COBBLESTONE);
		if (this.terrainBuilder == null) this.terrainBuilder = new TerrainBuilder(this, this.terrainModifier, 1f);
	}

	//DarthXenon: Is getLivingSound() replaced by getAmbientSound() ?
	//TODO: Removed override annotation
	/*protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}*/

	@Override
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	@Override
	public String getSpecies()
	{
		return "Pigman";
	}

	@Override
	public float getBlockRemovalCost(BlockPos pos)
	{
		return this.getBlockStrength(pos) * 20.0F;
	}

	@Override
	public boolean canClearBlock(BlockPos pos)
	{
		IBlockState blockState = this.world.getBlockState(pos);
		return (blockState.getBlock() == Blocks.AIR) || (this.isBlockDestructible(this.world, pos, blockState));
	}

	public boolean avoidsBlock(int id)
	{
		return ((id == 51) || (id == 7) || (id == 64) || (id == 8) || (id == 9) || (id == 10) || (id == 11));
	}

	public void supportForTick(EntityIMLiving entity, float amount)
	{
		this.supportThisTick += amount;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap)
	{
		if ((node.pos.x == -21) && (node.pos.z == 180)) this.planks = 10;
		IBlockState blockState = terrainMap.getBlockState(new BlockPos(node.pos));
		float materialMultiplier = (blockState.getBlock() != Blocks.AIR)
			&& (this.isBlockDestructible(terrainMap, new BlockPos(node.pos), blockState)) ? 3.2F : 1.0F;

		switch (node.action)
		{
			case BRIDGE:
				return prevNode.distanceTo(node) * 1.7F * materialMultiplier;
			case SCAFFOLD_UP:
				return prevNode.distanceTo(node) * 0.5F;
			case LADDER_UP_NX:
			case LADDER_UP_NZ:
			case LADDER_UP_PX:
			case LADDER_UP_PZ:
				return prevNode.distanceTo(node) * 1.3F * materialMultiplier;
			case LADDER_TOWER_UP_PX:
			case LADDER_TOWER_UP_NX:
			case LADDER_TOWER_UP_PZ:
			case LADDER_TOWER_UP_NZ:
				return prevNode.distanceTo(node) * 1.4F;
			default:
				break;
		}

		float multiplier = 1.0F;
		if ((terrainMap instanceof IBlockAccessExtended))
		{
			int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.pos.x, node.pos.y, node.pos.z) & 0x7;
			multiplier += mobDensity;
		}
		if (blockState.getBlock() == Blocks.AIR || blockState.getBlock() == Blocks.SNOW) return prevNode.distanceTo(node) * multiplier;
		if (blockState.getBlock() == Blocks.LADDER) return prevNode.distanceTo(node) * 0.7F * multiplier;
		if ((!blockState.getBlock().isPassable(terrainMap, new BlockPos(node.pos))) && (blockState != /*BlocksAndItems.blockNexus*/ModBlocks.NEXUS_BLOCK))
		{
			return prevNode.distanceTo(node) * 3.2F;
		}

		return super.getBlockPathCost(prevNode, node, terrainMap);
	}

	@Override
	public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder)
	{
		super.getPathOptionsFromNode(terrainMap, currentNode, pathFinder);
		if (this.planks <= 0) return;

		for (int i = 0; i < 4; i++)
		{
			if (this.getCollide(terrainMap, currentNode.pos.addVector(Coords.offsetAdjX[i], 0, Coords.offsetAdjZ[i])) > 0)
			{
				for (int yOffset = 0; yOffset > -4; yOffset--)
				{
					Vec3d vec = currentNode.pos.addVector(Coords.offsetAdjX[i], yOffset - 1, Coords.offsetAdjZ[i]);
					if (!terrainMap.isAirBlock(new BlockPos(vec))) break;
					pathFinder.addNode(currentNode.pos.addVector(Coords.offsetAdjX[i], yOffset, Coords.offsetAdjZ[i]), PathAction.BRIDGE);
				}
			}
		}
	}

	@Override
	protected void calcPathOptionsVertical(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder)
	{
		if ((currentNode.pos.x == -11) && (currentNode.pos.z == 177)) this.planks = 10;
		super.calcPathOptionsVertical(terrainMap, currentNode, pathFinder);
		if (this.planks <= 0) return;

		if (this.getCollide(terrainMap, currentNode.pos.addVector(0d, 1d, 0d)) > 0)
		{
			if (terrainMap.isAirBlock(new BlockPos(currentNode.pos.addVector(0d, 1d, 0d))))
			{
				if (currentNode.action == PathAction.NONE)
				{
					this.addAnyLadderPoint(terrainMap, currentNode, pathFinder);
				}
				else if (!this.continueLadder(terrainMap, currentNode, pathFinder))
				{
					this.addAnyLadderPoint(terrainMap, currentNode, pathFinder);
				}

			}

			if ((currentNode.action == PathAction.NONE) || (currentNode.action == PathAction.BRIDGE))
			{
				int maxHeight = 4;
				for (int i = this.getCollideSize().getY(); i < 4; i++)
				{
					Block block = terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(0d, i, 0d))).getBlock();
					if ((block != Blocks.AIR)
						&& (!block.isPassable(terrainMap,
							new BlockPos(currentNode.pos.addVector(0d, i, 0d)))))
					{
						maxHeight = i - this.getCollideSize().getY();
						break;
					}

				}

				for (int i = 0; i < 4; i++)
				{
					IBlockState blockState = terrainMap.getBlockState(new BlockPos(
						currentNode.pos.addVector(Coords.offsetAdjX[i], 1, Coords.offsetAdjZ[i])));
					if (blockState.isNormalCube())
					{
						for (int height = 0; height < maxHeight; height++)
						{
							blockState = terrainMap.getBlockState(new BlockPos(
								currentNode.pos.addVector(Coords.offsetAdjX[i], height, Coords.offsetAdjZ[i])));
							if (blockState.getBlock() != Blocks.AIR)
							{
								if (!blockState.isNormalCube()) break;
								pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.ladderTowerIndexOrient[i]);
								break;
							}
						}
					}
				}
			}

		}

		if ((terrainMap instanceof IBlockAccessExtended))
		{
			int data = ((IBlockAccessExtended)terrainMap).getLayeredData(
				currentNode.pos.x, currentNode.pos.y + 1,
				currentNode.pos.z);
			if (data == 16384)
			{
				pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.SCAFFOLD_UP);
			}
		}
	}

	protected void addAnyLadderPoint(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder)
	{
		for (int i = 0; i < 4; i++)
		{
			if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(Coords.offsetAdjX[i], 1, Coords.offsetAdjZ[i]))).isNormalCube())
				pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.ladderIndexOrient[i]);
		}
	}

	// NOOB HAUS: possible cases? LADDER_UP_PX, LADDER_UP_NX, LADDER_UP_PZ,
	// LADDER_UP_NZ, LADDER_TOWER_UP_PX,
	// LADDER_TOWER_UP_NX, LADDER_TOWER_UP_PZ, LADDER_TOWER_UP_NZ, SCAFFOLD_UP
	protected boolean continueLadder(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder)
	{
		switch (currentNode.action)
		{
			case LADDER_TOWER_UP_PX:
				if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(1d, 1d, 0d))).isNormalCube())
				{
					pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.LADDER_UP_PX);
				}
				return true;
			case LADDER_TOWER_UP_NX:
				if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(-1d, 1d, 0d))).isNormalCube())
				{
					pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.LADDER_UP_NX);
				}
				return true;
			case LADDER_TOWER_UP_PZ:
				if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(0d, 1d, 1d))).isNormalCube())
				{
					pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.LADDER_UP_PZ);
				}
				return true;
			case LADDER_TOWER_UP_NZ:
				if (terrainMap.getBlockState(new BlockPos(currentNode.pos.addVector(0d, 1d, -1d))).isNormalCube())
				{
					pathFinder.addNode(currentNode.pos.addVector(0d, 1d, 0d), PathAction.LADDER_UP_NZ);
				}
				return true;
			default:
				break;
		}

		return false;
	}

	@Override
	protected void dropFewItems(boolean flag, int bonus)
	{
		super.dropFewItems(flag, bonus);
		if (this.rand.nextInt(2) == 0)
		{
			this.entityDropItem(new ItemStack(Items.LEATHER), 0f);
		}
		else if (this.isBurning())
		{
			this.entityDropItem(new ItemStack(Items.COOKED_PORKCHOP), 0f);
		}
		else
		{
			this.entityDropItem(new ItemStack(Items.PORKCHOP), 0f);
		}
	}

	protected void updateAnimation()
	{
		if ((!this.world.isRemote) && (this.terrainModifier.isBusy()))
		{
			this.setSwinging(true);
			PathAction currentAction = this.getNavigatorNew().getCurrentWorkingAction();
			this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(currentAction == PathAction.NONE ? Items.IRON_PICKAXE : /*BlocksAndItems.itemEngyHammer*/ModItems.ENGY_HAMMER));
		}
		int swingSpeed = this.getSwingSpeed();
		if (this.isSwinging())
		{
			this.swingTimer += 1;
			if (this.swingTimer >= swingSpeed)
			{
				this.swingTimer = 0;
				this.setSwinging(false);
			}
		}
		else
		{
			this.swingTimer = 0;
		}

		this.swingProgress = (this.swingTimer / swingSpeed);
	}

	protected boolean isSwinging()
	{
		return this.getDataManager().get(IS_SWINGING);
	}

	protected void setSwinging(boolean flag)
	{
		this.getDataManager().set(IS_SWINGING, flag);
	}

	protected int getSwingSpeed()
	{
		return 10;
	}

	@Override
	public boolean canPlaceLadderAt(BlockPos pos)
	{
		if (EntityIMLiving.unDestructableBlocks.contains(this.world.getBlockState(pos).getBlock()))
		{
			return ((this.world.getBlockState(pos.west()).isNormalCube())
				|| (this.world.getBlockState(pos.east()).isNormalCube())
				|| (this.world.getBlockState(pos.north()).isNormalCube())
				|| (this.world.getBlockState(pos.south()).isNormalCube()));
		}
		return false;
	}

	@Override
	public void onBlockRemoved(BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String toString()
	{
		return "IMPigManEngineer-T" + this.getTier();
	}

}