// `^`^`^`
// ```java
// /**
//  * This Java file is part of the invmod client package and is responsible for handling client-side proxy operations,
//  * particularly the registration of entity renderers and the loading of animations for entities within the Invasion Mod.
//  * The ProxyClient class extends ProxyCommon and overrides specific methods to perform client-side tasks.
//  *
//  * Methods:
//  * - printGuiMessage(ITextComponent message): Displays a message in the Minecraft in-game chat GUI.
//  * - registerEntityRenderers(): Registers entity renderers for various custom entities in the mod, such as zombies,
//  *   skeletons, spiders, and more. It uses the GameRegistry to register a TileEntity and the RenderingRegistry to
//  *   associate entities with their respective renderers.
//  * - registerEntityRenderer(Class<? extends Entity> entityClass, Class<? extends Render<? extends Entity>> rendererClass,
//  *   Object... additionalArgs): A helper method that registers a renderer for a specific entity class. It uses reflection
//  *   to create an instance of the renderer with the appropriate constructor arguments.
//  * - loadAnimations(): Loads and defines animations for entities. It sets up keyframes, transitions, and animation phases
//  *   for different actions such as standing, running, attacking, etc. This method is crucial for adding dynamic and
//  *   realistic movements to the mod's entities.
//  *
//  * The code also includes imports and declarations for various classes and interfaces required for rendering entities and
//  * managing animations within the Minecraft game engine.
//  */
// package invmod.client;
// 
// // ... (imports and class definition)
// public class ProxyClient extends ProxyCommon {
//     // ... (method implementations)
// }
// ```
// ```java
// /**
//  * This code is designed to create and manage keyframe animations for a bird's leg movements, specifically targeting the thigh, leg, and ankle joints. It defines the motion for both left and right sides, with separate sequences for standing and running cycles. The code utilizes a KeyFrame class to store the animation data and interpolation types for smooth transitions between frames.
// 
//  * Methods:
//  * - KeyFrame.toRadians: Converts the angle values in the keyframes from degrees to radians, which is necessary for certain animation and rendering systems.
//  * - KeyFrame.cloneFrames: Creates a deep copy of a list of keyframes to be reused or modified without affecting the original list.
//  * - KeyFrame.mirrorFramesX: Mirrors the keyframe values along the X-axis, which is useful for creating symmetrical animations for the opposite side of the body.
//  * - KeyFrame.offsetFramesCircular: Offsets the keyframes in a circular pattern between a specified begin and end point, allowing for continuous looping animations such as running.
// 
//  * The code constructs detailed keyframe sequences for the left thigh, left leg, and left ankle, including both a standing pose and a running cycle. It then clones these sequences for the right side and applies mirroring and offsetting to ensure proper coordination and symmetry. Finally, all keyframes are stored in a map with their corresponding bone identifiers, ready for use in animating a 3D bird model's leg movements.
//  */
// ```
// ```plaintext
// This code is designed to create and manage animations for a bird model with articulated legs, wings, and beak. It uses keyframes to define the movement of each part over time and interpolates between these keyframes to create smooth animations. The code is structured into several parts, each handling a different aspect of the bird's animation:
// 
// 1. Leg Animation: Defines keyframes for the bird's leg movements, including the thighs, metatarsophalangeal articulations (ankles), and back claws. It uses linear interpolation to transition between keyframes.
// 
// 2. Wing Animation: Sets up keyframes for the bird's wing movements, distinguishing between inner and outer wing sections. It includes detailed movements such as wing flaps, spreads, tucks, and glides, with transitions between these actions.
// 
// 3. Beak Animation: Establishes keyframes for the opening and closing of the bird's beak, using linear interpolation to animate the upper and lower parts of the beak.
// 
// Each section of the code includes methods for:
// - Creating keyframes (`KeyFrame` class) with position and rotation data for different parts of the bird.
// - Converting angles from degrees to radians for proper animation processing (`KeyFrame.toRadians`).
// - Cloning frames for symmetrical parts of the bird (`KeyFrame.cloneFrames`).
// - Mirroring frames for opposite sides (`KeyFrame.mirrorFramesX`).
// 
// Finally, the animations are registered into an `AnimationRegistry` for each action (running, wing flapping, beak movement), allowing for the animations to be retrieved and played by the animation system. The code uses `EnumMap` and `ArrayList` to organize keyframes and animation phases, and `HashMap` to manage transitions between different animation actions.
// ```
// ```plaintext
// This code snippet appears to be part of an animation system for a bird's beak within a game or simulation, possibly using a Minecraft modding framework given the reference to FMLClientHandler. The code defines keyframes for the lower beak's animation and registers the animation with an animation registry.
// 
// KeyFrame Creation and Transformation:
// - The code creates a list of KeyFrame objects for the lower beak of a bird, specifying the rotation at different times to animate the beak opening and closing.
// - The keyframes are defined with a time value, three rotation values (presumably pitch, yaw, and roll), and an interpolation type (LINEAR).
// - The `KeyFrame.toRadians` method is called to convert the rotation values from degrees to radians, which is a common requirement for rotation in computer graphics.
// 
// Animation Definition and Registration:
// - An Animation object is created, specifying the bone class (BonesMouth), animation speed, transition speed between phases, the keyframes for the beak, and the phases of the animation.
// - The animation is registered with a unique string identifier ("bird_beak") in the AnimationRegistry, making it accessible for use in the game or simulation.
// 
// File Access Method:
// - The `getFile` method is an override that provides a way to access files from the game's data directory. It constructs a File object using a provided filename and the game's data directory path, which is retrieved from the FMLClientHandler instance.
// 
// Overall, this code is responsible for defining and registering a bird's beak animation and providing a method to access files relative to the game's data directory.
// ```
// `^`^`^`

