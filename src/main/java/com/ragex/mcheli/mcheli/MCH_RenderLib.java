package mcheli;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;



public class MCH_RenderLib
{
  public MCH_RenderLib() {}
  
  public static void drawLine(Vec3[] points, int color)
  {
    drawLine(points, color, 1, 1);
  }
  
  public static void drawLine(Vec3[] points, int color, int mode, int width)
  {
    int prevWidth = GL11.glGetInteger(2849);
    GL11.glDisable(3553);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color >> 0 & 0xFF), (byte)(color >> 24 & 0xFF));
    



    GL11.glLineWidth(width);
    
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawing(mode);
    for (Vec3 v : points)
    {
      tessellator.addVertex(v.xCoord, v.yCoord, v.zCoord);
    }
    tessellator.draw();
    
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
    GL11.glLineWidth(prevWidth);
  }
}
