// `^`^`^`
// ```java
// /**
//  * SoundHandler for Minecraft Mod
//  * 
//  * This class is part of the 'invmod' package and is responsible for handling the registration of custom sound events for a Minecraft mod. It utilizes the Forge modding API to interact with Minecraft's sound system.
//  * 
//  * Public Static Variables:
//  * - bigzombie1, chime1, egghatch1, etc.: Static SoundEvent variables representing various custom sounds to be used in the game.
//  * 
//  * Private Static Variables:
//  * - size: An integer tracking the number of registered sounds (currently unused).
//  * 
//  * Public Methods:
//  * - init(): Initializes and registers all the custom sound events with their respective names. This method should be called during the mod initialization phase to ensure all custom sounds are available in the game.
//  * 
//  * Private Methods:
//  * - registerSound(String name): Takes a sound name as a parameter and creates a new SoundEvent with a ResourceLocation based on the mod's ID and the sound name. It then registers the SoundEvent with Forge's SOUND_EVENTS registry and returns the SoundEvent object.
//  * 
//  * Note: The commented-out 'register' method appears to be an older version of the sound registration process and is not currently in use.
//  * 
//  * Usage:
//  * This class should be utilized within the mod to register and manage custom sound events, which can then be played in various situations within the game, such as when an entity performs an action or an event occurs.
//  */
// package invmod;
// 
// // Import statements and class definition...
// ```
// 
// `^`^`^`

package invmod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundHandler {

	public static SoundEvent bigzombie1;
	public static SoundEvent chime1;
	public static SoundEvent egghatch1;
	public static SoundEvent egghatch2;
	public static SoundEvent fireball1;
	public static SoundEvent rumble1;
	public static SoundEvent scrape1;
	public static SoundEvent scrape2;
	public static SoundEvent scrape3;
	public static SoundEvent v_death1;
	public static SoundEvent v_hiss1;
	public static SoundEvent v_longscreech1;
	public static SoundEvent v_screech1;
	public static SoundEvent v_screech2;
	public static SoundEvent v_screech3;
	public static SoundEvent v_squawk1;
	public static SoundEvent v_squawk2;
	public static SoundEvent v_squawk3;
	public static SoundEvent v_squawk4;
	public static SoundEvent zap1;
	public static SoundEvent zap2;
	public static SoundEvent zap3;

	private static int size = 0;

	public static void init() {
		bigzombie1 = registerSound("bigzombie1");
		chime1 = registerSound("chime1");
		egghatch1 = registerSound("egghatch1");
		egghatch2 = registerSound("egghatch2");
		fireball1 = registerSound("fireball1");
		rumble1 = registerSound("rumble1");
		scrape1 = registerSound("scrape1");
		scrape2 = registerSound("scrape2");
		scrape3 = registerSound("scrape3");
		v_death1 = registerSound("v_death1");
		v_hiss1 = registerSound("v_hiss1");
		v_longscreech1 = registerSound("v_longscreech1");
		v_screech1 = registerSound("v_screech1");
		v_screech2 = registerSound("v_screech2");
		v_screech3 = registerSound("v_screech3");
		v_squawk1 = registerSound("v_squawk1");
		v_squawk2 = registerSound("v_squawk2");
		v_squawk3 = registerSound("v_squawk3");
		v_squawk4 = registerSound("v_squawk4");
		zap1 = registerSound("zap1");
		zap2 = registerSound("zap2");
		zap3 = registerSound("zap3");
	}

	/*
	 * public static SoundEvent register(String soundLocation) { ResourceLocation
	 * resource = new ResourceLocation(Reference.MODID + ":" + soundLocation);
	 * SoundEvent snd = new SoundEvent(resource); SoundEvent.REGISTRY.register(size,
	 * resource, snd); size++; return snd; }
	 */

	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}

}
