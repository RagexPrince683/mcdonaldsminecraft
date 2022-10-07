package mcheli.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;




@SideOnly(Side.CLIENT)
public abstract class MCH_Gui
  extends GuiScreen
{
  protected int centerX = 0;
  protected int centerY = 0;
  protected Random rand = new Random();
  protected float smoothCamPartialTicks;
  public static int scaleFactor;
  
  public MCH_Gui(Minecraft minecraft)
  {
    this.mc = minecraft;
    this.smoothCamPartialTicks = 0.0F;
    this.zLevel = -110.0F;
  }
  

  public void initGui()
  {
    super.initGui();
  }
  

  public boolean doesGuiPauseGame()
  {
    return false;
  }
  

  public void onTick() {}
  

  public abstract boolean isDrawGui(EntityPlayer paramEntityPlayer);
  
  public abstract void drawGui(EntityPlayer paramEntityPlayer, boolean paramBoolean);
  
  public void drawScreen(int par1, int par2, float partialTicks)
  {
    this.smoothCamPartialTicks = partialTicks;
    
    ScaledResolution scaledresolution = new W_ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    scaleFactor = scaledresolution.getScaleFactor();
    

    if (!this.mc.gameSettings.hideGUI)
    {
      this.width = (this.mc.displayWidth / scaleFactor);
      this.height = (this.mc.displayHeight / scaleFactor);
      this.centerX = (this.width / 2);
      this.centerY = (this.height / 2);
      
      GL11.glPushMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      
      if (this.mc.thePlayer != null) { drawGui(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView != 0);
      }
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
    }
  }
  








  public void drawTexturedModalRectRotate(double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, float rot)
  {
    GL11.glPushMatrix();
    
    GL11.glTranslated(left + width / 2.0D, top + height / 2.0D, 0.0D);
    GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
    
    float f = 0.00390625F;
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(-width / 2.0D, height / 2.0D, this.zLevel, uLeft * 0.00390625D, (vTop + vHeight) * 0.00390625D);
    tessellator.addVertexWithUV(width / 2.0D, height / 2.0D, this.zLevel, (uLeft + uWidth) * 0.00390625D, (vTop + vHeight) * 0.00390625D);
    tessellator.addVertexWithUV(width / 2.0D, -height / 2.0D, this.zLevel, (uLeft + uWidth) * 0.00390625D, vTop * 0.00390625D);
    tessellator.addVertexWithUV(-width / 2.0D, -height / 2.0D, this.zLevel, uLeft * 0.00390625D, vTop * 0.00390625D);
    tessellator.draw();
    
    GL11.glPopMatrix();
  }
  








  public void drawTexturedRect(double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, double textureWidth, double textureHeight)
  {
    float fx = (float)(1.0D / textureWidth);
    float fy = (float)(1.0D / textureHeight);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(left, top + height, this.zLevel, uLeft * fx, (vTop + vHeight) * fy);
    tessellator.addVertexWithUV(left + width, top + height, this.zLevel, (uLeft + uWidth) * fx, (vTop + vHeight) * fy);
    tessellator.addVertexWithUV(left + width, top, this.zLevel, (uLeft + uWidth) * fx, vTop * fy);
    tessellator.addVertexWithUV(left, top, this.zLevel, uLeft * fx, vTop * fy);
    tessellator.draw();
  }
  
  public void drawLineStipple(double[] line, int color, int factor, int pattern)
  {
    GL11.glEnable(2852);
    GL11.glLineStipple(factor, (short)pattern);
    drawLine(line, color);
    GL11.glDisable(2852);
  }
  
  public void drawLine(double[] line, int color)
  {
    drawLine(line, color, 1);
  }
  
  public void drawString(String s, int x, int y, int color)
  {
    drawString(this.mc.fontRenderer, s, x, y, color);
  }
  
  public void drawDigit(String s, int x, int y, int interval, int color)
  {
    GL11.glEnable(3042);
    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    int srcBlend = GL11.glGetInteger(3041);
    int dstBlend = GL11.glGetInteger(3040);
    GL11.glBlendFunc(770, 771);
    
    W_McClient.MOD_bindTexture("textures/gui/digit.png");
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      if ((c >= '0') && (c <= '9'))
      {
        drawTexturedModalRect(x + interval * i, y, 16 * (c - '0'), 0, 16, 16);
      }
      if (c == '-')
      {
        drawTexturedModalRect(x + interval * i, y, 160, 0, 16, 16);
      }
    }
    
    GL11.glBlendFunc(srcBlend, dstBlend);
    GL11.glDisable(3042);
  }
  
  public void drawCenteredString(String s, int x, int y, int color)
  {
    drawCenteredString(this.mc.fontRenderer, s, x, y, color);
  }
  
  public void drawLine(double[] line, int color, int mode)
  {
    GL11.glPushMatrix();
    
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
    


    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(mode);
    for (int i = 0; i < line.length; i += 2)
    {
      tessellator.addVertex(line[(i + 0)], line[(i + 1)], this.zLevel);
    }
    tessellator.draw();
    
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    
    GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
    GL11.glPopMatrix();
  }
  
  public void drawPoints(double[] points, int color, int pointWidth)
  {
    int prevWidth = GL11.glGetInteger(2833);
    
    GL11.glPushMatrix();
    
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
    


    GL11.glPointSize(pointWidth);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(0);
    for (int i = 0; i < points.length; i += 2)
    {
      tessellator.addVertex(points[i], points[(i + 1)], 0.0D);
    }
    tessellator.draw();
    
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    
    GL11.glPopMatrix();
    GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
    GL11.glPointSize(prevWidth);
  }
  
  public void drawPoints(ArrayList<Double> points, int color, int pointWidth)
  {
    int prevWidth = GL11.glGetInteger(2833);
    
    GL11.glPushMatrix();
    
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
    


    GL11.glPointSize(pointWidth);
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(0);
    for (int i = 0; i < points.size(); i += 2)
    {
      tessellator.addVertex(((Double)points.get(i)).doubleValue(), ((Double)points.get(i + 1)).doubleValue(), 0.0D);
    }
    tessellator.draw();
    
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    
    GL11.glPopMatrix();
    GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
    GL11.glPointSize(prevWidth);
  }
}
