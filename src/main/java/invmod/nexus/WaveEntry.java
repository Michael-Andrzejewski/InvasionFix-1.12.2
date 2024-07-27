// `^`^`^`
// ```java
// /**
//  * This class represents a wave entry for a game, managing the spawning of entities within a specified time frame.
//  * It handles the scheduling, spawning, and alerting mechanisms for entities during a wave event.
//  *
//  * Public Methods:
//  * - WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool): 
//  *   Constructs a wave entry with a specified time range, amount of entities, granularity, and a pool of entity patterns.
//  * - WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool, 
//  *   int angleRange, int minPointsInRange): 
//  *   Constructs a wave entry with an additional angle range and minimum spawn points in range.
//  * - WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool, 
//  *   int minAngle, int maxAngle, int minPointsInRange): 
//  *   Constructs a wave entry with specified minimum and maximum angles for spawning.
//  * - int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner): 
//  *   Spawns entities based on elapsed time and updates the spawn list.
//  * - void resetToBeginning(): 
//  *   Resets the wave entry to its initial state.
//  * - void setToTime(int millis): 
//  *   Sets the elapsed time to a specific value.
//  * - int getTimeBegin(): 
//  *   Returns the start time of the wave.
//  * - int getTimeEnd(): 
//  *   Returns the end time of the wave.
//  * - int getAmount(): 
//  *   Returns the total amount of entities to spawn.
//  * - int getGranularity(): 
//  *   Returns the granularity of the spawn timing.
//  * - void addAlert(String message, int timeElapsed): 
//  *   Adds an alert message to be triggered at a specific elapsed time.
//  * - String toString(): 
//  *   Returns a string representation of the wave entry.
//  *
//  * Private Methods:
//  * - void sendNextAlert(ISpawnerAccess spawner): 
//  *   Sends the next alert message if the alert time has been reached.
//  * - void reviseSpawnAngles(ISpawnerAccess spawner): 
//  *   Revises the spawn angles if the current angles do not have enough spawn points.
//  *
//  * The class uses a combination of time tracking, angle calculations, and spawn point validation to manage the spawning
//  * process effectively. It also includes alerting functionality to notify about specific events during the wave.
//  */
// ```
// `^`^`^`

package invmod.nexus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import invmod.util.ISelect;
import invmod.util.ModLogger;

public class WaveEntry {
	private int timeBegin;
	private int timeEnd;
	private int amount;
	private int granularity;
	private int amountQueued;
	private int elapsed;
	private int toNextSpawn;
	private int minAngle;
	private int maxAngle;
	private int minPointsInRange;
	private int nextAlert;
	private ISelect<IEntityIMPattern> mobPool;
	private List<EntityConstruct> spawnList;
	private Map<Integer, String> alerts;

