package com.modularwarfare.common.entity.grenades;

import com.modularwarfare.ModularWarfare;
import com.modularwarfare.common.grenades.GrenadeType;
import com.modularwarfare.common.init.ModSounds;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class EntityC4 extends EntityGrenade {

    private static final DataParameter GRENADE_NAME = EntityDataManager.createKey(EntityC4.class, DataSerializers.STRING);
    public EntityLivingBase shootingEntity;

    public float smokeTime = 12 * 20;

    public EntityC4(World worldIn) {
        super(worldIn);
    }
    public boolean stuck = false;
    public int stuckToX, stuckToY, stuckToZ;

    public EntityC4(World world, EntityLivingBase thrower, boolean isRightClick, GrenadeType grenadeType) {
        super(world, thrower, isRightClick, grenadeType);
        this.smokeTime = grenadeType.smokeTime * 20;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.35f, 0.35f);
        this.setEntityInvulnerable(false);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    public void onUpdate() {
        Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        if (!this.hasNoGravity()) {
            this.motionY -= 0.04D;
        }
        if(stuck)
            this.motionY = 0;
        --this.fuse;
        if (this.fuse <= 0) {
            explode();
        } else {
            this.handleWaterMovement();
            if (!this.isInWater()) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.2D, this.posZ, 0.0D, 0.0D, 0.0D);
            } else {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, this.posY + 0.2D, this.posZ, 0.0D, 0.1D, 0.0D);
            }
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (raytraceresult != null){

            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            stuck = true;
            stuckToX = raytraceresult.getBlockPos().getX();
            stuckToY = raytraceresult.getBlockPos().getY();
            stuckToZ = raytraceresult.getBlockPos().getZ();
        }
    }

    @Override
    public void explode(){
        float f = 4.0F;
        this.world.createExplosion(this, this.posX, this.posY + (double)(this.height / 16.0F), this.posZ, 4.0F, true);
        this.setDead();
    }

    public String getGrenadeName() {
        return (String) this.dataManager.get(GRENADE_NAME);
    }

    public void setGrenadeName(String grenadeName) {
        this.dataManager.set(GRENADE_NAME, grenadeName);
    }


    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(GRENADE_NAME, "");
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("posX", this.posX);
        compound.setDouble("posY", this.posY);
        compound.setDouble("posZ", this.posZ);
        compound.setDouble("motionX", this.motionX);
        compound.setDouble("motionY", this.motionY);
        compound.setDouble("motionZ", this.motionZ);
        compound.setFloat("fuse", this.fuse);
        compound.setFloat("smokeTime", this.smokeTime);
    }
    @Nullable
    public EntityLivingBase getTntPlacedBy()
    {
        return this.shootingEntity;
    }
    @Nullable
    public EntityLivingBase getExplosivePlacedBy()
    {
        if (this.shootingEntity == null)
        {
            return null;
        }
        else
        {
            return this.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)this.shootingEntity : null;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        posX = compound.getDouble("posX");
        posY = compound.getDouble("posY");
        posZ = compound.getDouble("posZ");
        motionX = compound.getDouble("motionX");
        motionY = compound.getDouble("motionY");
        motionZ = compound.getDouble("motionZ");
        fuse = compound.getFloat("fuse");
        smokeTime = compound.getFloat("smokeTime");
    }
}