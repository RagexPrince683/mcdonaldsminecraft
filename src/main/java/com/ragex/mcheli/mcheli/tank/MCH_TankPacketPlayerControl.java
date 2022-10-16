package mcheli.tank;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.aircraft.MCH_PacketPlayerControlBase;



public class MCH_TankPacketPlayerControl
  extends MCH_PacketPlayerControlBase
{
  public byte switchVtol;
  
  public MCH_TankPacketPlayerControl()
  {
    this.switchVtol = -1;
  }
  
  public int getMessageID() { return 537919504; }
  


  public void readData(ByteArrayDataInput data)
  {
    super.readData(data);
    try
    {
      this.switchVtol = data.readByte();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  


  public void writeData(DataOutputStream dos)
  {
    super.writeData(dos);
    try
    {
      dos.writeByte(this.switchVtol);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
