package mcheli.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;



public class MCH_EntityParticleExplode
  extends MCH_EntityParticleBase
{
  private static final ResourceLocation texture = new ResourceLocation("textures/entity/explosion.png");
  
  private int nowCount;
  
  private int endCount;
  private TextureManager theRenderEngine;
  private float size;
  
  public MCH_EntityParticleExplode(World w, double x, double y, double z, double size, double age, double mz)
  {
    super(w, x, y, z, 0.0D, 0.0D, 0.0D);
    this.theRenderEngine = Minecraft.getMinecraft().renderEngine;
    this.endCount = (1 + (int)age);
    this.size = ((float)size);
  }
  

  public void renderParticle(Tessellator tessellator, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
  {
    int i = (int)((this.nowCount + p_70539_2_) * 15.0F / this.endCount);
    
    if (i <= 15)
    {
      GL11.glEnable(3042);
      int srcBlend = GL11.glGetInteger(3041);
      int dstBlend = GL11.glGetInteger(3040);
      GL11.glBlendFunc(770, 771);
      
      GL11.glDisable(2884);
      this.theRenderEngine.bindTexture(texture);
      float f6 = i % 4 / 4.0F;
      float f7 = f6 + 0.24975F;
      float f8 = i / 4 / 4.0F;
      float f9 = f8 + 0.24975F;
      float f10 = 2.0F * this.size;
      float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_70539_2_ - interpPosX);
      float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_70539_2_ - interpPosY);
      float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_70539_2_ - interpPosZ);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      
      RenderHelper.disableStandardItemLighting();
      tessellator.startDrawingQuads();
      tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.setBrightness(15728880);
      tessellator.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
      tessellator.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
      tessellator.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
      tessellator.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
      tessellator.draw();
      GL11.glPolygonOffset(0.0F, 0.0F);
      GL11.glEnable(2896);
      GL11.glEnable(2884);
      
      GL11.glBlendFunc(srcBlend, dstBlend);
      GL11.glDisable(3042);
    }
  }
  
  public int getBrightnessForRender(float p_70070_1_)
  {
    return 15728880;
  }
  



  public void onUpdate()
  {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.nowCount += 1;
    
    if (this.nowCount == this.endCount)
    {
      setDead();
    }
  }
  
  public int getFXLayer()
  {
    return 3;
  }
}
