// `^`^`^`
// ```java
// /**
//  * This class represents a Wave in a game, managing the timing and spawning of entities during the wave.
//  * It tracks the progress of the wave, including the total time, break time, and elapsed time.
//  *
//  * Constructor:
//  * Wave(int waveTotalTime, int waveBreakTime, List<WaveEntry> entries) - Initializes a new wave with specified total time, break time, and a list of WaveEntry objects.
//  *
//  * Methods:
//  * addEntry(WaveEntry entry) - Adds a new WaveEntry to the wave.
//  * doNextSpawns(int elapsedMillis, ISpawnerAccess spawner) - Spawns entities based on elapsed time and updates the number of entities spawned.
//  * getTimeInWave() - Returns the amount of time elapsed since the wave started.
//  * getWaveTotalTime() - Returns the total time allocated for the wave.
//  * getWaveBreakTime() - Returns the break time before the next wave starts.
//  * isComplete() - Checks if the wave duration has been completed.
//  * resetWave() - Resets the wave's elapsed time and WaveEntry states to the beginning.
//  * setWaveToTime(int millis) - Sets the wave's elapsed time to a specific value.
//  * getTotalMobAmount() - Calculates the total number of entities to be spawned by summing up the amounts in all WaveEntries.
//  *
//  * This class is essential for controlling the flow of a game's wave-based entity spawning system.
//  */
// package invmod.nexus;
// 
// // ... (rest of the code)
// ```
// `^`^`^`

package invmod.nexus;

import java.util.List;

public class Wave {
	private List<WaveEntry> entries;
	private int elapsed;
	private int waveTotalTime;
	private int waveBreakTime;

	public Wave(int waveTotalTime, int waveBreakTime, List<WaveEntry> entries) {
		this.entries = entries;
		this.waveTotalTime = waveTotalTime;
		this.waveBreakTime = waveBreakTime;
		this.elapsed = 0;
	}

	public void addEntry(WaveEntry entry) {
		this.entries.add(entry);
	}

	public int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner) {
		int numberOfSpawns = 0;
		this.elapsed += elapsedMillis;
		for (WaveEntry entry : this.entries) {
			if ((this.elapsed >= entry.getTimeBegin()) && (this.elapsed < entry.getTimeEnd())) {
				numberOfSpawns += entry.doNextSpawns(elapsedMillis, spawner);
			}
		}
		return numberOfSpawns;
	}

	public int getTimeInWave() {
		return this.elapsed;
	}

	public int getWaveTotalTime() {
		return this.waveTotalTime;
	}

	public int getWaveBreakTime() {
		return this.waveBreakTime;
	}

	public boolean isComplete() {
		return this.elapsed > this.waveTotalTime;
	}

	public void resetWave() {
		this.elapsed = 0;
		for (WaveEntry entry : this.entries) {
			entry.resetToBeginning();
		}
	}

	public void setWaveToTime(int millis) {
		this.elapsed = millis;
	}

	public int getTotalMobAmount() {
		int total = 0;
		for (WaveEntry entry : this.entries) {
			total += entry.getAmount();
		}
		return total;
	}
}