package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;

public class MCH_PacketSeatPlayerControl
  extends MCH_Packet
{
  public boolean isUnmount;
  public byte switchSeat;
  public boolean parachuting;
  
  public MCH_PacketSeatPlayerControl()
  {
    this.isUnmount = false;
    this.switchSeat = 0;
  }
  
  public int getMessageID() { return 536875040; }
  


  public void readData(ByteArrayDataInput data)
  {
    try
    {
      byte bf = data.readByte();
      this.isUnmount = ((bf >> 3 & 0x1) != 0);
      this.switchSeat = ((byte)(bf >> 1 & 0x3));
      this.parachuting = ((bf >> 0 & 0x1) != 0);
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
      byte bf = (byte)((this.isUnmount ? 8 : 0) | this.switchSeat << 1 | (this.parachuting ? 1 : 0));
      


      dos.writeByte(bf);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
