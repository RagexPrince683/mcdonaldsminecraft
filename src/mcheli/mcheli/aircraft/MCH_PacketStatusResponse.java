package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;



public class MCH_PacketStatusResponse
  extends MCH_Packet
{
  public int entityID_AC;
  public byte seatNum;
  public byte[] weaponIDs;
  
  public MCH_PacketStatusResponse()
  {
    this.entityID_AC = -1;
    this.seatNum = -1;
    this.weaponIDs = new byte[] { -1 };
  }
  
  public int getMessageID() { return 268439649; }
  


  public void readData(ByteArrayDataInput data)
  {
    try
    {
      this.entityID_AC = data.readInt();
      this.seatNum = data.readByte();
      if (this.seatNum > 0)
      {
        this.weaponIDs = new byte[this.seatNum];
        for (int i = 0; i < this.seatNum; i++)
        {
          this.weaponIDs[i] = data.readByte();
        }
      }
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
      dos.writeInt(this.entityID_AC);
      if ((this.seatNum > 0) && (this.weaponIDs != null) && (this.weaponIDs.length == this.seatNum))
      {
        dos.writeByte(this.seatNum);
        for (int i = 0; i < this.seatNum; i++)
        {
          dos.writeByte(this.weaponIDs[i]);
        }
      }
      else
      {
        dos.writeByte(-1);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void sendStatus(MCH_EntityAircraft ac, EntityPlayer player)
  {
    MCH_PacketStatusResponse s = new MCH_PacketStatusResponse();
    
    s.setParameter(ac);
    
    W_Network.sendToPlayer(s, player);
  }
  
  protected void setParameter(MCH_EntityAircraft ac) {
    if (ac == null) { return;
    }
    this.entityID_AC = W_Entity.getEntityId(ac);
    this.seatNum = ((byte)(ac.getSeatNum() + 1));
    if (this.seatNum > 0)
    {
      this.weaponIDs = new byte[this.seatNum];
      for (int i = 0; i < this.seatNum; i++)
      {
        this.weaponIDs[i] = ((byte)ac.getWeaponIDBySeatID(i));
      }
    }
    else
    {
      this.weaponIDs = new byte[] { -1 };
    }
  }
}
