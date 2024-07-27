// `^`^`^`
// ```java
// /**
//  * This class represents a custom item within a Minecraft mod, specifically a "Strange Bone" item.
//  * The item is designed to interact with tamed wolves to potentially convert them into a special ally entity.
//  *
//  * Class: ItemStrangeBone
//  * Superclass: ModItem
//  * Package: invmod.item
//  *
//  * Public Methods:
//  * - ItemStrangeBone(): Constructor that sets the item's name and maximum stack size to 1.
//  * - getDamage(ItemStack stack): Returns the damage value of the item, which is always 0.
//  * - itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase targetEntity, EnumHand hand):
//  *   Attempts to interact with an entity. If the entity is a tamed wolf (but not an EntityIMWolf), it searches for a TileEntityNexus
//  *   within a certain radius. If found, it converts the wolf into an EntityIMWolf associated with the nexus, spawns it into the world,
//  *   and kills the original wolf. The item stack is then reduced by 1. If no nexus is found, it sends a message to the player.
//  *   Returns true if the entity is a wolf and false otherwise.
//  *
//  * Usage:
//  * - The item can be used by players to convert tamed wolves near a nexus block into EntityIMWolf, a special ally entity.
//  * - This conversion will only happen if the wolf is tamed and a nexus block is within the search radius.
//  * - The item has a limited use as it shrinks the item stack upon successful conversion of a wolf.
//  */
// ```
// `^`^`^`

package invmod.item;

import invmod.ModBlocks;
import invmod.entity.ally.EntityIMWolf;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;

public class ItemStrangeBone extends ModItem {

	public ItemStrangeBone() {
		super("strangeBone");
		this.setMaxStackSize(1);
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase targetEntity,
			EnumHand hand) {
		if ((!targetEntity.world.isRemote) && ((targetEntity instanceof EntityWolf))
				&& (!(targetEntity instanceof EntityIMWolf))) {
			EntityWolf wolf = (EntityWolf) targetEntity;

			if (wolf.isTamed()) {
				TileEntityNexus nexus = null;
				int x = MathHelper.floor(wolf.posX);
				int y = MathHelper.floor(wolf.posY);
				int z = MathHelper.floor(wolf.posZ);

				for (int i = -7; i < 8; i++) {
					for (int j = -4; j < 5; j++) {
						for (int k = -7; k < 8; k++) {
							BlockPos newBlockPos = new BlockPos(x, y, z).add(i, j, k);
							if (wolf.world.getBlockState(newBlockPos)
									.getBlock() == /* BlocksAndItems.blockNexus */ModBlocks.NEXUS_BLOCK) {
								nexus = (TileEntityNexus) wolf.world.getTileEntity(newBlockPos);
								break;
							}
						}
					}
				}

				if (nexus != null) {
					EntityIMWolf newWolf = new EntityIMWolf(wolf, nexus);
					wolf.world.spawnEntity(newWolf);
					wolf.setDead();
					itemStack.shrink(1);
				} else {
					player.sendMessage(new TextComponentTranslation("The wolf doesn't like this strange bone."));
				}
			}

			return true;
		}

		return false;
	}

}