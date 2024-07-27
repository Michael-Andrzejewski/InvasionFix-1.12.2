// `^`^`^`
// ```java
// /**
//  * This class extends the SlotItemHandler to create a custom slot that is intended to be used as an output slot in a machine's inventory.
//  * It is part of the 'invmod' package, specifically within the 'inventory.slot' subpackage, indicating its role in inventory management.
//  *
//  * Constructor:
//  * - SlotOutput(IItemHandler iinventory, int i, int j, int k): Initializes the slot with the specified item handler and position.
//  *
//  * Methods:
//  * - isItemValid(ItemStack itemstack): Always returns false, indicating that no items can be manually placed in this slot, as it is meant for output only.
//  * - putStack(@Nonnull ItemStack stack): This method is overridden but not implemented, suggesting that the behavior for adding a stack to this slot will be handled elsewhere or will be added in future development.
//  *
//  * Overall, this class is designed to represent a slot in a GUI (Graphical User Interface) where processed items are placed, and it prevents the player from placing items into it directly, ensuring the slot is used exclusively for machine outputs.
//  */
// ```
// `^`^`^`

package invmod.inventory.slot;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutput extends SlotItemHandler {
	public SlotOutput(IItemHandler iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public void putStack(@Nonnull ItemStack stack) {

	}
}