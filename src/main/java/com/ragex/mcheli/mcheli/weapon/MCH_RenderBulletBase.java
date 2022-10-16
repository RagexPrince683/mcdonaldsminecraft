package mcheli.weapon;

import mcheli.MCH_Color;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public abstract class MCH_RenderBulletBase extends mcheli.wrapper.W_Render
{
  public MCH_RenderBulletBase() {}
  
  public void doRender(Entity e, double var2, double var4, double var6, float var8, float var9)
  {
    if (((e instanceof MCH_EntityBaseBullet)) && (((MCH_EntityBaseBullet)e).getInfo() != null))
    {
      MCH_Color c = ((MCH_EntityBaseBullet)e).getInfo().color;
      
      for (int y = 0; y < 3; y++)
      {
        net.minecraft.block.Block b = W_WorldFunc.getBlock(e.worldObj, (int)(e.posX + 0.5D), (int)(e.posY + 1.5D - y), (int)(e.posZ + 0.5D));
        if ((b != null) && (b == W_Block.getWater()))
        {
          c = ((MCH_EntityBaseBullet)e).getInfo().colorInWater;
          break;
        }
      }
      
      GL11.glColor4f(c.r, c.g, c.b, c.a);
    }
    else
    {
      GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
    }
    
    GL11.glAlphaFunc(516, 0.001F);
    
    GL11.glEnable(2884);
    

    GL11.glEnable(3042);
    int srcBlend = GL11.glGetInteger(3041);
    int dstBlend = GL11.glGetInteger(3040);
    GL11.glBlendFunc(770, 771);
    
    renderBullet(e, var2, var4, var6, var8, var9);
    
    GL11.glColor4f(0.75F, 0.75F, 0.75F, 1.0F);
    
    GL11.glBlendFunc(srcBlend, dstBlend);
    GL11.glDisable(3042);
  }
  
  public void renderModel(MCH_EntityBaseBullet e)
  {
    MCH_BulletModel model = e.getBulletModel();
    if (model != null)
    {
      bindTexture("textures/bullets/" + model.name + ".png");
      model.model.renderAll();
    }
  }
  
  public abstract void renderBullet(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);
}
