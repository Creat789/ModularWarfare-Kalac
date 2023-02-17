package com.modularwarfare.common.vest;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.config.VestRenderConfig;
import com.modularwarfare.client.model.ModelVest;
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

public class VestType extends BaseType {

    public int size = 16;
    public boolean allowSmallerVestStorage = false;
    public int durability = 100;
    public double defense;
    public boolean isfoodCapsule = false;
    public boolean ismedicalPencil = false;
    public boolean isammoStorage = false;

    @Override
    public void loadExtraValues() {
        if (maxStackSize == null)
            maxStackSize = 1;
        loadBaseValues();
    }

    @Override
    public void reloadModel() {
        model = new ModelVest(ModularWarfare.getRenderConfig(this, VestRenderConfig.class), this);
    }

    @Override
    public String getAssetDir() {
        return "vest";
    }

    /***
     * Provider for the extraslots storage
     */
    public static class Provider implements ICapabilitySerializable<NBTBase> {
        final IItemHandlerModifiable items;

        public Provider(final VestType type) {
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
}