package invmod.item;

import invmod.entity.projectile.EntityIMArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;


public class ItemSearingBow extends ItemBow
{

	public static final String[] bowPullIconNameArray = new String[] { "searingBow_pulling_0", "searingBow_pulling_1", "searingBow_pulling_2" };
	public final String name = "searingBow";

	public ItemSearingBow()
	{
		super();
		//this.setRegistryName(this.name);
		//GameRegistry.register(this);
		//this.setUnlocalizedName(this.name);
		//this.setCreativeTab(mod_Invasion.tabInvmod);
	}

	/*@Override
	public void onPlayerStoppedUsing(ItemStack stackIn, World worldIn, EntityLivingBase wielder, int timeLeft)
	{
		int var6 = this.getMaxItemUseDuration(stackIn) - timeLeft;

		if (wielder instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)wielder;

			boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stackIn) > 0;
			ItemStack extraArrows = this.findAmmo(player);

			ArrowLooseEvent event = new ArrowLooseEvent(player, stackIn, worldIn, var6, (extraArrows != null) || flag);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.isCanceled()) return;

			var6 = event.getCharge();

			if (flag || extraArrows != null)
			{
				if (extraArrows == null) extraArrows = new ItemStack(Items.ARROW);

				float f = var6 / 20.0F;
				f = (f * f + f * 2.0F) / 3.0F;
				boolean special = false;

				if (f < 0.1D) return;
				if (f >= 3.8F)
				{
					special = true;
					f = 1.0F;
				}
				else if (f > 1.0F)
				{
					f = 1.0F;
				}

				if (!special)
				{
					// EntityIMArrow var8 = new EntityIMArrow(par2World,
					// par3EntityPlayer, f * 2.0F);
					//EntityIMArrow var8 = new EntityIMArrow(worldIn, wielder, f * 2.0F);
					EntityIMArrow var8 = new EntityIMArrow(worldIn, wielder);
					var8.setVelocity(var8.motionX * 2, var8.motionY * 2, var8.motionZ * 2);
					if (f == 1.0F) var8.setIsCritical(true);

					int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackIn);

					if (var9 > 0) var8.setDamage(var8.getDamage() + var9 * 0.5D + 0.5D);

					int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackIn);

					if (var10 > 0) var8.setKnockbackStrength(var10);

					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackIn) > 0) var8.setFire(100);

					if (flag)
					{
						var8.canBePickedUp = 2;
					}
					else
					{
						player.inventory.decrStackSize(player.inventory.getSlotFor(new ItemStack(Items.ARROW)), 1);
					}

					if (!worldIn.isRemote) worldIn.spawnEntity(var8);
				}
				else
				{
					//EntityIMArrow var8 = new EntityIMArrow(worldIn, wielder, f * 2.0F);
					EntityIMArrow var8 = new EntityIMArrow(worldIn, wielder);
					var8.setVelocity(var8.motionX * 2, var8.motionY * 2, var8.motionZ * 2);

					var8.setFire(100);
					var8.setDamage((var8.getDamage() + 1 * 0.5D + 0.5D) * 3 / 2 + 1);
					if (!worldIn.isRemote) worldIn.spawnEntity(var8);
				}

				stackIn.damageItem(1, wielder);
				//worldIn.playSoundAtEntity(wielder, "random.bow", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
				player.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1f, 1f / (Item.itemRand.nextFloat() * 0.4f + 1.2f));
			}
		}

	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stackIn, World worldIn, EntityLivingBase entity)
	{
		return stackIn;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.BOW;
	}


	//DarthXenon: Too similar to super.onItemRightClick() to necessitate overriding.
	/*@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer, EnumHand hand) {
		/*ArrowNockEvent event = new ArrowNockEvent(entityPlayer, itemStack, hand, world, true);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.getResult();
		}*/
	/*ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
	if (ret != null) return ret;
	
	
	
	if ((entityPlayer.capabilities.isCreativeMode)
			|| (entityPlayer.inventory.hasItemStack(Items.ARROW) || (EnchantmentHelper
					.getEnchantmentLevel(Enchantment.infinity.effectId,
							itemStack) > 0))) {
		entityPlayer.setItemInUse(itemStack,
				getMaxItemUseDuration(itemStack));
	}
	
	return itemStack;
	}*/

	/*public String getName()
	{
		return this.name;
	}

	protected ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				if (this.isArrow(itemstack)) return itemstack;
			}

			return null;
		}
	}*/

}