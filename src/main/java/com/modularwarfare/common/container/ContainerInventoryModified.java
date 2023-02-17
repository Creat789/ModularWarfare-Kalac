package com.modularwarfare.common.container;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.backpacks.BackpackType;
import com.modularwarfare.common.backpacks.ItemBackpack;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.guns.*;
import com.modularwarfare.common.network.PacketBackpackEquip;
import com.modularwarfare.common.network.PacketVestEquip;
import com.modularwarfare.common.vest.ItemVest;
import com.modularwarfare.common.vest.VestType;
import com.modularwarfare.utility.ModUtil;
import mchhui.modularmovements.coremod.minecraft.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 * Modified copy of Vanilla's Player inventory
 */
public class ContainerInventoryModified extends Container {

    public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public final InventoryCraftResult craftResult = new InventoryCraftResult();
    public IExtraItemHandler extra;
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;
    private static final EntityEquipmentSlot[] EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
            EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public VestType type;
    public GunType gun;
    public int SlotMax;

    public ContainerInventoryModified(final InventoryPlayer playerInv, final boolean isLocalWorld, final EntityPlayer player) {
        this.isLocalWorld = isLocalWorld;
        this.thePlayer = player;
        this.onCraftMatrixChanged(this.craftMatrix);
        this.addSlots(playerInv, player);
    }

