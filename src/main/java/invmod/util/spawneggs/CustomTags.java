// `^`^`^`
// ```java
// /**
//  * This Java class, CustomTags, is part of the invmod.util.spawneggs package and is used to create custom NBTTagCompound instances for various entities in Minecraft. These tags are used to define specific properties and behaviors for entities when they are spawned into the game world. The class contains a collection of static methods, each returning an NBTTagCompound with predefined attributes for different entity types and scenarios.
// 
//  * Methods:
//  * - poweredCreeper(): Creates a tag for a powered creeper (charged by lightning).
//  * - IMZombie_T1 to IMZombie_T3(): Creates tags for different tiers of custom zombies.
//  * - IMZombie_T2_tar(): Creates a tag for a tar-flavored tier 2 custom zombie.
//  * - IMSpider_T1_baby(): Creates a tag for a baby tier 1 custom spider.
//  * - IMSpider_T2(): Creates a tag for a tier 2 custom spider.
//  * - IMSpider_T2_mother(): Creates a tag for a mother tier 2 custom spider.
//  * - IMThrower_T2(): Creates a tag for a tier 2 custom thrower entity.
//  * - IMZombiePigman_T1 to IMZombiePigman_T3(): Creates tags for different tiers of custom zombie pigmen.
//  * - witherSkeleton(): Creates a tag for a wither skeleton with equipment.
//  * - villagerZombie(): Creates a tag for a zombie villager.
//  * - babyZombie(): Creates a tag for a baby zombie.
//  * - horseType(int type): Creates a tag for a horse with a specified type.
//  * - createItemTag(byte count, short damage, short id): Creates a tag for an item with specified count, damage, and ID.
//  * - getEntityTag(String entityID): Creates a tag with an entity ID.
//  * - ridingTag(NBTTagCompound ridden): Creates a tag for an entity riding another entity.
//  * - spiderJockey(boolean wither): Creates a tag for a spider jockey, with an option for a wither skeleton rider.
//  * - chickenJockey(boolean villager): Creates a tag for a chicken jockey, with an option for a villager zombie rider.
// 
//  * This class is essential for customizing entities in Minecraft modding, allowing for the creation of unique mobs with specific characteristics.
//  */
// package invmod.util.spawneggs;
// 
// // ... (rest of the imports and class code)
// ```
// `^`^`^`

package invmod.util.spawneggs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CustomTags {

	public static NBTTagCompound poweredCreeper() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("powered", (byte) 1);
		return tag;
	}

	public static NBTTagCompound IMZombie_T1() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 0);
		tag.setInteger("tier", 1);
		return tag;
	}

	public static NBTTagCompound IMZombie_T2() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 0);
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMZombie_T2_tar() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 2);
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMZombie_T3() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 0);
		tag.setInteger("tier", 3);
		return tag;
	}

	public static NBTTagCompound IMSpider_T1_baby() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 1);
		tag.setInteger("tier", 1);
		return tag;
	}

	public static NBTTagCompound IMSpider_T2() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 0);
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMSpider_T2_mother() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 1);
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMThrower_T2() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMZombiePigman_T1() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 1);
		tag.setInteger("tier", 1);
		return tag;
	}

	public static NBTTagCompound IMZombiePigman_T2() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 1);
		tag.setInteger("tier", 2);
		return tag;
	}

	public static NBTTagCompound IMZombiePigman_T3() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("flavour", 1);
		tag.setInteger("tier", 3);
		return tag;
	}

	public static NBTTagCompound witherSkeleton() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("SkeletonType", (byte) 1);
		NBTTagList list = new NBTTagList();
		NBTTagCompound swordItem = createItemTag((byte) 1, (short) 0, (short) 272);
		list.appendTag(swordItem);
		for (int i = 0; i < 4; ++i)
			list.appendTag(new NBTTagCompound());
		tag.setTag("Equipment", list);
		return tag;
	}

	public static NBTTagCompound villagerZombie() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("IsVillager", (byte) 1);
		return tag;
	}

	public static NBTTagCompound babyZombie() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("IsBaby", (byte) 1);
		return tag;
	}

	public static NBTTagCompound horseType(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Type", type);
		return tag;
	}

	public static NBTTagCompound createItemTag(byte count, short damage, short id) {
		NBTTagCompound item = new NBTTagCompound();
		item.setByte("Count", count);
		item.setShort("Damage", damage);
		item.setShort("id", id);
		return item;
	}

	public static NBTTagCompound getEntityTag(String entityID) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("id", entityID);
		return tag;
	}

	public static NBTTagCompound ridingTag(NBTTagCompound ridden) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("Riding", ridden);
		return tag;
	}

	public static NBTTagCompound spiderJockey(boolean wither) {
		NBTTagCompound skele = (wither) ? witherSkeleton() : new NBTTagCompound();
		skele.setTag("Riding", getEntityTag("Spider"));
		return skele;
	}

	public static NBTTagCompound chickenJockey(boolean villager) {
		NBTTagCompound zomb = babyZombie();
		if (villager)
			zomb.setByte("IsVillager", (byte) 1);
		zomb.setTag("Riding", getEntityTag("Chicken"));
		return zomb;
	}

}