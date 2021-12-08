package invmod.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import invmod.ModBlocks;
import invmod.ModItems;
import invmod.SoundHandler;
import invmod.mod_invasion;
import invmod.block.BlockNexus;
import invmod.entity.EntityIMLiving;
import invmod.entity.ai.AttackerAI;
import invmod.entity.ally.EntityIMWolf;
import invmod.entity.monster.EntityIMMob;
import invmod.entity.projectile.EntityIMBolt;
import invmod.nexus.IMWaveBuilder;
import invmod.nexus.IMWaveSpawner;
import invmod.nexus.INexusAccess;
import invmod.nexus.Wave;
import invmod.nexus.WaveSpawnerException;
import invmod.util.ComparatorEntityDistance;
import invmod.util.ModLogger;
import invmod.util.config.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;


public class TileEntityNexus extends TileEntity implements INexusAccess, ITickable, IItemHandlerModifiable
{

	//private static final long BIND_EXPIRE_TIME = 300000L;
	private IMWaveSpawner waveSpawner;
	private IMWaveBuilder waveBuilder;
	private ItemStack[] nexusItemStacks;
	private AxisAlignedBB boundingBoxToRadius;
	private final ArrayList<String> boundPlayers;
	private List<EntityIMLiving> mobList;
	private AttackerAI attackerAI;
	private int activationTimer;
	private int currentWave;
	private int spawnRadius;
	private int nexusLevel;
	private int nexusKills;
	private int generation;
	private int cookTime;
	private int maxHp;
	private int hp;
	private int lastHp;
	private int mode;
	private int powerLevel;
	private int lastPowerLevel;
	private int powerLevelTimer;
	private int mobsLeftInWave;
	private int lastMobsLeftInWave;
	private int mobsToKillInWave;
	private int nextAttackTime;
	private int daysToAttack;
	private long lastWorldTime;
	private int zapTimer;
	private int errorState;
	private int tickCount;
	private int cleanupTimer;
	private int immuneTicks = 0; //DarthXenon: Cooldown between attacks
	private long spawnerElapsedRestore;
	private long timer;
	private long waveDelayTimer;
	private long waveDelay;
	private boolean continuousAttack;
	private boolean mobsSorted;
	private boolean resumedFromNBT;
	private boolean activated;

	public TileEntityNexus()
	{
		this(null);
	}

	public TileEntityNexus(World world)
	{
		this.world = world;
		this.spawnRadius = 52;
		this.waveSpawner = new IMWaveSpawner(this, this.spawnRadius);
		this.waveBuilder = new IMWaveBuilder();
		this.nexusItemStacks = new ItemStack[this.getSlots()];
		for(int slot = 0; slot < this.getSlots(); slot++) {
			this.nexusItemStacks[slot] = ItemStack.EMPTY;
	    }
		//this.boundingBoxToRadius = new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX(), this.pos.getY(), this.pos.getZ());
		this.boundingBoxToRadius = new AxisAlignedBB(
			this.pos.getX() - (this.spawnRadius + 10), this.pos.getY() - (this.spawnRadius + 40), this.pos.getZ() - (this.spawnRadius + 10),
			this.pos.getX() + (this.spawnRadius + 10), this.pos.getY() + (this.spawnRadius + 40), this.pos.getZ() + (this.spawnRadius + 10));
		this.boundPlayers = new ArrayList<>();
		this.mobList = new ArrayList();
		this.attackerAI = new AttackerAI(this);
		this.activationTimer = 0;
		this.cookTime = 0;
		this.currentWave = 0;
		this.nexusLevel = 1;
		this.nexusKills = 0;
		this.generation = 0;
		this.maxHp = (this.hp = this.lastHp = 100);
		this.mode = 0;
		this.powerLevelTimer = 0;
		this.powerLevel = 0;
		this.lastPowerLevel = 0;
		this.mobsLeftInWave = 0;
		this.nextAttackTime = 0;
		this.daysToAttack = 0;
		this.lastWorldTime = 0L;
		this.errorState = 0;
		this.tickCount = 0;
		this.timer = 0L;
		this.zapTimer = 0;
		this.cleanupTimer = 0;
		this.waveDelayTimer = -1L;
		this.waveDelay = 0L;
		this.continuousAttack = false;
		this.mobsSorted = false;
		this.resumedFromNBT = false;
		this.activated = false;
	}

