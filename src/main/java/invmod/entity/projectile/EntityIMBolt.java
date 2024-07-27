// `^`^`^`
// ```java
// /**
//  * This class represents a custom projectile entity called EntityIMBolt within a Minecraft mod.
//  * The entity simulates a bolt, such as a lightning bolt or energy beam, with visual and auditory effects.
//  *
//  * Constructors:
//  * - EntityIMBolt(World world): Initializes a new bolt entity with default parameters in the given world.
//  * - EntityIMBolt(World world, double x, double y, double z): Initializes a bolt at the specified coordinates.
//  * - EntityIMBolt(World world, double x, double y, double z, double x2, double y2, double z2, int ticksToRender, int soundMade):
//  *   Initializes a bolt with a specified trajectory, render duration, and sound effect.
//  *
//  * Public Methods:
//  * - writeSpawnData(ByteArrayDataOutput data): Serializes the entity's spawn data into a byte array.
//  * - readSpawnData(ByteArrayDataInput data): Deserializes the entity's spawn data from a byte array.
//  * - onUpdate(): Called each tick to update the entity's age, play sound, and remove the entity when expired.
//  * - getVertices(): Returns an array of vertices representing the bolt's shape, recalculating as needed.
//  * - getYaw(): Returns the yaw angle of the bolt's heading.
//  * - getPitch(): Returns the pitch angle of the bolt's heading.
//  * - handleHealthUpdate(byte byte0): Handles health updates, playing a sound if necessary.
//  *
//  * Overridden Entity Methods:
//  * - entityInit(): Initializes the entity; currently empty.
//  * - readEntityFromNBT(NBTTagCompound nbttagcompound): Reads the entity's data from NBT; currently empty.
//  * - writeEntityToNBT(NBTTagCompound nbttagcompound): Writes the entity's data to NBT; currently empty.
//  *
//  * Private Methods:
//  * - setHeading(float x, float y, float z): Calculates the heading of the bolt based on the given vector.
//  * - doVertexUpdate(): Updates the vertices representing the bolt's shape.
//  * - createSegment(int begin, int end): Recursively creates segments of the bolt's path.
//  * - createVertex(int begin, int mid, int end): Creates a vertex for the bolt's path with some randomness.
//  *
//  * The class also implements IEntityAdditionalSpawnData for custom spawn packet handling.
//  */
// ```
// `^`^`^`

