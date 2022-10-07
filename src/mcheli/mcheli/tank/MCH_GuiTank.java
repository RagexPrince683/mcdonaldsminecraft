package mcheli.tank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.MCH_KeyName;
import mcheli.aircraft.MCH_AircraftCommonGui;
import mcheli.aircraft.MCH_EntityAircraft;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;



@SideOnly(Side.CLIENT)
public class MCH_GuiTank
  extends MCH_AircraftCommonGui
{
  public MCH_GuiTank(Minecraft minecraft)
  {
    super(minecraft);
  }
  
  public boolean isDrawGui(EntityPlayer player)
  {
    return MCH_EntityAircraft.getAircraft_RiddenOrControl(player) instanceof MCH_EntityTank;
  }
  
  public void drawGui(EntityPlayer player, boolean isThirdPersonView)
  {
    MCH_EntityAircraft ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(player);
    
    if ((!(ac instanceof MCH_EntityTank)) || (ac.isDestroyed()))
    {
      return;
    }
    
    MCH_EntityTank tank = (MCH_EntityTank)ac;
    
    int seatID = ac.getSeatIdByEntity(player);
    
    GL11.glLineWidth(scaleFactor);
    

    if (tank.getCameraMode(player) == 1)
    {
      drawNightVisionNoise();
    }
    
    if ((!isThirdPersonView) || (MCH_Config.DisplayHUDThirdPerson.prmBool))
    {
      drawHud(ac, player, seatID);
    }
    
    drawDebugtInfo(tank);
    
    if ((!isThirdPersonView) || (MCH_Config.DisplayHUDThirdPerson.prmBool))
    {
      if ((tank.getTVMissile() != null) && ((tank.getIsGunnerMode(player)) || (tank.isUAV())))
      {
        drawTvMissileNoise(tank, tank.getTVMissile());

      }
      else
      {
        drawKeybind(tank, player, seatID);
      }
    }
    

    drawHitBullet(tank, -14101432, seatID);
  }
  
  public void drawDebugtInfo(MCH_EntityTank ac)
  {
    if (MCH_Config.DebugLog)
    {
      int LX = this.centerX - 100;
      

      super.drawDebugtInfo(ac);
    }
  }
  
  public void drawKeybind(MCH_EntityTank tank, EntityPlayer player, int seatID)
  {
    if (MCH_Config.HideKeybind.prmBool) return;
    MCH_TankInfo info = tank.getTankInfo();
    if (info == null) { return;
    }
    

    int colorActive = -1342177281;
    int colorInactive = -1349546097;
    int RX = this.centerX + 120;
    int LX = this.centerX - 200;
    
    drawKeyBind(tank, info, player, seatID, RX, LX, colorActive, colorInactive);
    


    if ((seatID == 0) && (tank.hasBrake()))
    {
      String msg = "Brake : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchHovering.prmInt);
      drawString(msg, RX, this.centerY - 30, colorActive);
    }
    
    if ((seatID > 0) && (tank.canSwitchGunnerModeOtherSeat(player)))
    {
      String msg = (tank.getIsGunnerMode(player) ? "Normal" : "Camera") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchMode.prmInt);
      
      drawString(msg, RX, this.centerY - 40, colorActive);
    }
    

    if ((tank.getIsGunnerMode(player)) && (info.cameraZoom > 1))
    {

      String msg = "Zoom : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
      drawString(msg, LX, this.centerY - 80, colorActive);
    }
    else if (seatID == 0)
    {

      if ((tank.canFoldHatch()) || (tank.canUnfoldHatch()))
      {
        String msg = "OpenHatch : " + MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt);
        drawString(msg, LX, this.centerY - 80, colorActive);
      }
    }
  }
}
