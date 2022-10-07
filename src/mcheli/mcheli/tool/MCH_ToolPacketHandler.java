package mcheli.tool;

import mcheli.MCH_Config;
import mcheli.MCH_ConfigPrm;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.multiplay.MCH_PacketIndSpotEntity;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MCH_ToolPacketHandler
{
  public MCH_ToolPacketHandler() {}
  
  public static void onPacket_IndSpotEntity(EntityPlayer player, com.google.common.io.ByteArrayDataInput data)
  {
    if (player.worldObj.isRemote)
    {

      return;
    }
    
    MCH_PacketIndSpotEntity pc = new MCH_PacketIndSpotEntity();
    pc.readData(data);
    
    ItemStack itemStack = player.getHeldItem();
    if ((itemStack != null) && ((itemStack.getItem() instanceof mcheli.tool.rangefinder.MCH_ItemRangeFinder)))
    {

      if (pc.targetFilter == 0)
      {
        if (MCH_Multiplay.markPoint(player, player.posX, player.posY + player.getEyeHeight(), player.posZ))
        {
          W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
        }
        else
        {
          W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
        }
        
      }
      else if (itemStack.getItemDamage() < itemStack.getMaxDamage())
      {
        if (MCH_Config.RangeFinderConsume.prmBool)
        {
          itemStack.damageItem(1, player);
        }
        
        int time = (pc.targetFilter & 0xFC) == 0 ? 60 : MCH_Config.RangeFinderSpotTime.prmInt;
        if (MCH_Multiplay.spotEntity(player, null, player.posX, player.posY + player.getEyeHeight(), player.posZ, pc.targetFilter, MCH_Config.RangeFinderSpotDist.prmInt, time, 20.0F))
        {



          W_WorldFunc.MOD_playSoundAtEntity(player, "pi", 1.0F, 1.0F);
        }
        else
        {
          W_WorldFunc.MOD_playSoundAtEntity(player, "ng", 1.0F, 1.0F);
        }
      }
    }
  }
}
