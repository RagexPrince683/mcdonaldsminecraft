package mcheli.block;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_MOD;
import mcheli.MCH_ModelManager;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_AircraftInfo.WeaponSet;
import mcheli.aircraft.MCH_AircraftInfoManager;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponInfoManager;
import mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class MCH_CurrentRecipe
{
  public final IRecipe recipe;
  public final int index;
  public final String displayName;
  public final List<ResourceLocation> descTexture;
  private final MCH_AircraftInfo acInfo;
  public List<String> infoItem;
  public List<String> infoData;
  private int descMaxPage;
  private int descPage;
  private W_ModelCustom model;
  public int modelRot;
  private ResourceLocation modelTexture;
  
  public MCH_CurrentRecipe(MCH_IRecipeList list, int idx)
  {
    if (list.getRecipeListSize() > 0)
    {
      this.recipe = list.getRecipe(idx);
    }
    else
    {
      this.recipe = null;
    }
    this.index = idx;
    this.displayName = (this.recipe != null ? this.recipe.getRecipeOutput().getDisplayName() : "None");
    this.descTexture = getDescTexture(this.recipe);
    this.descPage = 0;
    this.descMaxPage = this.descTexture.size();
    
    MCH_AircraftInfo info = null;
    if ((list instanceof MCH_AircraftInfoManager))
    {
      info = ((MCH_AircraftInfoManager)list).getAcInfoFromItem(this.recipe);
      if (info != null)
      {

        this.descMaxPage += 1;
        
        String dir = info.getDirectoryName();
        String name = info.name;
        this.model = MCH_ModelManager.get(dir, name);
        if (this.model != null)
        {
          this.modelTexture = new ResourceLocation("mcheli", "textures/" + dir + "/" + name + ".png");
          
          this.descMaxPage += 1;
          
          if ((list instanceof MCP_PlaneInfoManager))
          {
            this.modelRot = 0;
          }
          else
          {
            this.modelRot = 1;
          }
        }
      }
    }
    
    getAcInfoText(info);
    
    this.acInfo = info;
  }
  
  private void getAcInfoText(MCH_AircraftInfo info)
  {
    this.infoItem = new ArrayList();
    this.infoData = new ArrayList();
    if (info == null)
    {
      return;
    }
    
    getAcInfoTextSub("Name", info.getItemStack().getDisplayName());
    getAcInfoTextSub("HP", "" + info.maxHp);
    int seatNum = !info.isUAV ? info.getNumSeat() : info.getNumSeat() - 1;
    getAcInfoTextSub("Num of Seat", "" + seatNum);
    getAcInfoTextSub("GunnerMode", info.isEnableGunnerMode ? "YES" : "NO");
    getAcInfoTextSub("NightVision", info.isEnableNightVision ? "YES" : "NO");
    getAcInfoTextSub("Radar", info.isEnableEntityRadar ? "YES" : "NO");
    getAcInfoTextSub("Inventory", "" + info.inventorySize);
    

    if ((info instanceof MCP_PlaneInfo))
    {
      MCP_PlaneInfo pinfo = (MCP_PlaneInfo)info;
      getAcInfoTextSub("VTOL", pinfo.isEnableVtol ? "YES" : "NO");
    }
    
    if (info.getWeaponNum() > 0)
    {
      getAcInfoTextSub("Armed----------------");
      for (int i = 0; i < info.getWeaponNum(); i++)
      {
        String type = info.getWeaponSetById(i).type;
        MCH_WeaponInfo winfo = MCH_WeaponInfoManager.get(type);
        if (winfo != null)
        {
          getAcInfoTextSub(winfo.getWeaponTypeName(), winfo.displayName);
        }
        else
        {
          getAcInfoTextSub("ERROR", "Not found weapon " + (i + 1));
        }
      }
    }
  }
  
  private void getAcInfoTextSub(String item, String data) {
    this.infoItem.add(item + " :");
    this.infoData.add(data);
  }
  
  private void getAcInfoTextSub(String item) {
    this.infoItem.add(item);
    this.infoData.add("");
  }
  
  public void switchNextPage()
  {
    if (this.descMaxPage >= 2)
    {
      this.descPage = ((this.descPage + 1) % this.descMaxPage);
    }
    else
    {
      this.descPage = 0;
    }
  }
  
  public void switchPrevPage() {
    this.descPage -= 1;
    if ((this.descPage < 0) && (this.descMaxPage >= 2))
    {
      this.descPage = (this.descMaxPage - 1);
    }
    else
    {
      this.descPage = 0;
    }
  }
  
  public int getDescCurrentPage() {
    return this.descPage;
  }
  
  public void setDescCurrentPage(int page) {
    if (this.descMaxPage > 0)
    {
      this.descPage = (page < this.descMaxPage ? page : this.descMaxPage - 1);
    }
    else
    {
      this.descPage = 0;
    }
  }
  
  public int getDescMaxPage() {
    return this.descMaxPage;
  }
  
  public ResourceLocation getCurrentPageTexture()
  {
    if (this.descPage < this.descTexture.size())
    {
      return (ResourceLocation)this.descTexture.get(this.descPage);
    }
    return null;
  }
  
  public W_ModelCustom getModel()
  {
    return this.model;
  }
  
  public ResourceLocation getModelTexture() {
    return this.modelTexture;
  }
  
  public MCH_AircraftInfo getAcInfo()
  {
    return this.acInfo;
  }
  
  public boolean isCurrentPageTexture()
  {
    return (this.descPage >= 0) && (this.descPage < this.descTexture.size());
  }
  
  public boolean isCurrentPageModel() {
    if ((getAcInfo() != null) && (getModel() != null))
    {

      if (this.descPage == this.descTexture.size())
      {
        return true;
      }
    }
    return false;
  }
  
  public boolean isCurrentPageAcInfo() {
    if (getAcInfo() != null)
    {

      if (this.descPage == this.descMaxPage - 1)
      {
        return true;
      }
    }
    return false;
  }
  
  private List<ResourceLocation> getDescTexture(IRecipe r)
  {
    List<ResourceLocation> list = new ArrayList();
    if (r != null)
    {
      for (int i = 0; i < 20; i++)
      {
        String itemName = r.getRecipeOutput().getUnlocalizedName();
        if (itemName.startsWith("tile."))
        {
          itemName = itemName.substring(5);
        }
        if (itemName.indexOf(":") >= 0)
        {
          itemName = itemName.substring(itemName.indexOf(":") + 1);
        }
        itemName = "/textures/drafting_table_desc/" + itemName + "#" + i + ".png";
        File filePng = new File(MCH_MOD.sourcePath, "/assets/mcheli/" + itemName);
        if (filePng.exists())
        {
          list.add(new ResourceLocation("mcheli", itemName));
        }
      }
    }
    return list;
  }
}
