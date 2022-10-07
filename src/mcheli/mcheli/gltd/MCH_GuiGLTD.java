package mcheli.gltd;

import mcheli.MCH_Camera;
import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.MCH_KeyName;
import mcheli.gui.MCH_Gui;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public class MCH_GuiGLTD extends MCH_Gui
{
  public MCH_GuiGLTD(net.minecraft.client.Minecraft minecraft)
  {
    super(minecraft);
  }
  

  public void initGui()
  {
    super.initGui();
  }
  

  public boolean doesGuiPauseGame()
  {
    return false;
  }
  
  public boolean isDrawGui(EntityPlayer player)
  {
    return (player.ridingEntity != null) && ((player.ridingEntity instanceof MCH_EntityGLTD));
  }
  
  public void drawGui(EntityPlayer player, boolean isThirdPersonView)
  {
    if ((isThirdPersonView) && (!MCH_Config.DisplayHUDThirdPerson.prmBool)) { return;
    }
    GL11.glLineWidth(scaleFactor);
    
    if (!isDrawGui(player)) return;
    MCH_EntityGLTD gltd = (MCH_EntityGLTD)player.ridingEntity;
    

    if (gltd.camera.getMode(0) == 1)
    {
      GL11.glEnable(3042);
      GL11.glColor4f(0.0F, 1.0F, 0.0F, 0.3F);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      
      GL11.glBlendFunc(1, 1);
      
      mcheli.wrapper.W_McClient.MOD_bindTexture("textures/gui/alpha.png");
      drawTexturedModalRectRotate(0.0D, 0.0D, this.width, this.height, this.rand.nextInt(256), this.rand.nextInt(256), 256.0D, 256.0D, 0.0F);
      

      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
    }
    

    drawString(String.format("x%.1f", new Object[] { Float.valueOf(gltd.camera.getCameraZoom()) }), this.centerX - 70, this.centerY + 10, -805306369);
    

    drawString(gltd.weaponCAS.getName(), this.centerX - 200, this.centerY + 65, gltd.countWait == 0 ? -819986657 : -807468024);
    


    drawCommonPosition(gltd, -819986657);
    
    drawString(gltd.camera.getModeName(0), this.centerX + 30, this.centerY - 50, -819986657);
    
    drawSight(gltd.camera, -819986657);
    
    drawTargetPosition(gltd, -819986657, -807468024);
    
    drawKeyBind(gltd.camera, -805306369, -813727873);
  }
  

  public void drawKeyBind(MCH_Camera camera, int color, int colorCannotUse)
  {
    int OffX = this.centerX + 55;
    int OffY = this.centerY + 40;
    drawString("DISMOUNT :", OffX, OffY + 0, color);
    drawString("CAM MODE :", OffX, OffY + 10, color);
    drawString("ZOOM IN   :", OffX, OffY + 20, camera.getCameraZoom() < 10.0F ? color : colorCannotUse);
    
    drawString("ZOOM OUT :", OffX, OffY + 30, camera.getCameraZoom() > 1.0F ? color : colorCannotUse);
    

    OffX += 60;
    drawString(MCH_KeyName.getDescOrName(42) + " or " + MCH_KeyName.getDescOrName(MCH_Config.KeyUnmount.prmInt), OffX, OffY + 0, color);
    
    drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyCameraMode.prmInt), OffX, OffY + 10, color);
    drawString(MCH_KeyName.getDescOrName(MCH_Config.KeyZoom.prmInt), OffX, OffY + 20, camera.getCameraZoom() < 10.0F ? color : colorCannotUse);
    
    drawString(MCH_KeyName.getDescOrName(MCH_Config.KeySwWeaponMode.prmInt), OffX, OffY + 30, camera.getCameraZoom() > 1.0F ? color : colorCannotUse);
  }
  


  public void drawCommonPosition(MCH_EntityGLTD gltd, int color)
  {
    int OFFSETX = 145;
    drawString(String.format("X: %+.1f", new Object[] { Double.valueOf(gltd.posX) }), this.centerX - 145, this.centerY + 0, color);
    drawString(String.format("Y: %+.1f", new Object[] { Double.valueOf(gltd.posY) }), this.centerX - 145, this.centerY + 10, color);
    drawString(String.format("Z: %+.1f", new Object[] { Double.valueOf(gltd.posZ) }), this.centerX - 145, this.centerY + 20, color);
    drawString(String.format("AX: %+.1f", new Object[] { Float.valueOf(gltd.riddenByEntity.rotationYaw) }), this.centerX - 145, this.centerY + 40, color);
    drawString(String.format("AY: %+.1f", new Object[] { Float.valueOf(gltd.riddenByEntity.rotationPitch) }), this.centerX - 145, this.centerY + 50, color);
  }
  

  public void drawTargetPosition(MCH_EntityGLTD gltd, int color, int colorDanger)
  {
    if (gltd.riddenByEntity == null) return;
    net.minecraft.world.World w = gltd.riddenByEntity.worldObj;
    float yaw = gltd.riddenByEntity.rotationYaw;
    float pitch = gltd.riddenByEntity.rotationPitch;
    double tX = -MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F);
    
    double tZ = MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F);
    
    double tY = -MathHelper.sin(pitch / 180.0F * 3.1415927F);
    
    double dist = MathHelper.sqrt_double(tX * tX + tY * tY + tZ * tZ);
    
    tX = tX * 80.0D / dist;
    tY = tY * 80.0D / dist;
    tZ = tZ * 80.0D / dist;
    
    MCH_Camera c = gltd.camera;
    
    Vec3 src = W_WorldFunc.getWorldVec3(w, c.posX, c.posY, c.posZ);
    Vec3 dst = W_WorldFunc.getWorldVec3(w, c.posX + tX, c.posY + tY, c.posZ + tZ);
    MovingObjectPosition m = W_WorldFunc.clip(w, src, dst);
    
    int OS_X = 50;
    if (m != null)
    {
      drawString(String.format("X: %+.2fm", new Object[] { Double.valueOf(m.hitVec.xCoord) }), this.centerX + 50, this.centerY - 5 - 15, color);
      drawString(String.format("Y: %+.2fm", new Object[] { Double.valueOf(m.hitVec.yCoord) }), this.centerX + 50, this.centerY - 5, color);
      drawString(String.format("Z: %+.2fm", new Object[] { Double.valueOf(m.hitVec.zCoord) }), this.centerX + 50, this.centerY - 5 + 15, color);
      
      double x = m.hitVec.xCoord - c.posX;
      double y = m.hitVec.yCoord - c.posY;
      double z = m.hitVec.zCoord - c.posZ;
      double len = Math.sqrt(x * x + y * y + z * z);
      drawCenteredString(String.format("[%.2fm]", new Object[] { Double.valueOf(len) }), this.centerX, this.centerY + 30, len > 20.0D ? color : colorDanger);

    }
    else
    {
      drawString("X: --.--m", this.centerX + 50, this.centerY - 5 - 15, color);
      drawString("Y: --.--m", this.centerX + 50, this.centerY - 5, color);
      drawString("Z: --.--m", this.centerX + 50, this.centerY - 5 + 15, color);
      drawCenteredString("[--.--m]", this.centerX, this.centerY + 30, colorDanger);
    }
  }
  

  private void drawSight(MCH_Camera camera, int color)
  {
    double posX = this.centerX;
    double posY = this.centerY;
    
    int SW = 30;
    int SH = 20;
    int SINV = 10;
    double[] line2 = { posX - 30.0D, posY - 10.0D, posX - 30.0D, posY - 20.0D, posX - 30.0D, posY - 20.0D, posX - 10.0D, posY - 20.0D, posX - 30.0D, posY + 10.0D, posX - 30.0D, posY + 20.0D, posX - 30.0D, posY + 20.0D, posX - 10.0D, posY + 20.0D, posX + 30.0D, posY - 10.0D, posX + 30.0D, posY - 20.0D, posX + 30.0D, posY - 20.0D, posX + 10.0D, posY - 20.0D, posX + 30.0D, posY + 10.0D, posX + 30.0D, posY + 20.0D, posX + 30.0D, posY + 20.0D, posX + 10.0D, posY + 20.0D };
    




    drawLine(line2, color);
  }
}
