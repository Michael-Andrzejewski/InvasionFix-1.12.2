package invmod.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemInfusedSword extends ItemSword {

	public final String name = "infusedSword";
	// <3 minecraftforum ;)

	public ItemInfusedSword() {
		super(ToolMaterial.DIAMOND);
		// this.setRegistryName(this.name);
		// GameRegistry.register(this);
		// amount of entity hits it takes to recharge sword.
		this.setMaxDamage(40);
		// this.setUnlocalizedName(this.name);
		// this.setCreativeTab(mod_Invasion.tabInvmod);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
		if (this.isDamaged(itemstack)) {
			this.setDamage(itemstack, this.getDamage(itemstack) - 1);

		}
		return true;
	}

	// Iirc this is already handled by the sword base class itself
	/*
	 * @Override public float getStrVsBlock(ItemStack par1ItemStack, IBlockState
	 * par2Block) { if (par2Block == Blocks.WEB) { return 15.0F; }
	 * 
	 * Material material = par2Block.getMaterial(); return (material !=
	 * Material.PLANTS) && (material != Material.VINE) && (material !=
	 * Material.CORAL) && (material != Material.LEAVES) && (material !=
	 * Material.SPONGE) && (material != Material.CACTUS) ? 1.0F : 1.5F; }
	 */

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.NONE;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 0;
	}

	/*
	 * @Override public ActionResult<ItemStack> onItemRightClick(ItemStack
	 * itemstack, World world, EntityPlayer entityplayer, EnumHand hand) {
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World wolrdIn, EntityPlayer player, EnumHand handIn) {
		ItemStack itemstack = player.getHeldItem(handIn);
		if (itemstack.getItemDamage() == 0) {
			// if player isSneaking then refill hunger else refill health
			if (player.isSneaking()) {
				player.getFoodStats().addStats(6, 0.5f);
				// world.playSoundAtEntity(entityplayer, "random.burp", 0.5F,
				// world.rand.nextFloat() * 0.1F + 0.9F);
				player.playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5f, wolrdIn.rand.nextFloat() * 0.1f + 0.9f);
			} else {
				player.heal(6.0F);
				// spawn heart particles around the player
				wolrdIn.spawnParticle(EnumParticleTypes.HEART, player.posX + 1.5D, player.posY, player.posZ, 0.0D, 0.0D,
						0.0D);
				wolrdIn.spawnParticle(EnumParticleTypes.HEART, player.posX - 1.5D, player.posY, player.posZ, 0.0D, 0.0D,
						0.0D);
				wolrdIn.spawnParticle(EnumParticleTypes.HEART, player.posX, player.posY, player.posZ + 1.5D, 0.0D, 0.0D,
						0.0D);
				wolrdIn.spawnParticle(EnumParticleTypes.HEART, player.posX, player.posY, player.posZ - 1.5D, 0.0D, 0.0D,
						0.0D);
			}

			itemstack.setItemDamage(this.getMaxDamage());
		}

		return new ActionResult<>(EnumActionResult.PASS, itemstack);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return state.getBlock() == Blocks.WEB;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase playerIn) {
		return true;
	}

}