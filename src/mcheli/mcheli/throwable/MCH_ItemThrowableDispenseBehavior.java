package mcheli.throwable;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MCH_ItemThrowableDispenseBehavior extends net.minecraft.dispenser.BehaviorDefaultDispenseItem
{
  public MCH_ItemThrowableDispenseBehavior() {}
  
  public ItemStack dispenseStack(IBlockSource bs, ItemStack itemStack)
  {
    EnumFacing enumfacing = mcheli.wrapper.W_BlockDispenser.getFacing(bs.getBlockMetadata());
    double x = bs.getX() + enumfacing.getFrontOffsetX() * 2.0D;
    double y = bs.getY() + enumfacing.getFrontOffsetY() * 2.0D;
    double z = bs.getZ() + enumfacing.getFrontOffsetZ() * 2.0D;
    
    if ((itemStack.getItem() instanceof MCH_ItemThrowable))
    {
      MCH_ThrowableInfo info = MCH_ThrowableInfoManager.get(itemStack.getItem());
      
      if (info != null)
      {
        bs.getWorld().playSound(x, y, z, "random.bow", 0.5F, 0.4F / (bs.getWorld().rand.nextFloat() * 0.4F + 0.8F), false);
        
        if (!bs.getWorld().isRemote)
        {
          mcheli.MCH_Lib.DbgLog(bs.getWorld(), "MCH_ItemThrowableDispenseBehavior.dispenseStack(%s)", new Object[] { info.name });
          
          MCH_EntityThrowable entity = new MCH_EntityThrowable(bs.getWorld(), x, y, z);
          entity.motionX = (enumfacing.getFrontOffsetX() * info.dispenseAcceleration);
          entity.motionY = (enumfacing.getFrontOffsetY() * info.dispenseAcceleration);
          entity.motionZ = (enumfacing.getFrontOffsetZ() * info.dispenseAcceleration);
          entity.setInfo(info);
          bs.getWorld().spawnEntityInWorld(entity);
          
          itemStack.splitStack(1);
        }
      }
    }
    
    return itemStack;
  }
}
