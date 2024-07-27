// `^`^`^`
// ```java
// /**
//  * This class is responsible for checking if there is a newer version of the Invasion mod available and informing the player about updates.
//  * It is marked as deprecated, indicating that it may no longer be the preferred method for version checking and could be removed in future releases.
//  *
//  * Methods:
//  * - checkForUpdates(EntityPlayerMP entityplayer): Checks if there is a newer version of the mod and sends update messages to the player.
//  * - getLatestVersion(): Retrieves the latest version number of the mod from a predefined URL.
//  * - getLatestNews(): Fetches the latest news or change log associated with the mod from a predefined URL.
//  * - getHTML(String urlToRead): Makes an HTTP GET request to the given URL and returns the content as a list of strings.
//  * - merge(List<String> text): Concatenates a list of strings into a single string.
//  *
//  * Note: The methods use a hardcoded URL to fetch version information, which is not a recommended practice due to potential security risks and lack of flexibility.
//  * Additionally, the exception handling is minimal, and there is no logging or user feedback provided in case of an error, except within the getHTML method.
//  */
// ```
// `^`^`^`

package invmod.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import invmod.Reference;
import invmod.mod_invasion;
import invmod.util.config.Config;
import net.minecraft.entity.player.EntityPlayerMP;

public class VersionChecker {

	@Deprecated
	public static boolean checkForUpdates(EntityPlayerMP entityplayer) {
		String latestVersionNumber = getLatestVersion();
		String latestNews = getLatestNews();
		try {
			if (Config.UPDATE_MESSAGES && latestVersionNumber != null && latestNews != null) {
				if (!latestVersionNumber.equals("null")) {
					if (Version.get(latestVersionNumber).comparedState(Reference.versionNumber) == 1) {
						mod_invasion.sendMessageToPlayer(entityplayer,
								"Invasion mod outdated, consider updating to the latest version");
						mod_invasion.sendMessageToPlayer(entityplayer,
								"Changes in v" + latestVersionNumber + ": " + latestNews);
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	// Get and return latest version
	@Deprecated
	public static String getLatestVersion() {
		String[] text = merge(getHTML("https://dl.dropboxusercontent.com/u/96357007/invasion_mod/Invasion_mod.txt"))
				.split(":");
		if (!text[0].contains("UTF-8") && !text[0].contains("HTML") && !text[0].contains("http"))
			return text[0];
		return "null";
	}

	// Get and return recent News
	@Deprecated
	public static String getLatestNews() {
		String[] text = merge(getHTML("https://dl.dropboxusercontent.com/u/96357007/invasion_mod/Invasion_mod.txt"))
				.split(":");
		if (text.length > 1 && !text[1].contains("UTF-8") && !text[1].contains("HTML") && !text[1].contains("http"))
			return text[1];
		return "null";
	}

	public static List<String> getHTML(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		List<String> result = new ArrayList<String>();

		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = rd.readLine()) != null) {
				result.add(line.trim());
			}

			rd.close();
		} catch (Exception e) {
			result.clear();
			result.add("null");
			ModLogger.logWarn("An error occured while connecting to URL '" + urlToRead + ".'");
		}

		return result;
	}

	public static String merge(List<String> text) {
		StringBuilder builder = new StringBuilder();

		for (String s : text) {
			builder.append(s);
		}

		return builder.toString();
	}
}
