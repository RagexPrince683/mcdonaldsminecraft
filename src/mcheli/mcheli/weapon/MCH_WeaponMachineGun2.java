package mcheli.weapon;

import mcheli.MCH_Lib;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;


public class MCH_WeaponMachineGun2
  extends MCH_WeaponBase
{
  public MCH_WeaponMachineGun2(World w, Vec3 v, float yaw, float pitch, String nm, MCH_WeaponInfo wi)
  {
    super(w, v, yaw, pitch, nm, wi);
    this.power = 16;
    this.acceleration = 4.0F;
    this.explosionPower = 1;
    this.interval = 2;
    this.numMode = 2;
  }
  

  public void modifyParameters()
  {
    if (this.explosionPower == 0)
    {
      this.numMode = 0;
    }
  }
  
  public String getName() {
    return super.getName() + (getCurrentMode() == 0 ? "" : " [HE]");
  }
  
  public boolean shot(MCH_WeaponParam prm) {
    if (!this.worldObj.isRemote)
    {
      Vec3 v = MCH_Lib.RotVec3(0.0D, 0.0D, 1.0D, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
      
      MCH_EntityBullet e = new MCH_EntityBullet(this.worldObj, prm.posX, prm.posY, prm.posZ, v.xCoord, v.yCoord, v.zCoord, prm.rotYaw, prm.rotPitch, this.acceleration);
      

      e.setName(this.name);
      
      e.setParameterFromWeapon(this, prm.entity, prm.user);
      
      if (getInfo().modeNum < 2)
      {
        e.explosionPower = this.explosionPower;
      }
      else
      {
        e.explosionPower = (prm.option1 == 0 ? -this.explosionPower : this.explosionPower);
      }
      
      e.posX += e.motionX * 0.5D;
      e.posY += e.motionY * 0.5D;
      e.posZ += e.motionZ * 0.5D;
      
      this.worldObj.spawnEntityInWorld(e);
      
      playSound(prm.entity);
    }
    else
    {
      this.optionParameter1 = getCurrentMode();
    }
    
    return true;
  }
}
