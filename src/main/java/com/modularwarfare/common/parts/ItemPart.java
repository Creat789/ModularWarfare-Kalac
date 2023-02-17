package com.modularwarfare.common.parts;

import com.modularwarfare.common.type.BaseItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ItemPart extends BaseItem {

    public static final Function<PartType, ItemPart> factory = type -> { return new ItemPart(type); };
    public PartType type;

    public ItemPart(PartType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add("Utilisable pour craft un item");

        } else {
            tooltip.add("\u00A71Parts");
            tooltip.add("\u00a7e+" + "Shift pour plus d'informations");
        }
    }



    @Override
    public boolean getShareTag() {
        return true;
    }

}