package mcheli.aircraft;

import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.MCH_KeyName;
import mcheli.MCH_Lib;
import mcheli.gui.MCH_Gui;
import mcheli.hud.MCH_Hud;
import mcheli.weapon.MCH_EntityTvMissile;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public abstract class MCH_AircraftCommonGui extends MCH_Gui
{
  public MCH_AircraftCommonGui(Minecraft minecraft)
  {
    super(minecraft);
  }
  
  public void drawHud(MCH_EntityAircraft ac, EntityPlayer player, int seatId)
  {
    MCH_AircraftInfo info = ac.getAcInfo();
    if (info == null) { return;
    }
    if ((ac.isMissileCameraMode(player)) && (ac.getTVMissile() != null) && (info.hudTvMissile != null))
    {
      info.hudTvMissile.draw(ac, player, this.smoothCamPartialTicks);
    }
    else
    {
      if (seatId < 0) return;
      if (seatId < info.hudList.size())
      {
        MCH_Hud hud = (MCH_Hud)info.hudList.get(seatId);
        if (hud != null)
        {
          hud.draw(ac, player, this.smoothCamPartialTicks);
        }
      }
    }
  }
  
  public void drawDebugtInfo(MCH_EntityAircraft ac) {
    int LX;
    if (MCH_Config.DebugLog)
    {
      LX = this.centerX - 100;
    }
  }
  


  public void drawNightVisionNoise()
  {
    GL11.glEnable(3042);
    GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
    int srcBlend = GL11.glGetInteger(3041);
    int dstBlend = GL11.glGetInteger(3040);
    
    GL11.glBlendFunc(1, 1);
    
    W_McClient.MOD_bindTexture("textures/gui/alpha.png");
    drawTexturedModalRectRotate(0.0D, 0.0D, this.width, this.height, this.rand.nextInt(256), this.rand.nextInt(256), 256.0D, 256.0D, 0.0F);
    
    GL11.glBlendFunc(srcBlend, dstBlend);
    GL11.glDisable(3042);
  }
  
  public void drawHitBullet(int hs, int hsMax, int color)
  {
    if (hs > 0)
    {
      int cx = this.centerX;
      int cy = this.centerY;
      int IVX = 10;
      int IVY = 10;
      int SZX = 5;
      int SZY = 5;
      double[] ls = { cx - IVX, cy - IVY, cx - SZX, cy - SZY, cx - IVX, cy + IVY, cx - SZX, cy + SZY, cx + IVX, cy - IVY, cx + SZX, cy - SZY, cx + IVX, cy + IVY, cx + SZX, cy + SZY };
      





      color = MCH_Config.hitMarkColorRGB;
      int alpha = hs * (256 / hsMax);
      color |= (int)(MCH_Config.hitMarkColorAlpha * alpha) << 24;
      drawLine(ls, color);
    }
  }
  

  public void drawHitBullet(MCH_EntityAircraft ac, int color, int seatID)
  {
    drawHitBullet(ac.getHitStatus(), ac.getMaxHitStatus(), color);
  }
  
  protected void drawTvMissileNoise(MCH_EntityAircraft ac, MCH_EntityTvMissile tvmissile)
  {
    GL11.glEnable(3042);
    GL11.glColor4f(0.5F, 0.5F, 0.5F, 0.4F);
    int srcBlend = GL11.glGetInteger(3041);
    int dstBlend = GL11.glGetInteger(3040);
    
    GL11.glBlendFunc(1, 1);
    
    W_McClient.MOD_bindTexture("textures/gui/noise.png");
    drawTexturedModalRectRotate(0.0D, 0.0D, this.width, this.height, this.rand.nextInt(256), this.rand.nextInt(256), 256.0D, 256.0D, 0.0F);
    
    GL11.glBlendFunc(srcBlend, dstBlend);
    GL11.glDisable(3042);
  }
  

  public void drawKeyBind(MCH_EntityAircraft ac, MCH_AircraftInfo info, EntityPlayer player, int seatID, int RX, int LX, int colorActive, int colorInactive)
  {
    String msg = "";
    int c = 0;
    
    if ((seatID == 0) && (ac.canPutToRack()))
    {
      msg = "PutRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyPutToRack.prmInt);
      drawString(msg, LX, this.centerY - 10, colorActive);
    }
    if ((seatID == 0) && (ac.canDownFromRack()))
    {
      msg = "DownRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyDownFromRack.prmInt);
      drawString(msg, LX, this.centerY - 0, colorActive);
    }
    if ((seatID == 0) && (ac.canRideRack()))
    {
      msg = "RideRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyPutToRack.prmInt);
      drawString(msg, LX, this.centerY + 10, colorActive);
    }
    if ((seatID == 0) && (ac.ridingEntity != null))
    {
      msg = "DismountRack : " + MCH_KeyName.getDescOrName(MCH_Config.KeyDownFromRack.prmInt);
      drawString(msg, LX, this.centerY + 10, colorActive);
    }
    

    if (((seatID > 0) && (ac.getSeatNum() > 1)) || (Keyboard.isKeyDown(MCH_Config.KeyFreeLook.prmInt)))
    {
      c = seatID == 0 ? 65328 : colorActive;
      String sk = seatID == 0 ? MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt) + " + " : "";
      msg = "NextSeat : " + sk + MCH_KeyName.getDescOrName(MCH_Config.KeyGUI.prmInt);
      drawString(msg, RX, this.centerY - 70, c);
      msg = "PrevSeat : " + sk + MCH_KeyName.getDescOrName(MCH_Config.KeyExtra.prmInt);
      drawString(msg, RX, this.centerY - 60, c);
    }
    

    msg = "Gunner " + (ac.getGunnerStatus() ? "ON" : "OFF") + " : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt) + " + " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
    

    drawString(msg, LX, this.centerY - 40, colorActive);
    


    if ((seatID >= 0) && (seatID <= 1) && (ac.haveFlare()))
    {
      c = ac.isFlarePreparation() ? colorInactive : colorActive;
      msg = "Flare : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFlare.prmInt);
      drawString(msg, RX, this.centerY - 50, c);
    }
    

    if ((seatID == 0) && (info.haveLandingGear()))
    {
      if (ac.canFoldLandingGear())
      {
        msg = "Gear Up : " + MCH_KeyName.getDescOrName(MCH_Config.KeyGearUpDown.prmInt);
        drawString(msg, RX, this.centerY - 40, colorActive);
      }
      else if (ac.canUnfoldLandingGear())
      {
        msg = "Gear Down : " + MCH_KeyName.getDescOrName(MCH_Config.KeyGearUpDown.prmInt);
        drawString(msg, RX, this.centerY - 40, colorActive);
      }
    }
    

    MCH_WeaponSet ws = ac.getCurrentWeapon(player);
    if (ac.getWeaponNum() > 1)
    {
      msg = "Weapon : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwitchWeapon2.prmInt);
      drawString(msg, LX, this.centerY - 70, colorActive);
    }
    if (ws.getCurrentWeapon().numMode > 0)
    {
      msg = "WeaponMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt);
      drawString(msg, LX, this.centerY - 60, colorActive);
    }
    

    if (ac.canSwitchSearchLight(player))
    {
      msg = "SearchLight : " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
      drawString(msg, LX, this.centerY - 50, colorActive);



    }
    else if (ac.canSwitchCameraMode(seatID))
    {
      msg = "CameraMode : " + MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt);
      drawString(msg, LX, this.centerY - 50, colorActive);
    }
    


    if ((seatID == 0) && (ac.getSeatNum() >= 1))
    {
      int color = colorActive;
      if ((info.isEnableParachuting) && (MCH_Lib.getBlockIdY(ac, 3, -10) == 0))
      {
        msg = "Parachuting : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
      }
      else if (ac.canStartRepelling())
      {
        msg = "Repelling : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
        color = 65280;
      }
      else
      {
        msg = "Dismount : " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt);
      }
      drawString(msg, LX, this.centerY - 30, color);
    }
    

    if (((seatID == 0) && (ac.canSwitchFreeLook())) || ((seatID > 0) && (ac.canSwitchGunnerModeOtherSeat(player))))
    {
      msg = "FreeLook : " + MCH_KeyName.getDescOrName(MCH_Config.KeyFreeLook.prmInt);
      drawString(msg, LX, this.centerY - 20, colorActive);
    }
  }
}
