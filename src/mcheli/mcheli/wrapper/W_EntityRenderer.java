package mcheli.wrapper;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.MCH_Lib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;




public class W_EntityRenderer
{
  public W_EntityRenderer() {}
  
  public static void setItemRenderer(Minecraft mc, ItemRenderer ir)
  {
    W_Reflection.setItemRenderer(ir);
  }
  



  public static boolean isShaderSupport()
  {
    return (OpenGlHelper.shadersSupported) && (!MCH_Config.DisableShader.prmBool);
  }
  


  public static void activateShader(String n)
  {
    activateShader(new ResourceLocation("mcheli", "shaders/post/" + n + ".json"));
  }
  
  public static void activateShader(ResourceLocation r) {
    Minecraft mc = Minecraft.getMinecraft();
    


    try
    {
      mc.entityRenderer.theShaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), r);
      

      mc.entityRenderer.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
    }
    catch (IOException ioexception)
    {
      ioexception.printStackTrace();
    }
    catch (JsonSyntaxException jsonsyntaxexception)
    {
      MCH_Lib.Log("Failed to load shader: " + r, new Object[0]);
      jsonsyntaxexception.printStackTrace();
    }
  }
  




  public static void deactivateShader()
  {
    Minecraft.getMinecraft().entityRenderer.deactivateShader();
  }
  




  public static void renderEntityWithPosYaw(RenderManager rm, Entity par1Entity, double par2, double par4, double par6, float par8, float par9, boolean b)
  {
    rm.func_147939_a(par1Entity, par2, par4, par6, par8, par9, b);
  }
}