	public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool) {
		this(timeBegin, timeEnd, amount, granularity, mobPool, -180, 180, 1);
	}

	public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool,
			int angleRange, int minPointsInRange) {
		this(timeBegin, timeEnd, amount, granularity, mobPool, 0, 0, minPointsInRange);
		this.minAngle = (new Random().nextInt(360) - 180);
		this.maxAngle = (this.minAngle + angleRange);
		while (this.maxAngle > 180)
			this.maxAngle -= 360;
	}

	public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool,
			int minAngle, int maxAngle, int minPointsInRange) {
		this.spawnList = new ArrayList();
		this.alerts = new HashMap();
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.amount = amount;
		this.granularity = granularity;
		this.mobPool = mobPool;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.minPointsInRange = minPointsInRange;
		this.amountQueued = 0;
		this.elapsed = 0;
		this.toNextSpawn = 0;
		this.nextAlert = 2147483647;
	}

	public int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner) {
		this.toNextSpawn -= elapsedMillis;
		if (this.nextAlert <= this.elapsed - this.toNextSpawn) {
			this.sendNextAlert(spawner);
		}

		if (this.toNextSpawn <= 0) {
			this.elapsed += this.granularity;
			this.toNextSpawn += this.granularity;
			if (this.toNextSpawn < 0) {
				this.elapsed -= this.toNextSpawn;
				this.toNextSpawn = 0;
			}

			int amountToSpawn = Math.round(this.amount * this.elapsed / (this.timeEnd - this.timeBegin))
					- this.amountQueued;
			if (amountToSpawn > 0) {
				if (amountToSpawn + this.amountQueued > this.amount) {
					amountToSpawn = this.amount - this.amountQueued;
				}
				while (amountToSpawn > 0) {
					IEntityIMPattern pattern = this.mobPool.selectNext();
					if (pattern != null) {
						EntityConstruct mobConstruct = pattern.generateEntityConstruct(this.minAngle, this.maxAngle);
						if (mobConstruct != null) {
							amountToSpawn--;
							this.amountQueued += 1;
							this.spawnList.add(mobConstruct);
						}
					} else {
						ModLogger.logWarn("A selection pool in wave entry " + this.toString() + " returned empty");
						ModLogger.logWarn("Pool: " + this.mobPool.toString());
					}
				}
			}
		}

		if (this.spawnList.size() > 0) {
			int numberOfSpawns = 0;
			if (spawner.getNumberOfPointsInRange(this.minAngle, this.maxAngle,
					SpawnType.HUMANOID) >= this.minPointsInRange) {
				for (int i = this.spawnList.size() - 1; i >= 0; i--) {
					if (spawner.attemptSpawn(this.spawnList.get(i), this.minAngle, this.maxAngle)) {
						numberOfSpawns++;
						this.spawnList.remove(i);
					}
				}
			} else {
				this.reviseSpawnAngles(spawner);
			}
			return numberOfSpawns;
		}
		return 0;
	}

	public void resetToBeginning() {
		this.elapsed = 0;
		this.amountQueued = 0;
		this.mobPool.reset();
	}

	public void setToTime(int millis) {
		this.elapsed = millis;
	}

	public int getTimeBegin() {
		return this.timeBegin;
	}

	public int getTimeEnd() {
		return this.timeEnd;
	}

	public int getAmount() {
		return this.amount;
	}

	public int getGranularity() {
		return this.granularity;
	}

	public void addAlert(String message, int timeElapsed) {
		this.alerts.put(Integer.valueOf(timeElapsed), message);
		if (timeElapsed < this.nextAlert)
			this.nextAlert = timeElapsed;
	}

	@Override
	public String toString() {
		return "WaveEntry@" + Integer.toHexString(this.hashCode()) + "#time=" + this.timeBegin + "-" + this.timeEnd
				+ "#amount=" + this.amount;
	}

	private void sendNextAlert(ISpawnerAccess spawner) {
		String message = this.alerts.remove(Integer.valueOf(this.nextAlert));
		if (message != null) {
			spawner.sendSpawnAlert(message);
		}
		this.nextAlert = 2147483647;
		if (this.alerts.size() > 0) {
			for (Integer key : this.alerts.keySet()) {
				if (key.intValue() < this.nextAlert)
					this.nextAlert = key.intValue();
			}
		}
	}

	private void reviseSpawnAngles(ISpawnerAccess spawner) {
		int angleRange = this.maxAngle - this.minAngle;
		while (angleRange < 0)
			angleRange += 360;
		if (angleRange == 0) {
			angleRange = 360;
		}
		List validAngles = new ArrayList();

		for (int angle = -180; angle < 180; angle += angleRange) {
			int nextAngle = angle + angleRange;
			if (nextAngle >= 180)
				nextAngle -= 360;
			if (spawner.getNumberOfPointsInRange(angle, nextAngle, SpawnType.HUMANOID) >= this.minPointsInRange) {
				validAngles.add(Integer.valueOf(angle));
			}
		}
		if (validAngles.size() > 0) {
			this.minAngle = ((Integer) validAngles.get(new Random().nextInt(validAngles.size()))).intValue();
			this.maxAngle = (this.minAngle + angleRange);
			while (this.maxAngle >= 180) {
				this.maxAngle -= 360;
			}
		}

		if (this.minPointsInRange > 1) {
			ModLogger.logInfo("Can't find a direction with enough spawn points: " + this.minPointsInRange
					+ ". Lowering requirement.");

			this.minPointsInRange = 1;
		} else if (this.maxAngle - this.minAngle < 360) {
			ModLogger.logInfo("Can't find a direction with enough spawn points: " + this.minPointsInRange
					+ ". Switching to 360 degree mode for this entry");

			this.minAngle = -180;
			this.maxAngle = 180;
		} else {
			ModLogger.logWarn("Wave entry cannot find a single spawn point");
			spawner.noSpawnPointNotice();
		}
	}
}