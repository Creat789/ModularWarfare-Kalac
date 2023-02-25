package com.modularwarfare.common.handler;

import com.modularwarfare.ModConfig;
import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.capability.extraslots.CapabilityExtra;
import com.modularwarfare.common.capability.extraslots.IExtraItemHandler;
import com.modularwarfare.common.entity.item.EntityItemLoot;
import com.modularwarfare.common.guns.ItemGun;
import com.modularwarfare.common.network.PacketClientKillFeedEntry;
import com.modularwarfare.common.network.PacketExplosion;
import com.modularwarfare.common.type.BaseItem;
import com.modularwarfare.common.world.ModularWarfareWorldListener;
import com.modularwarfare.melee.common.melee.ItemMelee;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CommonEventHandler {

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void dropItemsAt(final EntityPlayer player, final List<EntityItem> drops, final Entity e) {
        final IExtraItemHandler armors = player.getCapability(CapabilityExtra.CAPABILITY, null);
        for (int i = 0; i < armors.getSlots(); ++i) {
            if (armors.getStackInSlot(i) != null && !armors.getStackInSlot(i).isEmpty()) {
                final EntityItem ei = new EntityItem(e.world, e.posX, e.posY + e.getEyeHeight(), e.posZ, armors.getStackInSlot(i).copy());
                ei.setPickupDelay(40);
                final float f1 = e.world.rand.nextFloat() * 0.5f;
                final float f2 = e.world.rand.nextFloat() * 3.1415927f * 2.0f;
                ei.motionX = -MathHelper.sin(f2) * f1;
                ei.motionZ = MathHelper.cos(f2) * f1;
                ei.motionY = 0.20000000298023224;
                drops.add(ei);
                armors.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }


    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source.getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source.getTrueSource();
            Item main = player != null ? player.getHeldItemMainhand() != null ? player.getHeldItemMainhand().getItem() : null : null;
            if (main instanceof ItemMelee) {
                return;
            }
        }
    }
    @SubscribeEvent
    public void playerDeath(final PlayerDropsEvent event) {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote && !event.getEntity().world.getGameRules().getBoolean("keepInventory")) {
            this.dropItemsAt(event.getEntityPlayer(), event.getDrops(), event.getEntityPlayer());
        }
    }
    @SubscribeEvent
    public void onLivingDeath(final LivingDeathEvent event) {
        if (ModConfig.INSTANCE.killFeed.enableKillFeed) {
            final Entity entity = event.getEntity();
            if (entity instanceof EntityPlayer) {
                if (!entity.world.isRemote) {
                    if (event.getSource().isProjectile()) {
                        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                            ItemStack heldStack = ((EntityPlayer) event.getSource().getTrueSource()).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                            if (heldStack != null) {
                                if (heldStack.getItem() instanceof ItemGun) {
                                    final String text = getRandomMessage((event.getSource().getTrueSource()).getDisplayName().getFormattedText(), (event.getEntity()).getDisplayName().getFormattedText());
                                    ModularWarfare.NETWORK.sendToAll(new PacketClientKillFeedEntry(text, ModConfig.INSTANCE.killFeed.messageDuration, ((ItemGun) heldStack.getItem()).type.internalName));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getRandomMessage(String killer, String victim) {
        if (ModConfig.INSTANCE.killFeed.messageList != null && ModConfig.INSTANCE.killFeed.messageList.size() > 0) {
            int r = getRandomNumberInRange(0, ModConfig.INSTANCE.killFeed.messageList.size() - 1);
            String choosen = ModConfig.INSTANCE.killFeed.messageList.get(r);
            choosen = choosen.replace("{killer}", killer).replace("{victim}", victim);
            choosen = choosen.replace("&", "§");
            return choosen;
        }
        return "";
    }

    @SubscribeEvent
    public void onLivingAttack(final LivingAttackEvent event) {
        if (!event.getEntityLiving().world.isRemote)
            return;
        final Entity entity = event.getEntity();
        if (entity.getEntityWorld().isRemote) {
            ModularWarfare.PROXY.addBlood(event.getEntityLiving(), 10, true);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof EntityItemLoot) {
            return;
        }
    }

    private static final ModularWarfareWorldListener WORLD_LISTENER = new ModularWarfareWorldListener();

    @SubscribeEvent
    public void onInitWorld(WorldEvent.Load event) {
        World world = event.getWorld();
        world.addEventListener(WORLD_LISTENER);
    }

    @SubscribeEvent
    public void onUnloadWorld(WorldEvent.Unload event) {
        World world = event.getWorld();
        world.removeEventListener(WORLD_LISTENER);
    }

    @SubscribeEvent
    public void onEntityInteractBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (ModConfig.INSTANCE.guns.guns_interaction_hand) {
            if (event.getWorld().isRemote) {
                if (Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null) {
                    if (Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemGun) {
                        if (!(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockContainer)) {
                            event.setUseBlock(Event.Result.DENY);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void explosionEvent(ExplosionEvent e) {
        Vec3d pos = e.getExplosion().getPosition();
        ModularWarfare.NETWORK.sendToAll(new PacketExplosion(pos.x, pos.y, pos.z));
    }


    @SubscribeEvent
    public void onEntityInteract(final PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof EntityItemLoot) {
            if (!event.getWorld().isRemote && event.getTarget().onGround && !event.getEntityPlayer().isSpectator()) {
                final EntityItemLoot loot = (EntityItemLoot) event.getTarget();
                if (loot.getCustomAge() > 20) {
                    final ItemStack stack = loot.getItem();
                    if (stack.getItem() != Items.AIR && event.getTarget().onGround) {
                        loot.pickup(event.getEntityPlayer());
                    }
                }
            }
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public void onEntityJoin(final EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        if (ModConfig.INSTANCE.drops.advanced_drops_models) {
            if (event.getEntity().getClass() == EntityItem.class) {
                final EntityItem item = (EntityItem) event.getEntity();
                if (!item.getItem().isEmpty()) {
                    if (item.getItem().getItem() instanceof BaseItem || ModConfig.INSTANCE.drops.advanced_drops_models_everything) {
                        final EntityItemLoot loot = new EntityItemLoot((EntityItem) event.getEntity());
                        event.getEntity().setDead();
                        loot.setInfinitePickupDelay();
                        event.setResult(Event.Result.DENY);
                        event.setCanceled(true);
                        event.getWorld().spawnEntity(loot);
                        return;
                    }
                }
            }
        }
    }

}
