package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.attachment.AttachmentType;
import com.modularwarfare.common.attachment.ItemAttachment;
import com.modularwarfare.common.cosmetics.ItemOverlay;
import com.modularwarfare.common.cosmetics.ItemSpray;
import com.modularwarfare.common.guns.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketGunAddAttachment extends PacketBase {

    public int slot;

    public PacketGunAddAttachment() {
    }

    public PacketGunAddAttachment(int slot) {
        this.slot = slot;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(this.slot);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.slot = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP entityPlayer) {
        if (entityPlayer.getHeldItemMainhand() != null) {
            if (entityPlayer.getHeldItemMainhand().getItem() instanceof ItemGun) {
                ItemStack gunStack = entityPlayer.getHeldItemMainhand();
                ItemGun itemGun = (ItemGun) entityPlayer.getHeldItemMainhand().getItem();
                GunType gunType = itemGun.type;
                InventoryPlayer inventory = entityPlayer.inventory;

                if (inventory.getStackInSlot(slot) != ItemStack.EMPTY) {
                    ItemStack attachStack = inventory.getStackInSlot(slot);
                    if (attachStack.getItem() instanceof ItemAttachment) {
                        ItemAttachment itemAttachment = (ItemAttachment) attachStack.getItem();
                        AttachmentType attachType = itemAttachment.type;
                        if (gunType.acceptedAttachments.get(attachType.attachmentType) != null && gunType.acceptedAttachments.get(attachType.attachmentType).size() >= 1) {
                            if (gunType.acceptedAttachments.containsKey(attachType.attachmentType)) {
                                if (gunType.acceptedAttachments.get(attachType.attachmentType) != null) {
                                    if (gunType.acceptedAttachments.get(attachType.attachmentType).size() >= 1) {
                                        if (gunType.acceptedAttachments.get(attachType.attachmentType).contains(attachType.internalName)) {
                                            ItemStack itemStack = GunType.getAttachment(gunStack, attachType.attachmentType);
                                            if (itemStack != null && itemStack.getItem() != Items.AIR) {
                                                ItemAttachment localItemAttachment = (ItemAttachment) itemStack.getItem();
                                                AttachmentType localAttachType = localItemAttachment.type;
                                                GunType.removeAttachment(gunStack, localAttachType.attachmentType);
                                                inventory.addItemStackToInventory(itemStack);
                                            }
                                        }
                                    }
                                    ItemStack attachmentStack = new ItemStack(itemAttachment);
                                    NBTTagCompound tag = new NBTTagCompound();
                                    tag.setInteger("skinId", 0);
                                    attachmentStack.setTagCompound(tag);
                                    GunType.addAttachment(gunStack, attachType.attachmentType, attachmentStack);
                                    inventory.getStackInSlot(this.slot).shrink(1);
                                    ModularWarfare.NETWORK.sendTo(new PacketPlaySound(entityPlayer.getPosition(), "attachment.apply", 1f, 1f), entityPlayer);
                                }
                            }
                        }
                    }
                    if (attachStack.getItem() instanceof ItemSpray) {
                        ItemSpray spray = (ItemSpray) attachStack.getItem();
                        if (gunStack.getTagCompound() != null) {
                            for (int i = 0; i < gunType.modelSkins.length; i++) {
                                if (gunType.modelSkins[i].internalName.equalsIgnoreCase(spray.type.skinName)) {
                                    NBTTagCompound nbtTagCompound = gunStack.getTagCompound();
                                    nbtTagCompound.setInteger("skinId", i);
                                    gunStack.setTagCompound(nbtTagCompound);
                                    inventory.getStackInSlot(slot).damageItem(1, entityPlayer);
                                    if(inventory.getStackInSlot(slot).getMaxDamage() !=0 && inventory.getStackInSlot(slot).getItemDamage() == inventory.getStackInSlot(slot).getMaxDamage()) {
                                        inventory.removeStackFromSlot(slot);
                                    }
                                    ModularWarfare.NETWORK.sendTo(new PacketPlaySound(entityPlayer.getPosition(), "spray", 1f, 1f), entityPlayer);
                                }
                            }
                        }
                    }
                    if(attachStack.getItem() instanceof ItemOverlay){
                        if (gunStack.getTagCompound() != null) {
                            ItemOverlay itemOverlay = (ItemOverlay)attachStack.getItem();
                            attachStack.damageItem(1, entityPlayer);
                            if(attachStack.getMaxDamage() !=0 && attachStack.getItemDamage() == attachStack.getMaxDamage()) {
                                inventory.removeStackFromSlot(slot);
                            }
                            GunType.setOverlay(gunStack, itemOverlay.type);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void handleClientSide(EntityPlayer entityPlayer) {
        // UNUSED
    }

}