package invmod.client;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import invmod.Reference;
import invmod.client.render.AnimationRegistry;
import invmod.client.render.RenderB;
import invmod.client.render.RenderBolt;
import invmod.client.render.RenderBoulder;
import invmod.client.render.RenderBurrower;
import invmod.client.render.RenderEgg;
import invmod.client.render.RenderGiantBird;
import invmod.client.render.RenderIMCreeper;
import invmod.client.render.RenderIMSkeleton;
import invmod.client.render.RenderIMWolf;
import invmod.client.render.RenderIMZombie;
import invmod.client.render.RenderIMZombiePigman;
import invmod.client.render.RenderImp;
import invmod.client.render.RenderInvis;
import invmod.client.render.RenderPigEngy;
import invmod.client.render.RenderSpiderIM;
import invmod.client.render.RenderThrower;
import invmod.client.render.RenderTrap;
import invmod.client.render.animation.Animation;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationPhaseInfo;
import invmod.client.render.animation.BonesBirdLegs;
import invmod.client.render.animation.BonesMouth;
import invmod.client.render.animation.BonesWings;
import invmod.client.render.animation.InterpType;
import invmod.client.render.animation.KeyFrame;
import invmod.client.render.animation.Transition;
import invmod.common.ProxyCommon;
import invmod.entity.EntityIMSpawnProxy;
import invmod.entity.ally.EntityIMWolf;
import invmod.entity.block.EntityIMEgg;
import invmod.entity.block.trap.EntityIMTrap;
import invmod.entity.monster.EntityIMBird;
import invmod.entity.monster.EntityIMBurrower;
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
import invmod.tileentity.TileEntityNexus;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ProxyClient extends ProxyCommon {

	public void printGuiMessage(ITextComponent message) {
		FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(message);
	}

	@Override
	public void registerEntityRenderers() {
		GameRegistry.registerTileEntity(TileEntityNexus.class, new ResourceLocation(Reference.MODID, "nexus"));

		registerEntityRenderer(EntityIMZombie.class, RenderIMZombie.class);
		registerEntityRenderer(EntityIMZombiePigman.class, RenderIMZombiePigman.class);
		registerEntityRenderer(EntityIMSkeleton.class, RenderIMSkeleton.class);
		registerEntityRenderer(EntityIMSpider.class, RenderSpiderIM.class);
		registerEntityRenderer(EntityIMPigEngy.class, RenderPigEngy.class);
		registerEntityRenderer(EntityIMImp.class, RenderImp.class);
		registerEntityRenderer(EntityIMThrower.class, RenderThrower.class);
		registerEntityRenderer(EntityIMBurrower.class, RenderBurrower.class);
		registerEntityRenderer(EntityIMWolf.class, RenderIMWolf.class);
		registerEntityRenderer(EntityIMBoulder.class, RenderBoulder.class);
		registerEntityRenderer(EntityIMTrap.class, RenderTrap.class);
		registerEntityRenderer(EntityIMBolt.class, RenderBolt.class);
		registerEntityRenderer(EntityIMSpawnProxy.class, RenderInvis.class);
		registerEntityRenderer(EntityIMEgg.class, RenderEgg.class);

		registerEntityRenderer(EntityIMCreeper.class, RenderIMCreeper.class);
		registerEntityRenderer(EntityIMBird.class, RenderB.class);
		registerEntityRenderer(EntityIMGiantBird.class, RenderGiantBird.class);
	}

	private static void registerEntityRenderer(Class<? extends Entity> entityClass,
			final Class<? extends Render<? extends Entity>> rendererClass, final Object... additionalArgs) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory() {
			@Override
			public Render createRenderFor(RenderManager renderManager) {
				Render renderer;

				Object[] initArgs = new Object[additionalArgs.length + 1];
				initArgs[0] = renderManager;
				if (additionalArgs.length != 0) {
					for (int i = 0; i < additionalArgs.length; i++) {
						initArgs[i + 1] = additionalArgs[i];
					}
				}

				Class<?>[] initClasses = new Class[initArgs.length];
				for (int i = 0; i < initArgs.length; i++) {
					initClasses[i] = initArgs[i].getClass();
				}

				try {
					Constructor c = rendererClass.getConstructor(initClasses);
					renderer = (Render) c.newInstance(initArgs);
				} catch (Exception e) {
					e.printStackTrace();
					renderer = null;
				}
				return renderer;
			}
		});
	}

	@Override
	public void loadAnimations() {
		System.out.println("ProxyClient, loadAnimation()");
		EnumMap allKeyFrames = new EnumMap(BonesBirdLegs.class);
		List animationPhases = new ArrayList(2);
		int x = 17;
		float totalFrames = 331 + x;

		Map transitions = new HashMap(1);
		Transition defaultTransition = new Transition(AnimationAction.STAND, 1.0F / totalFrames, 0.0F);
		transitions.put(AnimationAction.STAND, defaultTransition);
		transitions.put(AnimationAction.STAND_TO_RUN,
				new Transition(AnimationAction.STAND_TO_RUN, 1.0F / totalFrames, 1.0F / totalFrames));
		transitions.put(AnimationAction.LEGS_RETRACT,
				new Transition(AnimationAction.LEGS_RETRACT, 1.0F / totalFrames, (211.0F + x) / totalFrames));
		transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1,
				new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1, 1.0F / totalFrames, (171.0F + x) / totalFrames));
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND, 0.0F, 1.0F / totalFrames, defaultTransition,
				transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.RUN, 38.0F / totalFrames, 38.0F / totalFrames);
		transitions.put(AnimationAction.RUN, defaultTransition);
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.STAND_TO_RUN, 1.0F / totalFrames,
				38.0F / totalFrames, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.RUN, (170.0F + x) / totalFrames, 38.0F / totalFrames);
		transitions.put(AnimationAction.RUN, defaultTransition);
		transitions.put(AnimationAction.STAND, new Transition(AnimationAction.STAND, (170.0F + x) / totalFrames, 0.0F));
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.RUN, 38.0F / totalFrames, (170.0F + x) / totalFrames,
				defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.LEGS_UNRETRACT, (251.0F + x) / totalFrames,
				(251.0F + x) / totalFrames);
		transitions.put(AnimationAction.LEGS_UNRETRACT, defaultTransition);
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_RETRACT, (211.0F + x) / totalFrames,
				(251.0F + x) / totalFrames, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.STAND, (291.0F + x) / totalFrames, 0.0F);
		transitions.put(AnimationAction.STAND, defaultTransition);
		transitions.put(AnimationAction.LEGS_RETRACT,
				new Transition(AnimationAction.LEGS_RETRACT, (291.0F + x) / totalFrames, (211.0F + x) / totalFrames));
		transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1, new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1,
				(291.0F + x) / totalFrames, (291.0F + x) / totalFrames));
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_UNRETRACT, (251.0F + x) / totalFrames,
				(291.0F + x) / totalFrames, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.LEGS_CLAW_ATTACK_P2, (331.0F + x) / totalFrames,
				(171.0F + x) / totalFrames);
		transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P2, defaultTransition);
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_CLAW_ATTACK_P1, (291.0F + x) / totalFrames,
				(331.0F + x) / totalFrames, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.STAND, (211.0F + x) / totalFrames, 0.0F);
		transitions.put(AnimationAction.STAND, defaultTransition);
		transitions.put(AnimationAction.LEGS_RETRACT,
				new Transition(AnimationAction.LEGS_RETRACT, (211.0F + x) / totalFrames, (211.0F + x) / totalFrames));
		transitions.put(AnimationAction.LEGS_CLAW_ATTACK_P1, new Transition(AnimationAction.LEGS_CLAW_ATTACK_P1,
				(211.0F + x) / totalFrames, (291.0F + x) / totalFrames));
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.LEGS_CLAW_ATTACK_P2, (171.0F + x) / totalFrames,
				(211.0F + x) / totalFrames, defaultTransition, transitions));

		float frameUnit = 1.0F / totalFrames;
		float runBegin = 38.0F * frameUnit;
		float runEnd = (170 + x) * frameUnit;

		List leftThighFrames = new ArrayList(13);
		leftThighFrames.add(new KeyFrame(0.0F, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(1.0F * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(5.0F * frameUnit, -12.6F, 0.2F, 5.0F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(10.0F * frameUnit, 21.200001F, -0.6F, 5.2F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(15.0F * frameUnit, -32.0F, -1.7F, 5.7F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(25.0F * frameUnit, -57.0F, -6.4F, 9.0F, InterpType.LINEAR));
		leftThighFrames.add(new KeyFrame(35.0F * frameUnit, -76.5F, -19.299999F, 21.200001F, InterpType.LINEAR));
		KeyFrame.toRadians(leftThighFrames);

		List leftThighRunCycle = new ArrayList(7);
		leftThighRunCycle.add(new KeyFrame(38.0F * frameUnit, -74.099998F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame(44.0F * frameUnit, -63.700001F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((80 + x) * frameUnit, 13.1F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((101 + x) * frameUnit, 35.700001F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((110 + x) * frameUnit, 20.0F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((140 + x) * frameUnit, -33.0F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((170 + x) * frameUnit, -74.099998F, 0.0F, -6.5F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((171 + x) * frameUnit, -76.0F, 0.0F, -5.6F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((211 + x) * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((251 + x) * frameUnit, 9.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((291 + x) * frameUnit, -15.0F, 0.0F, -5.0F, InterpType.LINEAR));
		leftThighRunCycle.add(new KeyFrame((331 + x) * frameUnit, -76.0F, 0.0F, -5.6F, InterpType.LINEAR));
		KeyFrame.toRadians(leftThighRunCycle);

		List rightThighFrames = new ArrayList(13);
		rightThighFrames.add(new KeyFrame(0.0F, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
		rightThighFrames.add(new KeyFrame(1.0F * frameUnit, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
		rightThighFrames.add(new KeyFrame(37.0F * frameUnit, -15.0F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(rightThighFrames);
		List rightThighRunCycle = KeyFrame.cloneFrames(leftThighRunCycle);
		KeyFrame.mirrorFramesX(rightThighRunCycle);
		KeyFrame.offsetFramesCircular(rightThighRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

		leftThighFrames.addAll(leftThighRunCycle);
		rightThighFrames.addAll(rightThighRunCycle);
		allKeyFrames.put(BonesBirdLegs.LEFT_KNEE, leftThighFrames);
		allKeyFrames.put(BonesBirdLegs.RIGHT_KNEE, rightThighFrames);

		List leftLegFrames = new ArrayList(19);
		leftLegFrames.add(new KeyFrame(0.0F, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegFrames.add(new KeyFrame(1.0F * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegFrames.add(new KeyFrame(10.0F * frameUnit, -80.300003F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegFrames.add(new KeyFrame(25.0F * frameUnit, -44.200001F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegFrames.add(new KeyFrame(35.0F * frameUnit, -5.6F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(leftLegFrames);

		List leftLegRunCycle = new ArrayList(16);
		leftLegRunCycle.add(new KeyFrame(38.0F * frameUnit, 6.6F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(44.0F * frameUnit, 6.5F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(47.0F * frameUnit, -11.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(50.0F * frameUnit, -24.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(53.0F * frameUnit, -32.900002F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(56.0F * frameUnit, -40.799999F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(59.0F * frameUnit, -46.700001F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(62.0F * frameUnit, -45.799999F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(82.0F * frameUnit, -45.599998F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame(97.0F * frameUnit, -17.1F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((85 + x) * frameUnit, 0.75F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((90 + x) * frameUnit, -0.4F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((101 + x) * frameUnit, -43.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((115 + x) * frameUnit, -60.099998F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((154 + x) * frameUnit, -50.5F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((170 + x) * frameUnit, 6.6F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((171 + x) * frameUnit, -37.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((211 + x) * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((251 + x) * frameUnit, 15.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((291 + x) * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftLegRunCycle.add(new KeyFrame((331 + x) * frameUnit, -37.0F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(leftLegRunCycle);

		List rightLegFrames = new ArrayList(19);
		rightLegFrames.add(new KeyFrame(0.0F, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		rightLegFrames.add(new KeyFrame(37.0F * frameUnit, -41.0F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(rightLegFrames);

		List rightLegRunCycle = KeyFrame.cloneFrames(leftLegRunCycle);
		KeyFrame.mirrorFramesX(rightLegRunCycle);
		KeyFrame.offsetFramesCircular(rightLegRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

		leftLegFrames.addAll(leftLegRunCycle);
		rightLegFrames.addAll(rightLegRunCycle);
		allKeyFrames.put(BonesBirdLegs.LEFT_ANKLE, leftLegFrames);
		allKeyFrames.put(BonesBirdLegs.RIGHT_ANKLE, rightLegFrames);

		List leftAnkleFrames = new ArrayList(27);
		leftAnkleFrames.add(new KeyFrame(0.0F, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(1.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(5.0F * frameUnit, 31.700001F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(10.0F * frameUnit, 45.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(20.0F * frameUnit, 52.799999F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(25.0F * frameUnit, 51.599998F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleFrames.add(new KeyFrame(30.0F * frameUnit, 42.299999F, -5.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(leftAnkleFrames);
		List leftAnkleRunCycle = new ArrayList(21);
		leftAnkleRunCycle.add(new KeyFrame(38.0F * frameUnit, 28.799999F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(44.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(47.0F * frameUnit, 7.6F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(50.0F * frameUnit, 12.4F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(53.0F * frameUnit, 12.6F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(56.0F * frameUnit, 11.8F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(59.0F * frameUnit, 8.5F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(62.0F * frameUnit, 1.6F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(82.0F * frameUnit, -1.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(87.0F * frameUnit, -5.5F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(90.0F * frameUnit, -0.7F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(93.0F * frameUnit, 6.8F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame(97.0F * frameUnit, -4.6F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((85 + x) * frameUnit, 20.700001F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((95 + x) * frameUnit, 34.200001F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((100 + x) * frameUnit, 45.599998F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((110 + x) * frameUnit, 36.599998F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((115 + x) * frameUnit, 38.400002F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((124 + x) * frameUnit, 50.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((140 + x) * frameUnit, 45.299999F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((154 + x) * frameUnit, 52.900002F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((170 + x) * frameUnit, 25.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((171 + x) * frameUnit, -38.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((211 + x) * frameUnit, 0.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((251 + x) * frameUnit, 22.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((291 + x) * frameUnit, 0.0F, -5.0F, 0.0F, InterpType.LINEAR));
		leftAnkleRunCycle.add(new KeyFrame((331 + x) * frameUnit, -38.0F, -5.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(leftAnkleRunCycle);

		List rightAnkleFrames = new ArrayList(27);
		rightAnkleFrames.add(new KeyFrame(0.0F, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		rightAnkleFrames.add(new KeyFrame(1.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		rightAnkleFrames.add(new KeyFrame(37.0F * frameUnit, -0.4F, -5.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(rightAnkleFrames);
		List rightAnkleRunCycle = KeyFrame.cloneFrames(leftAnkleRunCycle);
		KeyFrame.mirrorFramesX(rightAnkleRunCycle);
		KeyFrame.offsetFramesCircular(rightAnkleRunCycle, runBegin, runEnd, (runEnd - runBegin) / 2.0F);

		leftAnkleFrames.addAll(leftAnkleRunCycle);
		rightAnkleFrames.addAll(rightAnkleRunCycle);
		allKeyFrames.put(BonesBirdLegs.LEFT_METATARSOPHALANGEAL_ARTICULATIONS, leftAnkleFrames);
		allKeyFrames.put(BonesBirdLegs.RIGHT_METATARSOPHALANGEAL_ARTICULATIONS, rightAnkleFrames);

		List leftBackClawFrames = new ArrayList(21);
		leftBackClawFrames.add(new KeyFrame(0.0F, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((170 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((171 + x) * frameUnit, 84.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((211 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((251 + x) * frameUnit, -7.5F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((291 + x) * frameUnit, 77.0F, 0.0F, 0.0F, InterpType.LINEAR));
		leftBackClawFrames.add(new KeyFrame((331 + x) * frameUnit, 84.0F, 0.0F, 0.0F, InterpType.LINEAR));

		KeyFrame.toRadians(leftBackClawFrames);
		List rightBackClawFrames = KeyFrame.cloneFrames(leftBackClawFrames);
		KeyFrame.mirrorFramesX(rightBackClawFrames);

		allKeyFrames.put(BonesBirdLegs.LEFT_BACK_CLAW, leftBackClawFrames);
		allKeyFrames.put(BonesBirdLegs.RIGHT_BACK_CLAW, rightBackClawFrames);

		Animation birdRun = new Animation(BonesBirdLegs.class, 1.0F, 0.04651163F, allKeyFrames, animationPhases);
		AnimationRegistry.instance().registerAnimation("bird_run", birdRun);

		EnumMap allKeyFramesWings = new EnumMap(BonesWings.class);
		animationPhases = new ArrayList(3);

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.WINGFLAP, 0.2714932F, 0.0F);
		transitions.put(AnimationAction.WINGFLAP, defaultTransition);
		transitions.put(AnimationAction.WINGTUCK, new Transition(AnimationAction.WINGTUCK, 0.06787331F, 0.2760181F));
		transitions.put(AnimationAction.WINGGLIDE, new Transition(AnimationAction.WINGGLIDE, 0.06787331F, 0.8190045F));
		animationPhases.add(
				new AnimationPhaseInfo(AnimationAction.WINGFLAP, 0.0F, 0.2714932F, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.WINGSPREAD, 0.5429865F, 0.5475113F);
		transitions.put(AnimationAction.WINGSPREAD, defaultTransition);
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGTUCK, 0.2760181F, 0.5429865F, defaultTransition,
				transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.WINGTUCK, 0.8190045F, 0.2760181F);
		transitions.put(AnimationAction.WINGTUCK, defaultTransition);
		transitions.put(AnimationAction.WINGFLAP, new Transition(AnimationAction.WINGFLAP, 0.8190045F, 0.06787331F));
		transitions.put(AnimationAction.WINGGLIDE, new Transition(AnimationAction.WINGGLIDE, 0.8190045F, 0.8190045F));
		animationPhases.add(new AnimationPhaseInfo(AnimationAction.WINGSPREAD, 0.5475113F, 0.8190045F,
				defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.WINGGLIDE, 1.0F, 0.8190045F);
		transitions.put(AnimationAction.WINGGLIDE, defaultTransition);
		transitions.put(AnimationAction.WINGFLAP, new Transition(AnimationAction.WINGFLAP, 1.0F, 0.06787331F));
		transitions.put(AnimationAction.WINGTUCK, new Transition(AnimationAction.WINGTUCK, 1.0F, 0.2760181F));
		animationPhases.add(
				new AnimationPhaseInfo(AnimationAction.WINGGLIDE, 0.8190045F, 1.0F, defaultTransition, transitions));

		frameUnit = 0.004524887F;
		List rightInnerWingFrames = new ArrayList(12);
		rightInnerWingFrames.add(new KeyFrame(0.0F, 2.0F, -48.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(5.0F * frameUnit, 4.0F, -38.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(10.0F * frameUnit, 5.5F, -27.5F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(15.0F * frameUnit, 5.5F, -7.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(20.0F * frameUnit, 5.5F, 15.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(25.0F * frameUnit, 4.5F, 30.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 38.0F, 9.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(35.0F * frameUnit, 1.0F, 20.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(40.0F * frameUnit, 1.0F, 3.5F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(45.0F * frameUnit, 1.0F, -19.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(50.0F * frameUnit, -3.0F, -38.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(55.0F * frameUnit, -1.0F, -48.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(60.0F * frameUnit, 2.0F, -48.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames
				.add(new KeyFrame(61.0F * frameUnit, 5.5F, -7.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
		rightInnerWingFrames
				.add(new KeyFrame(121.0F * frameUnit, 0.71F, 88.599998F, 0.0F, 11.0F, -8.0F, 9.0F, InterpType.LINEAR));
		rightInnerWingFrames
				.add(new KeyFrame(181.0F * frameUnit, 5.5F, -7.0F, 0.0F, 7.0F, -8.0F, 6.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(209.0F * frameUnit, 5.5F, -5.0F, 0.0F, InterpType.LINEAR));
		rightInnerWingFrames.add(new KeyFrame(221.0F * frameUnit, 5.5F, -7.0F, 0.0F, InterpType.LINEAR));

		KeyFrame.toRadians(rightInnerWingFrames);
		List leftInnerWingFrames = KeyFrame.cloneFrames(rightInnerWingFrames);
		KeyFrame.mirrorFramesX(leftInnerWingFrames);
		allKeyFramesWings.put(BonesWings.LEFT_SHOULDER, rightInnerWingFrames);
		allKeyFramesWings.put(BonesWings.RIGHT_SHOULDER, leftInnerWingFrames);

		List rightOuterWingFrames = new ArrayList(13);
		rightOuterWingFrames.add(new KeyFrame(0.0F, 2.0F, 34.5F, 0.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(5.0F * frameUnit, 5.0F, 13.0F, -7.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(10.0F * frameUnit, 7.0F, 8.5F, -10.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(15.0F * frameUnit, 7.5F, -2.5F, -10.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(25.0F * frameUnit, 5.0F, 7.0F, -10.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(30.0F * frameUnit, 2.0F, 15.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(35.0F * frameUnit, -3.0F, 37.0F, 12.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(40.0F * frameUnit, -9.0F, 56.0F, 27.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(45.0F * frameUnit, -13.0F, 68.0F, 28.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(50.0F * frameUnit, -13.5F, 70.0F, 31.5F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(53.0F * frameUnit, -9.0F, 71.0F, 31.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(55.0F * frameUnit, -3.5F, 65.5F, 22.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(58.0F * frameUnit, 0.0F, 52.0F, 8.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(60.0F * frameUnit, 2.0F, 34.5F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(61.0F * frameUnit, -5.0F, -2.5F, -10.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(76.0F * frameUnit, 0.0F, 0.0F, 15.0F, 22.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(101.0F * frameUnit, 0.0F, 0.0F, 83.0F, 20.33F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(121.0F * frameUnit, 0.0F, 0.0F, 90.0F, 19.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(141.0F * frameUnit, 0.0F, 0.0F, 83.0F, 20.33F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(166.0F * frameUnit, 0.0F, 0.0F, 15.0F, 22.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames
				.add(new KeyFrame(181.0F * frameUnit, -5.0F, -2.5F, -10.0F, 23.0F, 1.0F, 0.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(209.0F * frameUnit, -5.0F, -1.3F, -10.0F, InterpType.LINEAR));
		rightOuterWingFrames.add(new KeyFrame(221.0F * frameUnit, -5.0F, -2.5F, -10.0F, InterpType.LINEAR));
		KeyFrame.toRadians(rightOuterWingFrames);
		List leftOuterWingFrames = KeyFrame.cloneFrames(rightOuterWingFrames);
		KeyFrame.mirrorFramesX(leftOuterWingFrames);
		allKeyFramesWings.put(BonesWings.LEFT_ELBOW, rightOuterWingFrames);
		allKeyFramesWings.put(BonesWings.RIGHT_ELBOW, leftOuterWingFrames);

		Animation wingFlap = new Animation(BonesWings.class, 1.0F, 0.01666667F, allKeyFramesWings, animationPhases);
		AnimationRegistry.instance().registerAnimation("wing_flap_2_piece", wingFlap);

		EnumMap allKeyFramesBeak = new EnumMap(BonesMouth.class);
		animationPhases = new ArrayList(3);

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.MOUTH_CLOSE, 0.5F, 0.5083333F);
		transitions.put(AnimationAction.MOUTH_CLOSE, defaultTransition);
		animationPhases
				.add(new AnimationPhaseInfo(AnimationAction.MOUTH_OPEN, 0.0F, 0.5F, defaultTransition, transitions));

		transitions = new HashMap(1);
		defaultTransition = new Transition(AnimationAction.MOUTH_OPEN, 1.0F, 0.0F);
		transitions.put(AnimationAction.MOUTH_OPEN, defaultTransition);
		animationPhases
				.add(new AnimationPhaseInfo(AnimationAction.MOUTH_CLOSE, 0.5F, 1.0F, defaultTransition, transitions));

		frameUnit = 0.008333334F;
		List upperBeakFrames = new ArrayList(3);
		upperBeakFrames.add(new KeyFrame(0.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
		upperBeakFrames.add(new KeyFrame(60.0F * frameUnit, -8.0F, 0.0F, 0.0F, InterpType.LINEAR));
		upperBeakFrames.add(new KeyFrame(120.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(upperBeakFrames);
		allKeyFramesBeak.put(BonesMouth.UPPER_MOUTH, upperBeakFrames);

		List lowerBeakFrames = new ArrayList(3);
		lowerBeakFrames.add(new KeyFrame(0.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
		lowerBeakFrames.add(new KeyFrame(60.0F * frameUnit, 20.0F, 0.0F, 0.0F, InterpType.LINEAR));
		lowerBeakFrames.add(new KeyFrame(120.0F * frameUnit, 0.0F, 0.0F, 0.0F, InterpType.LINEAR));
		KeyFrame.toRadians(lowerBeakFrames);
		allKeyFramesBeak.put(BonesMouth.LOWER_MOUTH, lowerBeakFrames);

		Animation beak = new Animation(BonesMouth.class, 1.0F, 0.1F, allKeyFramesBeak, animationPhases);
		AnimationRegistry.instance().registerAnimation("bird_beak", beak);
	}

	@Override
	public File getFile(String fileName) {
		return new File(FMLClientHandler.instance().getClient().mcDataDir.getPath() + fileName);
	}
}