package invmod.entity.projectile;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import invmod.SoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityIMBolt extends Entity implements IEntityAdditionalSpawnData {
	private int age;
	private int ticksToRender;
	private long timeCreated;
	private double[][] vertices;
	private long lastVertexUpdate;
	private float yaw;
	private float pitch;
	private double distance;
	private float widthVariance;
	private float vecX;
	private float vecY;
	private float vecZ;
	private int soundMade;

	public EntityIMBolt(World world) {
		super(world);
		this.age = 0;
		this.timeCreated = (this.lastVertexUpdate = System.currentTimeMillis());
		this.vertices = new double[3][0];
		this.widthVariance = 6.0F;
		this.ignoreFrustumCheck = true;
	}

	public EntityIMBolt(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public EntityIMBolt(World world, double x, double y, double z, double x2, double y2, double z2, int ticksToRender,
			int soundMade) {
		this(world, x, y, z);
		this.vecX = ((float) (x2 - x));
		this.vecY = ((float) (y2 - y));
		this.vecZ = ((float) (z2 - z));
		this.ticksToRender = ticksToRender;
		this.soundMade = soundMade;
		this.setHeading(this.vecX, this.vecY, this.vecZ);
		this.doVertexUpdate();
	}

	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeShort((short) this.ticksToRender);
		data.writeFloat((float) this.posX);
		data.writeFloat((float) this.posY);
		data.writeFloat((float) this.posZ);
		data.writeFloat(this.vecX);
		data.writeFloat(this.vecY);
		data.writeFloat(this.vecZ);
		data.writeByte((byte) this.soundMade);
	}

	public void readSpawnData(ByteArrayDataInput data) {
		this.ticksToRender = data.readShort();
		this.setPosition(data.readFloat(), data.readFloat(), data.readFloat());
		this.setHeading(data.readFloat(), data.readFloat(), data.readFloat());
		this.soundMade = data.readByte();
		this.doVertexUpdate();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.age += 1;
		if ((this.age == 1) && (this.soundMade == 1)) {
			// this.world.playSoundAtEntity(this, "invmod:zap", 1.0F, 1.0F);
			this.playSound(SoundHandler.zap1, 1f, 1f);
		}
		if (this.age > this.ticksToRender)
			this.setDead();
	}

	public double[][] getVertices() {
		long time = System.currentTimeMillis();
		if (time - this.timeCreated > this.ticksToRender * 50) {
			return null;
		}
		if (time - this.lastVertexUpdate >= 75L) {
			this.doVertexUpdate();
			while (this.lastVertexUpdate + 50L <= time) {
				this.lastVertexUpdate += 50L;
			}
		}
		return this.vertices;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	// TODO: Removed Override annotation
	public void handleHealthUpdate(byte byte0) {
		if (byte0 == 0) {
			// this.world.playSoundAtEntity(this, "invmod:zap", 1.0F, 1.0F);
			this.playSound(SoundHandler.zap1, 1f, 1f);
		} else if (byte0 != 1) {
			if (byte0 != 2)
				;
		}
	}

	@Override
	public void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	private void setHeading(float x, float y, float z) {
		float xzSq = x * x + z * z;
		this.yaw = ((float) (Math.atan2(x, z) * 180.0D / 3.141592653589793D) + 90.0F);
		this.pitch = ((float) (Math.atan2(MathHelper.sqrt(xzSq), y) * 180.0D / 3.141592653589793D));
		this.distance = Math.sqrt(xzSq + y * y);
	}

	private void doVertexUpdate() {
		this.world.profiler.startSection("IMBolt");
		this.widthVariance = (10.0F / (float) Math.log10(this.distance + 1.0D));
		int numberOfVertexes = 60;
		if (numberOfVertexes != this.vertices[0].length) {
			this.vertices[0] = new double[numberOfVertexes];
			this.vertices[1] = new double[numberOfVertexes];
			this.vertices[2] = new double[numberOfVertexes];
		}

		for (int vertex = 0; vertex < numberOfVertexes; vertex++) {
			this.vertices[1][vertex] = (vertex * this.distance / (numberOfVertexes - 1));
		}

		this.createSegment(0, numberOfVertexes - 1);
		this.world.profiler.endSection();
	}

	private void createSegment(int begin, int end) {
		int points = end + 1 - begin;
		if (points <= 4) {
			if (points == 3) {
				this.createVertex(begin, begin + 1, end);
			} else {
				this.createVertex(begin, begin + 1, end);
				this.createVertex(begin, begin + 2, end);
			}
			return;
		}
		int midPoint = begin + points / 2;
		this.createVertex(begin, midPoint, end);
		this.createSegment(begin, midPoint);
		this.createSegment(midPoint, end);
	}

	private void createVertex(int begin, int mid, int end) {
		double difference = this.vertices[0][end] - this.vertices[0][begin];
		double yDiffToMid = this.vertices[1][mid] - this.vertices[1][begin];
		double yRatio = yDiffToMid / (this.vertices[1][end] - this.vertices[1][begin]);
		this.vertices[0][mid] = (this.vertices[0][begin] + difference * yRatio
				+ (this.world.rand.nextFloat() - 0.5D) * yDiffToMid * this.widthVariance);
		difference = this.vertices[2][end] - this.vertices[2][begin];
		this.vertices[2][mid] = (this.vertices[2][begin] + difference * yRatio
				+ (this.world.rand.nextFloat() - 0.5D) * yDiffToMid * this.widthVariance);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {

	}
}