package mcheli;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;



public class MCH_TEST_ModelBiped
  extends ModelBiped
{
  public MCH_TEST_ModelBiped()
  {
    this.bipedHeadwear.addChild(new MCH_TEST_ModelRenderer(this));
  }
}
