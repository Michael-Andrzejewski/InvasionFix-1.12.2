package invmod.nexus;

import invmod.util.PolarAngle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnPointContainer
{
  private EnumMap<SpawnType, ArrayList<SpawnPoint>> spawnPoints;
  private boolean sorted;
  private Random random;
  private PolarAngle angleDesired;

  public SpawnPointContainer()
  {
    this.sorted = false;
    this.random = new Random();
    this.angleDesired = new PolarAngle(0);
    this.spawnPoints = new EnumMap(SpawnType.class);
    for (SpawnType type : SpawnType.values())
    {
      this.spawnPoints.put(type, new ArrayList());
    }
  }

  public void addSpawnPointXZ(SpawnPoint spawnPoint)
  {
    boolean flag = false;
    ArrayList spawnList = this.spawnPoints.get(spawnPoint.getType());
    for (int i = 0; i < spawnList.size(); i++)
    {
      SpawnPoint oldPoint = (SpawnPoint)spawnList.get(i);
      if ((oldPoint.getPos().getX() == spawnPoint.getPos().getX()) && (oldPoint.getPos().getZ() == spawnPoint.getPos().getZ()))
      {
        if (oldPoint.getPos().getY() > spawnPoint.getPos().getY())
        {
          spawnList.set(i, spawnPoint);
        }
        flag = true;
        break;
      }
    }

    if (!flag)
    {
      spawnList.add(spawnPoint);
    }
    this.sorted = false;
  }

  public SpawnPoint getRandomSpawnPoint(SpawnType spawnType)
  {
    ArrayList spawnList = this.spawnPoints.get(spawnType);
    if (spawnList.size() == 0)
    {
      return null;
    }
    return (SpawnPoint)spawnList.get(this.random.nextInt(spawnList.size()));
  }

  public SpawnPoint getRandomSpawnPoint(SpawnType spawnType, int minAngle, int maxAngle)
  {
    ArrayList spawnList = this.spawnPoints.get(spawnType);
    if (spawnList.size() == 0)
    {
      return null;
    }

    if (!this.sorted)
    {
      Collections.sort(spawnList);
      this.sorted = true;
    }

    this.angleDesired.setAngle(minAngle);
    int start = Collections.binarySearch(spawnList, this.angleDesired);
    if (start < 0) {
      start = -start - 1;
    }
    this.angleDesired.setAngle(maxAngle);
    int end = Collections.binarySearch(spawnList, this.angleDesired);
    if (end < 0) {
      end = -end - 1;
    }
    if (end > start) {
      return (SpawnPoint)spawnList.get(start + this.random.nextInt(end - start));
    }
    if ((start > end) && (end > 0))
    {
      int r = start + this.random.nextInt(spawnList.size() + end - start);
      if (r >= spawnList.size()) {
        r -= spawnList.size();
      }
      return (SpawnPoint)spawnList.get(r);
    }
    return null;
  }

  public int getNumberOfSpawnPoints(SpawnType type)
  {
    return ((ArrayList)this.spawnPoints.get(type)).size();
  }

  public int getNumberOfSpawnPoints(SpawnType spawnType, int minAngle, int maxAngle)
  {
    ArrayList spawnList = this.spawnPoints.get(spawnType);
    if ((spawnList.size() == 0) || (maxAngle - minAngle >= 360))
    {
      return spawnList.size();
    }

    if (!this.sorted)
    {
      Collections.sort(spawnList);
      this.sorted = true;
    }

    this.angleDesired.setAngle(minAngle);
    int start = Collections.binarySearch(spawnList, this.angleDesired);
    if (start < 0) {
      start = -start - 1;
    }
    this.angleDesired.setAngle(maxAngle);
    int end = Collections.binarySearch(spawnList, this.angleDesired);
    if (end < 0) {
      end = -end - 1;
    }
    if (end > start) {
      return end - start;
    }
    if ((start > end) && (end > 0)) {
      return end + spawnList.size() - start;
    }
    return 0;
  }

  public void pointDisplayTest(Block block, World world)
  {
    ArrayList points = this.spawnPoints.get(SpawnType.HUMANOID);
    SpawnPoint point = null;
    for (int i = 0; i < points.size(); i++)
    {
      point = (SpawnPoint)points.get(i);
      world.setBlockState(new BlockPos(point.getPos().getX(), point.getPos().getY(), point.getPos().getZ()), block.getDefaultState());
    }
  }
}