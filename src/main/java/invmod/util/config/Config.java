package invmod.util.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import invmod.Reference;
import invmod.util.ModLogger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	// Main settings
	public static Configuration config;
	public static File configDirectory;
	public static final List<Section> sections = new ArrayList<Section>();;

	// Sections
	public static final Section general = new Section("general", "general");
	public static final Section continiousMode = new Section("continiousMode",
		"continiousMode");
	public static final Section nightSpawns = new Section("nightspawns",
		"nightspawns");


	// General config
	public static boolean UPDATE_MESSAGES;
	public static boolean DROP_DESTRUCTED_BLOCKS;
	public static boolean DEBUG;
	public static int NEXUS_GUI_ID = 76;

	// ContiniousMode config
	public static int MIN_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE;
	public static int MAX_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE;

	// Nightspawn config
	public static boolean NIGHTSPAWNS_ENABLED;
	public static int MOB_LIMIT_OVERRIDE;
	public static int NIGHTSPAWNS_MOB_SIGHTRANGE = 20;
	public static int NIGHTSPAWNS_MOB_SENSERANGE = 12;
	public static int NIGHTSPAWNS_MOB_SPAWNCHANCE = 30;
	public static int NIGHTSPAWNS_MOB_MAX_GROUPSIZE = 3;
	public static boolean NIGHTSPAWNS_MOB_BURN_DURING_DAY = true;

	public static class Section
	{
		public final String name;

		public Section(String name, String lang)
		{
			this.name = name;
			this.register();
		}

		public void register()
		{
			sections.add(this);
		}

		public String getName()
		{
			return this.name.toLowerCase();
		}
	}

	public static void load(FMLPreInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new Config());
		configDirectory = event.getModConfigurationDirectory();
		if (!configDirectory.exists())
		{
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, "invasionmod.cfg");
		config = new Configuration(configFile);
		syncConfig();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{

		if (event.getModID().equals(Reference.MODID))
		{
			ModLogger.logInfo("Updating config...");
			syncConfig();
		}
	}

	public static void syncConfig()
	{
		try
		{
			// General config
			UPDATE_MESSAGES = config
				.getBoolean(
					"update messages",
					general.getName(),
					true,
					"Check if you are running the latest version of the invasion mod each time you start the game.");
			DROP_DESTRUCTED_BLOCKS = config
				.getBoolean("drop destructed blocks", general.getName(),
					true,
					"Drop the blocks which are destroyed by invasion mod monsters.");
			DEBUG = config.getBoolean("debug-enabled", general.getName(), true,
				"Enable debug mode, for testers and developers.");

			// ContiniousMode config
			MIN_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE = config.getInt("min days between attacks", continiousMode.getName(), 2, 0, Integer.MAX_VALUE, "The minimum amount of days between attacks in continious mode.");
			MAX_DAYS_BETWEEN_ATTACKS_CONTINIOUS_MODE = config.getInt("max days between attacks", continiousMode.getName(), 3, 0, Integer.MAX_VALUE, "The maximum amount of days between attacks in continious mode.");

			// Nightspawns config
			NIGHTSPAWNS_ENABLED = config.getBoolean("nightspawns enabled", nightSpawns.getName(), false, "Invasion mod mobs spawn at night without the presence of an active nexus.");
			MOB_LIMIT_OVERRIDE = config.getInt("mob limit override", nightSpawns.getName(), 100, 0, Integer.MAX_VALUE, "description");
			NIGHTSPAWNS_MOB_SIGHTRANGE = config.getInt("mob sight range", nightSpawns.getName(), 20, 0, Integer.MAX_VALUE, "description");
			NIGHTSPAWNS_MOB_SENSERANGE = config.getInt("mob sense range", nightSpawns.getName(), 12, 0, Integer.MAX_VALUE, "description");
			NIGHTSPAWNS_MOB_SPAWNCHANCE = config.getInt("mob spawnchance", nightSpawns.getName(), 30, 0, 100, "description");
			NIGHTSPAWNS_MOB_MAX_GROUPSIZE = config.getInt("mob max groupsize", nightSpawns.getName(), 3, 0, Integer.MAX_VALUE, "description");
			NIGHTSPAWNS_MOB_BURN_DURING_DAY = config.getBoolean("mob sunscreen", nightSpawns.getName(), true, "description");

			ModLogger.logInfo("Loaded configuration file!");
		}
		catch (Exception e)
		{
			ModLogger.logFatal("Failed to load configuration file!");
			e.printStackTrace();
		}
		finally
		{
			if (config.hasChanged())
			{
				config.save();
			}

		}
	}
}