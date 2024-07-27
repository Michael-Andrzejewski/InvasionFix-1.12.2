// `^`^`^`
// ```java
// /**
//  * This class is part of the Invasion Mod and is responsible for creating various types of mod-specific
//  * monster entities based on provided construction templates. The MobBuilder class uses a factory method
//  * to instantiate different monster entities, which are all subclasses of EntityIMMob, according to the
//  * type specified in the EntityConstruct object. Each monster entity is associated with a TileEntityNexus
//  * and a World object, which are passed during creation.
//  *
//  * Methods:
//  * - createMobFromConstruct(EntityConstruct mobConstruct, World world, TileEntityNexus nexus): This method
//  *   takes an EntityConstruct object containing the specifications for the monster to be created, the world
//  *   the monster will exist in, and the nexus associated with the monster. It uses a switch statement to
//  *   determine the type of monster to create based on the mob type specified in the EntityConstruct. It sets
//  *   up the monster's properties such as texture, flavour, and tier when applicable. If the mob type is not
//  *   recognized, it logs a warning. The method returns the created EntityIMMob or null if the type is missing.
//  *
//  * Monster Types Handled:
//  * - ZOMBIE: Creates an EntityIMZombie with texture, flavour, and tier.
//  * - ZOMBIEPIGMAN: Creates an EntityIMZombiePigman with texture and tier.
//  * - SPIDER: Creates an EntityIMSpider with texture, flavour, and tier.
//  * - SKELETON: Creates an EntityIMSkeleton.
//  * - PIG_ENGINEER: Creates an EntityIMPigEngy.
//  * - THROWER: Creates an EntityIMThrower with texture and tier.
//  * - BURROWER: Creates an EntityIMBurrower.
//  * - CREEPER: Creates an EntityIMCreeper.
//  * - IMP: Creates an EntityIMImp.
//  *
//  * If the mob type is not recognized, a warning is logged using ModLogger.
//  */
// package invmod.nexus;
// 
// // ... (imports)
// 
// public class MobBuilder {
//     // ... (class code)
// }
// ```
// `^`^`^`

package invmod.nexus;

import invmod.entity.monster.EntityIMBurrower;
import invmod.entity.monster.EntityIMCreeper;
import invmod.entity.monster.EntityIMImp;
import invmod.entity.monster.EntityIMMob;
import invmod.entity.monster.EntityIMPigEngy;
import invmod.entity.monster.EntityIMSkeleton;
import invmod.entity.monster.EntityIMSpider;
import invmod.entity.monster.EntityIMThrower;
import invmod.entity.monster.EntityIMZombie;
import invmod.entity.monster.EntityIMZombiePigman;
import invmod.tileentity.TileEntityNexus;
import invmod.util.ModLogger;
import net.minecraft.world.World;

public class MobBuilder {

	public EntityIMMob createMobFromConstruct(EntityConstruct mobConstruct, World world, TileEntityNexus nexus) {
		EntityIMMob mob = null;
		switch (mobConstruct.getMobType()) {
		case ZOMBIE:
			EntityIMZombie zombie = new EntityIMZombie(world, nexus);
			zombie.setTexture(mobConstruct.getTexture());
			zombie.setFlavour(mobConstruct.getFlavour());
			zombie.setTier(mobConstruct.getTier());
			mob = zombie;
			break;
		case ZOMBIEPIGMAN:
			EntityIMZombiePigman zombiePigman = new EntityIMZombiePigman(world, nexus);
			zombiePigman.setTexture(mobConstruct.getTexture());
			zombiePigman.setTier(mobConstruct.getTier());
			mob = zombiePigman;
			break;
		case SPIDER:
			EntityIMSpider spider = new EntityIMSpider(world, nexus);
			spider.setTexture(mobConstruct.getTexture());
			spider.setFlavour(mobConstruct.getFlavour());
			spider.setTier(mobConstruct.getTier());
			mob = spider;
			break;
		case SKELETON:
			EntityIMSkeleton skeleton = new EntityIMSkeleton(world, nexus);
			mob = skeleton;
			break;
		case PIG_ENGINEER:
			EntityIMPigEngy pigEngy = new EntityIMPigEngy(world, nexus);
			mob = pigEngy;
			break;
		case THROWER:
			EntityIMThrower thrower = new EntityIMThrower(world, nexus);
			thrower.setTexture(mobConstruct.getTier());
			thrower.setTier(mobConstruct.getTier());
			mob = thrower;
			break;
		case BURROWER:
			EntityIMBurrower burrower = new EntityIMBurrower(world, nexus);
			mob = burrower;
			break;
		case CREEPER:
			EntityIMCreeper creeper = new EntityIMCreeper(world, nexus);
			mob = creeper;
			break;
		case IMP:
			EntityIMImp imp = new EntityIMImp(world, nexus);
			mob = imp;
			break;
		default:
			ModLogger.logWarn("Missing mob type in MobBuilder: " + mobConstruct.getMobType());
			mob = null;
		}

		return mob;
	}

}