	@Override
	public void update()
	{
		if (this.world.isRemote) return;

		this.updateStatus();
		this.updateAI();

		if ((this.mode == 1) || (this.mode == 2) || (this.mode == 3))
		{
			if (this.resumedFromNBT)
			{
				this.boundingBoxToRadius = new AxisAlignedBB(this.pos.getX() - (this.spawnRadius + 10), 0.0D,
					this.pos.getZ() - (this.spawnRadius + 10),
					this.pos.getX() + (this.spawnRadius + 10), 127.0D,
					this.pos.getZ() + (this.spawnRadius + 10));
				if ((this.mode == 2) && (this.continuousAttack))
				{
					if (this.resumeSpawnerContinuous())
					{
						this.mobsLeftInWave = (this.lastMobsLeftInWave += this.acquireEntities());
						ModLogger.logDebug("mobsLeftInWave: " + this.mobsLeftInWave);
						ModLogger.logDebug("mobsToKillInWave: " + this.mobsToKillInWave);
					}
				}
				else
				{
					this.resumeSpawnerInvasion();
					this.acquireEntities();
				}
				this.attackerAI.onResume();
				this.resumedFromNBT = false;
			}
			try
			{
				this.tickCount++;
				if (this.tickCount == 60)
				{
					this.tickCount -= 60;
					this.bindPlayers();
					this.updateMobList();
				}

				if ((this.mode == 1) || (this.mode == 3))
					this.doInvasion(50);
				else if (this.mode == 2)
					this.doContinuous(50);
			}
			catch (WaveSpawnerException e)
			{
				ModLogger.logFatal(e.getMessage());
				e.printStackTrace();
				this.stop();
			}
		}

		if (this.cleanupTimer++ > 40)
		{
			this.cleanupTimer = 0;
			if (this.world.getBlockState(new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ())).getBlock() != ModBlocks.NEXUS_BLOCK)
			{
				this.stop();
				this.invalidate();
				ModLogger.logWarn("Stranded nexus entity trying to delete itself...");
			}
		}
	}

	public void emergencyStop()
	{
		ModLogger.logInfo("Nexus manually stopped by command");
		this.stop();
		this.killAllMobs();
	}

	public void debugStatus()
	{
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Current Time: " + this.world.getWorldTime());
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Time to next: " + this.nextAttackTime);
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Days to attack: " + this.daysToAttack);
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mobs left: " + this.mobsLeftInWave);
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Mode: " + this.mode);
	}

	public void debugStartInvaion(int startWave)
	{
		this.startInvasion(startWave);
		this.activated = true;
		BlockNexus.setBlockView(true, this.getWorld(), this.getPos());
	}

	public void debugStartContinuous()
	{
		this.startContinuousPlay();
		this.activated = true;
		BlockNexus.setBlockView(true, this.getWorld(), this.getPos());
	}

	public void createBolt(int x, int y, int z, int t)
	{
		EntityIMBolt bolt = new EntityIMBolt(this.world,
			this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D,
			x + 0.5D, y + 0.5D, z + 0.5D, t, 1);
		this.world.spawnEntity(bolt);
	}

	public boolean setSpawnRadius(int radius)
	{
		if ((!this.waveSpawner.isActive()) && (radius > 8))
		{
			this.spawnRadius = radius;
			this.waveSpawner.setRadius(radius);
			this.boundingBoxToRadius = new AxisAlignedBB(this.pos.getX() - (this.spawnRadius + 10), 0.0D, this.pos.getZ() - (this.spawnRadius + 10),
				this.pos.getX() + (this.spawnRadius + 10), 127.0D, this.pos.getZ() + (this.spawnRadius + 10));
			return true;
		}

		return false;
	}

	public void attackNexus(int damage)
	{
		if (this.immuneTicks != 0) return;
		this.immuneTicks = 10 + this.world.rand.nextInt(30);
		this.hp -= damage;
		if (this.hp <= 0)
		{
			this.hp = 0;
			if (this.mode == 1) this.theEnd();
		}
		while (this.hp + 5 <= this.lastHp)
		{
			mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus at " + (this.lastHp - 5) + " hp");
			this.lastHp -= 5;
		}
	}

	public void registerMobDied()
	{
		this.nexusKills += 1;
		this.mobsLeftInWave -= 1;
		if (this.mobsLeftInWave <= 0)
		{
			if (this.lastMobsLeftInWave > 0)
			{
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stable again!");
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Unleashing tapped energy...");
				this.lastMobsLeftInWave = this.mobsLeftInWave;
			}
			return;
		}
		while (this.mobsLeftInWave + this.mobsToKillInWave * 0.1F <= this.lastMobsLeftInWave)
		{
			mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus rift stabilised to " + (100 - 100 * this.mobsLeftInWave / this.mobsToKillInWave) + "%");
			this.lastMobsLeftInWave = ((int)(this.lastMobsLeftInWave - this.mobsToKillInWave * 0.1F));
		}
	}

	//DONE Unused.
	/*public void registerMobClose() {
	}*/

	public boolean isActivating()
	{
		return (this.activationTimer > 0) && (this.activationTimer < 400);
	}

	public int getMode()
	{
		return this.mode;
	}

	public int getActivationTimer()
	{
		return this.activationTimer;
	}

	public int getSpawnRadius()
	{
		return this.spawnRadius;
	}

	public int getNexusKills()
	{
		return this.nexusKills;
	}

	public int getGeneration()
	{
		return this.generation;
	}

	public int getNexusLevel()
	{
		return this.nexusLevel;
	}

	public int getPowerLevel()
	{
		return this.powerLevel;
	}

	public int getCookTime()
	{
		return this.cookTime;
	}

	public int getNexusID()
	{
		return -1;
	}

	public BlockPos getPosition()
	{
		return this.pos;
	}

	/*@Override
	public int getXCoord() {
		return this.pos.getX();
	}
	
	@Override
	public int getYCoord() {
		return this.pos.getY();
	}
	
	@Override
	public int getZCoord() {
		return this.pos.getZ();
	}*/

	public List<EntityIMLiving> getMobList()
	{
		return this.mobList;
	}

	public int getActivationProgressScaled(int i)
	{
		return this.activationTimer * i / 400;
	}

	public int getGenerationProgressScaled(int i)
	{
		return this.generation * i / 3000;
	}

	public int getCookProgressScaled(int i)
	{
		return this.cookTime * i / 1200;
	}

	public int getNexusPowerLevel()
	{
		return this.powerLevel;
	}

	public int getCurrentWave()
	{
		return this.currentWave;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		//TODO: check out of bounce
		return this.nexusItemStacks[i];
	}

	/*@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}*/

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		ModLogger.logDebug("Restoring TileEntityNexus from NBT");
		super.readFromNBT(nbttagcompound);
		// added 0 to gettaglist, because it asked an int
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 0);
		this.nexusItemStacks = new ItemStack[this.getSlots()];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist
				.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if ((byte0 >= 0) && (byte0 < this.nexusItemStacks.length))
			{
				this.nexusItemStacks[byte0] = new ItemStack(nbttagcompound1);
			}

		}
		// added 0 to gettaglist, because it asked an int
		nbttaglist = nbttagcompound.getTagList("boundPlayers", 0);
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			this.boundPlayers.add(nbttaglist.getCompoundTagAt(i).getString("name"));
			ModLogger.logDebug("Added bound player: " + nbttaglist.getCompoundTagAt(i).getString("name"));
		}

		this.activationTimer = nbttagcompound.getShort("activationTimer");
		this.mode = nbttagcompound.getInteger("mode");
		this.currentWave = nbttagcompound.getShort("currentWave");
		this.spawnRadius = nbttagcompound.getShort("spawnRadius");
		this.nexusLevel = nbttagcompound.getShort("nexusLevel");
		this.hp = nbttagcompound.getShort("hp");
		this.nexusKills = nbttagcompound.getInteger("nexusKills");
		this.generation = nbttagcompound.getShort("generation");
		this.powerLevel = nbttagcompound.getInteger("powerLevel");
		this.lastPowerLevel = nbttagcompound.getInteger("lastPowerLevel");
		this.nextAttackTime = nbttagcompound.getInteger("nextAttackTime");
		this.daysToAttack = nbttagcompound.getInteger("daysToAttack");
		this.continuousAttack = nbttagcompound.getBoolean("continuousAttack");
		this.activated = nbttagcompound.getBoolean("activated");

		BlockNexus.setBlockView(this.activated, this.getWorld(), this.getPos());

		this.boundingBoxToRadius = new AxisAlignedBB(
			this.pos.getX() - (this.spawnRadius + 10),
			this.pos.getY() - (this.spawnRadius + 40),
			this.pos.getZ() - (this.spawnRadius + 10),
			this.pos.getX() + (this.spawnRadius + 10),
			this.pos.getY() + (this.spawnRadius + 40),
			this.pos.getZ() + (this.spawnRadius + 10));

		ModLogger.logDebug("activationTimer = " + this.activationTimer);
		ModLogger.logDebug("mode = " + this.mode);
		ModLogger.logDebug("currentWave = " + this.currentWave);
		ModLogger.logDebug("spawnRadius = " + this.spawnRadius);
		ModLogger.logDebug("nexusLevel = " + this.nexusLevel);
		ModLogger.logDebug("hp = " + this.hp);
		ModLogger.logDebug("nexusKills = " + this.nexusKills);
		ModLogger.logDebug("powerLevel = " + this.powerLevel);
		ModLogger.logDebug("lastPowerLevel = " + this.lastPowerLevel);
		ModLogger.logDebug("nextAttackTime = " + this.nextAttackTime);

		this.waveSpawner.setRadius(this.spawnRadius);
		if ((this.mode == 1) || (this.mode == 3) || ((this.mode == 2) && (this.continuousAttack)))
		{
			ModLogger.logDebug("Nexus is active; flagging for restore");
			this.resumedFromNBT = true;
			this.spawnerElapsedRestore = nbttagcompound.getLong("spawnerElapsed");
			ModLogger.logDebug("spawnerElapsed = " + this.spawnerElapsedRestore);
		}

		this.attackerAI.readFromNBT(nbttagcompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("activationTimer", (short)this.activationTimer);
		nbttagcompound.setShort("currentWave", (short)this.currentWave);
		nbttagcompound.setShort("spawnRadius", (short)this.spawnRadius);
		nbttagcompound.setShort("nexusLevel", (short)this.nexusLevel);
		nbttagcompound.setShort("hp", (short)this.hp);
		nbttagcompound.setInteger("nexusKills", this.nexusKills);
		nbttagcompound.setShort("generation", (short)this.generation);
		nbttagcompound.setLong("spawnerElapsed", this.waveSpawner.getElapsedTime());
		nbttagcompound.setInteger("mode", this.mode);
		nbttagcompound.setInteger("powerLevel", this.powerLevel);
		nbttagcompound.setInteger("lastPowerLevel", this.lastPowerLevel);
		nbttagcompound.setInteger("nextAttackTime", this.nextAttackTime);
		nbttagcompound.setInteger("daysToAttack", this.daysToAttack);
		nbttagcompound.setBoolean("continuousAttack", this.continuousAttack);
		nbttagcompound.setBoolean("activated", this.activated);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.nexusItemStacks.length; i++)
		{
			if (this.nexusItemStacks[i] != ItemStack.EMPTY)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.nexusItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);

		NBTTagList nbttaglist2 = new NBTTagList();
		for (String playerName : this.boundPlayers.toArray(new String[this.boundPlayers.size()]))
		{
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setString("name", playerName);
			nbttaglist2.appendTag(nbttagcompound1);
		}
		nbttagcompound.setTag("boundPlayers", nbttaglist2);

		this.attackerAI.writeToNBT(nbttagcompound);
		return nbttagcompound;
	}

	public void askForRespawn(EntityIMLiving entity)
	{
		ModLogger.logWarn("Stuck entity asking for respawn: " + entity.toString() + "  " + entity.posX + ", " + entity.posY + ", " + entity.posZ);
		this.waveSpawner.askForRespawn(entity);
	}

	public AttackerAI getAttackerAI()
	{
		return this.attackerAI;
	}

	public void setActivationTimer(int i)
	{
		this.activationTimer = i;
	}

	public void setNexusLevel(int i)
	{
		this.nexusLevel = i;
	}

	public void setNexusKills(int i)
	{
		this.nexusKills = i;
	}

	public void setGeneration(int i)
	{
		this.generation = i;
	}

	public void setNexusPowerLevel(int i)
	{
		this.powerLevel = i;
	}

	public void setCookTime(int i)
	{
		this.cookTime = i;
	}

	public void setWave(int wave)
	{
		this.currentWave = wave;
	}

	private void startInvasion(int startWave)
	{
		System.out.println("Starting invasion at wave " + startWave);
		this.boundingBoxToRadius = new AxisAlignedBB(
			this.pos.getX() - (this.spawnRadius + 10),
			this.pos.getY() - (this.spawnRadius + 40),
			this.pos.getZ() - (this.spawnRadius + 10),
			this.pos.getX() + (this.spawnRadius + 10),
			this.pos.getY() + (this.spawnRadius + 40),
			this.pos.getZ() + (this.spawnRadius + 10));
		if ((this.mode == 2) && (this.continuousAttack))
		{
			mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Can't activate nexus when already under attack!");
			return;
		}

		if ((this.mode == 0) || (this.mode == 2))
		{
			if (this.waveSpawner.isReady())
			{
				try
				{
					this.currentWave = startWave;
					this.waveSpawner.beginNextWave(this.currentWave);
					if (this.mode == 0)
					{
						this.setMode(1);
					}
					else
					{
						this.setMode(3);
					}
					this.bindPlayers();
					this.hp = this.maxHp;
					this.lastHp = this.maxHp;
					this.waveDelayTimer = -1L;
					this.timer = System.currentTimeMillis();
					String s = "Bound player(s): ";
					for (int i = 0; i < this.getBoundPlayers().size(); i++)
						s = s + this.getBoundPlayers().get(i) + ", ";
					if (s == "Bound players(s): ") s = "Bound players: none  "; //This shouldn't happen, but just in case...
					mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), s.substring(0, s.length() - 2));
					mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "The first wave is coming soon!");
					//playSoundForBoundPlayers("invmod:rumble");
					this.playSoundForBoundPlayers(SoundHandler.rumble1);
				}
				catch (WaveSpawnerException e)
				{
					this.stop();
					ModLogger.logFatal(e.getMessage());
					mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), e.getMessage());
				}
			}
			else
			{
				ModLogger.logFatal("Wave spawner not in ready state");
			}
		}
		else
		{
			ModLogger.logWarn("Tried to activate nexus while already active");
		}
	}

	private void startContinuousPlay()
	{
		this.boundingBoxToRadius = new AxisAlignedBB(
			this.pos.getX() - (this.spawnRadius + 10),
			0.0D,
			this.pos.getZ() - (this.spawnRadius + 10),
			this.pos.getX() + (this.spawnRadius + 10),
			127.0D,
			this.pos.getZ() + (this.spawnRadius + 10));
		if ((this.mode == 4) && (this.waveSpawner.isReady()) && (this.activated))
		{
			this.setMode(2);
			this.hp = this.maxHp;
			this.lastHp = this.maxHp;
			this.lastPowerLevel = this.powerLevel;
			this.lastWorldTime = this.world.getWorldTime();
			this.nextAttackTime = ((int)(this.lastWorldTime / 24000L * 24000L) + 14000);
			if ((this.lastWorldTime % 24000L > 12000L)
				&& (this.lastWorldTime % 24000L < 16000L))
			{
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
			}
			else
			{
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus activated and stable");
			}
		}
		else
		{
			mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Couldn't activate nexus");
		}
	}

	private void doInvasion(int elapsed) throws WaveSpawnerException
	{
		if (this.waveSpawner.isActive())
		{
			if (this.hp <= 0)
			{
				this.theEnd();
			}
			else
			{
				this.generateFlux(1);
				if (this.waveSpawner.isWaveComplete())
				{
					if (this.waveDelayTimer == -1L)
					{
						mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " almost complete!");
						//playSoundForBoundPlayers("invmod:chime1");
						this.playSoundForBoundPlayers(SoundHandler.chime1);
						this.waveDelayTimer = 0L;
						this.waveDelay = this.waveSpawner.getWaveRestTime();
					}
					else
					{
						this.waveDelayTimer += elapsed;
						if (this.waveDelayTimer > this.waveDelay)
						{
							this.currentWave += 1;
							mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Wave " + this.currentWave + " about to begin");
							this.waveSpawner.beginNextWave(this.currentWave);
							this.waveDelayTimer = -1L;
							//playSoundForBoundPlayers("invmod:rumble1");
							this.playSoundForBoundPlayers(SoundHandler.rumble1);
							if (this.currentWave > this.nexusLevel)
							{
								this.nexusLevel = this.currentWave;
							}
						}
					}
				}
				else
				{
					this.waveSpawner.spawn(elapsed);
				}
			}
		}
	}

	private void playSoundForBoundPlayers(SoundEvent sound)
	{
		if (this.getBoundPlayers() != null)
		{
			for (int i = 0; i < this.getBoundPlayers().size(); i++)
			{
				try
				{
					EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(this.getBoundPlayers().get(i));
					if (player != null) player.playSound(sound, 1f, 1f);
				}
				catch (Exception name)
				{
					System.out.println("Problem while trying to play sound at player.");
				}
			}
		}
	}

	private void doContinuous(int elapsed)
	{
		this.powerLevelTimer += elapsed;
		if (this.powerLevelTimer > 2200)
		{
			this.powerLevelTimer -= 2200;
			this.generateFlux(5 + (int)(5 * this.powerLevel / 1550.0F));
			if ((this.nexusItemStacks[0] == ItemStack.EMPTY)
				|| (this.nexusItemStacks[0].getItem() != /*BlocksAndItems.itemDampingAgent*/ModItems.DAMPING_AGENT))
			{
				this.powerLevel += 1;
			}
		}

		if ((this.nexusItemStacks[0] != ItemStack.EMPTY)
			&& (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStrongDampingAgent*/ModItems.STRONG_DAMPING_AGENT))
		{
			if ((this.powerLevel >= 0) && (!this.continuousAttack))
			{
				this.powerLevel -= 1;
				if (this.powerLevel < 0)
				{
					this.stop();
				}
			}
		}

		if (!this.continuousAttack)
		{
			long currentTime = this.world.getWorldTime();
			int timeOfDay = (int)(this.lastWorldTime % 24000L);
			if ((timeOfDay < 12000) && (currentTime % 24000L >= 12000L) && (currentTime + 12000L > this.nextAttackTime))
			{
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "The night looms around the nexus...");
			}
			if (this.lastWorldTime > currentTime)
			{
				this.nextAttackTime = ((int)(this.nextAttackTime - (this.lastWorldTime - currentTime)));
			}
			this.lastWorldTime = currentTime;

			if (this.lastWorldTime >= this.nextAttackTime)
			{
				float difficulty = 1.0F + this.powerLevel / 4500;
				float tierLevel = 1.0F + this.powerLevel / 4500;
				int timeSeconds = 240;
				try
				{
					Wave wave = this.waveBuilder.generateWave(difficulty,
						tierLevel, timeSeconds);
					this.mobsLeftInWave = (this.lastMobsLeftInWave = this.mobsToKillInWave = (int)(wave
						.getTotalMobAmount() * 0.8F));
					this.waveSpawner.beginNextWave(wave);
					this.continuousAttack = true;
					int days = this.world.rand.nextInt(1
						+ Config.MAX_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE
						- Config.MIN_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE);
					this.nextAttackTime = ((int)(currentTime / 24000L * 24000L) + 14000 + days * 24000);
					this.hp = (this.lastHp = 100);
					this.zapTimer = 0;
					this.waveDelayTimer = -1L;
					mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Enemy forces are destabilising the nexus!");
					//playSoundForBoundPlayers("invmod:rumble");
					this.playSoundForBoundPlayers(SoundHandler.rumble1);
				}
				catch (WaveSpawnerException e)
				{
					ModLogger.logFatal(e.getMessage());
					e.printStackTrace();
					this.stop();
				}

			}

		}
		else if (this.hp <= 0)
		{
			this.continuousAttack = false;
			this.continuousNexusHurt();
		}
		else if (this.waveSpawner.isWaveComplete())
		{

			if (this.waveDelayTimer == -1L)
			{
				this.waveDelayTimer = 0L;
				this.waveDelay = this.waveSpawner.getWaveRestTime();
			}
			else
			{

				this.waveDelayTimer += elapsed;
				if ((this.waveDelayTimer > this.waveDelay)
					&& (this.zapTimer < -200))
				{
					this.waveDelayTimer = -1L;
					this.continuousAttack = false;
					this.waveSpawner.stop();
					this.hp = 100;
					this.lastHp = 100;
					this.lastPowerLevel = this.powerLevel;
				}
			}

			this.zapTimer -= 1;
			if (this.mobsLeftInWave <= 0)
			{
				if ((this.zapTimer <= 0) && (this.zapEnemy(1)))
				{
					this.zapEnemy(0);
					this.zapTimer = 23;
				}
			}
		}
		else
		{
			try
			{
				this.waveSpawner.spawn(elapsed);
			}
			catch (WaveSpawnerException e)
			{
				ModLogger.logFatal(e.getMessage());
				e.printStackTrace();
				this.stop();
			}
		}
	}

	private void updateStatus()
	{
		this.immuneTicks--;
		if (this.immuneTicks < 0) this.immuneTicks = 0;
		if (this.nexusItemStacks[0] != ItemStack.EMPTY)
		{
			if ((this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemEmptyTrap*/ModItems.TRAP_EMPTY))
			{
				if (this.cookTime < 1200)
				{
					if (this.mode == 0)
						this.cookTime += 1;
					else
					{
						this.cookTime += 9;
					}
				}
				if (this.cookTime >= 1200)
				{
					if (this.nexusItemStacks[1] == ItemStack.EMPTY)
					{
						this.nexusItemStacks[1] = new ItemStack(
							/*BlocksAndItems.itemRiftTrap*/ModItems.TRAP_RIFT, 1);
						if ((this.nexusItemStacks[0].getCount() -1) <= 0)
							this.nexusItemStacks[0] = ItemStack.EMPTY;
						this.cookTime = 0;
					}
					else if ((this.nexusItemStacks[1].getItem() == /*BlocksAndItems.itemRiftTrap*/ModItems.TRAP_RIFT))
					{
						this.nexusItemStacks[1].grow(1);
						if ((this.nexusItemStacks[0].getCount() -1) <= 0)
							this.nexusItemStacks[0] = ItemStack.EMPTY;
						this.cookTime = 0;
					}
				}
			}
			else if ((this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemRiftFlux*/ModItems.RIFT_FLUX) && (this.nexusItemStacks[0].getItemDamage() == 1))
			{
				if ((this.cookTime < 1200) && (this.nexusLevel >= 10)) this.cookTime += 5;

				if (this.cookTime >= 1200)
				{
					if (this.nexusItemStacks[1] == ItemStack.EMPTY)
					{
						this.nexusItemStacks[1] = new ItemStack(/*BlocksAndItems.itemStrongCatalyst*/ModItems.STRONG_CATALYST, 1);
						if ((this.nexusItemStacks[0].getCount() -1) <= 0) this.nexusItemStacks[0] = ItemStack.EMPTY;
						this.cookTime = 0;
					}
				}
			}
		}
		else
		{
			this.cookTime = 0;
		}

		if (this.activationTimer >= 400)
		{
			this.activationTimer = 0;
			if (this.nexusItemStacks[0] != ItemStack.EMPTY)
			{

				if (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemNexusCatalyst*/ModItems.NEXUS_CATALYST)
				{
					this.nexusItemStacks[0].shrink(1);
					if (this.nexusItemStacks[0].isEmpty()) this.nexusItemStacks[0] = ItemStack.EMPTY;
					this.activated = true;
					BlockNexus.setBlockView(true, this.getWorld(), this.getPos());
					this.startInvasion(1);

				}
				else if (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStrongCatalyst*/ModItems.STRONG_CATALYST)
				{
					this.nexusItemStacks[0].shrink(1);
					if (this.nexusItemStacks[0].isEmpty()) this.nexusItemStacks[0] = ItemStack.EMPTY;
					this.activated = true;
					BlockNexus.setBlockView(true, this.getWorld(), this.getPos());
					this.startInvasion(10);

				}
				else if (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStableNexusCatalyst*/ModItems.STABLE_NEXUS_CATALYST)
				{
					this.nexusItemStacks[0].shrink(1);
					if (this.nexusItemStacks[0].isEmpty()) this.nexusItemStacks[0] = ItemStack.EMPTY;
					this.activated = true;
					BlockNexus.setBlockView(true, this.getWorld(), this.getPos());
					this.startContinuousPlay();

				}
			}

		}
		else if ((this.mode == 0) || (this.mode == 4))
		{
			if (this.nexusItemStacks[0] != ItemStack.EMPTY)
			{
				if ((this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemNexusCatalyst*/ModItems.NEXUS_CATALYST)
					|| (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStrongCatalyst*/ModItems.STRONG_CATALYST))
				{
					this.activationTimer += 1;
					this.mode = 0;
				}
				else if (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStableNexusCatalyst*/ModItems.STABLE_NEXUS_CATALYST)
				{
					this.activationTimer += 1;
					this.mode = 4;
				}
			}
			else
			{
				this.activationTimer = 0;
			}
		}
		else if (this.mode == 2)
		{
			if (this.nexusItemStacks[0] != ItemStack.EMPTY)
			{
				if ((this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemNexusCatalyst*/ModItems.NEXUS_CATALYST)
					|| (this.nexusItemStacks[0].getItem() == /*BlocksAndItems.itemStrongCatalyst*/ModItems.STRONG_CATALYST))
				{
					this.activationTimer += 1;
				}
			}
			else
				this.activationTimer = 0;
		}
	}

	private void generateFlux(int increment)
	{
		this.generation += increment;
		if (this.generation % 10 == 0) {
			ModLogger.logDebug("TileEntityNexus: trying to generate Flux...");
		}
		if (this.generation >= 3000)
		{

			if (this.nexusItemStacks[1] == ItemStack.EMPTY)
			{
				this.nexusItemStacks[1] = new ItemStack(
					/*BlocksAndItems.itemRiftFlux*/ModItems.RIFT_FLUX, 1);
				this.generation -= 3000;
			}
			else if (this.nexusItemStacks[1].getItem() == /*BlocksAndItems.itemRiftFlux*/ModItems.RIFT_FLUX)
			{
				this.nexusItemStacks[1].grow(1);
				this.generation -= 3000;
			}
		}
	}

	private void stop()
	{
		if (this.mode == 3)
		{
			this.setMode(2);
			int days = this.world.rand.nextInt(1
				+ Config.MAX_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE
				- Config.MIN_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE);
			this.nextAttackTime = ((int)(this.world.getWorldTime() / 24000L * 24000L) + 14000 + days * 24000);
		}
		else
		{
			this.setMode(0);
		}

		this.waveSpawner.stop();
		this.activationTimer = 0;
		this.currentWave = 0;
		this.errorState = 0;
		this.activated = false;
		BlockNexus.setBlockView(this.activated, this.getWorld(), this.getPos());
		this.boundPlayers.clear();

		ModLogger.logInfo("Invasion ended.");
		mod_invasion.broadcastToAll("Invasion ended.");
	}

	private void bindPlayers()
	{
		List<EntityPlayer> players = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBoxToRadius);
		for (EntityPlayer entityPlayer : players)
		{
			long time = System.currentTimeMillis();
			String playerName = entityPlayer.getDisplayName().getUnformattedText();
			boolean endsWithS = playerName.toLowerCase().endsWith("s");
			if (!this.boundPlayers.contains(playerName))
			{
				System.out.println("Binding " + playerName + " to nexus ....");
				mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), playerName + (endsWithS ? "'" : "'s") + " life is now bound to the nexus");
				mod_invasion.sendMessageToPlayer(entityPlayer, "Your life is now bound to the nexus!", TextFormatting.DARK_PURPLE);
				this.boundPlayers.add(playerName);
			}
		}
	}

	private void updateMobList()
	{
		this.mobList = this.world.getEntitiesWithinAABB(EntityIMLiving.class, this.boundingBoxToRadius);
		this.mobsSorted = false;
	}

	public void setMode(int i)
	{
		this.mode = i;
		this.setActive(this.mode != 0);
	}

	private void setActive(boolean flag)
	{
// TODO: Fix this
//		if (this.world != null) {
//			int meta = this.world.getBlockMetadata(this.pos.getX(), this.pos.getY(),
//					this.pos.getZ());
//			if (flag) {
//				this.world.setBlockMetadataWithNotify(this.pos.getX(),
//						this.pos.getY(), this.pos.getZ(), (meta & 0x4) == 0 ? meta + 4
//								: meta, 3);
//			} else {
//				this.world.setBlockMetadataWithNotify(this.pos.getX(),
//						this.pos.getY(), this.pos.getZ(), (meta & 0x4) == 4 ? meta - 4
//								: meta, 3);
//			}
//		}
	}

	private int acquireEntities()
	{
		AxisAlignedBB bb = this.boundingBoxToRadius
			.expand(10.0D, 128.0D, 10.0D);

		List<EntityIMMob> entities = this.world.getEntitiesWithinAABB(
			EntityIMMob.class, bb);
		for (EntityIMMob entity : entities)
		{
			entity.acquiredByNexus(this);
		}
		ModLogger.logDebug("Acquired " + entities.size() + " entities after state restore");
		return entities.size();
	}

	private void theEnd()
	{
		if (!this.world.isRemote)
		{
			mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "The nexus is destroyed!");
			//this.stop();
			long time = System.currentTimeMillis();
			for (int i = 0; i < this.getBoundPlayers().size(); i++)
			{
				EntityPlayer player = this.world.getPlayerEntityByName(this.getBoundPlayers().get(i));
				if (player != null)
				{
					player.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
					//playSoundForBoundPlayers("random.explode");
					this.playSoundForBoundPlayers(SoundEvents.ENTITY_GENERIC_EXPLODE);
				}
			}
			this.stop();
			//this.boundPlayers.clear();
			this.killAllMobs();
		}
	}

	private void continuousNexusHurt()
	{
		mod_invasion.sendMessageToPlayers(this.getBoundPlayers(), "Nexus severely damaged!");
		//playSoundForBoundPlayers("random.explode");
		this.playSoundForBoundPlayers(SoundEvents.ENTITY_GENERIC_EXPLODE);
		this.killAllMobs();
		this.waveSpawner.stop();
		this.powerLevel = ((int)((this.powerLevel - (this.powerLevel - this.lastPowerLevel)) * 0.7F));
		this.lastPowerLevel = this.powerLevel;
		if (this.powerLevel < 0)
		{
			this.powerLevel = 0;
			this.stop();
		}
	}

	private void killAllMobs()
	{
		// monsters
		List<EntityIMLiving> mobs = this.world.getEntitiesWithinAABB(EntityIMLiving.class, this.boundingBoxToRadius);
		for (EntityIMLiving mob : mobs)
			mob.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);

		// wolves
		List<EntityIMWolf> wolves = this.world.getEntitiesWithinAABB(EntityIMWolf.class, this.boundingBoxToRadius);
		for (EntityIMWolf wolf : wolves)
			wolf.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
	}

	private boolean zapEnemy(int sfx)
	{
		if (this.mobList.size() > 0)
		{
			if (!this.mobsSorted)
			{
				Collections.sort(this.mobList, new ComparatorEntityDistance(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
			}
			EntityIMLiving mob = this.mobList.remove(this.mobList.size() - 1);
			mob.attackEntityFrom(DamageSource.MAGIC, 500.0F);
			EntityIMBolt bolt = new EntityIMBolt(this.world,
				this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D,
				mob.posX, mob.posY, mob.posZ, 15, sfx);
			this.world.spawnEntity(bolt);
			return true;
		}

		return false;
	}

	private boolean resumeSpawnerContinuous()
	{
		try
		{
			float difficulty = 1.0F + this.powerLevel / 4500;
			float tierLevel = 1.0F + this.powerLevel / 4500;
			int timeSeconds = 240;
			Wave wave = this.waveBuilder.generateWave(difficulty, tierLevel, timeSeconds);
			this.mobsToKillInWave = ((int)(wave.getTotalMobAmount() * 0.8F));
			ModLogger.logDebug("Original mobs to kill: " + this.mobsToKillInWave);
			this.mobsLeftInWave = (this.lastMobsLeftInWave = this.mobsToKillInWave
				- this.waveSpawner.resumeFromState(wave, this.spawnerElapsedRestore, this.spawnRadius));
			return true;
		}
		catch (WaveSpawnerException e)
		{
			float tierLevel;
			ModLogger.logFatal("Error resuming spawner:" + e.getMessage());
			this.waveSpawner.stop();
			return false;
		}
	}

	private boolean resumeSpawnerInvasion()
	{
		try
		{
			this.waveSpawner.resumeFromState(this.currentWave,
				this.spawnerElapsedRestore, this.spawnRadius);
			return true;
		}
		catch (WaveSpawnerException e)
		{
			ModLogger.logFatal("Error resuming spawner:" + e.getMessage());
			this.waveSpawner.stop();
			return false;
		}
	}

	private void updateAI()
	{
		this.attackerAI.update();
	}

	public ArrayList<String> getBoundPlayers()
	{
		return this.boundPlayers;
	}

	public boolean isActive()
	{
		return this.activated;
	}

	//DONE Unused.
	/*@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		this.world.markBlockForUpdate(this.pos);
	}*/

	//DONE: Unused.
	/*@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.pos, 0, tag);
	}*/

	public static TileEntityNexus getNearest(EntityPlayer player, int searchRange)
	{

		for (int counter = 0; counter <= searchRange; counter++)
		{
			for (int x = (int)player.posX - counter; x <= (int)player.posX + counter; x++)
			{
				for (int z = (int)player.posZ - counter; z <= (int)player.posZ + counter; z++)
				{
					for (int y = (int)player.posY - counter; y <= (int)player.posY + counter; y++)
					{
						if (y < 0) y = 0;
						if (y > player.world.getActualHeight()) y = player.world.getActualHeight();
						TileEntity result = player.world.getTileEntity(new BlockPos(x, y, z));

						if (result instanceof TileEntityNexus) return (TileEntityNexus)result;
					}
				}
			}
		}

		return null;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// DONE Auto-generated method stub
		return null;
	}

	@Override
	public int getSlots() {
		return 2;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ModLogger.logDebug("TileEntityNexus: trying to put " + stack.toString() + " into slot " + slot);
		this.nexusItemStacks[slot] = stack;
		return stack;
	}

    public boolean canExtractFromSlot(int slot)
    {
        return true;
    }

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.canExtractFromSlot(slot) == false)
        {
            return ItemStack.EMPTY;
        }

        ItemStack existingStack = this.nexusItemStacks[slot];

        if (existingStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        amount = Math.min(amount, Math.min(existingStack.getCount(), existingStack.getMaxStackSize()));

        ItemStack stack;

        /*if (simulate)
        {
            stack = existingStack.copy();
            stack.setCount(amount);

            return stack;
        }
        else
        {*/
            if (amount == existingStack.getCount())
            {
                stack = existingStack;
                this.nexusItemStacks[slot] = ItemStack.EMPTY;
            }
            else
            {
                stack = existingStack.splitStack(amount);

                if (existingStack.getCount() <= 0)
                {
                	this.nexusItemStacks[slot] = ItemStack.EMPTY;
                }
            }
        //}

        return stack;
		//return this.nexusItemStacks[slot];
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.nexusItemStacks[slot].getMaxStackSize();
	}
	
	 @Override
	  public void setStackInSlot(int slot, ItemStack stack) {
	    // super.setStackInSlot(slot, stack);
		 ModLogger.logDebug("TileEntityNexus: setStackInSlot called, i guess it's time to implement this function now ...");
	  }
}