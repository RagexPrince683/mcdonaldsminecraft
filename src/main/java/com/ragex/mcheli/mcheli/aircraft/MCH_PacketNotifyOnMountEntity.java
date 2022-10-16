package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketNotifyOnMountEntity
  extends MCH_Packet
{
  public int entityID_Ac;
  public int entityID_rider;
  public int seatID;
  
  public MCH_PacketNotifyOnMountEntity()
  {
    this.entityID_Ac = -1;
    this.entityID_rider = -1;
    this.seatID = -1;
  }
  
  public int getMessageID() { return 268439632; }
  


  public void readData(ByteArrayDataInput data)
  {
    try
    {
      this.entityID_Ac = data.readInt();
      this.entityID_rider = data.readInt();
      this.seatID = data.readByte();
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
      dos.writeInt(this.entityID_Ac);
      dos.writeInt(this.entityID_rider);
      dos.writeByte(this.seatID);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void send(MCH_EntityAircraft ac, Entity rider, int seatId)
  {
    if ((ac == null) || (rider == null)) return;
    Entity pilot = ac.getRiddenByEntity();
    if ((!(pilot instanceof EntityPlayer)) || (pilot.isDead)) { return;
    }
    MCH_PacketNotifyOnMountEntity s = new MCH_PacketNotifyOnMountEntity();
    
    s.entityID_Ac = W_Entity.getEntityId(ac);
    s.entityID_rider = W_Entity.getEntityId(rider);
    s.seatID = seatId;
    
    W_Network.sendToPlayer(s, (EntityPlayer)pilot);
  }
}
