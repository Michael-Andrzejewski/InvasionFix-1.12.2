package invmod;

import static invmod.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(Reference.MODID)
public class ModItems {

	//TRAPS
	public static final Item TRAP_EMPTY = Null();
	public static final Item TRAP_FLAME = Null();
	public static final Item TRAP_POISON = Null();
	public static final Item TRAP_RIFT = Null();
	
	//Crafting shit
	public static final Item CATALYST_MIXTURE = Null();
	public static final Item DAMPING_AGENT = Null();
	public static final Item DEBUG_WAND = Null();
	public static final Item ENGY_HAMMER = Null();
	public static final Item INFUSED_SWORD = Null();
	public static final Item NEXUS_CATALYST = Null();
	public static final Item PHASE_CRYSTAL = Null();
	public static final Item PROBE = Null();
	public static final Item RIFT_FLUX = Null();
	public static final Item SEARING_BOW = Null();
	public static final Item SMALL_REMNANTS = Null();
	public static final Item STABLE_CATALYST_MIXTURE = Null();
	public static final Item STABLE_NEXUS_CATALYST = Null();
	public static final Item STRANGE_BONE = Null();
	public static final Item STRONG_CATALYST = Null();
	public static final Item STRONG_DAMPING_AGENT = Null();
	
	@EventBusSubscriber(modid = Reference.MODID)
	public static class ItemRegistrationHandler {

		public static final List<Item> ITEMS = new ArrayList<Item>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					//setItemName(new ItemDagger(ModMaterials.ToolMaterials.TOOL_IRON_DAGGER, 25), "dagger_iron"),
			};

			IForgeRegistry<Item> registry = event.getRegistry();

			for (Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, mod_Invasion.tabInvmod);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable CreativeTabs tab) {
			return item.setUnlocalizedName(name).setRegistryName(Reference.MODID, name).setCreativeTab(tab);
		}
	}
}
