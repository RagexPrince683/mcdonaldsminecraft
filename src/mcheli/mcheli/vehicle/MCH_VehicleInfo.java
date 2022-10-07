package mcheli.vehicle;

import java.util.ArrayList;
import java.util.List;
import mcheli.aircraft.MCH_AircraftInfo;

public class MCH_VehicleInfo extends MCH_AircraftInfo
{
  public MCH_ItemVehicle item;
  public boolean isEnableMove;
  public boolean isEnableRot;
  public List<VPart> partList;
  
  public class VPart extends mcheli.aircraft.MCH_AircraftInfo.DrawnPart
  {
    public final boolean rotPitch;
    public final boolean rotYaw;
    public final int type;
    public List<VPart> child;
    public final boolean drawFP;
    public final float recoilBuf;
    
    public VPart(float x, float y, float z, String model, boolean drawfp, boolean roty, boolean rotp, int type, float rb)
    {
      super(x, y, z, 0.0F, 0.0F, 0.0F, model);
      this.rotYaw = roty;
      this.rotPitch = rotp;
      this.type = type;
      this.child = null;
      this.drawFP = drawfp;
      this.recoilBuf = rb;
    }
  }
  





  public float getMinRotationPitch() { return -90.0F; }
  public float getMaxRotationPitch() { return 90.0F; }
  
  public net.minecraft.item.Item getItem() { return this.item; }
  
  public MCH_VehicleInfo(String name)
  {
    super(name);
    this.item = null;
    this.isEnableMove = false;
    this.isEnableRot = false;
    this.partList = new ArrayList();
  }
  
  public boolean isValidData() throws Exception {
    return super.isValidData();
  }
  
  public String getDefaultHudName(int seatId) {
    return "vehicle";
  }
  
  public void loadItemData(String item, String data)
  {
    super.loadItemData(item, data);
    
    if (item.compareTo("canmove") == 0)
    {
      this.isEnableMove = toBool(data);
    }
    else if (item.compareTo("canrotation") == 0)
    {
      this.isEnableRot = toBool(data);
    }
    else if (item.compareTo("rotationpitchmin") == 0)
    {

      super.loadItemData("minrotationpitch", data);
    }
    else if (item.compareTo("rotationpitchmax") == 0)
    {

      super.loadItemData("maxrotationpitch", data);
    }
    else if (item.compareTo("addpart") == 0)
    {
      String[] s = data.split("\\s*,\\s*");
      if (s.length >= 7)
      {
        float rb = s.length >= 8 ? toFloat(s[7]) : 0.0F;
        VPart n = new VPart(toFloat(s[4]), toFloat(s[5]), toFloat(s[6]), "part" + this.partList.size(), toBool(s[0]), toBool(s[1]), toBool(s[2]), toInt(s[3]), rb);
        






        this.partList.add(n);
      }
    }
    else if (item.compareTo("addchildpart") == 0)
    {
      if (this.partList.size() > 0)
      {
        String[] s = data.split("\\s*,\\s*");
        if (s.length >= 7)
        {
          float rb = s.length >= 8 ? toFloat(s[7]) : 0.0F;
          VPart p = (VPart)this.partList.get(this.partList.size() - 1);
          if (p.child == null) p.child = new ArrayList();
          VPart n = new VPart(toFloat(s[4]), toFloat(s[5]), toFloat(s[6]), p.modelName + "_" + p.child.size(), toBool(s[0]), toBool(s[1]), toBool(s[2]), toInt(s[3]), rb);
          






          p.child.add(n);
        }
      }
    }
  }
  

  public String getDirectoryName()
  {
    return "vehicles";
  }
  

  public String getKindName()
  {
    return "vehicle";
  }
  
  public void preReload()
  {
    super.preReload();
    this.partList.clear();
  }
  
  public void postReload() {
    mcheli.MCH_MOD.proxy.registerModelsVehicle(this.name, true);
  }
}
