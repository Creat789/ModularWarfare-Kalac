package com.modularwarfare.common.network;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientProxy;
import com.modularwarfare.client.hud.GasSystem;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketGasClient extends PacketBase {

    private int flashAmount;

    public PacketGasClient() {
    }

    public PacketGasClient(int givenFlashAmount) {

        this.flashAmount = givenFlashAmount;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        data.writeInt(flashAmount);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
        this.flashAmount = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP entityPlayer) {
    }

    @Override
    public void handleClientSide(EntityPlayer entityPlayer) {

        GasSystem.hasTookScreenshot = false;
        GasSystem.flashValue += this.flashAmount;

        ((ClientProxy)(ModularWarfare.PROXY)).playFlashSound(entityPlayer);

        if (GasSystem.flashValue > 30) {
            GasSystem.flashValue = 30;
        }
    }
}