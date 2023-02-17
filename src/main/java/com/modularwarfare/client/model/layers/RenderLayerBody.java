package com.modularwarfare.client.model.layers;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.client.ClientRenderHooks;
import com.modularwarfare.client.model.ModelCustomArmor;
import com.modularwarfare.common.armor.ArmorType;
import com.modularwarfare.common.armor.ItemSpecialArmor;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.ExtraContainer;
import com.modularwarfare.common.capability.extraslots.ExtraContainerProvider;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.network.BackWeaponsManager;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.type.BaseType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLayerBody implements LayerRenderer<EntityPlayer>
{
    private final ModelRenderer modelRenderer;
    private RenderPlayer renderer;
    public IExtraItemHandler extra;

    public RenderLayerBody(final RenderPlayer renderer, final ModelRenderer modelRenderer) {
        this.modelRenderer = modelRenderer;
        this.renderer = renderer;
    }

    public void doRenderLayer(final EntityPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final int[] slots = new int[] {1, 2, 3, 4};

        if (player.hasCapability(CapabilityExtra.CAPABILITY, null)) {
            final IExtraItemHandler extraSlots = player.getCapability(CapabilityExtra.CAPABILITY, null);
            for (final int slot : slots) {
                final ItemStack itemStackSpecialArmor = extraSlots.getStackInSlot(slot);
                if (!itemStackSpecialArmor.isEmpty() && itemStackSpecialArmor.getItem() instanceof ItemSpecialArmor) {
                    this.renderBody(player, slot, ((ItemSpecialArmor) itemStackSpecialArmor.getItem()).type, scale);
                }
            }
        }
        if (player instanceof AbstractClientPlayer) {
            ItemStack gun = BackWeaponsManager.INSTANCE
                    .getItemToRender((AbstractClientPlayer) player);
            if (gun != ItemStack.EMPTY && !gun.isEmpty()) {
                BaseType type = ((BaseItem) gun.getItem()).baseType;
                {
                    GlStateManager.pushMatrix();
                    if (ClientRenderHooks.customRenderers[type.id] != null) {
                        GlStateManager.translate(0, -0.6, 0.35);
                        boolean isSneaking = player.isSneaking();
                        if (player instanceof EntityPlayerSP) {
                            ((EntityPlayerSP) player).movementInput.sneak = false;
                        } else {
                            player.setSneaking(false);
                        }
                        if (player instanceof EntityPlayerSP) {
                            ((EntityPlayerSP) player).movementInput.sneak = isSneaking;
                        } else {
                            player.setSneaking(isSneaking);
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }

        }
    }

    public void renderBody(final EntityPlayer player, final int slot, final ArmorType armorType, final float scale) {

        final IExtraItemHandler extraSlots = player.getCapability(CapabilityExtra.CAPABILITY, null);
        this.extra = player.getCapability(CapabilityExtra.CAPABILITY, null);


        if (armorType.hasModel()) {
            final ModelCustomArmor armorModel = (ModelCustomArmor)armorType.bipedModel;
            GlStateManager.pushMatrix();

            if (player.isSneaking()) {
                if (slot == 1) {
                    GlStateManager.translate(0.0f, 0.2f, 0.0f);
                    GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);

                } else {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                }
            }

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableRescaleNormal();
            final int skinId = 0;
            String path = skinId > 0 ? "skins/" + armorType.modelSkins[skinId].getSkin() : armorType.modelSkins[0].getSkin();
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(ModularWarfare.MOD_ID, "skins/armor/" + path + ".png"));
            GL11.glScalef(1.0f, 1.0f, 1.0f);
            GlStateManager.shadeModel(GL11.GL_SMOOTH);

            armorModel.renderCustom("armorModel", this.renderer.getMainModel().bipedBody,0.0625f,1f);
            armorModel.renderCustom("leftArmModel", this.renderer.getMainModel().bipedLeftArm,0.0625f,1f);
            armorModel.renderCustom("rightArmModel", this.renderer.getMainModel().bipedRightArm,0.0625f,1f);

            armorModel.renderCustom("rightLegModel", this.renderer.getMainModel().bipedRightLeg,0.0625f,1f);
            armorModel.renderCustom("leftLegModel", this.renderer.getMainModel().bipedLeftLeg,0.0625f,1f);
            armorModel.renderCustom("headModel", this.renderer.getMainModel().bipedHead,0.0625f,1f);
            GlStateManager.shadeModel(GL11.GL_FLAT);

            GlStateManager.popMatrix();
        }
    }


    public boolean shouldCombineTextures() {
        return true;
    }
}
