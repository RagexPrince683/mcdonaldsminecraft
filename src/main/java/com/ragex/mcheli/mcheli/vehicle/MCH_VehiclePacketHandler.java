package mcheli.vehicle;

import com.google.common.io.ByteArrayDataInput;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MCH_VehiclePacketHandler
{
  public MCH_VehiclePacketHandler() {}
  
  public static void onPacket_PlayerControl(EntityPlayer player, ByteArrayDataInput data)
  {
    if (!(player.ridingEntity instanceof MCH_EntityVehicle))
    {
      return;
    }
    
    if (player.worldObj.isRemote)
    {

      return;
    }
    
    MCH_PacketVehiclePlayerControl pc = new MCH_PacketVehiclePlayerControl();
    pc.readData(data);
    
    MCH_EntityVehicle vehicle = (MCH_EntityVehicle)player.ridingEntity;
    

    if (pc.isUnmount == 1)
    {
      vehicle.unmountEntity();
    }
    else if (pc.isUnmount == 2)
    {
      vehicle.unmountCrew();
    }
    else
    {
      if (pc.switchSearchLight) vehicle.setSearchLight(!vehicle.isSearchLightON());
      if (pc.switchCameraMode > 0) vehicle.switchCameraMode(player, pc.switchCameraMode - 1);
      if (pc.switchWeapon >= 0) vehicle.switchWeapon(player, pc.switchWeapon);
      if (pc.useWeapon)
      {
        MCH_WeaponParam prm = new MCH_WeaponParam();
        prm.entity = vehicle;
        prm.user = player;
        prm.setPosAndRot(pc.useWeaponPosX, pc.useWeaponPosY, pc.useWeaponPosZ, 0.0F, 0.0F);
        prm.option1 = pc.useWeaponOption1;
        prm.option2 = pc.useWeaponOption2;
        vehicle.useCurrentWeapon(prm);
      }
      
      if (vehicle.isPilot(player))
      {
        vehicle.throttleUp = pc.throttleUp;
        vehicle.throttleDown = pc.throttleDown;
        vehicle.moveLeft = pc.moveLeft;
        vehicle.moveRight = pc.moveRight;
      }
      
      if (pc.useFlareType > 0) { vehicle.useFlare(pc.useFlareType);
      }
      
      if (pc.unhitchChainId >= 0)
      {
        Entity e = player.worldObj.getEntityByID(pc.unhitchChainId);
        if ((e instanceof mcheli.chain.MCH_EntityChain))
        {
          e.setDead();
        }
      }
      

      if (pc.openGui) vehicle.openGui(player);
      if (pc.switchHatch > 0) { vehicle.foldHatch(pc.switchHatch == 2);
      }
      if (pc.isUnmount == 3) vehicle.unmountAircraft();
      if (pc.switchGunnerStatus) vehicle.setGunnerStatus(!vehicle.getGunnerStatus());
    }
  }
}
