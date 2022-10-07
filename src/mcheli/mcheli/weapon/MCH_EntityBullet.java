package mcheli.weapon;

import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;



public class MCH_EntityBullet
  extends MCH_EntityBaseBullet
{
  public MCH_EntityBullet(World par1World)
  {
    super(par1World);
  }
  



  public MCH_EntityBullet(World par1World, double pX, double pY, double pZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration)
  {
    super(par1World, pX, pY, pZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
  }
  

  public void onUpdate()
  {
    super.onUpdate();
    
    if ((!this.isDead) && (!this.worldObj.isRemote) && (getCountOnUpdate() > 1) && (getInfo() != null) && (this.explosionPower > 0))
    {
      float pDist = getInfo().proximityFuseDist;
      if (pDist > 0.1D)
      {
        pDist += 1.0F;
        
        float rng = pDist + MathHelper.abs(getInfo().acceleration);
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(rng, rng, rng));
        

        for (int i = 0; i < list.size(); i++)
        {
          Entity entity1 = (Entity)list.get(i);
          

          if ((canBeCollidedEntity(entity1)) && (entity1.getDistanceSqToEntity(this) < pDist * pDist))
          {

            MCH_Lib.DbgLog(this.worldObj, "MCH_EntityBullet.onUpdate:proximityFuse:" + entity1, new Object[0]);
            
            this.posX = ((entity1.posX + this.posX) / 2.0D);
            this.posY = ((entity1.posY + this.posY) / 2.0D);
            this.posZ = ((entity1.posZ + this.posZ) / 2.0D);
            
            MovingObjectPosition mop = W_MovingObjectPosition.newMOP((int)this.posX, (int)this.posY, (int)this.posZ, 0, W_WorldFunc.getWorldVec3EntityPos(this), false);
            




            onImpact(mop, 1.0F);
            break;
          }
        }
      }
    }
  }
  


  protected void onUpdateCollided()
  {
    double mx = this.motionX * this.accelerationFactor;
    double my = this.motionY * this.accelerationFactor;
    double mz = this.motionZ * this.accelerationFactor;
    
    float damageFactor = 1.0F;
    

    MovingObjectPosition m = null;
    for (int i = 0; i < 5; i++)
    {
      Vec3 vec3 = W_WorldFunc.getWorldVec3(this.worldObj, this.posX, this.posY, this.posZ);
      Vec3 vec31 = W_WorldFunc.getWorldVec3(this.worldObj, this.posX + mx, this.posY + my, this.posZ + mz);
      m = W_WorldFunc.clip(this.worldObj, vec3, vec31);
      
      boolean continueClip = false;
      
      if ((this.shootingEntity != null) && (W_MovingObjectPosition.isHitTypeTile(m)))
      {
        Block block = W_WorldFunc.getBlock(this.worldObj, m.blockX, m.blockY, m.blockZ);
        if (MCH_Config.bulletBreakableBlocks.contains(block))
        {
          W_WorldFunc.destroyBlock(this.worldObj, m.blockX, m.blockY, m.blockZ, true);
          
          continueClip = true;
        }
      }
      
      if (!continueClip) {
        break;
      }
    }
    

    Vec3 vec3 = W_WorldFunc.getWorldVec3(this.worldObj, this.posX, this.posY, this.posZ);
    Vec3 vec31 = W_WorldFunc.getWorldVec3(this.worldObj, this.posX + mx, this.posY + my, this.posZ + mz);
    
    if (getInfo().delayFuse > 0)
    {
      if (m != null)
      {
        boundBullet(m.sideHit);
        if (this.delayFuse == 0)
        {
          this.delayFuse = getInfo().delayFuse;
        }
      }
      return;
    }
    
    if (m != null)
    {
      vec31 = W_WorldFunc.getWorldVec3(this.worldObj, m.hitVec.xCoord, m.hitVec.yCoord, m.hitVec.zCoord);
    }
    
    Entity entity = null;
    List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(mx, my, mz).expand(21.0D, 21.0D, 21.0D));
    double d0 = 0.0D;
    
    for (int j = 0; j < list.size(); j++)
    {
      Entity entity1 = (Entity)list.get(j);
      

      if (canBeCollidedEntity(entity1))
      {
        float f = 0.3F;
        AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
        MovingObjectPosition m1 = axisalignedbb.calculateIntercept(vec3, vec31);
        
        if (m1 != null)
        {
          double d1 = vec3.distanceTo(m1.hitVec);
          
          if ((d1 < d0) || (d0 == 0.0D))
          {
            entity = entity1;
            d0 = d1;
          }
        }
      }
    }
    























    if (entity != null)
    {
      m = new MovingObjectPosition(entity);
    }
    
    if (m != null)
    {
      onImpact(m, damageFactor);
    }
  }
  

  public MCH_BulletModel getDefaultBulletModel()
  {
    return MCH_DefaultBulletModels.Bullet;
  }
}
