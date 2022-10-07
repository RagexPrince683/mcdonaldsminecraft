package mcheli.weapon;

import mcheli.MCH_Lib;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;


public class MCH_WeaponMachineGun1
  extends MCH_WeaponBase
{
  public MCH_WeaponMachineGun1(World w, Vec3 v, float yaw, float pitch, String nm, MCH_WeaponInfo wi)
  {
    super(w, v, yaw, pitch, nm, wi);
    this.power = 8;
    this.acceleration = 4.0F;
    this.explosionPower = 0;
    this.interval = 0;
  }
  
  public boolean shot(MCH_WeaponParam prm)
  {
    if (!this.worldObj.isRemote)
    {
      Vec3 v = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
      
      MCH_EntityBullet e = new MCH_EntityBullet(this.worldObj, prm.posX, prm.posY, prm.posZ, v.xCoord, v.yCoord, v.zCoord, prm.rotYaw, prm.rotPitch, this.acceleration);
      

      e.setName(this.name);
      
      e.setParameterFromWeapon(this, prm.entity, prm.user);
      
      e.posX += e.motionX * 0.5D;
      e.posY += e.motionY * 0.5D;
      e.posZ += e.motionZ * 0.5D;
      
      this.worldObj.spawnEntityInWorld(e);
      
      playSound(prm.entity);
    }
    








    return true;
  }
}
