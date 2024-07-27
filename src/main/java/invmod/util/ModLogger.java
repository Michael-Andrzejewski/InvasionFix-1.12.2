// `^`^`^`
// ```
// /*
//  * Executive Documentation Summary:
//  * 
//  * The ModLogger class serves as a centralized logging utility for a software module, providing a consistent interface for logging messages at various levels of severity. The class is part of the 'invmod.util' package and utilizes the Apache Log4j logging library for its functionality.
//  * 
//  * The class contains a static Logger instance, which is configured using a module identifier from the 'Reference' class. This ensures that all log messages are appropriately tagged with the module's ID for easy identification in log files.
//  * 
//  * Methods:
//  * - logInfo(String message): Logs an informational message. This is typically used for routine events that are useful to be aware of.
//  * - logWarn(String message): Logs a warning message. This indicates a potential issue that does not prevent the program from continuing but should be looked into.
//  * - logFatal(String message): Logs a fatal message. This is used for severe errors that will presumably abort the program execution.
//  * - logDebug(String message): Logs a debug message. This is useful for detailed troubleshooting during development and is not usually enabled in production.
//  * - log(Level level, String message): Logs a message with a custom log level. This method provides flexibility to log messages at arbitrary levels defined by the Level parameter.
//  * 
//  * This class is intended to be used by other parts of the software to log messages related to the module's operation, aiding in debugging and monitoring.
//  */
// ```
// `^`^`^`

package invmod.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import invmod.Reference;

public final class ModLogger {
	private static Logger logger = LogManager.getLogger(Reference.MODID);

	public static void logInfo(String message) {
		logger.log(Level.INFO, message);
	}

	public static void logWarn(String message) {
		logger.log(Level.WARN, message);
	}

	public static void logFatal(String message) {
		logger.log(Level.FATAL, message);
	}

	public static void logDebug(String message) {
		logger.log(Level.DEBUG, message);
	}

	public static void log(Level level, String message) {
		logger.log(level, message);
	}
}