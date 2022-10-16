package mcheli.weapon;

import java.util.Random;
import mcheli.MCH_Config;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class MCH_EntityDispensedItem extends MCH_EntityBaseBullet
{
  public MCH_EntityDispensedItem(World par1World)
  {
    super(par1World);
  }
  


  public MCH_EntityDispensedItem(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration)
  {
    super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
  }
  

  public void onUpdate()
  {
    super.onUpdate();
    
    if ((getInfo() != null) && (!getInfo().disableSmoke))
    {
      spawnParticle(getInfo().trajectoryParticleName, 3, 7.0F * getInfo().smokeSize);
    }
    
    if ((!this.worldObj.isRemote) && (getInfo() != null))
    {
      if (this.acceleration < 1.0E-4D)
      {
        this.motionX *= 0.999D;
        this.motionZ *= 0.999D;
      }
      
      if (isInWater())
      {
        this.motionX *= getInfo().velocityInWater;
        this.motionY *= getInfo().velocityInWater;
        this.motionZ *= getInfo().velocityInWater;
      }
    }
    




























    onUpdateBomblet();
  }
  


  protected void onImpact(MovingObjectPosition m, float damageFactor)
  {
    if (!this.worldObj.isRemote)
    {
      this.boundingBox.maxY += 2000.0D;
      this.boundingBox.minY += 2000.0D;
      
      EntityPlayer player = null;
      Item item = null;
      int itemDamage = 0;
      if ((m != null) && (getInfo() != null))
      {
        if ((this.shootingAircraft instanceof EntityPlayer))
        {
          player = (EntityPlayer)this.shootingAircraft;
        }
        if ((this.shootingEntity instanceof EntityPlayer))
        {
          player = (EntityPlayer)this.shootingEntity;
        }
        
        item = getInfo().dispenseItem;
        itemDamage = getInfo().dispenseDamege;
      }
      
      if ((player != null) && (!player.isDead) && (item != null))
      {
        EntityPlayer dummyPlayer = new MCH_DummyEntityPlayer(this.worldObj, player);
        dummyPlayer.rotationPitch = 90.0F;
        int RNG = getInfo().dispenseRange - 1;
        for (int x = -RNG; x <= RNG; x++)
        {
          for (int y = -RNG; y <= RNG; y++)
          {
            if ((y >= 0) && (y < 256))
            {
              for (int z = -RNG; z <= RNG; z++)
              {
                int dist = x * x + y * y + z * z;
                if (dist <= RNG * RNG)
                {
                  if (dist <= 0.5D * RNG * RNG)
                  {
                    useItemToBlock(m.blockX + x, m.blockY + y, m.blockZ + z, item, itemDamage, dummyPlayer);
                  }
                  else if (this.rand.nextInt(2) == 0)
                  {
                    useItemToBlock(m.blockX + x, m.blockY + y, m.blockZ + z, item, itemDamage, dummyPlayer);
                  }
                }
              }
            }
          }
        }
      }
      setDead();
    }
  }
  
  private void useItemToBlock(int x, int y, int z, Item item, int itemDamage, EntityPlayer dummyPlayer) {
    dummyPlayer.posX = (x + 0.5D);
    dummyPlayer.posY = (y + 2.5D);
    dummyPlayer.posZ = (z + 0.5D);
    dummyPlayer.rotationYaw = this.rand.nextInt(360);
    Block block = W_WorldFunc.getBlock(this.worldObj, x, y, z);
    Material blockMat = W_WorldFunc.getBlockMaterial(this.worldObj, x, y, z);
    if ((block != W_Blocks.air) && (blockMat != Material.air))
    {
      if (item == W_Item.getItemByName("water_bucket"))
      {
        if (MCH_Config.Collision_DestroyBlock.prmBool)
        {
          if (blockMat == Material.fire)
          {
            this.worldObj.setBlockToAir(x, y, z);
          }
          else if (blockMat == Material.lava)
          {
            int metadata = this.worldObj.getBlockMetadata(x, y, z);
            if (metadata == 0)
            {
              W_WorldFunc.setBlock(this.worldObj, x, y, z, W_Blocks.obsidian);
            }
            else if (metadata <= 4)
            {
              W_WorldFunc.setBlock(this.worldObj, x, y, z, W_Blocks.cobblestone);
            }
          }
        }
      }
      else if (!item.onItemUseFirst(new ItemStack(item, 1, itemDamage), dummyPlayer, this.worldObj, x, y, z, 1, x, y, z))
      {
        if (!item.onItemUse(new ItemStack(item, 1, itemDamage), dummyPlayer, this.worldObj, x, y, z, 1, x, y, z))
        {
          item.onItemRightClick(new ItemStack(item, 1, itemDamage), this.worldObj, dummyPlayer);
        }
      }
    }
  }
  
  public void sprinkleBomblet()
  {
    if (!this.worldObj.isRemote)
    {
      MCH_EntityDispensedItem e = new MCH_EntityDispensedItem(this.worldObj, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, this.rand.nextInt(360), 0.0F, this.acceleration);
      



      e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
      e.setName(getName());
      
      float MOTION = 1.0F;
      float RANDOM = getInfo().bombletDiff;
      e.motionX = (this.motionX * 1.0D + (this.rand.nextFloat() - 0.5F) * RANDOM);
      e.motionY = (this.motionY * 1.0D / 2.0D + (this.rand.nextFloat() - 0.5F) * RANDOM / 2.0F);
      e.motionZ = (this.motionZ * 1.0D + (this.rand.nextFloat() - 0.5F) * RANDOM);
      e.setBomblet();
      
      this.worldObj.spawnEntityInWorld(e);
    }
  }
  

  public MCH_BulletModel getDefaultBulletModel()
  {
    return MCH_DefaultBulletModels.Bomb;
  }
}
