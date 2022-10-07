package mcheli.weapon;

import java.util.List;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.uav.MCH_EntityUavStation;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class MCH_WeaponGuidanceSystem
{
  protected World worldObj;
  public Entity lastLockEntity;
  private Entity targetEntity;
  private int lockCount;
  private int lockSoundCount;
  private int continueLockCount;
  private int lockCountMax;
  private int prevLockCount;
  public boolean canLockInWater;
  public boolean canLockOnGround;
  public boolean canLockInAir;
  public boolean ridableOnly;
  public double lockRange;
  public int lockAngle;
  public MCH_IEntityLockChecker checker;
  
  public MCH_WeaponGuidanceSystem()
  {
    this(null);
  }
  
  public MCH_WeaponGuidanceSystem(World w)
  {
    this.worldObj = w;
    this.targetEntity = null;
    this.lastLockEntity = null;
    this.lockCount = 0;
    this.continueLockCount = 0;
    this.lockCountMax = 1;
    this.prevLockCount = 0;
    this.canLockInWater = false;
    this.canLockOnGround = false;
    this.canLockInAir = false;
    this.ridableOnly = false;
    this.lockRange = 50.0D;
    this.lockAngle = 10;
    this.checker = null;
  }
  
  public void setWorld(World w)
  {
    this.worldObj = w;
  }
  
  public void setLockCountMax(int i)
  {
    this.lockCountMax = (i > 0 ? i : 1);
  }
  
  public int getLockCountMax() {
    float stealth = getEntityStealth(this.targetEntity);
    
    return (int)(this.lockCountMax + this.lockCountMax * stealth);
  }
  
  public int getLockCount() {
    return this.lockCount;
  }
  

  public boolean isLockingEntity(Entity entity)
  {
    return (getLockCount() > 0) && (this.targetEntity != null) && (!this.targetEntity.isDead) && (W_Entity.isEqual(entity, this.targetEntity));
  }
  


  public Entity getLockingEntity()
  {
    return (getLockCount() > 0) && (this.targetEntity != null) && (!this.targetEntity.isDead) ? this.targetEntity : null;
  }
  
  public Entity getTargetEntity()
  {
    return this.targetEntity;
  }
  
  public boolean isLockComplete()
  {
    return (getLockCount() == getLockCountMax()) && (this.lastLockEntity != null);
  }
  
  public void update()
  {
    if ((this.worldObj != null) && (this.worldObj.isRemote))
    {
      if (this.lockCount != this.prevLockCount)
      {
        this.prevLockCount = this.lockCount;
      }
      else
      {
        this.lockCount = (this.prevLockCount = 0);
      }
    }
  }
  
  public static boolean isEntityOnGround(Entity entity)
  {
    if ((entity != null) && (!entity.isDead))
    {
      if (entity.onGround) return true;
      for (int i = 0; i < 12; i++)
      {
        int x = (int)(entity.posX + 0.5D);
        int y = (int)(entity.posY + 0.5D) - i;
        int z = (int)(entity.posZ + 0.5D);
        int blockId = W_WorldFunc.getBlockId(entity.worldObj, x, y, z);
        if ((blockId != 0) && (!W_WorldFunc.isBlockWater(entity.worldObj, x, y, z))) return true;
      }
    }
    return false;
  }
  

  public boolean lock(Entity user)
  {
    return lock(user, true);
  }
  
  public boolean lock(Entity user, boolean isLockContinue) {
    if (!this.worldObj.isRemote) { return false;
    }
    boolean result = false;
    if (this.lockCount == 0)
    {
      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(user, user.boundingBox.expand(this.lockRange, this.lockRange, this.lockRange));
      


      Entity tgtEnt = null;
      double dist = this.lockRange * this.lockRange * 2.0D;
      for (int i = 0; i < list.size(); i++)
      {
        Entity entity = (Entity)list.get(i);
        
        if (canLockEntity(entity))
        {
          double dx = entity.posX - user.posX;
          double dy = entity.posY - user.posY;
          double dz = entity.posZ - user.posZ;
          double d = dx * dx + dy * dy + dz * dz;
          
          Entity entityLocker = getLockEntity(user);
          
          float stealth = 1.0F - getEntityStealth(entity);
          double range = this.lockRange * stealth;
          float angle = this.lockAngle * (stealth / 2.0F + 0.5F);
          
          if ((d < range * range) && (d < dist) && (inLockRange(entityLocker, user.rotationYaw, user.rotationPitch, entity, angle)))
          {

            Vec3 v1 = W_WorldFunc.getWorldVec3(this.worldObj, entityLocker.posX, entityLocker.posY, entityLocker.posZ);
            Vec3 v2 = W_WorldFunc.getWorldVec3(this.worldObj, entity.posX, entity.posY + entity.height / 2.0F, entity.posZ);
            net.minecraft.util.MovingObjectPosition m = W_WorldFunc.clip(this.worldObj, v1, v2, false, true, false);
            if ((m == null) || (W_MovingObjectPosition.isHitTypeEntity(m)))
            {
              d = dist;
              tgtEnt = entity;
            }
          }
        }
      }
      this.targetEntity = tgtEnt;
      if (tgtEnt != null)
      {
        this.lockCount += 1;

      }
      

    }
    else if ((this.targetEntity != null) && (!this.targetEntity.isDead))
    {
      boolean canLock = true;
      if ((!this.canLockInWater) && (this.targetEntity.isInWater())) canLock = false;
      boolean ong = isEntityOnGround(this.targetEntity);
      if ((!this.canLockOnGround) && (ong)) canLock = false;
      if ((!this.canLockInAir) && (!ong)) { canLock = false;
      }
      if (canLock)
      {
        double dx = this.targetEntity.posX - user.posX;
        double dy = this.targetEntity.posY - user.posY;
        double dz = this.targetEntity.posZ - user.posZ;
        
        float stealth = 1.0F - getEntityStealth(this.targetEntity);
        double range = this.lockRange * stealth;
        

        if (dx * dx + dy * dy + dz * dz < range * range)
        {
          if ((this.worldObj.isRemote) && (this.lockSoundCount == 1))
          {
            mcheli.MCH_PacketNotifyLock.send(getTargetEntity());
          }
          this.lockSoundCount = ((this.lockSoundCount + 1) % 15);
          

          Entity entityLocker = getLockEntity(user);
          if (inLockRange(entityLocker, user.rotationYaw, user.rotationPitch, this.targetEntity, this.lockAngle))
          {
            if (this.lockCount < getLockCountMax())
            {
              this.lockCount += 1;
            }
            
          }
          else if (this.continueLockCount > 0)
          {
            this.continueLockCount -= 1;
            if ((this.continueLockCount <= 0) && (this.lockCount > 0))
            {
              this.lockCount -= 1;
            }
          }
          else
          {
            this.continueLockCount = 0;
            this.lockCount -= 1;
          }
          
          if (this.lockCount >= getLockCountMax())
          {
            if (this.continueLockCount <= 0)
            {
              this.continueLockCount = (getLockCountMax() / 3);
              if (this.continueLockCount > 20) { this.continueLockCount = 20;
              }
            }
            result = true;
            this.lastLockEntity = this.targetEntity;
            if (isLockContinue)
            {

              this.prevLockCount = (this.lockCount - 1);
            }
            else
            {
              clearLock();
            }
            
          }
        }
        else
        {
          clearLock();
        }
        
      }
      else
      {
        clearLock();
      }
      
    }
    else
    {
      clearLock();
    }
    

    return result;
  }
  
  public static float getEntityStealth(Entity entity)
  {
    if ((entity instanceof MCH_EntityAircraft))
    {
      return ((MCH_EntityAircraft)entity).getStealth();
    }
    if ((entity != null) && ((entity.ridingEntity instanceof MCH_EntityAircraft)))
    {
      return ((MCH_EntityAircraft)entity.ridingEntity).getStealth();
    }
    
    return 0.0F;
  }
  
  public void clearLock()
  {
    this.targetEntity = null;
    this.lockCount = 0;
    this.continueLockCount = 0;
    this.lockSoundCount = 0;
  }
  
  public Entity getLockEntity(Entity entity)
  {
    if ((entity.ridingEntity instanceof MCH_EntityUavStation))
    {
      MCH_EntityUavStation us = (MCH_EntityUavStation)entity.ridingEntity;
      if (us.getControlAircract() != null)
      {
        return us.getControlAircract();
      }
    }
    return entity;
  }
  
  public boolean canLockEntity(Entity entity)
  {
    if ((this.ridableOnly) && ((entity instanceof EntityPlayer)))
    {
      if (entity.ridingEntity == null) { return false;
      }
    }
    




    String className = entity.getClass().getName();
    

    if (className.indexOf("EntityCamera") >= 0) { return false;
    }
    
    if ((!mcheli.wrapper.W_Lib.isEntityLivingBase(entity)) && (!(entity instanceof MCH_EntityAircraft)))
    {


      return false;
    }
    
    if ((!this.canLockInWater) && (entity.isInWater())) return false;
    if ((this.checker != null) && (!this.checker.canLockEntity(entity))) return false;
    boolean ong = isEntityOnGround(entity);
    if ((!this.canLockOnGround) && (ong)) return false;
    if ((!this.canLockInAir) && (!ong)) return false;
    return true;
  }
  



  public static boolean inLockRange(Entity entity, float rotationYaw, float rotationPitch, Entity target, float lockAng)
  {
    double dx = target.posX - entity.posX;
    double dy = target.posY + target.height / 2.0F - entity.posY;
    double dz = target.posZ - entity.posZ;
    
    float entityYaw = (float)MCH_Lib.getRotate360(rotationYaw);
    float targetYaw = (float)MCH_Lib.getRotate360(Math.atan2(dz, dx) * 180.0D / 3.141592653589793D);
    float diffYaw = (float)MCH_Lib.getRotate360(targetYaw - entityYaw - 90.0F);
    
    double dxz = Math.sqrt(dx * dx + dz * dz);
    float targetPitch = -(float)(Math.atan2(dy, dxz) * 180.0D / 3.141592653589793D);
    float diffPitch = targetPitch - rotationPitch;
    
    return ((diffYaw < lockAng) || (diffYaw > 360.0F - lockAng)) && (Math.abs(diffPitch) < lockAng);
  }
}
