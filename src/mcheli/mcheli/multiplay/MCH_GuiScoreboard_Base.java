package mcheli.multiplay;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

public abstract class MCH_GuiScoreboard_Base extends mcheli.wrapper.W_GuiContainer {
	public List<net.minecraft.client.gui.Gui> listGui;
	public static final int BUTTON_ID_SHUFFLE = 256;
	public static final int BUTTON_ID_CREATE_TEAM = 512;
	public static final int BUTTON_ID_CREATE_TEAM_OK = 528;
	public static final int BUTTON_ID_CREATE_TEAM_CANCEL = 544;
	public static final int BUTTON_ID_CREATE_TEAM_FF = 560;
	public static final int BUTTON_ID_CREATE_TEAM_NEXT_C = 576;
	public static final int BUTTON_ID_CREATE_TEAM_PREV_C = 577;
	public static final int BUTTON_ID_JUMP_SPAWN_POINT = 768;
	public static final int BUTTON_ID_SWITCH_PVP = 1024;
	public static final int BUTTON_ID_DESTORY_ALL = 1280;
	private MCH_IGuiScoreboard screen_switcher;

	protected static enum SCREEN_ID {
		MAIN, CREATE_TEAM;

		private SCREEN_ID() {
		}
	}

	public MCH_GuiScoreboard_Base(MCH_IGuiScoreboard switcher, net.minecraft.entity.player.EntityPlayer player) {
		super(new MCH_ContainerScoreboard(player));
		this.screen_switcher = switcher;
		this.mc = Minecraft.getMinecraft();
	}

	public void initGui() {
	}

	public void initGui(List buttonList, net.minecraft.client.gui.GuiScreen parents) {
		this.listGui = new ArrayList();

		this.mc = Minecraft.getMinecraft();
		this.fontRendererObj = this.mc.fontRenderer;
		this.width = parents.width;
		this.height = parents.height;
		initGui();

		for (net.minecraft.client.gui.Gui b : this.listGui) {
			if ((b instanceof GuiButton))
				buttonList.add(b);
		}
		this.buttonList.clear();
	}

	public static void setVisible(Object g, boolean v) {
		if ((g instanceof GuiButton))
			((GuiButton) g).visible = v;
		if ((g instanceof net.minecraft.client.gui.GuiTextField)) {
			((net.minecraft.client.gui.GuiTextField) g).setVisible(v);
		}
	}

