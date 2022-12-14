package mcheli.mob;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.gui.MCH_Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;















@SideOnly(Side.CLIENT)
public class MCH_GuiSpawnGunner
  extends MCH_Gui
{
  public MCH_GuiSpawnGunner(Minecraft minecraft)
  {
    super(minecraft);
  }
  

  public void initGui()
  {
    super.initGui();
  }
  

  public boolean doesGuiPauseGame()
  {
    return false;
  }
  
  public boolean isDrawGui(EntityPlayer player)
  {
    return (player != null) && (player.worldObj != null) && (player.getCurrentEquippedItem() != null) && ((player.getCurrentEquippedItem().getItem() instanceof MCH_ItemSpawnGunner));
  }
  



  public void drawGui(EntityPlayer player, boolean isThirdPersonView)
  {
    if (isThirdPersonView) { return;
    }
    if (!isDrawGui(player)) { return;
    }
    GL11.glLineWidth(scaleFactor);
    GL11.glDisable(3042);
    
    draw(player, searchTarget(player));
  }
  
  private Entity searchTarget(EntityPlayer player) {
    float f = 1.0F;
    float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
    float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
    double dx = player.prevPosX + (player.posX - player.prevPosX) * f;
    double dy = player.prevPosY + (player.posY - player.prevPosY) * f + 1.62D - player.yOffset;
    double dz = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
    Vec3 vec3 = Vec3.createVectorHelper(dx, dy, dz);
    float f3 = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float f4 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float f5 = -MathHelper.cos(-pitch * 0.017453292F);
    float f6 = MathHelper.sin(-pitch * 0.017453292F);
    float f7 = f4 * f5;
    float f8 = f3 * f5;
    double d3 = 5.0D;
    Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
    Entity target = null;
    
    List list = player.worldObj.getEntitiesWithinAABB(MCH_EntityGunner.class, player.boundingBox.expand(5.0D, 5.0D, 5.0D));
    for (int i = 0; i < list.size(); i++)
    {
      MCH_EntityGunner gunner = (MCH_EntityGunner)list.get(i);
      if (gunner.boundingBox.calculateIntercept(vec3, vec31) != null)
      {
        if ((target == null) || (player.getDistanceSqToEntity(gunner) < player.getDistanceSqToEntity(target)))
        {
          target = gunner;
        }
      }
    }
    if (target != null)
    {
      return target;
    }
    
    MCH_ItemSpawnGunner item = (MCH_ItemSpawnGunner)player.getCurrentEquippedItem().getItem();
    
    if ((item.targetType == 1) && (!player.worldObj.isRemote) && (player.getTeam() == null))
    {
      return null;
    }
    
    list = player.worldObj.getEntitiesWithinAABB(MCH_EntitySeat.class, player.boundingBox.expand(5.0D, 5.0D, 5.0D));
    for (int i = 0; i < list.size(); i++)
    {
      MCH_EntitySeat seat = (MCH_EntitySeat)list.get(i);
      if ((seat.getParent() != null) && (seat.getParent().getAcInfo() != null) && (seat.boundingBox.calculateIntercept(vec3, vec31) != null))
      {



        if ((target == null) || (player.getDistanceSqToEntity(seat) < player.getDistanceSqToEntity(target)))
        {
          if ((seat.riddenByEntity instanceof MCH_EntityGunner))
          {
            target = seat.riddenByEntity;
          }
          else
          {
            target = seat;
          }
        }
      }
    }
    if (target == null)
    {
      list = player.worldObj.getEntitiesWithinAABB(MCH_EntityAircraft.class, player.boundingBox.expand(5.0D, 5.0D, 5.0D));
      for (int i = 0; i < list.size(); i++)
      {
        MCH_EntityAircraft ac = (MCH_EntityAircraft)list.get(i);
        if ((!ac.isUAV()) && (ac.getAcInfo() != null) && (ac.boundingBox.calculateIntercept(vec3, vec31) != null))
        {



          if ((target == null) || (player.getDistanceSqToEntity(ac) < player.getDistanceSqToEntity(target)))
          {
            if ((ac.getRiddenByEntity() instanceof MCH_EntityGunner))
            {
              target = ac.getRiddenByEntity();
            }
            else
            {
              target = ac;
            }
          }
        }
      }
    }
    
    return target;
  }
  
  void draw(EntityPlayer player, Entity entity)
  {
    if (entity == null) { return;
    }
    GL11.glEnable(3042);
    GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
    int srcBlend = GL11.glGetInteger(3041);
    int dstBlend = GL11.glGetInteger(3040);
    GL11.glBlendFunc(770, 771);
    
    double size = 512.0D;
    while ((size < this.width) || (size < this.height)) { size *= 2.0D;
    }
    GL11.glBlendFunc(srcBlend, dstBlend);
    GL11.glDisable(3042);
    
    double factor = size / 512.0D;
    double SCALE_FACTOR = scaleFactor * factor;
    double CX = this.mc.displayWidth / 2;
    double CY = this.mc.displayHeight / 2;
    

    double px = (CX - 0.0D) / SCALE_FACTOR;
    double py = (CY + 0.0D) / SCALE_FACTOR;
    
    GL11.glPushMatrix();
    

    if ((entity instanceof MCH_EntityGunner))
    {
      MCH_EntityGunner gunner = (MCH_EntityGunner)entity;
      String seatName = "";
      if ((gunner.ridingEntity instanceof MCH_EntitySeat))
      {
        seatName = "(seat " + (((MCH_EntitySeat)gunner.ridingEntity).seatID + 2) + ")";
      }
      else if ((gunner.ridingEntity instanceof MCH_EntityAircraft))
      {
        seatName = "(seat 1)";
      }
      drawCenteredString(gunner.getTeamName() + " Gunner " + seatName, (int)px, (int)py + 20, -8355840);
      int S = 10;
      drawLine(new double[] { px - S, py - S, px + S, py - S, px + S, py + S, px - S, py + S }, -8355840, 2);
    }
    else if ((entity instanceof MCH_EntitySeat))
    {
      MCH_EntitySeat seat = (MCH_EntitySeat)entity;
      if (seat.riddenByEntity == null)
      {
        drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -16711681);
        int S = 10;
        drawLine(new double[] { px - S, py - S, px + S, py - S, px + S, py + S, px - S, py + S }, -16711681, 2);
      }
      else
      {
        drawCenteredString("seat " + (seat.seatID + 2), (int)px, (int)py + 20, -65536);
        int S = 10;
        drawLine(new double[] { px - S, py - S, px + S, py - S, px + S, py + S, px - S, py + S }, -65536, 2);
        drawLine(new double[] { px - S, py - S, px + S, py + S }, -65536);
        drawLine(new double[] { px + S, py - S, px - S, py + S }, -65536);
      }
    }
    else if ((entity instanceof MCH_EntityAircraft))
    {
      MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
      if (ac.getRiddenByEntity() == null)
      {
        drawCenteredString("seat 1", (int)px, (int)py + 20, -16711681);
        int S = 10;
        drawLine(new double[] { px - S, py - S, px + S, py - S, px + S, py + S, px - S, py + S }, -16711681, 2);
      }
      else
      {
        drawCenteredString("seat 1", (int)px, (int)py + 20, -65536);
        int S = 10;
        drawLine(new double[] { px - S, py - S, px + S, py - S, px + S, py + S, px - S, py + S }, -65536, 2);
        drawLine(new double[] { px - S, py - S, px + S, py + S }, -65536);
        drawLine(new double[] { px + S, py - S, px - S, py + S }, -65536);
      }
    }
    
    GL11.glPopMatrix();
  }
}
