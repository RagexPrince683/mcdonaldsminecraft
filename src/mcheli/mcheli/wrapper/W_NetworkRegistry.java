package mcheli.wrapper;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class W_NetworkRegistry
{
  public static W_PacketHandler packetHandler;
  
  public W_NetworkRegistry() {}
  
  public static void registerChannel(W_PacketHandler handler, String name)
  {
    packetHandler = handler;
    W_Network.INSTANCE.registerMessage(W_PacketHandler.class, W_PacketBase.class, 0, Side.SERVER);
    W_Network.INSTANCE.registerMessage(W_PacketHandler.class, W_PacketBase.class, 0, Side.CLIENT);
  }
  




  public static void handlePacket(EntityPlayer player, byte[] data) {}
  




  public static void registerGuiHandler(Object mod, IGuiHandler handler)
  {
    NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler);
  }
}
