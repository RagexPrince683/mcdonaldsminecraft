package mcheli.wrapper;

import org.lwjgl.opengl.GL11;




public class W_OpenGlHelper
{
  public W_OpenGlHelper() {}
  
  public static void glBlendFunc(int i, int j, int k, int l)
  {
    GL11.glBlendFunc(i, j);
  }
}
