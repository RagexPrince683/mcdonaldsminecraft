package mcheli.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class W_TextureUtil
{
  private static W_TextureUtil instance = new W_TextureUtil();
  



  private W_TextureUtil() {}
  


  private TextureParam newParam()
  {
    return new TextureParam();
  }
  
  public static TextureParam getTextureInfo(String domain, String name) {
    TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    ResourceLocation r = new ResourceLocation(domain, name);
    textureManager.bindTexture(r);
    
    TextureParam info = instance.newParam();
    info.width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
    info.height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
    
    return info;
  }
  
  public class TextureParam
  {
    public int width;
    public int height;
    
    public TextureParam() {}
  }
}
