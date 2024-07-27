// `^`^`^`
// ```java
// /**
//  * Executive Summary:
//  * This code is part of the 'invmod' package, which is likely a mod for a game, possibly Minecraft, given the naming conventions used. The class 'Reference' serves as a central repository for constant values that define the mod's metadata and configuration settings for the game's mod loading system. These constants include the mod's name, ID, version, and various class paths for proxies and GUI factories. The class does not contain any methods but provides static final strings that are presumably used elsewhere in the mod to reference these key pieces of information consistently.
//  *
//  * Class Details:
//  * - NAME: A string constant holding the name of the mod, "Invasion".
//  * - MODID: A string constant representing the unique identifier for the mod, "mod_invasion".
//  * - VERSION: A string constant indicating the version of the mod, "0.0.1".
//  * - versionNumber: A Version object initialized with the version details, used for more complex version comparisons.
//  * - GUIFACTORY: A string constant specifying the fully qualified name of the GUI factory class, used for configuring custom GUI elements.
//  * - DEPEND: A string constant that would list mod dependencies if any were required, currently empty.
//  * - COMMONPROXY: A string constant indicating the fully qualified name of the common proxy class, used for server-side operations.
//  * - CLIENTPROXY: A string constant indicating the fully qualified name of the client proxy class, used for client-side operations.
//  * - PREFIX: A string constant that defines a prefix used in the mod, "invasion", which might be used for naming conventions in resources such as textures or configuration files.
//  */
// ```
// `^`^`^`

package invmod;

import invmod.util.Version;

public class Reference {

	public static final String NAME = "Invasion";
	public static final String MODID = "mod_invasion";
	public static final String VERSION = "0.0.1";
	public static Version versionNumber = new Version(0, 0, 1);
	public static final String GUIFACTORY = "invmod.client.gui.ConfigGuiFactory";
	public static final String DEPEND = "";
	public static final String COMMONPROXY = "invmod.common.ProxyCommon";
	public static final String CLIENTPROXY = "invmod.client.ProxyClient";
	public static final String PREFIX = "invasion";
}
