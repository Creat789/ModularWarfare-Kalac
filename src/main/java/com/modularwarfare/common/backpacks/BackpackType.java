package com.modularwarfare.common.backpacks;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.fpp.basic.configs.BackpackRenderConfig;
import com.modularwarfare.client.model.ModelBackpack;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BackpackType extends BaseType {

    public int size = 16;

    public boolean allowSmallerBackpackStorage = false;
    public Integer maxWeaponStorage = null;
    public int durability = 100;
    public boolean isAmmoNation = false;
    public Integer maxAmmunitionStorage;
    public  boolean isPartsBackpack = false;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null)
            maxStackSize = 1;
        loadBaseValues();
    }



    @Override
    public void reloadModel() {
        model = new ModelBackpack(ModularWarfare.getRenderConfig(this, BackpackRenderConfig.class), this);
    }

    @Override
    public String getAssetDir() {
        return "backpacks";
    }

    /***
     * Provider for the extraslots storage
     */
    public static class Provider implements ICapabilitySerializable<NBTBase> {
        final IItemHandlerModifiable items;

        public Provider(final BackpackType type) {
            this.items = new ItemStackHandler(type.size);
        }

        @Override
        public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.items : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.items, null);
        }

        @Override
        public void deserializeNBT(final NBTBase nbt) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.items, null, nbt);
        }
    }

    public static int getFreeSlot(IItemHandler vestInvent) {
        int FreeSlot = 0;
        for(int i=0; i < vestInvent.getSlots(); i++){
            if(vestInvent.getStackInSlot(i) != null){
                if(vestInvent.getStackInSlot(i).isEmpty()){
                    FreeSlot++;
                }
            }
        }
        return FreeSlot;
    }
    public static int getGunSlot(IItemHandler backpackInvent) {
        int numGuns = 0;
        for(int i=0; i < backpackInvent.getSlots(); i++){
            if(backpackInvent.getStackInSlot(i) != null){
                if(backpackInvent.getStackInSlot(i).getItem() instanceof ItemGun){
                    numGuns++;
                }
            }
        }
        return numGuns;
    }
}