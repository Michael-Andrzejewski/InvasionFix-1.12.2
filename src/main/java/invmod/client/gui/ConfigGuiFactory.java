// `^`^`^`
// ```java
// /**
//  * Executive Summary:
//  * This code defines a configuration GUI factory for a Minecraft mod. The factory is responsible for creating and managing the GUI elements that allow users to interact with the mod's settings. The code is part of the 'invmod' package, specifically within the 'client.gui' subpackage, indicating its role in the client-side graphical user interface.
//  *
//  * Class: ConfigGuiFactory
//  * Implements: IModGuiFactory
//  * Purpose: To provide a custom configuration GUI for the mod, which integrates with Minecraft's mod configuration system.
//  *
//  * Methods:
//  * - initialize(Minecraft minecraftInstance): A method required by IModGuiFactory, but not implemented here. It's meant to initialize the factory with the Minecraft instance.
//  * - createConfigGui(GuiScreen parentScreen): Creates and returns an instance of ConfigGui, which is the actual GUI screen used for mod configuration. It takes the current screen as a parameter to allow for hierarchical navigation.
//  * - runtimeGuiCategories(): Another method from IModGuiFactory that returns a set of runtime option category elements. It is not used in this implementation and returns null.
//  * - hasConfigGui(): A simple method that indicates the presence of a custom config GUI. It returns true, signifying that this mod has a custom GUI for configuration.
//  *
//  * Note: There are commented-out methods (mainConfigGuiClass and getHandlerFor) which suggest potential extensions or previous implementations for handling the configuration GUI class and runtime option handlers, respectively.
//  */
// ```
// `^`^`^`

package invmod.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ConfigGui(parentScreen);
	}

	/*
	 * @Override public Class<? extends GuiScreen> mainConfigGuiClass() { return
	 * ConfigGui.class; }
	 */

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	/*
	 * @Override public RuntimeOptionGuiHandler
	 * getHandlerFor(RuntimeOptionCategoryElement element) { return null; }
	 */

	@Override
	public boolean hasConfigGui() {
		return true;
	}
}
