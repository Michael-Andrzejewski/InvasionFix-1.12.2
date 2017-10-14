package invmod.client.render;

import invmod.Reference;
import invmod.entity.monster.EntityIMCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

public class RenderIMCreeper extends RenderLiving<EntityIMCreeper> {
	private static final ResourceLocation texture = new ResourceLocation(Reference.MODID + ":textures/creeper.png");

	private static ModelCreeper modelCreeper = new ModelCreeper(){
		@Override
		public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale){
			super.render(entityIn, limbSwing / 3f, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			this.creeperArmor.render(scale);
		}
		@Override
		public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn){
			super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
			this.creeperArmor.rotateAngleX = this.head.rotateAngleX;
			this.creeperArmor.rotateAngleY = this.head.rotateAngleY;
			this.creeperArmor.rotateAngleZ = this.head.rotateAngleZ;
		}
	};

	public RenderIMCreeper(RenderManager renderManager) {
		super(renderManager, modelCreeper, 0.5F);
	}
	
	//update creeper scale
	@Override
	protected void preRenderCallback(EntityIMCreeper par1EntityCreeper, float par2) {
		EntityIMCreeper entitycreeper = par1EntityCreeper;
		float f = entitycreeper.setCreeperFlashTime(par2);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;

		if (f < 0.0F) {
			f = 0.0F;
		}

		if (f > 1.0F) {
			f = 1.0F;
		}

		f *= f;
		f *= f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		GL11.glScalef(f2, f3, f2);
	}
	
	//update creeper color multiplier
	@Override
	protected int getColorMultiplier(EntityIMCreeper par1EntityCreeper, float par2, float par3) {
		EntityIMCreeper entitycreeper = par1EntityCreeper;
		float f = entitycreeper.setCreeperFlashTime(par3);

		if ((int) (f * 10.0F) % 2 == 0) {
			return 0;
		}

		int i = (int) (f * 0.2F * 255.0F);

		if (i < 0) {
			i = 0;
		}

		if (i > 255) {
			i = 255;
		}

		char c = 'a';
		char c1 = 'b';
		char c2 = 'c';
		return i << 24 | c << '\020' | c1 << '\b' | c2;
	}

	protected int func_27007_b(EntityIMCreeper par1EntityCreeper, int par2, float par3) {
		return -1;
	}

	protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
		return func_27007_b((EntityIMCreeper) par1EntityLiving, par2, par3);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityIMCreeper entity) {
		return texture;
	}
}