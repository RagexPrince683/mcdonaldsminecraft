package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;




public class MCH_PacketNotifyInfoReloaded
  extends MCH_Packet
{
  public int type;
  
  public MCH_PacketNotifyInfoReloaded()
  {
    this.type = -1;
  }
  
  public int getMessageID() { return 536875063; }
  


  public void readData(ByteArrayDataInput data)
  {
    try
    {
      this.type = data.readInt();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  

  public void writeData(DataOutputStream dos)
  {
    try
    {
      dos.writeInt(this.type);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void sendRealodAc()
  {
    MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
    s.type = 0;
    W_Network.sendToServer(s);
  }
  
  public static void sendRealodAllWeapon() {
    MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
    s.type = 1;
    W_Network.sendToServer(s);
  }
}
