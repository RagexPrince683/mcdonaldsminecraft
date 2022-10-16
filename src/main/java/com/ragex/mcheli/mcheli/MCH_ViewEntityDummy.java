package mcheli;

import mcheli.wrapper.W_Session;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;

public class MCH_ViewEntityDummy extends EntityPlayerSP
{
  private static MCH_ViewEntityDummy instance = null;
  private float zoom;
  
  private MCH_ViewEntityDummy(World world)
  {
    super(Minecraft.getMinecraft(), world, W_Session.newSession(), 0);
    this.hurtTime = 0;
    this.maxHurtTime = 1;
    setSize(1.0F, 1.0F);
  }
  
  public static MCH_ViewEntityDummy getInstance(World w)
  {
    if ((instance == null) || (instance.isDead))
    {
      if (w.isRemote)
      {
        instance = new MCH_ViewEntityDummy(w);
        if (Minecraft.getMinecraft().thePlayer != null)
        {
          instance.movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
        }
        instance.setPosition(0.0D, -4.0D, 0.0D);
        w.spawnEntityInWorld(instance);
      }
    }
    return instance;
  }
  
  public static void onUnloadWorld()
  {
    if (instance != null)
    {
      instance.setDead();
      instance = null;
    }
  }
  


  public void onUpdate() {}
  

  public void update(MCH_Camera camera)
  {
    if (camera == null) return;
    this.zoom = camera.getCameraZoom();
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    
    this.rotationYaw = camera.rotationYaw;
    this.rotationPitch = camera.rotationPitch;
    this.prevPosX = camera.posX;
    this.prevPosY = camera.posY;
    this.prevPosZ = camera.posZ;
    this.posX = camera.posX;
    this.posY = camera.posY;
    this.posZ = camera.posZ;
  }
  
  public static void setCameraPosition(double x, double y, double z)
  {
    if (instance == null) return;
    instance.prevPosX = x;
    instance.prevPosY = y;
    instance.prevPosZ = z;
    instance.lastTickPosX = x;
    instance.lastTickPosY = y;
    instance.lastTickPosZ = z;
    instance.posX = x;
    instance.posY = y;
    instance.posZ = z;
  }
  

  public float getFOVMultiplier()
  {
    return super.getFOVMultiplier() * (1.0F / this.zoom);
  }
}
