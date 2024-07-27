// `^`^`^`
// ```java
// /**
//  * This Java file is part of the invmod package and is responsible for defining and registering custom items for the mod.
//  * The ModItems class contains static references to various items that will be used in the game, such as traps and crafting materials.
//  * 
//  * The items are categorized into traps and crafting materials, each with a corresponding ObjectHolder annotation to facilitate their retrieval.
//  * 
//  * The ItemRegistrationHandler is a nested static class that subscribes to the Forge event bus to handle item registration events.
//  * It maintains a list of all custom items and registers them during the game's initialization phase.
//  * 
//  * Key methods in ItemRegistrationHandler:
//  * - registerItems: Called by Forge during the item registration event. It initializes an array of custom items, sets their names and creative tabs, and registers them to the Forge registry.
//  * - setItemName: Helper method to set the unlocalized name of an item.
//  * - setItemNameAndTab: Helper method to set both the unlocalized name and the creative tab of an item.
//  * 
//  * The items are intended to be used within the mod's gameplay, providing players with new traps and crafting components that enhance the game's complexity and options.
//  */
// package invmod;
// 
// // Import statements...
// 
// @ObjectHolder(Reference.MODID)
// public class ModItems {
//     // Static item references with ObjectHolder annotations...
//     
//     @EventBusSubscriber(modid = Reference.MODID)
//     public static class ItemRegistrationHandler {
//         // Static list to hold custom items...
//         
//         @SubscribeEvent
//         public static void registerItems(RegistryEvent.Register<Item> event) {
//             // Item registration logic...
//         }
//         
//         // Helper methods for item registration...
//     }
// }
// ```
// `^`^`^`

package invmod;

import static invmod.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import invmod.item.ItemCatalystMixture;
import invmod.item.ItemDampingAgent;
import invmod.item.ItemDebugWand;
import invmod.item.ItemEngyHammer;
import invmod.item.ItemInfusedSword;
import invmod.item.ItemNexusCatalyst;
import invmod.item.ItemPhaseCrystal;
import invmod.item.ItemProbe;
import invmod.item.ItemRiftFlux;
import invmod.item.ItemSearingBow;
import invmod.item.ItemSmallRemnants;
import invmod.item.ItemStableCatalystMixture;
import invmod.item.ItemStableNexusCatalyst;
import invmod.item.ItemStrangeBone;
import invmod.item.ItemStrongCatalyst;
import invmod.item.ItemStrongCatalystMixture;
import invmod.item.ItemStrongDampingAgent;
import invmod.item.trap.ItemEmptyTrap;
import invmod.item.trap.ItemFlameTrap;
import invmod.item.trap.ItemPoisonTrap;
import invmod.item.trap.ItemRiftTrap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModItems {

	// TRAPS
	@ObjectHolder("emptytrap")
	public static final Item TRAP_EMPTY = Null();
	@ObjectHolder("flametrap")
	public static final Item TRAP_FLAME = Null();
	@ObjectHolder("poisontrap")
	public static final Item TRAP_POISON = Null();
	@ObjectHolder("rifttrap")
	public static final Item TRAP_RIFT = Null();

	// Crafting shit
	@ObjectHolder("catalystmixture")
	public static final Item CATALYST_MIXTURE = Null();
	@ObjectHolder("dampingagent")
	public static final Item DAMPING_AGENT = Null();
	@ObjectHolder("debugwand")
	public static final Item DEBUG_WAND = Null();
	@ObjectHolder("engyhammer")
	public static final Item ENGY_HAMMER = Null();
	@ObjectHolder("infusedsword")
	public static final Item INFUSED_SWORD = Null();
	@ObjectHolder("nexuscatalyst")
	public static final Item NEXUS_CATALYST = Null();
	@ObjectHolder("phasecrystal")
	public static final Item PHASE_CRYSTAL = Null();
	@ObjectHolder("probe")
	public static final Item PROBE = Null();
	@ObjectHolder("riftflux")
	public static final Item RIFT_FLUX = Null();
	@ObjectHolder("searingbow")
	public static final Item SEARING_BOW = Null();
	@ObjectHolder("smallremnants")
	public static final Item SMALL_REMNANTS = Null();
	@ObjectHolder("stablecatalystmixture")
	public static final Item STABLE_CATALYST_MIXTURE = Null();
	@ObjectHolder("stablenexuscatalyst")
	public static final Item STABLE_NEXUS_CATALYST = Null();
	@ObjectHolder("strangebone")
	public static final Item STRANGE_BONE = Null();
	@ObjectHolder("strongcatalyst")
	public static final Item STRONG_CATALYST = Null();
	@ObjectHolder("strongcatalystmixture")
	public static final Item STRONG_CATALYST_MIXTURE = Null();
	@ObjectHolder("strongdampingagent")
	public static final Item STRONG_DAMPING_AGENT = Null();

	@EventBusSubscriber(modid = Reference.MODID)
	public static class ItemRegistrationHandler {

		public static final List<Item> ITEMS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					// setItemName(new ItemDagger(ModMaterials.ToolMaterials.TOOL_IRON_DAGGER, 25),
					// "dagger_iron"),
					setItemName(new ItemEmptyTrap(), "emptytrap"), setItemName(new ItemFlameTrap(), "flametrap"),
					setItemName(new ItemPoisonTrap(), "poisontrap"), setItemName(new ItemRiftTrap(), "rifttrap"),

					setItemName(new ItemCatalystMixture(), "catalystmixture"),
					setItemName(new ItemDampingAgent(), "dampingagent"), setItemName(new ItemDebugWand(), "debugwand"),
					setItemName(new ItemEngyHammer(), "engyhammer"),
					setItemName(new ItemInfusedSword(), "infusedsword"),
					setItemName(new ItemNexusCatalyst(), "nexuscatalyst"),
					setItemName(new ItemPhaseCrystal(), "phasecrystal"), setItemName(new ItemProbe(), "probe"),
					setItemName(new ItemRiftFlux(), "riftflux"), setItemName(new ItemSearingBow(), "searingbow"),
					setItemName(new ItemSmallRemnants(), "smallremnants"),
					setItemName(new ItemStableCatalystMixture(), "stablecatalystmixture"),
					setItemName(new ItemStableNexusCatalyst(), "stablenexuscatalyst"),
					setItemName(new ItemStrangeBone(), "strangebone"),
					setItemName(new ItemStrongCatalyst(), "strongcatalyst"),
					setItemName(new ItemStrongCatalystMixture(), "strongcatalystmixture"),
					setItemName(new ItemStrongDampingAgent(), "strongdampingagent")

			};

			IForgeRegistry<Item> registry = event.getRegistry();

			for (Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, mod_invasion.tabInvmod);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable CreativeTabs tab) {
			return item.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
		}
	}
}
