package mcheli.aircraft;

import java.util.List;
import mcheli.MCH_PacketIndOpenScreen;
import mcheli.command.MCH_PacketCommandSave;
import mcheli.multiplay.MCH_PacketIndMultiplayCommand;
import mcheli.weapon.MCH_WeaponDummy;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponInfo.RoundItem;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_GuiContainer;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;




public class MCH_AircraftGui
  extends W_GuiContainer
{
  private final EntityPlayer thePlayer;
  private final MCH_EntityAircraft aircraft;
  private int scaleFactor;
  private GuiButton buttonReload;
  private GuiButton buttonNext;
  private GuiButton buttonPrev;
  private GuiButton buttonInventory;
  private int currentWeaponId;
  private int reloadWait;
  private GuiTextField editCommand;
  public static final int BUTTON_RELOAD = 1;
  public static final int BUTTON_NEXT = 2;
  public static final int BUTTON_PREV = 3;
  public static final int BUTTON_CLOSE = 4;
  public static final int BUTTON_CONFIG = 5;
  public static final int BUTTON_INVENTORY = 6;
  
  public MCH_AircraftGui(EntityPlayer player, MCH_EntityAircraft ac)
  {
    super(new MCH_AircraftGuiContainer(player, ac));
    this.aircraft = ac;
    this.thePlayer = player;
    this.xSize = 210;
    this.ySize = 236;
    this.buttonReload = null;
    this.currentWeaponId = 0;
  }
  
  public void initGui()
  {
    super.initGui();
    this.buttonList.clear();
    
    this.buttonReload = new GuiButton(1, this.guiLeft + 85, this.guiTop + 40, 50, 20, "Reload");
    this.buttonNext = new GuiButton(3, this.guiLeft + 140, this.guiTop + 40, 20, 20, "<<");
    this.buttonPrev = new GuiButton(2, this.guiLeft + 160, this.guiTop + 40, 20, 20, ">>");
    this.buttonReload.enabled = canReload(this.thePlayer);
    this.buttonNext.enabled = (this.aircraft.getWeaponNum() >= 2);
    this.buttonPrev.enabled = (this.aircraft.getWeaponNum() >= 2);
    
    this.buttonInventory = new GuiButton(6, this.guiLeft + 210 - 30 - 60, this.guiTop + 90, 80, 20, "Inventory");
    this.buttonList.add(new GuiButton(5, this.guiLeft + 210 - 30 - 60, this.guiTop + 110, 80, 20, "MOD Options"));
    this.buttonList.add(new GuiButton(4, this.guiLeft + 210 - 30 - 20, this.guiTop + 10, 40, 20, "Close"));
    
    this.buttonList.add(this.buttonReload);
    this.buttonList.add(this.buttonNext);
    this.buttonList.add(this.buttonPrev);
    
    if ((this.aircraft != null) && (this.aircraft.getSizeInventory() > 0))
    {
      this.buttonList.add(this.buttonInventory);
    }
    
    this.editCommand = new GuiTextField(this.fontRendererObj, this.guiLeft + 25, this.guiTop + 215, 160, 15);
    this.editCommand.setText(this.aircraft.getCommand());
    this.editCommand.setMaxStringLength(512);
    

    this.currentWeaponId = 0;
    
    this.reloadWait = 10;
  }
  
  public void closeScreen()
  {
    MCH_PacketCommandSave.send(this.editCommand.getText());
    this.mc.thePlayer.closeScreen();
  }
  
  public boolean canReload(EntityPlayer player)
  {
    return this.aircraft.canPlayerSupplyAmmo(player, this.currentWeaponId);
  }
  
  public void updateScreen()
  {
    super.updateScreen();
    if (this.reloadWait > 0)
    {
      this.reloadWait -= 1;
      if (this.reloadWait == 0)
      {
        this.buttonReload.enabled = canReload(this.thePlayer);
        this.reloadWait = 20;
      }
    }
    
    this.editCommand.updateCursorCounter();
  }
  

  protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
  {
    this.editCommand.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
  }
  
  public void onGuiClosed()
  {
    super.onGuiClosed();
  }
  
  protected void actionPerformed(GuiButton button)
  {
    super.actionPerformed(button);
    
    if (!button.enabled) { return;
    }
    switch (button.id)
    {
    case 4: 
      closeScreen();
      break;
    case 1: 
      this.buttonReload.enabled = canReload(this.thePlayer);
      if (this.buttonReload.enabled)
      {
        MCH_PacketIndReload.send(this.aircraft, this.currentWeaponId);
        this.aircraft.supplyAmmo(this.currentWeaponId);
        this.reloadWait = 3;
        this.buttonReload.enabled = false;
      }
      break;
    case 2: 
      this.currentWeaponId += 1;
      if (this.currentWeaponId >= this.aircraft.getWeaponNum())
      {
        this.currentWeaponId = 0;
      }
      this.buttonReload.enabled = canReload(this.thePlayer);
      break;
    case 3: 
      this.currentWeaponId -= 1;
      if (this.currentWeaponId < 0)
      {
        this.currentWeaponId = (this.aircraft.getWeaponNum() - 1);
      }
      this.buttonReload.enabled = canReload(this.thePlayer);
      break;
    case 5: 
      MCH_PacketIndOpenScreen.send(2);
      break;
    case 6: 
      MCH_PacketIndOpenScreen.send(3);
    }
    
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2)
  {
    super.drawGuiContainerForegroundLayer(par1, par2);
    MCH_EntityAircraft ac = this.aircraft;
    drawString(ac.getGuiInventory().getInventoryName(), 10, 10, 16777215);
    
    if (this.aircraft.getNumEjectionSeat() > 0)
    {
      drawString("Parachute", 9, 95, 16777215);
    }
    int itemPosX;
    if (this.aircraft.getWeaponNum() > 0)
    {
      MCH_WeaponSet ws = this.aircraft.getWeapon(this.currentWeaponId);
      if ((ws != null) && (!(ws.getFirstWeapon() instanceof MCH_WeaponDummy)))
      {
        drawString(ws.getName(), 79, 30, 16777215);
        
        int rest = ws.getRestAllAmmoNum() + ws.getAmmoNum();
        int color = rest == ws.getAllAmmoNum() ? 2675784 : rest == 0 ? 16711680 : 16777215;
        String s = String.format("%4d/%4d", new Object[] { Integer.valueOf(rest), Integer.valueOf(ws.getAllAmmoNum()) });
        drawString(s, 145, 70, color);
        
        itemPosX = 90;
        for (MCH_WeaponInfo.RoundItem r : ws.getInfo().roundItems)
        {
          drawString("" + r.num, itemPosX, 80, 16777215);
          itemPosX += 20;
        }
        
        itemPosX = 85;
        for (MCH_WeaponInfo.RoundItem r : ws.getInfo().roundItems)
        {
          drawItemStack(r.itemStack, itemPosX, 62);
          itemPosX += 20;
        }
      }
    }
    else
    {
      drawString("None", 79, 45, 16777215);
    }
  }
  
  protected void keyTyped(char c, int code)
  {
    if (code == 1)
    {
      closeScreen();
    }
    else if (code == 28)
    {
      String s = this.editCommand.getText().trim();
      if (s.startsWith("/"))
      {
        s = s.substring(1);
      }
      if (!s.isEmpty())
      {
        MCH_PacketIndMultiplayCommand.send(768, s);
      }
    }
    else
    {
      this.editCommand.textboxKeyTyped(c, code);
    }
  }
  
  protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
  {
    ScaledResolution scaledresolution = new W_ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    this.scaleFactor = scaledresolution.getScaleFactor();
    
    W_McClient.MOD_bindTexture("textures/gui/gui.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    
    for (int i = 0; i < this.aircraft.getNumEjectionSeat(); i++)
    {
      drawTexturedModalRect(x + 10 + 18 * i - 1, y + 105 - 1, 215, 55, 18, 18);
    }
    
    int ff = (int)(this.aircraft.getFuelP() * 50.0F);
    if (ff >= 99) ff = 100;
    drawTexturedModalRect(x + 57, y + 30 + 50 - ff, 215, 0, 12, ff);
    
    ff = (int)(this.aircraft.getFuelP() * 100.0F + 0.5D);
    int color = ff > 20 ? -14101432 : 16711680;
    drawString(String.format("%3d", new Object[] { Integer.valueOf(ff) }) + "%", x + 30, y + 65, color);
    
    this.editCommand.drawTextBox();
  }
}
