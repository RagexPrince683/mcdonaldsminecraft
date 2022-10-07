package mcheli.throwable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcheli.wrapper.W_Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;





@SideOnly(Side.CLIENT)
public class MCH_RenderThrowable
  extends W_Render
{
  public MCH_RenderThrowable()
  {
    this.shadowSize = 0.0F;
  }
  






  public void doRender(Entity entity, double posX, double posY, double posZ, float par8, float tickTime)
  {
    MCH_EntityThrowable throwable = (MCH_EntityThrowable)entity;
    MCH_ThrowableInfo info = throwable.getInfo();
    if (info == null) { return;
    }
    
    GL11.glPushMatrix();
    
    GL11.glTranslated(posX, posY, posZ);
    

    GL11.glRotatef(entity.rotationYaw, 0.0F, -1.0F, 0.0F);
    GL11.glRotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
    
    setCommonRenderParam(true, entity.getBrightnessForRender(tickTime));
    
    if (info.model != null)
    {
      bindTexture("textures/throwable/" + info.name + ".png");
      
      info.model.renderAll();
    }
    
    restoreCommonRenderParam();
    
    GL11.glPopMatrix();
  }
  
  protected ResourceLocation getEntityTexture(Entity entity)
  {
    return TEX_DEFAULT;
  }
}
