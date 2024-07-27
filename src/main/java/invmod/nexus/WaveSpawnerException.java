// `^`^`^`
// ```java
// /**
//  * This file defines the WaveSpawnerException class within the invmod.nexus package.
//  * The WaveSpawnerException is a custom exception class that extends the standard Java Exception class.
//  * It is specifically designed to represent exceptions that can occur within the wave spawning system of a game or application.
//  *
//  * Constructors:
//  * - WaveSpawnerException(String message): Constructs a new WaveSpawnerException with a detailed error message.
//  *   The message provides information about the nature of the error that occurred during the wave spawning process.
//  *
//  * Usage:
//  * This exception should be thrown to indicate problems specifically related to the wave spawning mechanism,
//  * such as invalid wave configurations, timing issues, or resource allocation failures. By extending Exception,
//  * it allows for these specific types of errors to be caught and handled distinctly from other generic exceptions.
//  */
// ```
// 
// This summary provides an overview of the purpose and usage of the `WaveSpawnerException` class, detailing its constructor and the context in which it should be used.
// `^`^`^`

package invmod.nexus;

public class WaveSpawnerException extends Exception {
	public WaveSpawnerException(String message) {
		super(message);
	}
}