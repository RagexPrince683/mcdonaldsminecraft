package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;

public class MCH_PacketNotifyWeaponID
  extends MCH_Packet
{
  public int entityID_Ac;
  public int seatID;
  public int weaponID;
  public short ammo;
  public short restAmmo;
  
  public MCH_PacketNotifyWeaponID()
  {
    this.entityID_Ac = -1;
    this.seatID = -1;
    this.weaponID = -1;
    this.ammo = 0;
    this.restAmmo = 0;
  }
  
  public int getMessageID() { return 268439601; }
  


  public void readData(ByteArrayDataInput data)
  {
    try
    {
      this.entityID_Ac = data.readInt();
      this.seatID = data.readByte();
      this.weaponID = data.readByte();
      this.ammo = data.readShort();
      this.restAmmo = data.readShort();
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
      dos.writeByte(this.seatID);
      dos.writeByte(this.weaponID);
      dos.writeShort(this.ammo);
      dos.writeShort(this.restAmmo);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void send(Entity sender, int sid, int wid, int ammo, int rest_ammo)
  {
    MCH_PacketNotifyWeaponID s = new MCH_PacketNotifyWeaponID();
    
    s.entityID_Ac = W_Entity.getEntityId(sender);
    s.seatID = sid;
    s.weaponID = wid;
    s.ammo = ((short)ammo);
    s.restAmmo = ((short)rest_ammo);
    
    W_Network.sendToAllAround(s, sender, 150.0D);
  }
}