    public void addSlots(final InventoryPlayer playerInv, final EntityPlayer player) {
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        this.extra = player.getCapability(CapabilityExtra.CAPABILITY, null);

        this.addSlotToContainer(new SlotCrafting(playerInv.player, this.craftMatrix, this.craftResult, 0, 154, 28));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + (i * 2), 116 + (j * 18), 18 + (i * 18)));
            }
        }

        for (int k = 0; k < 4; k++) {
            final EntityEquipmentSlot slot = EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new Slot(playerInv, 36 + (3 - k), 8, 8 + (k * 18)) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(final ItemStack stack) {
                    return stack.getItem().isValidArmor(stack, slot, player);
                }

                @Override
                public boolean canTakeStack(final EntityPlayer playerIn) {
                    final ItemStack itemstack = this.getStack();
                    return !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false
                            : super.canTakeStack(playerIn);
                }

                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
                }
            });
        }

        // Second light gray slots bar
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + ((i + 1) * 9), 8 + (j * 18), 102 - 12 + (i * 18)));
            }
        }

        // First light gray slots bar
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + (i * 18), 166 - 12));
        }

        // This is for the OFFHAND MouseHover
        this.addSlotToContainer(new Slot(playerInv, 40, 76, 62) {
            @Override
            @Nullable
            @SideOnly(Side.CLIENT)
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });

        this.addSlotToContainer(new SlotBackpack(this.extra, 0, ModUtil.BACKPACK_SLOT_OFFSET_X, ModUtil.BACKPACK_SLOT_OFFSET_Y + 1) {
            @Override
            public void onSlotChanged() {
                ContainerInventoryModified.this.updateBackpack();
                ContainerInventoryModified.this.addSlots(playerInv, player);
                super.onSlotChanged();
            }
        });



        this.addSlotToContainer(new SlotVest(this.extra, 1, ModUtil.BACKPACK_SLOT_OFFSET_X, ModUtil.BACKPACK_SLOT_OFFSET_Y + 1 + 18) {
            @Override
            public void onSlotChanged() {
                ContainerInventoryModified.this.addSlots(playerInv, player);
                super.onSlotChanged();
            }
        });

        this.addSlotToContainer(new SlotSuits(this.extra, 2, ModUtil.BACKPACK_SLOT_OFFSET_X, ModUtil.BACKPACK_SLOT_OFFSET_Y + 1 + 36) {
            @Override
            public void onSlotChanged() {
                ContainerInventoryModified.this.addSlots(playerInv, player);
                super.onSlotChanged();
            }
        });

        this.addSlotToContainer(new SlotVestS(this.extra, 3, ModUtil.BACKPACK_SLOT_OFFSET_X + 18, ModUtil.BACKPACK_SLOT_OFFSET_Y + 1) {
            @Override
            public void onSlotChanged() {
                ContainerInventoryModified.this.addSlots(playerInv, player);
                super.onSlotChanged();
            }
        });

        this.addSlotToContainer(new SlotMask(this.extra, 4, ModUtil.BACKPACK_SLOT_OFFSET_X + 18, ModUtil.BACKPACK_SLOT_OFFSET_Y + 1 + 18) {
            @Override
            public void onSlotChanged() {
                ContainerInventoryModified.this.addSlots(playerInv, player);
                super.onSlotChanged();
            }
        });
        this.updateVest();
        this.updateBackpack();
    }

    private void updateVest() {
        if (this.extra.getStackInSlot(3).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            final IItemHandler vestInvent = this.extra.getStackInSlot(3).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            int xP = 0;
            int yP = 0;
            final int x = 1 + ModUtil.BACKPACK_CONTENT_OFFSET_X - 250;
            final int y = 1 + ModUtil.BACKPACK_CONTENT_OFFSET_Y;
            //extra.getStackInSlot(3).getItem().addInformation(extra.getStackInSlot(3), Minecraft.getMinecraft().player, extra.getStackInSlot(3)., false);

            for (int i = 0; i < vestInvent.getSlots(); i++) {
                this.addSlotToContainer(
                        new SlotItemHandler(vestInvent, i, x + (xP * ModUtil.INVENTORY_SLOT_SIZE_PIXELS),
                                -1 + y + (yP * ModUtil.INVENTORY_SLOT_SIZE_PIXELS)) {
                            // Don't allow nesting backpacks if they are bigger (or have the same size) as the current extraslots
                            @Override
                            public boolean isItemValid(@Nonnull final ItemStack stack) {
                                if (stack.getItem() instanceof ItemVest) {
                                    ItemVest itemVest = ((ItemVest) extra.getStackInSlot(3).getItem());
                                    if(itemVest.type.allowSmallerVestStorage){
                                        final int otherVestSize = ((ItemVest) stack.getItem()).type.size;
                                        final int thisVestSize = vestInvent.getSlots();
                                        if (otherVestSize <= thisVestSize) {
                                            return true;
                                        }
                                        return false;
                                    } else {
                                        return false;
                                    }
                                }
                                if(stack.getItem() instanceof ItemVest || stack.getItem() instanceof ItemBackpack){
                                    if (this.getNumberOfVest(vestInvent) >= 0) {
                                        return false;
                                    }
                                }
                                ItemVest itemVest = ((ItemVest) extra.getStackInSlot(3).getItem());
                                if(stack.getItem() instanceof ItemAmmo && itemVest.type.isfoodCapsule == false | stack.getItem() instanceof ItemBullet && itemVest.type.isfoodCapsule == false)
                                {
                                    return true;
                                } else if (stack.getItem() instanceof ItemFood && itemVest.type.isfoodCapsule == true || stack.getItem() instanceof ItemPotion && itemVest.type.isfoodCapsule == true){
                                    return true;
                                }  else if (itemVest.type.ismedicalPencil == true){
                                    /*if(stack.getItem() instanceof ItemMorphine || stack.getItem() instanceof ItemRegen || stack.getItem() instanceof ItemRegen2 || stack.getItem() instanceof ItemHeal || stack.getItem() instanceof ItemVitamin_pills || stack.getItem() instanceof  ItemPainKiller){
                                        return true;
                                    } else {
                                        return false;
                                    }*/
                                } if (itemVest.type.isammoStorage == true){
                                    if(stack.getItem() instanceof ItemAmmo || stack.getItem() instanceof ItemBullet) {                                   return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }

                            private int getNumberOfVest(IItemHandler vestInvent) {
                                int numVest = 0;
                                for(int i=0; i < vestInvent.getSlots(); i++){
                                    if(vestInvent.getStackInSlot(i) != null){
                                        if(vestInvent.getStackInSlot(i).getItem() instanceof ItemVest){
                                            numVest++;
                                        }
                                    }
                                }
                                return numVest;
                            }

                            public int getNumberOfSlot(IItemHandler vestInvent) {
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
                        });
                xP++;

                if ((xP % 4) == 0) {
                    xP = 0;
                    yP++;
                }
            }
        }
    }

    private void updateBackpack() {
        if (this.extra.getStackInSlot(0).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            final IItemHandler backpackInvent = this.extra.getStackInSlot(0).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            int xP = 0;
            int yP = 0;
            final int x = 1 + ModUtil.BACKPACK_CONTENT_OFFSET_X;
            final int y = 1 + ModUtil.BACKPACK_CONTENT_OFFSET_Y;

            for (int i = 0; i < backpackInvent.getSlots(); i++) {
                this.addSlotToContainer(
                        new SlotItemHandler(backpackInvent, i, x + (xP * ModUtil.INVENTORY_SLOT_SIZE_PIXELS),
                                -1 + y + (yP * ModUtil.INVENTORY_SLOT_SIZE_PIXELS)) {
                            // Don't allow nesting backpacks if they are bigger (or have the same size) as the current extraslots
                            @Override
                            public boolean isItemValid(@Nonnull final ItemStack stack) {
                                ItemBackpack itemBackpack = ((ItemBackpack) extra.getStackInSlot(0).getItem());
                                EntityPlayerMP entityPlayerMP;
                                if (stack.getItem() instanceof ItemBackpack) {
                                    if(itemBackpack.type.maxAmmunitionStorage != null) {
                                        if (this.getNumberOfAmmunition(backpackInvent) >= itemBackpack.type.maxAmmunitionStorage) {
                                            return false;
                                        }
                                    }

                                    if(itemBackpack.type.allowSmallerBackpackStorage){
                                        final int otherBackpackSize = ((ItemBackpack) stack.getItem()).type.size;
                                        final int thisBackpackSize = backpackInvent.getSlots();
                                        if (otherBackpackSize <= thisBackpackSize) {
                                            return true;
                                        }
                                        return false;
                                    } else if (stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case") || stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.baki") || stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.foodwar") || stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.kimetsu") || stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.hxh") || stack.getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.onepiece")) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                if(itemBackpack.type.isAmmoNation == true && stack.getItem() instanceof ItemArmor || itemBackpack.type.isAmmoNation == true && stack.getItem() instanceof ItemGun || itemBackpack.type.isAmmoNation == true && stack.getItem() instanceof ItemSpecialArmor) {
                                    return false;
                                }

                                if(stack.getItem() instanceof ItemBackpack && itemBackpack.type.isAmmoNation == false){
                                    if (this.getNumberOfBackpack(backpackInvent) >= 0) {
                                        return false;
                                    }
                                }
                                if(stack.getItem() instanceof ItemBackpack && itemBackpack.type.isPartsBackpack == true){
                                    if(stack.getItem() instanceof ItemPart){
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                if(stack.getItem() instanceof ItemBackpack && itemBackpack.type.isAmmoNation == true){
                                    if (this.getNumberOfAmmunition(backpackInvent) >= 0) {
                                        return false;
                                    }
                                }

                                if(stack.getItem() instanceof ItemVest)
                                    return false;

                                if(stack.getItem() instanceof ItemGun){
                                    if(itemBackpack.type.maxWeaponStorage != null) {
                                        if (this.getNumberOfGuns(backpackInvent) >= itemBackpack.type.maxWeaponStorage) {
                                            return false;
                                        }
                                    }
                                }
                                return super.isItemValid(stack);
                            }

                            private int getNumberOfBackpack(IItemHandler backpackInvent) {
                                int numBackpack = 0;
                                for(int i=0; i < backpackInvent.getSlots(); i++){
                                    if(backpackInvent.getStackInSlot(i) != null){
                                        if(backpackInvent.getStackInSlot(i).getItem() instanceof ItemBackpack){
                                            numBackpack++;
                                        }
                                        SlotMax++;
                                    }
                                    return SlotMax;
                                }
                                return numBackpack;
                            }

                            private int getNumberOfGuns(IItemHandler backpackInvent) {
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
                            private int getNumberOfAmmunition(IItemHandler backpackInvent) {
                                int numAmmo = 0;
                                for(int i=0; i < backpackInvent.getSlots(); i++){
                                    if(backpackInvent.getStackInSlot(i) != null){
                                        if(backpackInvent.getStackInSlot(i).getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.foodwar") || backpackInvent.getStackInSlot(i).getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.kimetsu") || backpackInvent.getStackInSlot(i).getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.hxh") || backpackInvent.getStackInSlot(i).getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case.baki") || backpackInvent.getStackInSlot(i).getItem().getRegistryName().toString().equals("modularwarfare:arkaniaz.ammunition_case")){
                                            numAmmo++;
                                        }
                                    }
                                }
                                return numAmmo;
                            }
                        });
                xP++;

                if ((xP % 4) == 0) {
                    xP = 0;
                    yP++;
                }
            }
        }
    }


    @Override
    public void onCraftMatrixChanged(final IInventory par1IInventory) {
        this.slotChangedCraftingGrid(this.thePlayer.getEntityWorld(), this.thePlayer, this.craftMatrix, this.craftResult);
    }

    @Override
    public void onContainerClosed(final EntityPlayer player) {
        super.onContainerClosed(player);
        this.craftResult.clear();

        if (!player.world.isRemote) {
            this.clearContainer(player, player.world, this.craftMatrix);
        }
        ModularWarfare.NETWORK.sendToServer(new PacketBackpackEquip());
        ModularWarfare.NETWORK.sendToServer(new PacketVestEquip());
    }

    @Override
    public boolean canInteractWith(final EntityPlayer par1EntityPlayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);

        return itemstack;
    }

    @Override
    public boolean canMergeSlot(final ItemStack stack, final Slot slot) {
        return (slot.inventory != this.craftResult) && super.canMergeSlot(stack, slot);
    }
}