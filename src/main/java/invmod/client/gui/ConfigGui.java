// `^`^`^`
// ```java
// /**
//  * This code is part of the invmod client GUI package and is responsible for creating a custom configuration GUI for the mod.
//  * It extends the GuiConfig class from Minecraft Forge, allowing users to modify the mod's settings from within the game.
//  *
//  * Classes and Methods:
//  * - ConfigGui: This is the main class that represents the configuration GUI for the mod. It is a subclass of GuiConfig.
//  *   - ConfigGui(GuiScreen parentScreen): Constructor that initializes the GUI with configuration elements, mod ID, and the title.
//  *     It takes a parentScreen parameter to return to the previous screen when the configuration GUI is closed.
//  *
//  * - getConfigElements(): A private static method that generates a list of configuration elements to be displayed in the GUI.
//  *   It iterates through the sections defined in the mod's configuration and creates a ConfigElement for each category.
//  *
//  * Dependencies:
//  * - The code imports various classes from Minecraft and Forge to create the GUI elements and handle the configuration.
//  * - It relies on the Config class from the invmod.util.config package to access the mod's configuration sections and categories.
//  * - The Reference class from invmod is used to obtain the mod's ID.
//  * - Localization is handled through the I18n class to provide a localized title for the GUI.
//  *
//  * Usage:
//  * - This GUI is intended to be accessed in-game, providing a user-friendly interface for configuring the mod's settings.
//  * - The configuration changes made through this GUI are reflected in the mod's behavior during gameplay.
//  */
// ```
// `^`^`^`

package invmod.client.gui;

import java.util.ArrayList;
import java.util.List;

import invmod.Reference;
import invmod.util.config.Config;
import invmod.util.config.Config.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig {

	public ConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), Reference.MODID, false, false,
				I18n.format("gui.invasion.config.title"));
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : Config.sections) {
			list.add(new ConfigElement(Config.config.getCategory(section.getName())));
		}

		return list;
	}
}
