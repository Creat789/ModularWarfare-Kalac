package com.modularwarfare.common.container;

import com.modularwarfare.common.vest.ItemVest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Backpack slot in the players inventory
 */
public class SlotVestS extends SlotItemHandler
{
    public SlotVestS(final IItemHandler inv, final int index, final int xPosition, final int yPosition)
    {
        super(inv, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull final ItemStack stack) {
        return stack.getItem() instanceof ItemVest;
    }
}
