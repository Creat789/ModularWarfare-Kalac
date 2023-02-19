package com.modularwarfare.common.backpacks;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.container.ContainerInventoryModified;
import com.modularwarfare.common.guns.*;
import com.modularwarfare.common.network.PacketGunSwitchMode;
import com.modularwarfare.common.init.ModSounds;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemBackpack extends BaseItem {

    public static final Function<BackpackType, ItemBackpack> factory = type -> { return new ItemBackpack(type); };
    public BackpackType type;
    public IExtraItemHandler extra;
    public ItemBackpack(BackpackType type) {
        super(type);
        this.maxStackSize = 1;
        this.type = type;
        this.render3d = false;
        this.isDamageable();
        isRepairable();
        this.setMaxDamage(type.durability);
    }
    @Override
    public void setType(BaseType type) {
        this.type = (BackpackType) type;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
        return new BackpackType.Provider(this.type);
    }

    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> lines, ITooltipFlag b)
    {
        final IItemHandler items = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        lines.add("\u00A71Backpack");
        lines.add("\u00a73WeaponStorage: " + "\u00A77" +TextFormatting.RED+BackpackType.getGunSlot(items) + "/" + (int)((type.maxWeaponStorage)));
        lines.add(TextFormatting.BLUE.toString() + "Slots: " +TextFormatting.RED+BackpackType.getFreeSlot(items) + "/" + type.size);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.hasCapability(CapabilityExtra.CAPABILITY, null)) {
            final IExtraItemHandler backpack = playerIn.getCapability(CapabilityExtra.CAPABILITY, null);
            if (backpack.getStackInSlot(0).isEmpty()) {
                backpack.setStackInSlot(0, itemstack.copy());
                itemstack.setCount(0);
                worldIn.playSound(null, playerIn.getPosition(), ModSounds.EQUIP_EXTRA, SoundCategory.PLAYERS, 2.0f, 1.0f);
                return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
            }
        }


        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(final ItemStack stack) {
        NBTTagCompound tags = super.getNBTShareTag(stack);

        // Add extraslots information to NBT tag that is sent to the client
        if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            if (tags == null) {
                tags = new NBTTagCompound();
            }

            final IItemHandler items = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            tags.setTag("_items", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(items, null));
        }

        return tags;
    }


    @Override
    public void readNBTShareTag(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);

        if (stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && (nbt != null)) {
            final IItemHandler items = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            final NBTBase itemTags = nbt.getTag("_items");
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(items, null, itemTags);
        }
    }

    @Override
    public void onUpdate(final ItemStack stack, final World worldIn, final Entity entityIn, final int itemSlot, final boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int damage, final int slot) {
        stack.damageItem(damage, entity);
    }
}
