package invmod;

import static invmod.util.InjectionUtil.Null;

import javax.annotation.Nonnull;

import invmod.entity.ally.EntityIMWolf;
import invmod.entity.block.EntityIMEgg;
import invmod.entity.block.trap.EntityIMTrap;
import invmod.entity.monster.EntityIMBird;
import invmod.entity.monster.EntityIMCreeper;
import invmod.entity.monster.EntityIMGiantBird;
import invmod.entity.monster.EntityIMImp;
import invmod.entity.monster.EntityIMPigEngy;
import invmod.entity.monster.EntityIMSkeleton;
import invmod.entity.monster.EntityIMSpider;
import invmod.entity.monster.EntityIMThrower;
import invmod.entity.monster.EntityIMZombie;
import invmod.entity.monster.EntityIMZombiePigman;
import invmod.entity.projectile.EntityIMBolt;
import invmod.entity.projectile.EntityIMBoulder;
import invmod.entity.projectile.EntityIMPrimedTNT;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class ModEntities {
	
	private static int entityID = 0;

	//No Egg
	@ObjectHolder("IMEgg")
	public static final EntityEntry IM_EGG = Null();
	@ObjectHolder("IMBoulder")
	public static final EntityEntry IM_BOULDER = Null();
	@ObjectHolder("IMBolt")
	public static final EntityEntry IM_BOLT = Null();
	@ObjectHolder("IMTrap")
	public static final EntityEntry IM_TRAP = Null();
	@ObjectHolder("IMPrimedTNT")
	public static final EntityEntry IM_PRIMED_TNT = Null();
	
	//Egg
	@ObjectHolder("IMZombie")
	public static final EntityEntry IM_ZOMBIE = Null();
	@ObjectHolder("IMSkeleton")
	public static final EntityEntry IM_SKELETON = Null();
	@ObjectHolder("IMSpider")
	public static final EntityEntry IM_SPIDER = Null();
	@ObjectHolder("IMPigEngy")
	public static final EntityEntry IM_PIG_ENGY = Null();
	@ObjectHolder("IMWolf")
	public static final EntityEntry IM_WOLF = Null();
	@ObjectHolder("IMCreeper")
	public static final EntityEntry IM_CREEPER = Null();
	@ObjectHolder("IMImp")
	public static final EntityEntry IM_IMP = Null();
	@ObjectHolder("IMZombiePigman")
	public static final EntityEntry IM_ZOMBIE_PIGMAN = Null();
	@ObjectHolder("IMThrower")
	public static final EntityEntry IM_THROWER = Null();
	@ObjectHolder("IMBird")
	public static final EntityEntry IM_BIRD = Null();
	@ObjectHolder("IMGiantBird")
	public static final EntityEntry IM_GIANT_BIRD = Null();
	
	
	@EventBusSubscriber(modid = Reference.MODID)
	public static class EntityRegistrationHandler {

		@SubscribeEvent
		public static void registerTileEntities(RegistryEvent.Register<EntityEntry> event) {
			final EntityEntry[] entityEntries = {
					//createEntityEntryWithoutEgg(EntitySlimePart.class, "slime_part", 64, 1, true)
					createEntityEntryWithoutEgg(EntityIMEgg.class, "IMEgg", 128, 1, true),
					createEntityEntryWithoutEgg(EntityIMBoulder.class, "IMBoulder", 36, 1, true),
					createEntityEntryWithoutEgg(EntityIMBolt.class, "IMBolt", 36, 1, false),
					createEntityEntryWithoutEgg(EntityIMTrap.class, "IMTrap", 36, 1, false),
					createEntityEntryWithoutEgg(EntityIMPrimedTNT.class, "IMPrimedTNT", 36, 1, true),
					
					//createEntityEntry(EntityCQRDummy.class, "dummy", 64, 1, true, 0xC29D62, 0x67502C)
					createEntityEntry(EntityIMZombie.class, "IMZombie", 128, 1, true, 0x6B753F, 0x281B0A),
					createEntityEntry(EntityIMSkeleton.class, "IMSkeleton", 128, 1, true, 0x9B9B9B, 0x797979),
					createEntityEntry(EntityIMSpider.class, "IMSpider", 128, 1, true, 0x504A3E, 0xA4121C),
					createEntityEntry(EntityIMPigEngy.class, "IMPigEngy", 128, 1, true, 0xEC9695, 0x420000),
					createEntityEntry(EntityIMWolf.class, "IMWolf", 128, 1, true, 0x99CCFF, 0xE6F2FF),
					createEntityEntry(EntityIMCreeper.class, "IMCreeper", 128, 1, true, 0x238F1F, 0xA5AAA6),
					createEntityEntry(EntityIMImp.class, "IMImp", 128, 1, true, 0xB40113, 0xFF0000),
					createEntityEntry(EntityIMZombiePigman.class, "IMZombiePigman", 128, 1, true, 0xEB8E91, 0x49652F),
					createEntityEntry(EntityIMThrower.class, "IMThrower", 128, 1, true, 0x5303814, 0x632808),
					//EntityRegistry.registerModEntity(EntityIMBird.class, "IMBird", 15, this, 128, 1, true);
					createEntityEntry(EntityIMBird.class, "IMBird", 128, 1, true, 0x2B2B2B, 0xEA7EDC),
					//EntityRegistry.registerModEntity(EntityIMGiantBird.class, "IMGiantBird", 16, this, 128, 1, true, 0x2B2B2B, 0xEA7EDC);
					createEntityEntry(EntityIMGiantBird.class, "IMGiantBird", 128, 1, true, 0x2B2B2B, 0xEA7EDC)
					
			};

			event.getRegistry().registerAll(entityEntries);
		}

		private static EntityEntry createEntityEntry(@Nonnull Class<? extends Entity> entityClass, String name,
				int trackerRange, int trackerUpdateFrequency, boolean sendVelocityUpdates, int eggColor1,
				int eggColor2) {
			return EntityEntryBuilder.create().entity(entityClass)
					.id(new ResourceLocation(Reference.MODID, name), entityID++).name(name).egg(eggColor1, eggColor2)
					.tracker(64, 1, false).build();
		}

		private static EntityEntry createEntityEntryWithoutEgg(@Nonnull Class<? extends Entity> entityClass,
				String name, int trackerRange, int trackerUpdateFrequency, boolean sendVelocityUpdates) {
			return EntityEntryBuilder.create().entity(entityClass)
					.id(new ResourceLocation(Reference.MODID, name), entityID++).name(name).tracker(64, 1, false)
					.build();
		}

	}
}
