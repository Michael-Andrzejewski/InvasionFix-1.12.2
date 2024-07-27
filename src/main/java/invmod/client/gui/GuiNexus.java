// `^`^`^`
// ```java
// /**
//  * GuiNexus is a graphical user interface (GUI) class for displaying the Nexus block entity's status and information to the player.
//  * It extends GuiContainer, utilizing Minecraft's GUI system to draw a custom container interface.
//  *
//  * The class defines the following methods:
//  *
//  * - GuiNexus(InventoryPlayer inventoryplayer, TileEntityNexus tileentityNexus): Constructor that initializes the GUI with the player's inventory and the Nexus tile entity.
//  *
//  * - drawGuiContainerForegroundLayer(int x, int y): Overrides the method from GuiContainer to draw text elements on the GUI, such as the Nexus level, number of mobs killed, spawn radius, activation status, and current wave or power level, depending on the Nexus's mode.
//  *
//  * - drawGuiContainerBackgroundLayer(float f, int un1, int un2): Overrides the method from GuiContainer to handle the rendering of the GUI's background and dynamic elements, such as progress bars for generation and cooking progress, and activation status, which change based on the Nexus's mode and activation state.
//  *
//  * The class uses a ResourceLocation to reference the texture used for the GUI background and holds a reference to the TileEntityNexus to access and display its data.
//  */
// ```
// 
// This executive documentation provides a concise summary of the `GuiNexus` class, detailing its purpose and describing each method's functionality within the context of a Minecraft mod that adds a Nexus block entity with various states and information to display to the player.
// `^`^`^`

package invmod.client.gui;

import org.lwjgl.opengl.GL11;

import invmod.Reference;
import invmod.inventory.container.ContainerNexus;
import invmod.tileentity.TileEntityNexus;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiNexus extends GuiContainer {
	private static final ResourceLocation background = new ResourceLocation(Reference.MODID + ":textures/nexusgui.png");
	private TileEntityNexus tileEntityNexus;

	public GuiNexus(InventoryPlayer inventoryplayer, TileEntityNexus tileentityNexus) {
		super(new ContainerNexus(inventoryplayer, tileentityNexus));
		this.tileEntityNexus = tileentityNexus;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		this.fontRenderer.drawString("Nexus - Level " + this.tileEntityNexus.getNexusLevel(), 46, 6, 4210752);
		this.fontRenderer.drawString(this.tileEntityNexus.getNexusKills() + " mobs killed", 96, 60, 4210752);
		this.fontRenderer.drawString("R: " + this.tileEntityNexus.getSpawnRadius(), 142, 72, 4210752);

		if ((this.tileEntityNexus.getMode() == 1) || (this.tileEntityNexus.getMode() == 3)) {
			this.fontRenderer.drawString("Activated!", 13, 62, 4210752);
			this.fontRenderer.drawString("Wave " + this.tileEntityNexus.getCurrentWave(), 55, 37, 4210752);
		} else if (this.tileEntityNexus.getMode() == 2) {
			this.fontRenderer.drawString("Power:", 56, 31, 4210752);
			this.fontRenderer.drawString("" + this.tileEntityNexus.getNexusPowerLevel(), 61, 44, 4210752);
		}

		if ((this.tileEntityNexus.isActivating()) && (this.tileEntityNexus.getMode() == 0)) {
			this.fontRenderer.drawString("Activating...", 13, 62, 4210752);
			if (this.tileEntityNexus.getMode() != 4)
				this.fontRenderer.drawString("Are you sure?", 8, 72, 4210752);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int un1, int un2) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);

		int l = this.tileEntityNexus.getGenerationProgressScaled(26);
		this.drawTexturedModalRect(j + 126, k + 28 + 26 - l, 185, 26 - l, 9, l);
		l = this.tileEntityNexus.getCookProgressScaled(18);
		this.drawTexturedModalRect(j + 31, k + 51, 204, 0, l, 2);

		if ((this.tileEntityNexus.getMode() == 1) || (this.tileEntityNexus.getMode() == 3)) {
			this.drawTexturedModalRect(j + 19, k + 29, 176, 0, 9, 31);
			this.drawTexturedModalRect(j + 19, k + 19, 194, 0, 9, 9);
		} else if (this.tileEntityNexus.getMode() == 2) {
			this.drawTexturedModalRect(j + 19, k + 29, 176, 31, 9, 31);
		}

		if (((this.tileEntityNexus.getMode() == 0) || (this.tileEntityNexus.getMode() == 2))
				&& (this.tileEntityNexus.isActivating())) {
			l = this.tileEntityNexus.getActivationProgressScaled(31);
			this.drawTexturedModalRect(j + 19, k + 29 + 31 - l, 176, 31 - l, 9, l);
		} else if ((this.tileEntityNexus.getMode() == 4) && (this.tileEntityNexus.isActivating())) {
			l = this.tileEntityNexus.getActivationProgressScaled(31);
			this.drawTexturedModalRect(j + 19, k + 29 + 31 - l, 176, 62 - l, 9, l);
		}
	}
}