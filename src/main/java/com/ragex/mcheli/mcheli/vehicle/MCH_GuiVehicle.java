package mcheli.vehicle;

import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.MCH_KeyName;
import mcheli.aircraft.MCH_AircraftCommonGui;
import mcheli.weapon.MCH_WeaponSet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class MCH_GuiVehicle extends MCH_AircraftCommonGui
{
  static final int COLOR1 = -14066;
  static final int COLOR2 = -2161656;
  
  public MCH_GuiVehicle(Minecraft minecraft)
  {
    super(minecraft);
  }
  
  public boolean isDrawGui(EntityPlayer player)
  {
    return (player.ridingEntity != null) && ((player.ridingEntity instanceof MCH_EntityVehicle));
  }
  




  public void drawGui(EntityPlayer player, boolean isThirdPersonView)
  {
    if ((player.ridingEntity == null) || (!(player.ridingEntity instanceof MCH_EntityVehicle)))
    {
      return;
    }
    
    MCH_EntityVehicle vehicle = (MCH_EntityVehicle)player.ridingEntity;
    if (vehicle.isDestroyed()) return;
    int seatID = vehicle.getSeatIdByEntity(player);
    
    org.lwjgl.opengl.GL11.glLineWidth(scaleFactor);
    

    if (vehicle.getCameraMode(player) == 1)
    {
      drawNightVisionNoise();
    }
    if ((vehicle.getIsGunnerMode(player)) && (vehicle.getTVMissile() != null))
    {
      drawTvMissileNoise(vehicle, vehicle.getTVMissile());
    }
    drawDebugtInfo(vehicle);
    
    if ((!isThirdPersonView) || (MCH_Config.DisplayHUDThirdPerson.prmBool))
    {
      drawHud(vehicle, player, seatID);
      drawKeyBind(vehicle, player);
    }
    

    drawHitBullet(vehicle, 51470, seatID);
  }
  
  public void drawKeyBind(MCH_EntityVehicle vehicle, EntityPlayer player)
  {
    if (MCH_Config.HideKeybind.prmBool) { return;
    }
    MCH_VehicleInfo info = vehicle.getVehicleInfo();
    if (info == null) { return;
    }
    

    int colorActive = -1342177281;
    int colorInactive = -1349546097;
    int RX = this.centerX + 120;
    int LX = this.centerX - 200;
    

    if (vehicle.haveFlare())
    {

      int c = vehicle.isFlarePreparation() ? colorInactive : colorActive;
      String msg = "Flare : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFlare.prmInt);
      drawString(msg, RX, this.centerY - 50, c);
    }
    

    String msg = "Gunner " + (vehicle.getGunnerStatus() ? "ON" : "OFF") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt) + " + " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
    

    drawString(msg, LX, this.centerY - 40, colorActive);
    


    if ((vehicle.getSizeInventory() <= 0) || (
    




      (vehicle.getTowChainEntity() != null) && (!vehicle.getTowChainEntity().isDead)))
    {

      msg = "Drop  : " + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
      drawString(msg, RX, this.centerY - 30, colorActive);
    }
    


    if (vehicle.camera.getCameraZoom() > 1.0F)
    {

      msg = "Zoom : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
      drawString(msg, LX, this.centerY - 80, colorActive);
    }
    

    MCH_WeaponSet ws = vehicle.getCurrentWeapon(player);
    if (vehicle.getWeaponNum() > 1)
    {
      msg = "Weapon : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchWeapon2.prmInt);
      drawString(msg, LX, this.centerY - 70, colorActive);
    }
    if (ws.getCurrentWeapon().numMode > 0)
    {
      msg = "WeaponMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt);
      drawString(msg, LX, this.centerY - 60, colorActive);
    }
    

    if (info.isEnableNightVision)
    {
      msg = "CameraMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
      drawString(msg, LX, this.centerY - 50, colorActive);
    }
    

    msg = "Dismount all : LShift";
    drawString(msg, LX, this.centerY - 30, colorActive);
    
    if (vehicle.getSeatNum() >= 2)
    {

      msg = "Dismount : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
      drawString(msg, LX, this.centerY - 40, colorActive);
    }
  }
}
