package invmod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import invmod.client.ProxyClient;
import invmod.command.InvasionCommand;
import invmod.entity.EntityIMSpawnProxy;
import invmod.entity.monster.EntityIMMob;
import invmod.event.PlayerEvents;
import invmod.nexus.IEntityIMPattern;
import invmod.nexus.MobBuilder;
import invmod.util.ISelect;
import invmod.util.config.Config;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class mod_invasion {

	@SidedProxy(clientSide = Reference.CLIENTPROXY, serverSide = Reference.COMMONPROXY)
	public static ProxyClient proxy;
	private static GuiHandler guiHandler = new GuiHandler();;
	public static HashMap<String, Long> deathList = new HashMap();
	private static MobBuilder defaultMobBuilder = new MobBuilder();
	private static ISelect<IEntityIMPattern> nightSpawnPool1;

	// Change this before releasing
	public static final String[] DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS = { "zombie_t1_any", "zombie_t2_any_basic",
			"zombie_t2_plain", "zombie_t2_tar", "zombie_t2_pigman", "zombie_t3_any", "zombiePigman_t1_any",
			"zombiePigman_t2_any", "zombiePigman_t3_any", "spider_t1_any", "spider_t2_any", "pigengy_t1_any",
			"skeleton_t1_any", "thrower_t1", "thrower_t2", "creeper_t1_basic", "imp_t1" };
	public static final float[] DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS = { 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
			0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };

	// Mob health
	public static HashMap<String, Integer> mobHealthNightspawn = new HashMap();
	public static HashMap<String, Integer> mobHealthInvasion = new HashMap();

	// Creative tab declaration
	public static CreativeTabs tabInvmod = new CreativeTabs("invasionTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.STRONG_CATALYST);
		}
	};

	@Instance("mod_invasion")
	public static mod_invasion instance;

	public mod_invasion() {
		instance = this;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event);
		// this.nightSpawnConfig();
		this.loadHealthConfig();
		this.loadEntities();
		proxy.registerEntityRenderers();
		// this.loadCreativeTabs();
		// BlocksAndItems.loadBlocks();
		// BlocksAndItems.loadItems();
		SoundHandler.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		// Register to receive subscribed events
		// FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		FMLInterModComms.sendMessage("Waila", "register", "invmod.common.util.IMWailaProvider.callbackRegister");
		// BlocksAndItems.registerItems(event);
		CraftingAndSmelting.addRecipes();

		if (Config.NIGHTSPAWNS_ENABLED) {
			// BiomeGenBase[] allBiomes =BiomeGenBase.getBiomeGenArray();
			Set<ResourceLocation> biomeRegistryKeys = Biome.REGISTRY.getKeys();
			Biome[] allBiomes = new Biome[biomeRegistryKeys.size()];
			int i = 0;
			for (ResourceLocation key : biomeRegistryKeys) {
				allBiomes[i] = Biome.REGISTRY.getObject(key);
				i++;
			}
			EntityRegistry.addSpawn(EntityIMSpawnProxy.class, Config.NIGHTSPAWNS_MOB_SPAWNCHANCE, 1, 1,
					EnumCreatureType.MONSTER, allBiomes);
		}
		/*
		 * if (Config.MOB_LIMIT_OVERRIDE != 70) { try { Class c =
		 * EnumCreatureType.class; Object[] consts = c.getEnumConstants(); Class sub =
		 * consts[0].getClass(); Field field =
		 * sub.getDeclaredField("maxNumberOfCreature"); field.setAccessible(true);
		 * field.set(EnumCreatureType.MONSTER,
		 * Integer.valueOf(Config.MOB_LIMIT_OVERRIDE)); } catch (Exception e) {
		 * ModLogger.logFatal(e.getMessage()); } }
		 */
	}

	@EventHandler
	public void postInitialise(FMLPostInitializationEvent evt) {

	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		ICommandManager commandManager = FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager();
		if ((commandManager instanceof CommandHandler))
			((CommandHandler) commandManager).registerCommand(new InvasionCommand());
	}

	@SubscribeEvent
	public void PlayerLoggedInEvent(PlayerLoggedInEvent event) {
		/*
		 * try { if (Config.UPDATE_MESSAGES)
		 * VersionChecker.checkForUpdates((EntityPlayerMP)event.player); } catch
		 * (Exception e) { }
		 */
	}

	// load mobhealth config
	private void loadHealthConfig() {
		// Invasion spawns
		mobHealthInvasion.put("IMCreeper-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMVulture-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMImp-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMPigManEngineer-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMSkeleton-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMSpider-T1-Spider-invasionSpawn-health", 18);
		mobHealthInvasion.put("IMSpider-T1-Baby-Spider-invasionSpawn-health", 3);
		mobHealthInvasion.put("IMSpider-T2-Jumping-Spider-invasionSpawn-health", 18);
		mobHealthInvasion.put("IMSpider-T2-Mother-Spider-invasionSpawn-health", 23);
		mobHealthInvasion.put("IMThrower-T1-invasionSpawn-health", 50);
		mobHealthInvasion.put("IMThrower-T2-invasionSpawn-health", 70);
		mobHealthInvasion.put("IMZombie-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMZombie-T2-invasionSpawn-health", 30);
		mobHealthInvasion.put("IMZombie-T3-invasionSpawn-health", 65);
		mobHealthInvasion.put("IMZombiePigman-T1-invasionSpawn-health", 20);
		mobHealthInvasion.put("IMZombiePigman-T2-invasionSpawn-health", 30);
		mobHealthInvasion.put("IMZombiePigman-T3-invasionSpawn-health", 65);

		// Nightspawns
		/*
		 * mobHealthNightspawn.put("IMCreeper-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMVulture-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMImp-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMPigManEngineer-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMSkeleton-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMSpider-T1-Spider-nightSpawn-health", 18);
		 * mobHealthNightspawn.put("IMSpider-T1-Baby-Spider-nightSpawn-health", 3);
		 * mobHealthNightspawn.put("IMSpider-T2-Jumping-Spider-nightSpawn-health", 18);
		 * mobHealthNightspawn.put("IMSpider-T2-Mother-Spider-nightSpawn-health", 23);
		 * mobHealthNightspawn.put("IMThrower-T1-nightSpawn-health", 50);
		 * mobHealthNightspawn.put("IMThrower-T2-nightSpawn-health", 70);
		 * mobHealthNightspawn.put("IMZombie-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMZombie-T2-nightSpawn-health", 30);
		 * mobHealthNightspawn.put("IMZombie-T3-nightSpawn-health", 65);
		 * mobHealthNightspawn.put("IMZombiePigman-T1-nightSpawn-health", 20);
		 * mobHealthNightspawn.put("IMZombiePigman-T2-nightSpawn-health", 30);
		 * mobHealthNightspawn.put("IMZombiePigman-T3-nightSpawn-health", 65);
		 */

	}

	/*
	 * //load Creativetab protected void loadCreativeTabs() { tabInvmod = new
	 * CreativeTabInvmod(); }
	 */

	// Load Entities
	protected void loadEntities() {

		// Animations and rendering
		proxy.loadAnimations();
		// proxy.registerEntityRenderers();

		// Register Entities
		/**
		 * EntityRegistry.registerModEntity(EntityIMZombie.class, "IMZombie", 5, this,
		 * 128, 1, true, 0x6B753F, 0x281B0A);
		 * EntityRegistry.registerModEntity(EntityIMSkeleton.class, "IMSkeleton", 6,
		 * this, 128, 1, true, 0x9B9B9B, 0x797979);
		 * EntityRegistry.registerModEntity(EntityIMSpider.class, "IMSpider", 7, this,
		 * 128, 1, true, 0x504A3E, 0xA4121C);
		 * EntityRegistry.registerModEntity(EntityIMPigEngy.class, "IMPigEngy", 8, this,
		 * 128, 1, true, 0xEC9695, 0x420000);
		 * EntityRegistry.registerModEntity(EntityIMWolf.class, "IMWolf", 9, this, 128,
		 * 1, true, 0x99CCFF, 0xE6F2FF);
		 * EntityRegistry.registerModEntity(EntityIMEgg.class, "IMEgg", 10, this, 128,
		 * 1, true, 0xD9D9D9, 0x4D4D4D);
		 * EntityRegistry.registerModEntity(EntityIMCreeper.class, "IMCreeper", 11,
		 * this, 128, 1, true, 0x238F1F, 0xA5AAA6);
		 * EntityRegistry.registerModEntity(EntityIMImp.class, "IMImp", 12, this, 128,
		 * 1, true, 0xB40113, 0xFF0000);
		 * EntityRegistry.registerModEntity(EntityIMZombiePigman.class,
		 * "IMZombiePigman", 13, this, 128, 1, true, 0xEB8E91, 0x49652F);
		 * EntityRegistry.registerModEntity(EntityIMThrower.class, "IMThrower", 14,
		 * this, 128, 1, true, 0x5303814, 0x632808);
		 * 
		 * EntityRegistry.registerModEntity(EntityIMBoulder.class, "IMBoulder", 1, this,
		 * 36, 4, true); EntityRegistry.registerModEntity(EntityIMBolt.class, "IMBolt",
		 * 2, this, 36, 5, false); EntityRegistry.registerModEntity(EntityIMTrap.class,
		 * "IMTrap", 3, this, 36, 5, false);
		 * EntityRegistry.registerModEntity(EntityIMPrimedTNT.class, "IMPrimedTNT", 4,
		 * this, 36, 4, true);
		 * 
		 * if (Config.DEBUG) { EntityRegistry.registerModEntity(EntityIMBird.class,
		 * "IMBird", 15, this, 128, 1, true);
		 * EntityRegistry.registerModEntity(EntityIMGiantBird.class, "IMGiantBird", 16,
		 * this, 128, 1, true, 0x2B2B2B, 0xEA7EDC); }
		 **/

		// spawneggs needed things and despensebehavior
		// BlockDispenser.dispenseBehaviorRegistry.putObject(itemSpawnEgg, new
		// DispenserBehaviorSpawnEgg());
		// BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BlocksAndItems.itemSpawnEgg,
		// new DispenserBehaviorSpawnEgg());

		// Add spawneggs
		/*
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 1,
		 * Reference.MODID+".IMZombie", "Zombie T1", CustomTags.IMZombie_T1(), 0x6B753F,
		 * 0x281B0A)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 2,
		 * Reference.MODID+".IMZombie", "Zombie T2", CustomTags.IMZombie_T2(), 0x497533,
		 * 0x7C7C7C)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 3,
		 * Reference.MODID+".IMZombie", "Tar Zombie T2", CustomTags.IMZombie_T2_tar(),
		 * 0x3A4225, 0x191C13)); SpawnEggRegistry.registerSpawnEgg(new
		 * SpawnEggInfo((short) 4, Reference.MODID+".IMZombie", "Zombie Brute T3",
		 * CustomTags.IMZombie_T3(), 0x586146, 0x1E4639));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 5,
		 * Reference.MODID+".IMSkeleton", "Skeleton T1", new NBTTagCompound(), 0x9B9B9B,
		 * 0x797979)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 6,
		 * Reference.MODID+".IMSpider", "Spider T1", new NBTTagCompound(), 0x504A3E,
		 * 0xA4121C)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 7,
		 * Reference.MODID+".IMSpider", "Spider T1 Baby", CustomTags.IMSpider_T1_baby(),
		 * 0x504A3E, 0xA4121C)); SpawnEggRegistry.registerSpawnEgg(new
		 * SpawnEggInfo((short) 8, Reference.MODID+".IMSpider", "Spider T2 Jumper",
		 * CustomTags.IMSpider_T2(), 0x444167, 0x0A0328));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 9,
		 * Reference.MODID+".IMSpider", "Spider T2 Mother",
		 * CustomTags.IMSpider_T2_mother(), 0x444167, 0x0A0328));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 10,
		 * Reference.MODID+".IMCreeper", "Creeper T1", new NBTTagCompound(), 0x238F1F,
		 * 0xA5AAA6)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 11,
		 * Reference.MODID+".IMPigEngy", "Pigman Engineer T1", new NBTTagCompound(),
		 * 0xEC9695, 0x420000)); SpawnEggRegistry.registerSpawnEgg(new
		 * SpawnEggInfo((short) 12, Reference.MODID+".IMThrower", "Thrower T1", new
		 * NBTTagCompound(), 0x545F37, 0x1D2D3E)); SpawnEggRegistry.registerSpawnEgg(new
		 * SpawnEggInfo((short) 13, Reference.MODID+".IMThrower", "Thrower T2",
		 * CustomTags.IMThrower_T2(), 0x5303814, 0x632808));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 14,
		 * Reference.MODID+".IMImp", "Imp T1", new NBTTagCompound(), 0xB40113,
		 * 0xFF0000)); SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 15,
		 * Reference.MODID+".IMZombiePigman", "Zombie Pigman T1",
		 * CustomTags.IMZombiePigman_T1(), 0xEB8E91, 0x49652F));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 16,
		 * Reference.MODID+".IMZombiePigman", "Zombie Pigman T2",
		 * CustomTags.IMZombiePigman_T2(), 0xEB8E91, 0x49652F));
		 * SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short) 17,
		 * Reference.MODID+".IMZombiePigman", "Zombie Pigman T3",
		 * CustomTags.IMZombiePigman_T3(), 0xEB8E91, 0x49652F));
		 * 
		 * if (Config.DEBUG){ SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo((short)
		 * 18, Reference.MODID+".IMGiantBird", "Vulture T1", new NBTTagCompound(),
		 * 0x2B2B2B, 0xEA7EDC)); }
		 */

	}

	@Deprecated
	protected void nightSpawnConfig() {
		/*
		 * String[] pool1Patterns = new
		 * String[DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length]; float[] pool1Weights = new
		 * float[DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS.length]; // TODO: load
		 * 
		 * RandomSelectionPool mobPool = new RandomSelectionPool(); nightSpawnPool1 =
		 * mobPool; if (DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length ==
		 * DEFAULT_NIGHT_MOB_PATTERN_1_SLOT_WEIGHTS.length) { for (int i = 0; i <
		 * DEFAULT_NIGHT_MOB_PATTERN_1_SLOTS.length; i++) {
		 * 
		 * if (IMWaveBuilder.isPatternNameValid(pool1Patterns[i])) {
		 * ModLogger.logDebug("Added entry for pattern 1 slot " + (i + 1));
		 * mobPool.addEntry(IMWaveBuilder.getPattern(pool1Patterns[i]),
		 * pool1Weights[i]); } else { ModLogger.logWarn("Pattern 1 slot " + (i + 1) +
		 * " in config not recognised. Proceeding as blank."); } } } else { ModLogger.
		 * logFatal("Mob pattern table element mismatch. Ensure each slot has a probability weight"
		 * ); }
		 */
	}

	public static void addToDeathList(String username, long timeStamp) {
		deathList.put(username, Long.valueOf(timeStamp));
	}

	@Override
	public String toString() {
		return "mod_Invasion";
	}

	public static Entity[] getNightMobSpawns1(World world) {
		ISelect mobPool = getMobSpawnPool();
		int numberOfMobs = world.rand.nextInt(Config.NIGHTSPAWNS_MOB_MAX_GROUPSIZE) + 1;
		Entity[] entities = new Entity[numberOfMobs];
		for (int i = 0; i < numberOfMobs; i++) {
			EntityIMMob mob = getMobBuilder().createMobFromConstruct(
					((IEntityIMPattern) mobPool.selectNext()).generateEntityConstruct(), world, null);
			mob.setEntityIndependent();
			// also set in entityLiving constructor, is needed for ai to function properly,
			// I believe
			mob.setAggroRange(Config.NIGHTSPAWNS_MOB_SIGHTRANGE);
			mob.setSenseRange(Config.NIGHTSPAWNS_MOB_SENSERANGE);
			mob.setBurnsInDay(Config.NIGHTSPAWNS_MOB_BURN_DURING_DAY);
			entities[i] = mob;
		}
		return entities;
	}

	public static MobBuilder getMobBuilder() {
		return defaultMobBuilder;
	}

	@Deprecated
	public static ISelect<IEntityIMPattern> getMobSpawnPool() {
		return nightSpawnPool1;
	}

	public static ItemStack getRenderHammerItem() {
		return new ItemStack(ModItems.ENGY_HAMMER, 1);
	}

	public static mod_invasion instance() {
		return instance;
	}

	public static void broadcastToAll(String message) {
		// FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg(new
		// ChatComponentText(message));
		List<EntityPlayerMP> playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();
		for (int i = 0; i < playerList.size(); i++) {
			sendMessageToPlayer(playerList.get(i), message);
		}
	}

	@Deprecated
	public static void sendMessageToPlayers(HashMap<String, Long> hashMap, String message) {
		sendMessageToPlayers(hashMap, message, null);
	}

	@Deprecated
	public static void sendMessageToPlayers(HashMap<String, Long> hashMap, String message, TextFormatting color) {
		if (hashMap != null) {
			for (Map.Entry entry : hashMap.entrySet()) {
				sendMessageToPlayer(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayerByUsername((String) entry.getKey()), message, color);
			}
		}
	}

	public static void sendMessageToPlayers(List<String> playerList, String msg) {
		sendMessageToPlayers(playerList, msg, null);
	}

	public static void sendMessageToPlayers(List<String> playerList, String msg, TextFormatting color) {
		if (playerList != null) {
			for (int i = 0; i < playerList.size(); i++) {
				sendMessageToPlayer(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayerByUsername(playerList.get(i)), msg, color);
			}
		}
	}

	public static void sendMessageToPlayer(EntityPlayerMP player, String message) {
		sendMessageToPlayer(player, message, null);
	}

	public static void sendMessageToPlayer(EntityPlayerMP player, String message, TextFormatting color) {
		sendMessageToPlayer((EntityPlayer) player, message, color);
	}

	public static void sendMessageToPlayer(EntityPlayer player, String message) {
		sendMessageToPlayer(player, message, null);
	}

	public static void sendMessageToPlayer(EntityPlayer player, String message, TextFormatting color) {
		TextComponentTranslation s = new TextComponentTranslation(message);
		if (color != null)
			s.getStyle().setColor(color);
		if (player != null)
			player.sendMessage(s);
	}

	public static int getMobHealth(EntityIMMob mob) {
		int health = 0;
		if (mob.isNexusBound()) {
			if (mobHealthInvasion.get(mob.toString() + "-invasionSpawn-health") != null) {
				health = mobHealthInvasion.get(mob.toString() + "-invasionSpawn-health");
			} else {
				return 20;
			}
		} else {
			/*
			 * if (mobHealthNightspawn.get(mob.toString() + "-nightSpawn-health") != null) {
			 * health = mobHealthNightspawn.get(mob.toString() + "-nightSpawn-health"); }
			 * else {
			 */
			return 20;
			// }
		}
		return health;
	}

}