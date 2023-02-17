package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ItemOverlay extends BaseItem {

    public static final Function<OverlayType, ItemOverlay> factory = type -> { return new ItemOverlay(type); };
    public OverlayType type;

    public ItemOverlay(OverlayType type) {
        super(type);
        this.type = type;
        this.render3d = false;
        this.setMaxDamage(type.usableMaxAmount);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("\u00A71Frame");
        tooltip.add("\u00a7e+" + "Application d'une customframe via les attachements");
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

}