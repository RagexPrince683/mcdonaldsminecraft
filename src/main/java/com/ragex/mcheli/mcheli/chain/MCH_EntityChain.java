package mcheli.chain;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.UUID;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;








public class MCH_EntityChain
  extends W_Entity
{
  private int isServerTowEntitySearchCount;
  public Entity towEntity;
  public Entity towedEntity;
  public String towEntityUUID;
  public String towedEntityUUID;
  private int chainLength;
  private boolean isTowing;
  
  public MCH_EntityChain(World world)
  {
    super(world);
    this.preventEntitySpawning = true;
    setSize(1.0F, 1.0F);
    this.yOffset = (this.height / 2.0F);
    
    this.towEntity = null;
    this.towedEntity = null;
    this.towEntityUUID = "";
    this.towedEntityUUID = "";
    this.isTowing = false;
    this.ignoreFrustumCheck = true;
    
    setChainLength(4);
    
    this.isServerTowEntitySearchCount = 50;
  }
  
  public MCH_EntityChain(World par1World, double par2, double par4, double par6)
  {
    this(par1World);
    setPosition(par2, par4 + this.yOffset, par6);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.prevPosX = par2;
    this.prevPosY = par4;
    this.prevPosZ = par6;
  }
  
  protected boolean canTriggerWalking() { return false; }
  
  protected void entityInit()
  {
    getDataWatcher().addObject(30, Integer.valueOf(0));
    getDataWatcher().addObject(31, Integer.valueOf(0));
  }
  




  public AxisAlignedBB getCollisionBox(Entity par1Entity)
  {
    return par1Entity.boundingBox;
  }
  

  public AxisAlignedBB getBoundingBox()
  {
    return null;
  }
  

  public boolean canBePushed()
  {
    return true;
  }
  

  public boolean attackEntityFrom(DamageSource d, float par2)
  {
    return false;
  }
  


















  public void setChainLength(int n)
  {
    if (n > 15) n = 15;
    if (n < 3) n = 3;
    this.chainLength = n;
  }
  
  public int getChainLength()
  {
    return this.chainLength;
  }
  
  public void setDead()
  {
    super.setDead();
    playDisconnectTowingEntity();
    this.isTowing = false;
    this.towEntity = null;
    this.towedEntity = null;
  }
  
  public boolean isTowingEntity()
  {
    return (this.isTowing) && (!this.isDead) && (this.towedEntity != null) && (this.towEntity != null);
  }
  

  public boolean canBeCollidedWith()
  {
    return false;
  }
  
  public void setTowEntity(Entity towedEntity, Entity towEntity)
  {
    if ((towedEntity != null) && (towEntity != null) && (!towedEntity.isDead) && (!towEntity.isDead) && (!W_Entity.isEqual(towedEntity, towEntity)))
    {


      this.isTowing = true;
      this.towedEntity = towedEntity;
      this.towEntity = towEntity;
      if (!this.worldObj.isRemote)
      {
        getDataWatcher().updateObject(30, Integer.valueOf(W_Entity.getEntityId(towedEntity)));
        getDataWatcher().updateObject(31, Integer.valueOf(W_Entity.getEntityId(towEntity)));
        
        this.isServerTowEntitySearchCount = 0;
      }
      
      if ((towEntity instanceof MCH_EntityAircraft))
      {
        ((MCH_EntityAircraft)towEntity).setTowChainEntity(this);
      }
      if ((towedEntity instanceof MCH_EntityAircraft))
      {
        ((MCH_EntityAircraft)towedEntity).setTowedChainEntity(this);
      }
      
    }
    else
    {
      this.isTowing = false;
      this.towedEntity = null;
      this.towEntity = null;
    }
  }
  

  public void searchTowingEntity()
  {
    if (((this.towedEntity == null) || (this.towEntity == null)) && (!this.towedEntityUUID.isEmpty()) && (!this.towEntityUUID.isEmpty()) && (this.boundingBox != null))
    {


      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));
      

      if (list != null)
      {
        for (int i = 0; i < list.size(); i++)
        {
          Entity entity = (Entity)list.get(i);
          String uuid = entity.getPersistentID().toString();
          if ((this.towedEntity == null) && (uuid.compareTo(this.towedEntityUUID) == 0))
          {
            this.towedEntity = entity;

          }
          else if ((this.towEntity == null) && (uuid.compareTo(this.towEntityUUID) == 0))
          {
            this.towEntity = entity;


          }
          else if ((this.towEntity != null) && (this.towedEntity != null))
          {
            setTowEntity(this.towedEntity, this.towEntity);
            break;
          }
        }
      }
    }
  }
  

  public void onUpdate()
  {
    super.onUpdate();
    
    if ((this.towedEntity == null) || (this.towedEntity.isDead) || (this.towEntity == null) || (this.towEntity.isDead))
    {
      this.towedEntity = null;
      this.towEntity = null;
      this.isTowing = false;
    }
    
    if (this.towedEntity != null)
    {
      this.towedEntity.fallDistance = 0.0F;
    }
    
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    
    if (this.worldObj.isRemote)
    {
      onUpdate_Client();
    }
    else
    {
      onUpdate_Server();
    }
  }
  

  public void onUpdate_Client()
  {
    if (!isTowingEntity())
    {
      setTowEntity(this.worldObj.getEntityByID(getDataWatcher().getWatchableObjectInt(30)), this.worldObj.getEntityByID(getDataWatcher().getWatchableObjectInt(31)));
    }
    


    double d4 = this.posX + this.motionX;
    double d5 = this.posY + this.motionY;
    double d11 = this.posZ + this.motionZ;
    setPosition(d4, d5, d11);
    
    if (this.onGround)
    {
      this.motionX *= 0.5D;
      this.motionY *= 0.5D;
      this.motionZ *= 0.5D;
    }
    
    this.motionX *= 0.99D;
    this.motionY *= 0.95D;
    this.motionZ *= 0.99D;
  }
  
  public void onUpdate_Server()
  {
    if (this.isServerTowEntitySearchCount > 0)
    {
      searchTowingEntity();
      
      if ((this.towEntity != null) && (this.towedEntity != null))
      {
        this.isServerTowEntitySearchCount = 0;
      }
      else
      {
        this.isServerTowEntitySearchCount -= 1;
      }
      

    }
    else if ((this.towEntity == null) || (this.towedEntity == null))
    {
      setDead();
    }
    

    this.motionY -= 0.01D;
    
    if (!this.isTowing)
    {
      this.motionX *= 0.8D;
      this.motionY *= 0.8D;
      this.motionZ *= 0.8D;
    }
    
    moveEntity(this.motionX, this.motionY, this.motionZ);
    
    if (isTowingEntity())
    {
      setPosition(this.towEntity.posX, this.towEntity.posY + 2.0D, this.towEntity.posZ);
      updateTowingEntityPosRot();
    }
    
    this.motionX *= 0.99D;
    this.motionY *= 0.95D;
    this.motionZ *= 0.99D;
  }
  


  public void updateTowingEntityPosRot()
  {
    double dx = this.towedEntity.posX - this.towEntity.posX;
    double dy = this.towedEntity.posY - this.towEntity.posY;
    double dz = this.towedEntity.posZ - this.towEntity.posZ;
    double dist = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    
    float DIST = getChainLength();
    float MAX_DIST = getChainLength() + 2;
    if (dist > DIST)
    {
      this.towedEntity.rotationYaw = ((float)(Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) + 90.0F);
      this.towedEntity.prevRotationYaw = this.towedEntity.rotationYaw;
      
      double tmp = dist - DIST;
      float accl = 0.001F;
      this.towedEntity.motionX -= dx * accl / tmp;
      this.towedEntity.motionY -= dy * accl / tmp;
      this.towedEntity.motionZ -= dz * accl / tmp;
      if (dist > MAX_DIST)
      {
        this.towedEntity.setPosition(this.towEntity.posX + dx * MAX_DIST / dist, this.towEntity.posY + dy * MAX_DIST / dist, this.towEntity.posZ + dz * MAX_DIST / dist);
      }
    }
  }
  



  public void playDisconnectTowingEntity()
  {
    W_WorldFunc.MOD_playSoundEffect(this.worldObj, this.posX, this.posY, this.posZ, "chain_ct", 1.0F, 1.0F);
  }
  

  protected void writeEntityToNBT(NBTTagCompound nbt)
  {
    if ((this.isTowing) && (this.towEntity != null) && (!this.towEntity.isDead) && (this.towedEntity != null) && (!this.towedEntity.isDead))
    {
      nbt.setString("TowEntityUUID", this.towEntity.getPersistentID().toString());
      nbt.setString("TowedEntityUUID", this.towedEntity.getPersistentID().toString());
      nbt.setInteger("ChainLength", getChainLength());
    }
  }
  

  protected void readEntityFromNBT(NBTTagCompound nbt)
  {
    this.towEntityUUID = nbt.getString("TowEntityUUID");
    this.towedEntityUUID = nbt.getString("TowedEntityUUID");
    setChainLength(nbt.getInteger("ChainLength"));
  }
  
  @SideOnly(Side.CLIENT)
  public float getShadowSize() { return 0.0F; }
  

  public boolean interactFirst(EntityPlayer player)
  {
    return false;
  }
}
