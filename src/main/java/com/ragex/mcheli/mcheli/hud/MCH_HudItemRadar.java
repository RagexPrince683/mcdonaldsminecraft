package mcheli.hud;

import java.util.ArrayList;
import mcheli.MCH_Lib;
import mcheli.MCH_Vector2;

public class MCH_HudItemRadar
  extends MCH_HudItem
{
  private final String rot;
  private final String left;
  private final String top;
  private final String width;
  private final String height;
  private final boolean isEntityRadar;
  
  public MCH_HudItemRadar(int fileLine, boolean isEntityRadar, String rot, String left, String top, String width, String height)
  {
    super(fileLine);
    this.isEntityRadar = isEntityRadar;
    this.rot = toFormula(rot);
    this.left = toFormula(left);
    this.top = toFormula(top);
    this.width = toFormula(width);
    this.height = toFormula(height);
  }
  

  public void execute()
  {
    if (this.isEntityRadar)
    {
      if ((EntityList != null) && (EntityList.size() > 0))
      {
        drawEntityList(EntityList, (float)calc(this.rot), centerX + calc(this.left), centerY + calc(this.top), calc(this.width), calc(this.height));

      }
      

    }
    else if ((EnemyList != null) && (EnemyList.size() > 0))
    {
      drawEntityList(EnemyList, (float)calc(this.rot), centerX + calc(this.left), centerY + calc(this.top), calc(this.width), calc(this.height));
    }
  }
  


  protected void drawEntityList(ArrayList<MCH_Vector2> src, float r, double left, double top, double w, double h)
  {
    double w1 = -w / 2.0D;
    double w2 = w / 2.0D;
    double h1 = -h / 2.0D;
    double h2 = h / 2.0D;
    double w_factor = w / 64.0D;
    double h_factor = h / 64.0D;
    double[] list = new double[src.size() * 2];
    int idx = 0;
    for (MCH_Vector2 v : src)
    {
      list[(idx + 0)] = (v.x / 2.0D * w_factor);
      list[(idx + 1)] = (v.y / 2.0D * h_factor);
      idx += 2;
    }
    MCH_Lib.rotatePoints(list, r);
    ArrayList<Double> drawList = new ArrayList();
    for (int i = 0; i + 1 < list.length; i += 2)
    {
      if ((list[(i + 0)] > w1) && (list[(i + 0)] < w2) && (list[(i + 1)] > h1) && (list[(i + 1)] < h2))
      {
        drawList.add(Double.valueOf(list[(i + 0)] + left + w / 2.0D));
        drawList.add(Double.valueOf(list[(i + 1)] + top + h / 2.0D));
      }
    }
    drawPoints(drawList, colorSetting, scaleFactor * 2);
  }
}
