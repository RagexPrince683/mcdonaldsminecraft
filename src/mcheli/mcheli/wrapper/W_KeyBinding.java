package mcheli.wrapper;

import net.minecraft.client.settings.KeyBinding;

public class W_KeyBinding
{
  public W_KeyBinding() {}
  
  public static int getKeyCode(KeyBinding k) {
    return k.getKeyCode();
  }
}
