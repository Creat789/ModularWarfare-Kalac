package com.modularwarfare.common.network;

import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class PacketBackpackEquip extends PacketBase {

	public PacketBackpackEquip() {}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
	}

	@Override
	public void handleServerSide(EntityPlayerMP entityPlayer) {
		final IExtraItemHandler extraSlots = entityPlayer.getCapability(CapabilityExtra.CAPABILITY, null);
		final ItemStack itemstackBackpack = extraSlots.getStackInSlot(0);
		if (!itemstackBackpack.isEmpty()) {
			int railing = (int)(Math.random() * (10-1)) + 1;
			if(railing < 3)
			{
				itemstackBackpack.damageItem(1, entityPlayer);
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer entityPlayer) {}

}
