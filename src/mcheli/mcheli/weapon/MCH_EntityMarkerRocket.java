package mcheli.weapon;

import java.util.Random;
import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.DataWatcher;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;


public class MCH_EntityMarkerRocket
  extends MCH_EntityBaseBullet
{
  public int countDown;
  
  public MCH_EntityMarkerRocket(World par1World)
  {
    super(par1World);
    setMarkerStatus(0);
    this.countDown = 0;
  }
  


  public MCH_EntityMarkerRocket(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration)
  {
    super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    setMarkerStatus(0);
    this.countDown = 0;
  }
  
  protected void entityInit()
  {
    super.entityInit();
    getDataWatcher().addObject(28, Byte.valueOf((byte)0));
  }
  
  public void setMarkerStatus(int n) {
    if (!this.worldObj.isRemote)
    {
      getDataWatcher().updateObject(28, Byte.valueOf((byte)n));
    }
  }
  
  public int getMarkerStatus() {
    return getDataWatcher().getWatchableObjectByte(28);
  }
  
  public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
  {
    return false;
  }
  

  public void onUpdate()
  {
    int status = getMarkerStatus();
    
    if (this.worldObj.isRemote)
    {
      if (getInfo() == null)
      {
        super.onUpdate();
      }
      if ((getInfo() != null) && (!getInfo().disableSmoke))
      {
        if (status != 0)
        {

          if (status == 1)
          {
            super.onUpdate();
            spawnParticle(getInfo().trajectoryParticleName, 3, 5.0F * getInfo().smokeSize * 0.5F);
          }
          else
          {
            float gb = this.rand.nextFloat() * 0.3F;
            spawnParticle("explode", 5, 10 + this.rand.nextInt(4), this.rand.nextFloat() * 0.2F + 0.8F, gb, gb, (this.rand.nextFloat() - 0.5F) * 0.7F, 0.3F + this.rand.nextFloat() * 0.3F, (this.rand.nextFloat() - 0.5F) * 0.7F);

          }
          
        }
        
      }
      

    }
    else if ((status == 0) || (isInWater()))
    {
      setDead();
    }
    else if (status == 1)
    {
      super.onUpdate();


    }
    else if (this.countDown > 0)
    {
      this.countDown -= 1;
      if (this.countDown == 40)
      {
        int num = 6 + this.rand.nextInt(2);
        for (int i = 0; i < num; i++)
        {
          MCH_EntityBomb e = new MCH_EntityBomb(this.worldObj, this.posX + (this.rand.nextFloat() - 0.5F) * 15.0F, 260.0F + this.rand.nextFloat() * 10.0F + i * 30, this.posZ + (this.rand.nextFloat() - 0.5F) * 15.0F, 0.0D, -0.5D, 0.0D, 0.0F, 90.0F, 4.0D);
          





          e.setName(getName());
          
          e.explosionPower = (3 + this.rand.nextInt(2));
          e.explosionPowerInWater = 0;
          e.setPower(30);
          e.piercing = 0;
          e.shootingAircraft = this.shootingAircraft;
          e.shootingEntity = this.shootingEntity;
          
          this.worldObj.spawnEntityInWorld(e);
        }
      }
    }
    else
    {
      setDead();
    }
  }
  


  public void spawnParticle(String name, int num, float size, float r, float g, float b, float mx, float my, float mz)
  {
    if (this.worldObj.isRemote)
    {
      if ((name.isEmpty()) || (num < 1) || (num > 50)) return;
      double x = (this.posX - this.prevPosX) / num;
      double y = (this.posY - this.prevPosY) / num;
      double z = (this.posZ - this.prevPosZ) / num;
      for (int i = 0; i < num; i++)
      {
        MCH_ParticleParam prm = new MCH_ParticleParam(this.worldObj, "smoke", this.prevPosX + x * i, this.prevPosY + y * i, this.prevPosZ + z * i);
        
        prm.motionX = mx;
        prm.motionY = my;
        prm.motionZ = mz;
        prm.size = (size + this.rand.nextFloat());
        prm.setColor(1.0F, r, g, b);
        prm.isEffectWind = true;
        MCH_ParticlesUtil.spawnParticle(prm);
      }
    }
  }
  

  protected void onImpact(MovingObjectPosition m, float damageFactor)
  {
    if (this.worldObj.isRemote) return;
    if ((m.entityHit != null) || (W_MovingObjectPosition.isHitTypeEntity(m))) { return;
    }
    int x = m.blockX;
    int y = m.blockY;
    int z = m.blockZ;
    
    switch (m.sideHit) {
    case 0: 
      y--; break;
    case 1:  y++; break;
    case 2:  z--; break;
    case 3:  z++; break;
    case 4:  x--; break;
    case 5:  x++;
    }
    
    if (this.worldObj.isAirBlock(x, y, z))
    {
      if (MCH_Config.Explosion_FlamingBlock.prmBool)
      {
        W_WorldFunc.setBlock(this.worldObj, x, y, z, W_Blocks.fire);
      }
      
      int noAirBlockCount = 0;
      for (int i = y + 1; i < 256; i++)
      {
        if (!this.worldObj.isAirBlock(x, i, z))
        {
          noAirBlockCount++;
          if (noAirBlockCount >= 5) {
            break;
          }
        }
      }
      

      if (noAirBlockCount < 5)
      {
        setMarkerStatus(2);
        setPosition(x + 0.5D, y + 1.1D, z + 0.5D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.countDown = 100;
      }
      else
      {
        setDead();
      }
    }
    else
    {
      setDead();
    }
  }
  

  public MCH_BulletModel getDefaultBulletModel()
  {
    return MCH_DefaultBulletModels.Rocket;
  }
}
