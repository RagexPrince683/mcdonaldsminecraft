package mcheli;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class MCH_Key
{
  public int key;
  private boolean isPress;
  private boolean isBeforePress;
  
  public MCH_Key(int k)
  {
    this.key = k;
    this.isPress = false;
    this.isBeforePress = false;
  }
  
  public boolean isKeyDown() {
    return (!this.isBeforePress) && (this.isPress == true);
  }
  
  public boolean isKeyPress() {
    return this.isPress;
  }
  
  public boolean isKeyUp() {
    return (this.isBeforePress == true) && (!this.isPress);
  }
  
  public void update()
  {
    if (this.key == 0) return;
    this.isBeforePress = this.isPress;
    if (this.key >= 0)
    {
      this.isPress = org.lwjgl.input.Keyboard.isKeyDown(this.key);
    }
    else
    {
      this.isPress = Mouse.isButtonDown(this.key + 100);
    }
  }
  
  public static boolean isKeyDown(int key)
  {
    if (key > 0) return org.lwjgl.input.Keyboard.isKeyDown(key);
    if (key < 0) return Mouse.isButtonDown(key + 100);
    return false;
  }
  
  public static boolean isKeyDown(KeyBinding keyBind) {
    return isKeyDown(mcheli.wrapper.W_KeyBinding.getKeyCode(keyBind));
  }
}
