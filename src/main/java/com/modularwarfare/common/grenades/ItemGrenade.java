package com.modularwarfare.common.grenades;

import com.modularwarfare.common.entity.grenades.*;
import com.modularwarfare.common.init.ModSounds;
import com.modularwarfare.common.type.BaseItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ItemGrenade extends BaseItem {

    public static final Function<GrenadeType, ItemGrenade> factory = type -> {
        return new ItemGrenade(type);
    };
    public GrenadeType type;

    public ItemGrenade(GrenadeType type) {
        super(type);
        this.type = type;
        this.render3d = true;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer playerIn = (EntityPlayer) entityLiving;
            World worldIn = playerIn.world;
            if (!worldIn.isRemote) {
                switch (type.grenadeType) {
                    case C4:
                        EntityC4 c4 = new EntityC4(worldIn, playerIn, false, type);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        worldIn.spawnEntity(c4);
                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        break;
                    case Frag:
                        EntityGrenade grenade = new EntityGrenade(worldIn, playerIn, false, type);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        worldIn.spawnEntity(grenade);
                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        break;
                    case Smoke:
                        EntitySmokeGrenade smoke = new EntitySmokeGrenade(worldIn, playerIn, true, type);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        worldIn.spawnEntity(smoke);
                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        break;
                    case Stun:
                        EntityStunGrenade stun = new EntityStunGrenade(worldIn, playerIn, true, type);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        worldIn.spawnEntity(stun);
                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        break;
                    case Gas:
                        EntityGasGrenade gas = new EntityGasGrenade(worldIn, playerIn, true, type);
                        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        worldIn.spawnEntity(gas);
                        if (!playerIn.capabilities.isCreativeMode) {
                            stack.shrink(1);
                        }
                        break;
                }
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.GRENADE_THROW, SoundCategory.PLAYERS, 0.5f, 1.0f);
        if (!worldIn.isRemote) {
            switch (type.grenadeType) {
                case Frag:
                    EntityGrenade grenade = new EntityGrenade(worldIn, playerIn, true, type);
                    worldIn.spawnEntity(grenade);

                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    break;
                case Smoke:
                    EntitySmokeGrenade smoke = new EntitySmokeGrenade(worldIn, playerIn, true, type);
                    worldIn.spawnEntity(smoke);

                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    break;
                case Stun:
                    EntityStunGrenade stun = new EntityStunGrenade(worldIn, playerIn, true, type);
                    worldIn.spawnEntity(stun);

                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    break;
                case Gas:
                    EntityGasGrenade gas = new EntityGasGrenade(worldIn, playerIn, true, type);
                    worldIn.spawnEntity(gas);

                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    break;
                case C4:
                    EntityC4 c4 = new EntityC4(worldIn, playerIn, true, type);
                    worldIn.spawnEntity(c4);

                    if (!playerIn.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                    break;
            }

        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add("Puissance:" + type.explosionPower);
            tooltip.add("FuseTime:" + type.fuseTime);
            tooltip.add("SmokeTime:" + type.smokeTime);
        } else {
            tooltip.add("\u00A71Grenade");
            tooltip.add("\u00a7e+" + "Shift pour plus d'informations");
        }
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

}