// `^`^`^`
// ```
// /*
//  * Executive Summary: ModItemModels Class
//  * 
//  * Purpose:
//  * The ModItemModels class is designed to handle the registration of item models for a Minecraft mod during the model registration event. It ensures that all custom items and item blocks introduced by the mod are properly displayed with their respective models in the game. This class is part of the client-side code and is specifically used to define how items should be visually represented.
//  * 
//  * Methods:
//  * - registerItemModels(ModelRegistryEvent event): This static method is triggered during the ModelRegistryEvent. It iterates through all items and item blocks provided by the mod and registers their models if they have not been registered already. This method ensures that each item has a corresponding model resource location, which is necessary for the item to be rendered correctly in the game.
//  * 
//  * - registerItemModel(Item item): A helper method that calls registerCustomItemModel with default parameters for the item's model resource location and the 'inventory' variant. It simplifies the process of registering a model for an item without custom metadata or variants.
//  * 
//  * - registerCustomItemModel(Item item, int meta, String modelLocation, String variant): This method sets a custom model resource location for an item with specific metadata (meta) and variant. It also adds the item to the list of registered item models to prevent duplicate registrations.
//  * 
//  * Usage:
//  * This class is annotated with @EventBusSubscriber to automatically subscribe its event methods to the Forge event bus. It is intended to be used in the initialization phase of a Minecraft mod where item models are registered to the game.
//  * 
//  * Note:
//  * The class relies on lists of items and item blocks from ModItems and ModBlocks, which are assumed to be part of the mod's content. It also uses a static list to keep track of already registered models to optimize the registration process.
//  */
// ```
// `^`^`^`

package invmod.client;

import java.util.ArrayList;
import java.util.List;

import invmod.ModBlocks;
import invmod.ModItems;
import invmod.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
public class ModItemModels {

	private static final List<Item> REGISTERED_ITEM_MODELS = new ArrayList<Item>();

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) {

		// register all other item models
		for (Item item : ModItems.ItemRegistrationHandler.ITEMS) {
			if (!REGISTERED_ITEM_MODELS.contains(item)) {
				registerItemModel(item);
			}
		}

		// register all other item block models
		for (ItemBlock itemBlock : ModBlocks.BlockRegistrationHandler.ITEM_BLOCKS) {
			if (!REGISTERED_ITEM_MODELS.contains(itemBlock)) {
				registerItemModel(itemBlock);
			}
		}
	}

	private static void registerItemModel(Item item) {
		registerCustomItemModel(item, 0, item.getRegistryName().toString(), "inventory");
	}

	private static void registerCustomItemModel(Item item, int meta, String modelLocation, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelLocation, variant));
		REGISTERED_ITEM_MODELS.add(item);
	}

}