	public void updateScreenButtons(List list) {
	}

	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
	}

	public int getTeamNum() {
		return this.mc.theWorld.getScoreboard().getTeams().size();
	}

	protected void acviveScreen() {
	}

	public void onSwitchScreen() {
		for (Object b : this.listGui) {
			setVisible(b, true);
		}
		acviveScreen();
	}

	public void leaveScreen() {
		for (Object b : this.listGui) {
			setVisible(b, false);
		}
	}

	public void keyTypedScreen(char c, int code) {
		keyTyped(c, code);
	}

	public void mouseClickedScreen(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		try {
			mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		} catch (Exception e) {
			if (p_73864_3_ == 0) {
				for (int l = 0; l < this.buttonList.size(); l++) {
					GuiButton guibutton = (GuiButton) this.buttonList.get(l);

					if (guibutton.mousePressed(this.mc, p_73864_1_, p_73864_2_)) {
						guibutton.func_146113_a(this.mc.getSoundHandler());
						actionPerformed(guibutton);
					}
				}
			}
		}
	}

	public void drawGuiContainerForegroundLayerScreen(int param1, int param2) {
		drawGuiContainerForegroundLayer(param1, param2);
	}

	protected void actionPerformedScreen(GuiButton btn) {
		actionPerformed(btn);
	}

	public void switchScreen(SCREEN_ID id) {
		this.screen_switcher.switchScreen(id);
	}

	public static int getScoreboradWidth(Minecraft mc) {
		ScaledResolution scaledresolution = new mcheli.wrapper.W_ScaledResolution(mc, mc.displayWidth,
				mc.displayHeight);
		int ScaledWidth = scaledresolution.getScaledWidth() - 40;
		int width = ScaledWidth * 3 / 4 / (mc.theWorld.getScoreboard().getTeams().size() + 1);
		if (width > 150) {
			width = 150;
		}
		return width;
	}

	public static int getScoreBoardLeft(Minecraft mc, int teamNum, int teamIndex) {
		ScaledResolution scaledresolution = new mcheli.wrapper.W_ScaledResolution(mc, mc.displayWidth,
				mc.displayHeight);
		int ScaledWidth = scaledresolution.getScaledWidth();
		return (int) (ScaledWidth / 2 + (getScoreboradWidth(mc) + 10) * (-teamNum / 2.0D + teamIndex));
	}

	public static void drawList(Minecraft mc, FontRenderer fontRendererObj, boolean mng) {
		ArrayList<ScorePlayerTeam> teamList = new ArrayList();
		teamList.add(null);
		for (Object team : mc.theWorld.getScoreboard().getTeams()) {
			teamList.add((ScorePlayerTeam) team);
		}
		java.util.Collections.sort(teamList, new java.util.Comparator<ScorePlayerTeam>() {
			public int compare(ScorePlayerTeam o1, ScorePlayerTeam o2) {
				if ((o1 == null) && (o2 == null))
					return 0;
				if (o1 == null)
					return -1;
				if (o2 == null)
					return 1;
				return o1.getRegisteredName().compareTo(o2.getRegisteredName());
			}
		});
		for (int i = 0; i < teamList.size(); i++) {
			if (mng)
				drawPlayersList(mc, fontRendererObj, (ScorePlayerTeam) teamList.get(i), 1 + i, 1 + teamList.size());
			else {
				drawPlayersList(mc, fontRendererObj, (ScorePlayerTeam) teamList.get(i), i, teamList.size());
			}
		}
	}

	public static void drawPlayersList(Minecraft mc, FontRenderer fontRendererObj, ScorePlayerTeam team, int teamIndex,
			int teamNum) {
		ScaledResolution scaledresolution = new mcheli.wrapper.W_ScaledResolution(mc, mc.displayWidth,
				mc.displayHeight);
		int ScaledWidth = scaledresolution.getScaledWidth();
		int ScaledHeight = scaledresolution.getScaledHeight();
		net.minecraft.scoreboard.ScoreObjective scoreobjective = mc.theWorld.getScoreboard().func_96539_a(0);

		net.minecraft.client.network.NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		List list = nethandlerplayclient.playerInfoList;
		int MaxPlayers = (list.size() / 5 + 1) * 5;
		MaxPlayers = MaxPlayers < 10 ? 10 : MaxPlayers;
		if (MaxPlayers > nethandlerplayclient.currentServerMaxPlayers) {
			MaxPlayers = nethandlerplayclient.currentServerMaxPlayers;
		}

		int width = getScoreboradWidth(mc);

		int listLeft = getScoreBoardLeft(mc, teamNum, teamIndex);
		int listTop = ScaledHeight / 2 - (MaxPlayers * 9 + 10) / 2;
		drawRect(listLeft - 1, listTop - 1 - 18, listLeft + width, listTop + 9 * MaxPlayers, Integer.MIN_VALUE);

		String teamName = ScorePlayerTeam.formatPlayerName(team, team == null ? "No team" : team.getRegisteredName());
		int teamNameX = listLeft + width / 2 - fontRendererObj.getStringWidth(teamName) / 2;
		fontRendererObj.drawStringWithShadow(teamName, teamNameX, listTop - 18, -1);

		String ff_onoff = "FriendlyFire : " + (team.getAllowFriendlyFire() ? "ON" : team == null ? "ON" : "OFF");
		int ff_onoffX = listLeft + width / 2 - fontRendererObj.getStringWidth(ff_onoff) / 2;
		fontRendererObj.drawStringWithShadow(ff_onoff, ff_onoffX, listTop - 9, -1);

		int drawY = 0;
		for (int i = 0; i < MaxPlayers; i++) {
			int x = listLeft;
			int y = listTop + drawY * 9;
			int rectY = listTop + i * 9;
			drawRect(x, rectY, x + width - 1, rectY + 8, 553648127);
			org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			org.lwjgl.opengl.GL11.glEnable(3008);

			if (i < list.size()) {

				GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) list.get(i);

				String playerName = guiplayerinfo.name;
				ScorePlayerTeam steam = mc.theWorld.getScoreboard().getPlayersTeam(playerName);
				if (((steam == null) && (team == null))
						|| ((steam != null) && (team != null) && (steam.isSameTeam(team)))) {

					drawY++;

					fontRendererObj.drawStringWithShadow(playerName, x, y, -1);

					if (scoreobjective != null) {
						int j4 = x + fontRendererObj.getStringWidth(playerName) + 5;
						int k4 = x + width - 12 - 5;

						if (k4 - j4 > 5) {
							net.minecraft.scoreboard.Score score = scoreobjective.getScoreboard()
									.func_96529_a(guiplayerinfo.name, scoreobjective);
							String s1 = net.minecraft.util.EnumChatFormatting.YELLOW + "" + score.getScorePoints();
							fontRendererObj.drawStringWithShadow(s1, k4 - fontRendererObj.getStringWidth(s1), y,
									16777215);
						}
					}

					drawResponseTime(x + width - 12, y, guiplayerinfo.responseTime);
				}
			}
		}
	}

	public static void drawResponseTime(int x, int y, int responseTime) {
		org.lwjgl.opengl.GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(icons);
		byte b2;
		if (responseTime < 0) {
			b2 = 5;
		} else {
			if (responseTime < 150) {
				b2 = 0;
			} else {
				if (responseTime < 300) {
					b2 = 1;
				} else {
					if (responseTime < 600) {
						b2 = 2;
					} else {
						if (responseTime < 1000) {
							b2 = 3;
						} else {
							b2 = 4;
						}
					}
				}
			}
		}
		static_drawTexturedModalRect(x, y, 0, 176 + b2 * 8, 10, 8, 0.0D);
	}

	public static void static_drawTexturedModalRect(int x, int y, int x2, int y2, int x3, int y3, double zLevel) {
		float f = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + y3, zLevel, (x2 + 0) * 0.00390625F, (y2 + y3) * 0.00390625F);
		tessellator.addVertexWithUV(x + x3, y + y3, zLevel, (x2 + x3) * 0.00390625F, (y2 + y3) * 0.00390625F);
		tessellator.addVertexWithUV(x + x3, y + 0, zLevel, (x2 + x3) * 0.00390625F, (y2 + 0) * 0.00390625F);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (x2 + 0) * 0.00390625F, (y2 + 0) * 0.00390625F);
		tessellator.draw();
	}
}
