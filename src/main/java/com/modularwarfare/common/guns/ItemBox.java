package com.modularwarfare.common.guns;

import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.tooltips.LegendaryTooltipsConfig;
import com.modularwarfare.tooltips.render.TooltipDecor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ItemBox extends BaseItem {

    public static final Function<BoxType, ItemBox> factory = type -> { return new ItemBox(type); };
    public BoxType type;

    public ItemBox(BoxType type) {
        super(type);
        this.type = type;
        this.render3d = false;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }
    @SideOnly(Side.CLIENT)

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add("Placer dans la zone de crafting pour ouvrir");

        } else {
            tooltip.add("\u00A71Bullet Box");
            tooltip.add("\u00a7e+" + "Shift pour plus d'informations");
        }
    }


}