package mcheli.gltd;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_Camera;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_GLTDPacketHandler
{
  public MCH_GLTDPacketHandler() {}
  
  public static void onPacket_GLTDPlayerControl(EntityPlayer player, ByteArrayDataInput data)
  {
    if (!(player.ridingEntity instanceof MCH_EntityGLTD))
    {
      return;
    }
    
    if (player.worldObj.isRemote)
    {

      return;
    }
    
    MCH_PacketGLTDPlayerControl pc = new MCH_PacketGLTDPlayerControl();
    pc.readData(data);
    
    MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.ridingEntity;
    
    if (pc.unmount)
    {
      if (gltd.riddenByEntity != null)
      {
        gltd.riddenByEntity.mountEntity(null);
      }
    }
    else
    {
      if (pc.switchCameraMode >= 0) gltd.camera.setMode(0, pc.switchCameraMode);
      if (pc.switchWeapon >= 0) gltd.switchWeapon(pc.switchWeapon);
      if (pc.useWeapon)
      {
        gltd.useCurrentWeapon(pc.useWeaponOption1, pc.useWeaponOption2);
      }
    }
  }
}
