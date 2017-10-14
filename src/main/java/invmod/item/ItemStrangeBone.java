package invmod.item;

import invmod.BlocksAndItems;
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
	
	public ItemStrangeBone(){
		super("strangeBone");
		this.setMaxStackSize(1);
	}
	
	@Override
	public int getDamage(ItemStack stack){
		return 0;
	}
	
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase targetEntity, EnumHand hand){
		if ((!targetEntity.worldObj.isRemote) && ((targetEntity instanceof EntityWolf)) && (!(targetEntity instanceof EntityIMWolf))){
			EntityWolf wolf = (EntityWolf)targetEntity;

			if (wolf.isTamed()){
				TileEntityNexus nexus = null;
				int x = MathHelper.floor_double(wolf.posX);
				int y = MathHelper.floor_double(wolf.posY);
				int z = MathHelper.floor_double(wolf.posZ);

				for (int i = -7; i < 8; i++){
					for (int j = -4; j < 5; j++){
						for (int k = -7; k < 8; k++){
							BlockPos newBlockPos = new BlockPos(x,y,z).add(i,j,k);
							if (wolf.worldObj.getBlockState(newBlockPos).getBlock() == BlocksAndItems.blockNexus){
								nexus = (TileEntityNexus)wolf.worldObj.getTileEntity(newBlockPos);
								break;
							}
						}
					}
				}

				if (nexus != null){
					EntityIMWolf newWolf = new EntityIMWolf(wolf, nexus);
					wolf.worldObj.spawnEntityInWorld(newWolf);
					wolf.setDead();
					itemStack.stackSize -= 1;
				} else {
					player.addChatMessage(new TextComponentTranslation("The wolf doesn't like this strange bone."));
				}
			}

			return true;
		}

		return false;
	}
	
}