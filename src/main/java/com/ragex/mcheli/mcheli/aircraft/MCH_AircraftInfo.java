package mcheli.aircraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import mcheli.MCH_BaseInfo;
import mcheli.MCH_CommonProxy;
import mcheli.MCH_MOD;
import mcheli.hud.MCH_Hud;
import mcheli.hud.MCH_HudManager;
import mcheli.weapon.MCH_WeaponInfoManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.IModelCustom;

public abstract class MCH_AircraftInfo extends MCH_BaseInfo {
	public final String name;
	public String displayName;
	public HashMap<String, String> displayNameLang;
	public int itemID;
	public List<String> recipeString;
	public List<IRecipe> recipe;
	public boolean isShapedRecipe;
	public String category;
	public boolean creativeOnly;
	public boolean invulnerable;
	public boolean isEnableGunnerMode;
	public int cameraZoom;
	public boolean isEnableConcurrentGunnerMode;
	public boolean isEnableNightVision;
	public boolean isEnableEntityRadar;
	public boolean isEnableEjectionSeat;
	public boolean isEnableParachuting;
	public Flare flare;
	public float bodyHeight;
	public float bodyWidth;
	public boolean isFloat;
	public float floatOffset;
	public float gravity;
	public float gravityInWater;
	public int maxHp;
	public float armorMinDamage;
	public float armorMaxDamage;
	public float armorDamageFactor;
	public boolean enableBack;
	public int inventorySize;
	public boolean isUAV;
	public boolean isSmallUAV;
	public boolean isTargetDrone;
	public float autoPilotRot;
	public float onGroundPitch;
	public boolean canMoveOnGround;
	public boolean canRotOnGround;
	public List<WeaponSet> weaponSetList;
	public List<MCH_SeatInfo> seatList;
	public List<Integer[]> exclusionSeatList;
	public List<MCH_Hud> hudList;
	public MCH_Hud hudTvMissile;
	public float damageFactor;
	public float submergedDamageHeight;
	public boolean regeneration;
	public List<MCH_BoundingBox> extraBoundingBox;
	public List<Wheel> wheels;
	public int maxFuel;
	public float fuelConsumption;
	public float fuelSupplyRange;
	public float ammoSupplyRange;
	public float repairOtherVehiclesRange;
	public int repairOtherVehiclesValue;
	public float stealth;
	public boolean canRide;
	public float entityWidth;
	public float entityHeight;
	public float entityPitch;
	public float entityRoll;
	public float stepHeight;
	public List<MCH_SeatRackInfo> entityRackList;
	public int mobSeatNum;
	public int entityRackNum;
	public MCH_MobDropOption mobDropOption;
	public List<RepellingHook> repellingHooks;
	public List<RideRack> rideRacks;
	public List<ParticleSplash> particleSplashs;
	public List<SearchLight> searchLights;
	public float rotorSpeed;
	public boolean enableSeaSurfaceParticle;
	public float pivotTurnThrottle;
	public float trackRollerRot;
	public float partWheelRot;
	public float onGroundPitchFactor;
	public float onGroundRollFactor;
	public Vec3 turretPosition;
	public boolean defaultFreelook;
	public Vec3 unmountPosition;
	public float thirdPersonDist;
	public float markerWidth;
	public float markerHeight;
	public float bbZmin;
	public float bbZmax;
	public float bbZ;
	public boolean alwaysCameraView;
	public List<CameraPosition> cameraPosition;
	public float cameraRotationSpeed;
	public float speed;
	public float motionFactor;
	public float mobilityYaw;
	public float mobilityPitch;
	public float mobilityRoll;
	public float mobilityYawOnGround;
	public float minRotationPitch;
	public float maxRotationPitch;
	public float minRotationRoll;
	public float maxRotationRoll;
	public boolean limitRotation;
	public float throttleUpDown;
	public float throttleUpDownOnEntity;
	private List<String> textureNameList;
	public int textureCount;
	public float particlesScale;
	public boolean hideEntity;
	public boolean smoothShading;
	public String soundMove;
	public float soundRange;
	public float soundVolume;
	public float soundPitch;
	public IModelCustom model;
	public List<Hatch> hatchList;
	public List<Camera> cameraList;
	public List<PartWeapon> partWeapon;
	public List<WeaponBay> partWeaponBay;
	public List<Canopy> canopyList;
	public List<LandingGear> landingGear;
	public List<Throttle> partThrottle;
	public List<RotPart> partRotPart;
	public List<CrawlerTrack> partCrawlerTrack;
	public List<TrackRoller> partTrackRoller;
	public List<PartWheel> partWheel;
	public List<PartWheel> partSteeringWheel;
	public List<Hatch> lightHatchList;
	private String lastWeaponType = "";
	private int lastWeaponIndex = -1;
	private PartWeapon lastWeaponPart;

	public abstract Item getItem();

	public class Wheel {
		public final float size;
		public final Vec3 pos;

		public Wheel(Vec3 v, float sz) {
			this.pos = v;
			this.size = sz;
		}

		public Wheel(Vec3 v) {
			this(v, 1.0F);
		}
	}

	public class Flare {
		public int[] types;
		public Vec3 pos;

		public Flare() {
			this.types = new int[0];
			this.pos = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
		}
	}

	public class SearchLight {
		public final int colorStart;
		public final int colorEnd;
		public final Vec3 pos;
		public final float height;
		public final float width;
		public final float angle;
		public final boolean fixDir;
		public final float yaw;
		public final float pitch;
		public final boolean steering;
		public final float stRot;

		public SearchLight(Vec3 pos, int cs, int ce, float h, float w, boolean fix, float y, float p, boolean st,
				float stRot) {
			this.colorStart = cs;
			this.colorEnd = ce;
			this.pos = pos;
			this.height = h;
			this.width = w;
			this.angle = ((float) (Math.atan2(w / 2.0F, h) * 180.0D / 3.141592653589793D));
			this.fixDir = fix;
			this.steering = st;
			this.yaw = y;
			this.pitch = p;
			this.stRot = stRot;
		}
	}

	public class RideRack {
		public final String name;
		public final int rackID;

		public RideRack(String n, int id) {
			this.name = n;
			this.rackID = id;
		}
	}

	public class ParticleSplash {
		public final int num;
		public final float acceleration;
		public final float size;
		public final Vec3 pos;
		public final int age;
		public final float motionY;
		public final float gravity;

		public ParticleSplash(Vec3 v, int nm, float siz, float acc, int ag, float my, float gr) {
			this.num = nm;
			this.pos = v;
			this.size = siz;
			this.acceleration = acc;
			this.age = ag;
			this.motionY = my;
			this.gravity = gr;
		}
	}

	public class RepellingHook {
		final Vec3 pos;
		final int interval;

		public RepellingHook(Vec3 pos, int inv) {
			this.pos = pos;
			this.interval = inv;
		}
	}

	public class WeaponSet {
		public final String type;
		public ArrayList<MCH_AircraftInfo.Weapon> weapons;

		public WeaponSet(String t) {
			this.type = t;
			this.weapons = new ArrayList();
		}
	}

	public class Weapon {
		public final Vec3 pos;
		public final float yaw;
		public final float pitch;
		public final boolean canUsePilot;
		public final int seatID;
		public final float defaultYaw;
		public final float minYaw;
		public final float maxYaw;
		public final float minPitch;
		public final float maxPitch;
		public final boolean turret;

		public Weapon(float x, float y, float z, float yaw, float pitch, boolean canPirot, int seatId, float defy,
				float mny, float mxy, float mnp, float mxp, boolean turret) {
			this.pos = Vec3.createVectorHelper(x, y, z);
			this.yaw = yaw;
			this.pitch = pitch;
			this.canUsePilot = canPirot;
			this.seatID = seatId;
			this.defaultYaw = defy;
			this.minYaw = mny;
			this.maxYaw = mxy;
			this.minPitch = mnp;
			this.maxPitch = mxp;
			this.turret = turret;
		}
	}

	public class DrawnPart {
		public final Vec3 pos;

		public final Vec3 rot;
		public final String modelName;
		public IModelCustom model;

		public DrawnPart(float px, float py, float pz, float rx, float ry, float rz, String name) {
			this.pos = Vec3.createVectorHelper(px, py, pz);
			this.rot = Vec3.createVectorHelper(rx, ry, rz);
			this.modelName = name;
			this.model = null;
		}
	}

	public class Hatch extends MCH_AircraftInfo.DrawnPart {
		public final float maxRotFactor;
		public final float maxRot;
		public final boolean isSlide;

		public Hatch(float px, float py, float pz, float rx, float ry, float rz, float mr, String name, boolean slide) {
			super(px, py, pz, rx, ry, rz, name);
			this.maxRot = mr;
			this.maxRotFactor = (this.maxRot / 90.0F);
			this.isSlide = slide;
		}
	}

	public class PartWheel extends MCH_AircraftInfo.DrawnPart {
		final float rotDir;
		final Vec3 pos2;

		public PartWheel(float px, float py, float pz, float rx, float ry, float rz, float rd, float px2, float py2,
				float pz2, String name) {
			super(px, py, pz, rx, ry, rz, name);
			this.rotDir = rd;
			this.pos2 = Vec3.createVectorHelper(px2, py2, pz2);
		}

		public PartWheel(float px, float py, float pz, float rx, float ry, float rz, float rd, String name) {
			this(px, py, pz, rx, ry, rz, rd, px, py, pz, name);
		}
	}

	public class TrackRoller extends MCH_AircraftInfo.DrawnPart {
		final int side;

		public TrackRoller(float px, float py, float pz, String name) {
			super(px, py, pz, 0.0F, 0.0F, 0.0F, name);
			this.side = (px >= 0.0F ? 1 : 0);
		}
	}

	public class CrawlerTrackPrm {
		float x;
		float y;
		float nx;
		float ny;
		float r;

		public CrawlerTrackPrm(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	public class CrawlerTrack extends MCH_AircraftInfo.DrawnPart {
		public float len = 0.35F;
		public double[] cx;
		public double[] cy;
		public List<MCH_AircraftInfo.CrawlerTrackPrm> lp;
		public float z;
		public int side;

		public CrawlerTrack(String name) {
			super(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, name);
		}
	}

	public class Camera extends MCH_AircraftInfo.DrawnPart {
		public final boolean yawSync;
		public final boolean pitchSync;

		public Camera(float px, float py, float pz, float rx, float ry, float rz, String name, boolean ys, boolean ps) {
			super(px, py, pz, rx, ry, rz, name);
			this.yawSync = ys;
			this.pitchSync = ps;
		}
	}

	public class Throttle extends MCH_AircraftInfo.DrawnPart {
		public final Vec3 slide;

		public final float rot2;

		public Throttle(float px, float py, float pz, float rx, float ry, float rz, float rot, String name, float px2,
				float py2, float pz2) {
			super(px, py, pz, rx, ry, rz, name);
			this.rot2 = rot;
			this.slide = Vec3.createVectorHelper(px2, py2, pz2);
		}
	}

	public class LandingGear extends MCH_AircraftInfo.DrawnPart {
		public Vec3 slide;
		public final float maxRotFactor;
		public boolean enableRot2;
		public Vec3 rot2;
		public float maxRotFactor2;
		public final boolean reverse;
		public final boolean hatch;

		public LandingGear(float x, float y, float z, float rx, float ry, float rz, String model, float maxRotF,
				boolean rev, boolean isHatch) {
			super(x, y, z, rx, ry, rz, model);
			this.slide = null;
			this.maxRotFactor = maxRotF;
			this.enableRot2 = false;
			this.rot2 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
			this.maxRotFactor2 = 0.0F;
			this.reverse = rev;
			this.hatch = isHatch;
		}
	}

	public class Canopy extends MCH_AircraftInfo.DrawnPart {
		public final float maxRotFactor;
		public final boolean isSlide;

		public Canopy(float px, float py, float pz, float rx, float ry, float rz, float mr, String name,
				boolean slide) {
			super(px, py, pz, rx, ry, rz, name);
			this.maxRotFactor = mr;
			this.isSlide = slide;
		}
	}

	public class PartWeapon extends MCH_AircraftInfo.DrawnPart {
		public final String[] name;
		public final boolean rotBarrel;
		public final boolean isMissile;
		public final boolean hideGM;
		public final boolean yaw;
		public final boolean pitch;
		public final float recoilBuf;
		public List<MCH_AircraftInfo.PartWeaponChild> child;
		public final boolean turret;

		public PartWeapon(String[] name, boolean rotBrl, boolean missile, boolean hgm, boolean y, boolean p, float px,
				float py, float pz, String modelName, float rx, float ry, float rz, float rb, boolean turret) {
			super(px, py, pz, rx, ry, rz, modelName);
			this.name = name;
			this.rotBarrel = rotBrl;
			this.isMissile = missile;
			this.hideGM = hgm;
			this.yaw = y;
			this.pitch = p;
			this.recoilBuf = rb;
			this.child = new ArrayList();
			this.turret = turret;
		}
	}

	public class PartWeaponChild extends MCH_AircraftInfo.DrawnPart {
		public final String[] name;

		public final boolean yaw;
		public final boolean pitch;
		public final float recoilBuf;

		public PartWeaponChild(String[] name, boolean y, boolean p, float px, float py, float pz, String modelName,
				float rx, float ry, float rz, float rb) {
			super(px, py, pz, rx, ry, rz, modelName);
			this.name = name;
			this.yaw = y;
			this.pitch = p;
			this.recoilBuf = rb;
		}
	}

	public class WeaponBay extends MCH_AircraftInfo.DrawnPart {
		public final float maxRotFactor;
		public final boolean isSlide;
		private final String weaponName;
		public Integer[] weaponIds;

		public WeaponBay(String wn, float px, float py, float pz, float rx, float ry, float rz, float mr, String name,
				boolean slide) {
			super(px, py, pz, rx, ry, rz, name);
			this.maxRotFactor = mr;
			this.isSlide = slide;
			this.weaponName = wn;
			this.weaponIds = new Integer[0];
		}
	}

	public class RotPart extends MCH_AircraftInfo.DrawnPart {
		public final float rotSpeed;
		public final boolean rotAlways;

		public RotPart(float px, float py, float pz, float rx, float ry, float rz, float mr, boolean a, String name) {
			super(px, py, pz, rx, ry, rz, name);
			this.rotSpeed = mr;
			this.rotAlways = a;
		}
	}

	public class CameraPosition {
		public final Vec3 pos;
		public final boolean fixRot;
		public final float yaw;
		public final float pitch;

		public CameraPosition(Vec3 vec3, boolean fixRot, float yaw, float pitch) {
			this.pos = vec3;
			this.fixRot = fixRot;
			this.yaw = yaw;
			this.pitch = pitch;
		}

		public CameraPosition(Vec3 vec3) {
			this(vec3, false, 0.0F, 0.0F);
		}

		public CameraPosition() {
			this(Vec3.createVectorHelper(0.0D, 0.0D, 0.0D));
		}
	}

	public ItemStack getItemStack() {
		return new ItemStack(getItem());
	}

	public abstract String getDirectoryName();

	public abstract String getKindName();

	public MCH_AircraftInfo(String s) {
		this.name = s;
		this.displayName = this.name;
		this.displayNameLang = new HashMap();
		this.itemID = 0;
		this.recipeString = new ArrayList();
		this.recipe = new ArrayList();
		this.isShapedRecipe = true;
		this.category = "zzz";

		this.creativeOnly = false;
		this.invulnerable = false;
		this.isEnableGunnerMode = false;
		this.isEnableConcurrentGunnerMode = false;
		this.isEnableNightVision = false;
		this.isEnableEntityRadar = false;
		this.isEnableEjectionSeat = false;
		this.isEnableParachuting = false;
		this.flare = new Flare();
		this.weaponSetList = new ArrayList();
		this.seatList = new ArrayList();
		this.exclusionSeatList = new ArrayList();
		this.hudList = new ArrayList();
		this.hudTvMissile = null;
		this.bodyHeight = 0.7F;
		this.bodyWidth = 2.0F;
		this.isFloat = false;
		this.floatOffset = 0.0F;
		this.gravity = -0.04F;
		this.gravityInWater = -0.04F;
		this.maxHp = 50;
		this.damageFactor = 0.2F;
		this.submergedDamageHeight = 0.0F;
		this.inventorySize = 0;
		this.armorDamageFactor = 1.0F;
		this.armorMaxDamage = 100000.0F;
		this.armorMinDamage = 0.0F;
		this.enableBack = false;
		this.isUAV = false;
		this.isSmallUAV = false;
		this.isTargetDrone = false;
		this.autoPilotRot = -0.6F;
		this.regeneration = false;
		this.onGroundPitch = 0.0F;
		this.canMoveOnGround = true;
		this.canRotOnGround = true;
		this.cameraZoom = getDefaultMaxZoom();
		this.extraBoundingBox = new ArrayList();
		this.maxFuel = 0;
		this.fuelConsumption = 1.0F;
		this.fuelSupplyRange = 0.0F;
		this.ammoSupplyRange = 0.0F;
		this.repairOtherVehiclesRange = 0.0F;
		this.repairOtherVehiclesValue = 10;
		this.stealth = 0.0F;
		this.canRide = true;
		this.entityWidth = 1.0F;
		this.entityHeight = 1.0F;
		this.entityPitch = 0.0F;
		this.entityRoll = 0.0F;
		this.stepHeight = getDefaultStepHeight();
		this.entityRackList = new ArrayList();
		this.mobSeatNum = 0;
		this.entityRackNum = 0;
		this.mobDropOption = new MCH_MobDropOption();
		this.repellingHooks = new ArrayList();
		this.rideRacks = new ArrayList();
		this.particleSplashs = new ArrayList();
		this.searchLights = new ArrayList();
		this.markerHeight = 1.0F;
		this.markerWidth = 2.0F;
		this.bbZmax = 1.0F;
		this.bbZmin = -1.0F;
		this.rotorSpeed = getDefaultRotorSpeed();
		this.wheels = getDefaultWheelList();
		this.onGroundPitchFactor = 0.0F;
		this.onGroundRollFactor = 0.0F;
		this.turretPosition = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
		this.defaultFreelook = false;
		this.unmountPosition = null;
		this.thirdPersonDist = 4.0F;

		this.cameraPosition = new ArrayList();
		this.alwaysCameraView = false;
		this.cameraRotationSpeed = 100.0F;

		this.speed = 0.1F;
		this.motionFactor = 0.96F;
		this.mobilityYaw = 1.0F;
		this.mobilityPitch = 1.0F;
		this.mobilityRoll = 1.0F;
		this.mobilityYawOnGround = 1.0F;
		this.minRotationPitch = getMinRotationPitch();
		this.maxRotationPitch = getMaxRotationPitch();
		this.minRotationRoll = getMinRotationPitch();
		this.maxRotationRoll = getMaxRotationPitch();
		this.limitRotation = false;
		this.throttleUpDown = 1.0F;
		this.throttleUpDownOnEntity = 2.0F;
		this.pivotTurnThrottle = 0.0F;
		this.trackRollerRot = 30.0F;
		this.partWheelRot = 30.0F;

		this.textureNameList = new ArrayList();
		this.textureNameList.add(this.name);
		this.textureCount = 0;
		this.particlesScale = 1.0F;
		this.enableSeaSurfaceParticle = false;
		this.hideEntity = false;
		this.smoothShading = true;

		this.soundMove = "";
		this.soundPitch = 1.0F;
		this.soundVolume = 1.0F;
		this.soundRange = getDefaultSoundRange();

		this.model = null;
		this.hatchList = new ArrayList();
		this.cameraList = new ArrayList();
		this.partWeapon = new ArrayList();
		this.lastWeaponPart = null;
		this.partWeaponBay = new ArrayList();
		this.canopyList = new ArrayList();
		this.landingGear = new ArrayList();
		this.partThrottle = new ArrayList();
		this.partRotPart = new ArrayList();
		this.partCrawlerTrack = new ArrayList();
		this.partTrackRoller = new ArrayList();
		this.partWheel = new ArrayList();
		this.partSteeringWheel = new ArrayList();
		this.lightHatchList = new ArrayList();
	}

	public float getDefaultSoundRange() {
		return 100.0F;
	}

	public List<Wheel> getDefaultWheelList() {
		return new ArrayList();
	}

	public float getDefaultRotorSpeed() {
		return 0.0F;
	}

	private float getDefaultStepHeight() {
		return 0.0F;
	}

	public boolean haveRepellingHook() {
		return this.repellingHooks.size() > 0;
	}

	public boolean haveFlare() {
		return this.flare.types.length > 0;
	}

	public boolean haveCanopy() {
		return this.canopyList.size() > 0;
	}

	public boolean haveLandingGear() {
		return this.landingGear.size() > 0;
	}

	public abstract String getDefaultHudName(int paramInt);

	public boolean isValidData() throws Exception {
		if (this.cameraPosition.size() <= 0) {
			this.cameraPosition.add(new CameraPosition());
		}

		this.bbZ = ((this.bbZmax + this.bbZmin) / 2.0F);

		if (this.isTargetDrone) {
			this.isUAV = true;
		}

		if ((this.isEnableParachuting) && (this.repellingHooks.size() > 0)) {
			this.isEnableParachuting = false;
			this.repellingHooks.clear();
		}

		if (this.isUAV) {
			this.alwaysCameraView = true;
			if (this.seatList.size() == 0) {
				MCH_SeatInfo s = new MCH_SeatInfo(Vec3.createVectorHelper(0.0D, 0.0D, 0.0D), false);
				this.seatList.add(s);
			}
		}

		this.mobSeatNum = this.seatList.size();
		this.entityRackNum = this.entityRackList.size();

		if (getNumSeat() < 1) {
			throw new Exception();
		}
		if (getNumHud() < getNumSeat()) {
			for (int i = getNumHud(); i < getNumSeat(); i++) {
				this.hudList.add(MCH_HudManager.get(getDefaultHudName(i)));
			}
		}

		if ((getNumSeat() == 1) && (getNumHud() == 1)) {
			this.hudList.add(MCH_HudManager.get(getDefaultHudName(1)));
		}

		for (MCH_SeatRackInfo ei : this.entityRackList) {
			this.seatList.add(ei);
		}
		this.entityRackList.clear();

		if (this.hudTvMissile == null) {
			this.hudTvMissile = MCH_HudManager.get("tv_missile");
		}

		if (this.textureNameList.size() < 1)
			throw new Exception();
		if (this.itemID <= 0) {
		}

		for (int i = 0; i < this.partWeaponBay.size(); i++) {
			WeaponBay wb = (WeaponBay) this.partWeaponBay.get(i);
			String[] weaponNames = wb.weaponName.split("\\s*/\\s*");
			if (weaponNames.length <= 0) {
				this.partWeaponBay.remove(i);
			} else {
				List<Integer> list = new ArrayList();
				for (String s : weaponNames) {
					int id = getWeaponIdByName(s);
					if (id >= 0) {
						list.add(Integer.valueOf(id));
					}
				}
				if (list.size() <= 0) {
					this.partWeaponBay.remove(i);

				} else {
					((WeaponBay) this.partWeaponBay.get(i)).weaponIds = ((Integer[]) list.toArray(new Integer[0]));
				}
			}
		}
		return true;
	}

	public int getInfo_MaxSeatNum() {
		return 30;
	}

	public int getNumSeatAndRack() {
		return this.seatList.size();
	}

	public int getNumSeat() {
		return this.mobSeatNum;
	}

	public int getNumRack() {
		return this.entityRackNum;
	}

	public int getNumHud() {
		return this.hudList.size();
	}

	public float getMaxSpeed() {
		return 0.8F;
	}

	public float getMinRotationPitch() {
		return -89.9F;
	}

	public float getMaxRotationPitch() {
		return 80.0F;
	}

	public float getMinRotationRoll() {
		return -80.0F;
	}

	public float getMaxRotationRoll() {
		return 80.0F;
	}

	public int getDefaultMaxZoom() {
		return 1;
	}

	public boolean haveHatch() {
		return this.hatchList.size() > 0;
	}

	public boolean havePartCamera() {
		return this.cameraList.size() > 0;
	}

	public boolean havePartThrottle() {
		return this.partThrottle.size() > 0;
	}

	public WeaponSet getWeaponSetById(int id) {
		return (id >= 0) && (id < this.weaponSetList.size()) ? (WeaponSet) this.weaponSetList.get(id) : null;
	}

	public Weapon getWeaponById(int id) {
		WeaponSet ws = getWeaponSetById(id);
		return ws != null ? (Weapon) ws.weapons.get(0) : null;
	}

	public int getWeaponIdByName(String s) {
		for (int i = 0; i < this.weaponSetList.size(); i++) {
			if (((WeaponSet) this.weaponSetList.get(i)).type.equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}

	public Weapon getWeaponByName(String s) {
		for (int i = 0; i < this.weaponSetList.size(); i++) {
			if (((WeaponSet) this.weaponSetList.get(i)).type.equalsIgnoreCase(s)) {
				return getWeaponById(i);
			}
		}
		return null;
	}

	public int getWeaponNum() {
		return this.weaponSetList.size();
	}

	public void loadItemData(String item, String data) {
		if (item.compareTo("displayname") == 0) {
			this.displayName = data.trim();
		} else if (item.compareTo("adddisplayname") == 0) {
			String[] s = data.split("\\s*,\\s*");
			if ((s != null) && (s.length == 2)) {
				this.displayNameLang.put(s[0].trim(), s[1].trim());
			}
		} else if (item.equalsIgnoreCase("Category")) {
			this.category = data.toUpperCase().replaceAll("[,;:]", ".").replaceAll("[ \t]", "");
		} else if (item.equalsIgnoreCase("CanRide")) {
			this.canRide = toBool(data, true);
		} else if (item.equalsIgnoreCase("CreativeOnly")) {
			this.creativeOnly = toBool(data, false);
		} else if (item.equalsIgnoreCase("Invulnerable")) {
			this.invulnerable = toBool(data, false);
		} else if (item.equalsIgnoreCase("MaxFuel")) {
			this.maxFuel = toInt(data, 0, 100000000);
		} else if (item.equalsIgnoreCase("FuelConsumption")) {
			this.fuelConsumption = toFloat(data, 0.0F, 10000.0F);
		} else if (item.equalsIgnoreCase("FuelSupplyRange")) {
			this.fuelSupplyRange = toFloat(data, 0.0F, 1000.0F);
		} else if (item.equalsIgnoreCase("AmmoSupplyRange")) {
			this.ammoSupplyRange = toFloat(data, 0.0F, 1000.0F);
		} else if (item.equalsIgnoreCase("RepairOtherVehicles")) {
			String[] s = splitParam(data);
			if (s.length >= 1) {
				this.repairOtherVehiclesRange = toFloat(s[0], 0.0F, 1000.0F);
				if (s.length >= 2)
					this.repairOtherVehiclesValue = toInt(s[1], 0, 10000000);
			}
		} else if (item.compareTo("itemid") == 0) {
			this.itemID = toInt(data, 0, 65535);
		} else if (item.compareTo("addtexture") == 0) {
			this.textureNameList.add(data.toLowerCase());
		} else if (item.compareTo("particlesscale") == 0) {
			this.particlesScale = toFloat(data, 0.0F, 50.0F);
		} else if (item.equalsIgnoreCase("EnableSeaSurfaceParticle")) {
			this.enableSeaSurfaceParticle = toBool(data);
		} else if (item.equalsIgnoreCase("AddParticleSplash")) {
			String[] s = splitParam(data);
			if (s.length >= 3) {
				Vec3 v = toVec3(s[0], s[1], s[2]);
				int num = s.length >= 4 ? toInt(s[3], 1, 100) : 2;
				float size = s.length >= 5 ? toFloat(s[4]) : 2.0F;
				float acc = s.length >= 6 ? toFloat(s[5]) : 1.0F;
				int age = s.length >= 7 ? toInt(s[6], 1, 100000) : 80;
				float motionY = s.length >= 8 ? toFloat(s[7]) : 0.01F;
				float gravity = s.length >= 9 ? toFloat(s[8]) : 0.0F;
				this.particleSplashs.add(new ParticleSplash(v, num, size, acc, age, motionY, gravity));
			}
		} else if ((item.equalsIgnoreCase("AddSearchLight")) || (item.equalsIgnoreCase("AddFixedSearchLight"))
				|| (item.equalsIgnoreCase("AddSteeringSearchLight"))) {

			String[] s = splitParam(data);
			if (s.length >= 7) {
				Vec3 v = toVec3(s[0], s[1], s[2]);
				int cs = hex2dec(s[3]);
				int ce = hex2dec(s[4]);
				float h = toFloat(s[5]);
				float w = toFloat(s[6]);
				float yaw = s.length >= 8 ? toFloat(s[7]) : 0.0F;
				float pitch = s.length >= 9 ? toFloat(s[8]) : 0.0F;
				float stRot = s.length >= 10 ? toFloat(s[9]) : 0.0F;
				boolean fixDir = !item.equalsIgnoreCase("AddSearchLight");
				boolean steering = item.equalsIgnoreCase("AddSteeringSearchLight");
				this.searchLights.add(new SearchLight(v, cs, ce, h, w, fixDir, yaw, pitch, steering, stRot));
			}
		} else if (item.equalsIgnoreCase("AddPartLightHatch")) {
			String[] s = splitParam(data);
			if (s.length >= 6) {
				float mx = s.length >= 7 ? toFloat(s[6], -1800.0F, 1800.0F) : 90.0F;
				this.lightHatchList.add(new Hatch(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]),
						toFloat(s[4]), toFloat(s[5]), mx, "light_hatch" + this.lightHatchList.size(), false));

			}

		} else if (item.equalsIgnoreCase("AddRepellingHook")) {

			String[] s = splitParam(data);
			if ((s != null) && (s.length >= 3)) {
				int inv = s.length >= 4 ? toInt(s[3], 1, 100000) : 10;
				this.repellingHooks.add(new RepellingHook(toVec3(s[0], s[1], s[2]), inv));
			}
		} else if (item.equalsIgnoreCase("AddRack")) {

			String[] s = data.toLowerCase().split("\\s*,\\s*");
			if ((s != null) && (s.length >= 7)) {
				String[] names = s[0].split("\\s*/\\s*");
				float range = s.length >= 8 ? toFloat(s[7]) : 6.0F;
				float para = s.length >= 9 ? toFloat(s[8], 0.0F, 1000000.0F) : 20.0F;
				float yaw = s.length >= 10 ? toFloat(s[9]) : 0.0F;
				float pitch = s.length >= 11 ? toFloat(s[10]) : 0.0F;
				boolean rs = s.length >= 12 ? toBool(s[11]) : false;
				this.entityRackList.add(new MCH_SeatRackInfo(names, toDouble(s[1]), toDouble(s[2]), toDouble(s[3]),
						new CameraPosition(toVec3(s[4], s[5], s[6]).addVector(0.0D, 1.5D, 0.0D)), range, para, yaw,
						pitch, rs));

			}

		} else if (item.equalsIgnoreCase("RideRack")) {
			String[] s = splitParam(data);
			if (s.length >= 2) {
				RideRack r = new RideRack(s[0].trim().toLowerCase(), toInt(s[1], 1, 10000));
				this.rideRacks.add(r);
			}
		} else if ((item.equalsIgnoreCase("AddSeat")) || (item.equalsIgnoreCase("AddGunnerSeat"))
				|| (item.equalsIgnoreCase("AddFixRotSeat"))) {

			if (this.seatList.size() >= getInfo_MaxSeatNum()) {
				return;
			}
			String[] s = splitParam(data);
			if (s.length < 3) {
				return;
			}
			Vec3 p = toVec3(s[0], s[1], s[2]);
			if (item.equalsIgnoreCase("AddSeat")) {
				boolean rs = s.length >= 4 ? toBool(s[3]) : false;
				MCH_SeatInfo seat = new MCH_SeatInfo(p, rs);
				this.seatList.add(seat);
			} else {
				MCH_SeatInfo seat;
				if (s.length >= 6) {
					CameraPosition c = new CameraPosition(toVec3(s[3], s[4], s[5]));

					boolean sg = s.length >= 7 ? toBool(s[6]) : false;
					if (item.equalsIgnoreCase("AddGunnerSeat")) {
						if (s.length >= 9) {
							float minPitch = toFloat(s[7], -90.0F, 90.0F);
							float maxPitch = toFloat(s[8], -90.0F, 90.0F);
							if (minPitch > maxPitch) {
								float t = minPitch;
								minPitch = maxPitch;
								maxPitch = t;
							}
							boolean rs = s.length >= 10 ? toBool(s[9]) : false;
							seat = new MCH_SeatInfo(p, true, c, true, sg, false, 0.0F, 0.0F, minPitch, maxPitch, rs);
						} else {
							seat = new MCH_SeatInfo(p, true, c, true, sg, false, 0.0F, 0.0F, false);
						}
					} else {
						boolean fixRot = s.length >= 9;
						float fixYaw = fixRot ? toFloat(s[7]) : 0.0F;
						float fixPitch = fixRot ? toFloat(s[8]) : 0.0F;
						boolean rs = s.length >= 10 ? toBool(s[9]) : false;
						seat = new MCH_SeatInfo(p, true, c, true, sg, fixRot, fixYaw, fixPitch, rs);
					}
				} else {
					seat = new MCH_SeatInfo(p, true, new CameraPosition(), false, false, false, 0.0F, 0.0F, false);
				}
				this.seatList.add(seat);
			}
		} else if (item.equalsIgnoreCase("SetWheelPos")) {
			String[] s = splitParam(data);
			if (s.length >= 4) {
				float x = Math.abs(toFloat(s[0]));
				float y = toFloat(s[1]);
				this.wheels.clear();
				for (int i = 2; i < s.length; i++) {
					this.wheels.add(new Wheel(Vec3.createVectorHelper(x, y, toFloat(s[i]))));
				}
				Collections.sort(this.wheels, new Comparator<MCH_AircraftInfo.Wheel>() {
					public int compare(MCH_AircraftInfo.Wheel arg0, MCH_AircraftInfo.Wheel arg1) {
						return arg0.pos.zCoord > arg1.pos.zCoord ? -1 : 1;
					}
				});
			}
		} else if (item.equalsIgnoreCase("ExclusionSeat")) {
			String[] s = splitParam(data);
			if (s.length >= 2) {
				Integer[] a = new Integer[s.length];
				for (int i = 0; i < a.length; i++) {
					a[i] = Integer.valueOf(toInt(s[i], 1, 10000) - 1);
				}
				this.exclusionSeatList.add(a);
			}
		} else if ((MCH_MOD.proxy.isRemote()) && (item.equalsIgnoreCase("HUD"))) {
			this.hudList.clear();
			String[] ss = data.split("\\s*,\\s*");
			for (String s : ss) {
				MCH_Hud hud = MCH_HudManager.get(s);
				if (hud == null) {
					hud = MCH_Hud.NoDisp;
				}
				this.hudList.add(hud);
			}
		} else if (item.compareTo("enablenightvision") == 0) {
			this.isEnableNightVision = toBool(data);
		} else if (item.compareTo("enableentityradar") == 0) {
			this.isEnableEntityRadar = toBool(data);
		} else if (item.equalsIgnoreCase("EnableEjectionSeat")) {
			this.isEnableEjectionSeat = toBool(data);
		} else if (item.equalsIgnoreCase("EnableParachuting")) {
			this.isEnableParachuting = toBool(data);
		} else if (item.equalsIgnoreCase("MobDropOption")) {
			String[] s = splitParam(data);
			if (s.length >= 3) {
				this.mobDropOption.pos = toVec3(s[0], s[1], s[2]);
				this.mobDropOption.interval = (s.length >= 4 ? toInt(s[3]) : 12);
			}
		} else if (item.equalsIgnoreCase("Width")) {
			this.bodyWidth = toFloat(data, 0.1F, 1000.0F);
		} else if (item.equalsIgnoreCase("Height")) {
			this.bodyHeight = toFloat(data, 0.1F, 1000.0F);
		} else if (item.compareTo("float") == 0) {
			this.isFloat = toBool(data);
		} else if (item.compareTo("floatoffset") == 0) {

			this.floatOffset = (-toFloat(data));
		} else if (item.compareTo("gravity") == 0) {
			this.gravity = toFloat(data, -50.0F, 50.0F);
		} else if (item.compareTo("gravityinwater") == 0) {
			this.gravityInWater = toFloat(data, -50.0F, 50.0F);
		} else if (item.compareTo("cameraposition") == 0) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 3) {
				this.alwaysCameraView = (s.length >= 4 ? toBool(s[3]) : false);
				boolean fixRot = s.length >= 5;
				float yaw = s.length >= 5 ? toFloat(s[4]) : 0.0F;
				float pitch = s.length >= 6 ? toFloat(s[5]) : 0.0F;
				this.cameraPosition.add(new CameraPosition(toVec3(s[0], s[1], s[2]), fixRot, yaw, pitch));
			}
		} else if (item.equalsIgnoreCase("UnmountPosition")) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 3) {
				this.unmountPosition = toVec3(s[0], s[1], s[2]);
			}
		} else if (item.equalsIgnoreCase("ThirdPersonDist")) {
			this.thirdPersonDist = toFloat(data, 4.0F, 100.0F);
		} else if (item.equalsIgnoreCase("TurretPosition")) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 3) {
				this.turretPosition = toVec3(s[0], s[1], s[2]);
			}
		} else if (item.equalsIgnoreCase("CameraRotationSpeed")) {
			this.cameraRotationSpeed = toFloat(data, 0.0F, 10000.0F);
		} else if (item.compareTo("regeneration") == 0) {
			this.regeneration = toBool(data);
		} else if (item.compareTo("speed") == 0) {
			this.speed = toFloat(data, 0.0F, getMaxSpeed());
		} else if (item.equalsIgnoreCase("EnableBack")) {
			this.enableBack = toBool(data);
		} else if (item.equalsIgnoreCase("MotionFactor")) {
			this.motionFactor = toFloat(data, 0.0F, 1.0F);
		} else if (item.equalsIgnoreCase("MobilityYawOnGround")) {
			this.mobilityYawOnGround = toFloat(data, 0.0F, 100.0F);
		} else if (item.equalsIgnoreCase("MobilityYaw")) {
			this.mobilityYaw = toFloat(data, 0.0F, 100.0F);
		} else if (item.equalsIgnoreCase("MobilityPitch")) {
			this.mobilityPitch = toFloat(data, 0.0F, 100.0F);
		} else if (item.equalsIgnoreCase("MobilityRoll")) {
			this.mobilityRoll = toFloat(data, 0.0F, 100.0F);
		} else if (item.equalsIgnoreCase("MinRotationPitch")) {
			this.limitRotation = true;
			this.minRotationPitch = toFloat(data, getMinRotationPitch(), 0.0F);
		} else if (item.equalsIgnoreCase("MaxRotationPitch")) {
			this.limitRotation = true;
			this.maxRotationPitch = toFloat(data, 0.0F, getMaxRotationPitch());
		} else if (item.equalsIgnoreCase("MinRotationRoll")) {
			this.limitRotation = true;
			this.minRotationRoll = toFloat(data, getMinRotationRoll(), 0.0F);
		} else if (item.equalsIgnoreCase("MaxRotationRoll")) {
			this.limitRotation = true;
			this.maxRotationRoll = toFloat(data, 0.0F, getMaxRotationRoll());
		} else if (item.compareTo("throttleupdown") == 0) {
			this.throttleUpDown = toFloat(data, 0.0F, 3.0F);
		} else if (item.equalsIgnoreCase("ThrottleUpDownOnEntity")) {
			this.throttleUpDownOnEntity = toFloat(data, 0.0F, 100000.0F);
		} else if (item.equalsIgnoreCase("Stealth")) {
			this.stealth = toFloat(data, 0.0F, 1.0F);
		} else if (item.equalsIgnoreCase("EntityWidth")) {
			this.entityWidth = toFloat(data, -100.0F, 100.0F);
		} else if (item.equalsIgnoreCase("EntityHeight")) {
			this.entityHeight = toFloat(data, -100.0F, 100.0F);
		} else if (item.equalsIgnoreCase("EntityPitch")) {
			this.entityPitch = toFloat(data, -360.0F, 360.0F);
		} else if (item.equalsIgnoreCase("EntityRoll")) {
			this.entityRoll = toFloat(data, -360.0F, 360.0F);
		} else if (item.equalsIgnoreCase("StepHeight")) {
			this.stepHeight = toFloat(data, 0.0F, 1000.0F);
		} else if (item.equalsIgnoreCase("CanMoveOnGround")) {
			this.canMoveOnGround = toBool(data);
		} else if (item.equalsIgnoreCase("CanRotOnGround")) {
			this.canRotOnGround = toBool(data);
		} else if ((item.equalsIgnoreCase("AddWeapon")) || (item.equalsIgnoreCase("AddTurretWeapon"))) {
			String[] s = data.split("\\s*,\\s*");
			String type = s[0].toLowerCase();
			if ((s.length >= 4) && (MCH_WeaponInfoManager.contains(type))) {
				float y = s.length >= 5 ? toFloat(s[4]) : 0.0F;
				float p = s.length >= 6 ? toFloat(s[5]) : 0.0F;
				boolean canUsePilot = s.length >= 7 ? toBool(s[6]) : true;
				int seatID = s.length >= 8 ? toInt(s[7], 1, getInfo_MaxSeatNum()) - 1 : 0;
				if (seatID <= 0)
					canUsePilot = true;
				float dfy = s.length >= 9 ? toFloat(s[8]) : 0.0F;
				dfy = MathHelper.wrapAngleTo180_float(dfy);
				float mny = s.length >= 10 ? toFloat(s[9]) : 0.0F;
				float mxy = s.length >= 11 ? toFloat(s[10]) : 0.0F;
				float mnp = s.length >= 12 ? toFloat(s[11]) : 0.0F;
				float mxp = s.length >= 13 ? toFloat(s[12]) : 0.0F;
				Weapon e = new Weapon(toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), y, p, canUsePilot, seatID, dfy, mny,
						mxy, mnp, mxp, item.equalsIgnoreCase("AddTurretWeapon"));

				if (type.compareTo(this.lastWeaponType) != 0) {
					this.weaponSetList.add(new WeaponSet(type));
					this.lastWeaponIndex += 1;
					this.lastWeaponType = type;
				}
				((WeaponSet) this.weaponSetList.get(this.lastWeaponIndex)).weapons.add(e);
			}
		} else if ((item.equalsIgnoreCase("AddPartWeapon")) || (item.equalsIgnoreCase("AddPartRotWeapon"))
				|| (item.equalsIgnoreCase("AddPartTurretWeapon")) || (item.equalsIgnoreCase("AddPartTurretRotWeapon"))
				|| (item.equalsIgnoreCase("AddPartWeaponMissile"))) {

			String[] s = data.split("\\s*,\\s*");

			if (s.length >= 7) {
				float rx = 0.0F;
				float ry = 0.0F;
				float rz = 0.0F;
				float rb = 0.0F;
				boolean isRot = (item.equalsIgnoreCase("AddPartRotWeapon"))
						|| (item.equalsIgnoreCase("AddPartTurretRotWeapon"));

				boolean isMissile = item.equalsIgnoreCase("AddPartWeaponMissile");
				boolean turret = (item.equalsIgnoreCase("AddPartTurretWeapon"))
						|| (item.equalsIgnoreCase("AddPartTurretRotWeapon"));

				if (isRot) {

					rx = s.length >= 10 ? toFloat(s[7]) : 0.0F;
					ry = s.length >= 10 ? toFloat(s[8]) : 0.0F;
					rz = s.length >= 10 ? toFloat(s[9]) : -1.0F;

				} else {
					rb = s.length >= 8 ? toFloat(s[7]) : 0.0F;
				}

				PartWeapon w = new PartWeapon(splitParamSlash(s[0].toLowerCase().trim()), isRot, isMissile,
						toBool(s[1]), toBool(s[2]), toBool(s[3]), toFloat(s[4]), toFloat(s[5]), toFloat(s[6]),
						"weapon" + this.partWeapon.size(), rx, ry, rz, rb, turret);

				this.lastWeaponPart = w;
				this.partWeapon.add(w);
			}
		} else if (item.equalsIgnoreCase("AddPartWeaponChild")) {
			String[] s = data.split("\\s*,\\s*");
			if ((s.length >= 5) && (this.lastWeaponPart != null)) {
				float rb = s.length >= 6 ? toFloat(s[5]) : 0.0F;
				PartWeaponChild w = new PartWeaponChild(this.lastWeaponPart.name, toBool(s[0]), toBool(s[1]),
						toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
						this.lastWeaponPart.modelName + "_" + this.lastWeaponPart.child.size(), 0.0F, 0.0F, 0.0F, rb);

				this.lastWeaponPart.child.add(w);
			}
		} else if ((item.compareTo("addrecipe") == 0) || (item.compareTo("addshapelessrecipe") == 0)) {
			this.isShapedRecipe = (item.compareTo("addrecipe") == 0);
			this.recipeString.add(data.toUpperCase());
		} else if (item.compareTo("maxhp") == 0) {
			this.maxHp = toInt(data, 1, 1000000000);
		} else if (item.compareTo("inventorysize") == 0) {
			this.inventorySize = toInt(data, 0, 54);
		} else if (item.compareTo("damagefactor") == 0) {
			this.damageFactor = toFloat(data, 0.0F, 1.0F);
		} else if (item.equalsIgnoreCase("SubmergedDamageHeight")) {
			this.submergedDamageHeight = toFloat(data, -1000.0F, 1000.0F);
		} else if (item.equalsIgnoreCase("ArmorDamageFactor")) {
			this.armorDamageFactor = toFloat(data, 0.0F, 10000.0F);
		} else if (item.equalsIgnoreCase("ArmorMinDamage")) {
			this.armorMinDamage = toFloat(data, 0.0F, 1000000.0F);
		} else if (item.equalsIgnoreCase("ArmorMaxDamage")) {
			this.armorMaxDamage = toFloat(data, 0.0F, 1000000.0F);
		} else if (item.equalsIgnoreCase("FlareType")) {
			String[] s = data.split("\\s*,\\s*");
			this.flare.types = new int[s.length];
			for (int i = 0; i < s.length; i++) {
				this.flare.types[i] = toInt(s[i], 1, 10);
			}
		} else if (item.equalsIgnoreCase("FlareOption")) {
			String[] s = splitParam(data);
			if (s.length >= 3) {
				this.flare.pos = toVec3(s[0], s[1], s[2]);
			}
		} else if (item.equalsIgnoreCase("Sound")) {
			this.soundMove = data.toLowerCase();
		} else if (item.equalsIgnoreCase("SoundRange")) {
			this.soundRange = toFloat(data, 1.0F, 1000.0F);
		} else if (item.equalsIgnoreCase("SoundVolume")) {
			this.soundVolume = toFloat(data, 0.0F, 10.0F);
		} else if (item.equalsIgnoreCase("SoundPitch")) {
			this.soundPitch = toFloat(data, 0.0F, 10.0F);
		} else if (item.equalsIgnoreCase("UAV")) {
			this.isUAV = toBool(data);
			this.isSmallUAV = false;
		} else if (item.equalsIgnoreCase("SmallUAV")) {
			this.isUAV = toBool(data);
			this.isSmallUAV = true;
		} else if (item.equalsIgnoreCase("TargetDrone")) {
			this.isTargetDrone = toBool(data);
		} else if (item.compareTo("autopilotrot") == 0) {
			this.autoPilotRot = toFloat(data, -5.0F, 5.0F);
		} else if (item.compareTo("ongroundpitch") == 0) {

			this.onGroundPitch = (-toFloat(data, -90.0F, 90.0F));
		} else if (item.compareTo("enablegunnermode") == 0) {
			this.isEnableGunnerMode = toBool(data);
		} else if (item.compareTo("hideentity") == 0) {
			this.hideEntity = toBool(data);
		} else if (item.equalsIgnoreCase("SmoothShading")) {
			this.smoothShading = toBool(data);
		} else if (item.compareTo("concurrentgunnermode") == 0) {
			this.isEnableConcurrentGunnerMode = toBool(data);
		} else if ((item.equalsIgnoreCase("AddPartWeaponBay")) || (item.equalsIgnoreCase("AddPartSlideWeaponBay"))) {
			boolean slide = item.equalsIgnoreCase("AddPartSlideWeaponBay");
			String[] s = data.split("\\s*,\\s*");
			WeaponBay n = null;
			if (slide) {
				if (s.length >= 4) {
					n = new WeaponBay(s[0].trim().toLowerCase(), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), 0.0F,
							0.0F, 0.0F, 90.0F, "wb" + this.partWeaponBay.size(), slide);

					this.partWeaponBay.add(n);
				}

			} else if (s.length >= 7) {
				float mx = s.length >= 8 ? toFloat(s[7], -180.0F, 180.0F) : 90.0F;
				n = new WeaponBay(s[0].trim().toLowerCase(), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
						toFloat(s[5]), toFloat(s[6]), mx / 90.0F, "wb" + this.partWeaponBay.size(), slide);

				this.partWeaponBay.add(n);
			}

		} else if ((item.compareTo("addparthatch") == 0) || (item.compareTo("addpartslidehatch") == 0)) {
			boolean slide = item.compareTo("addpartslidehatch") == 0;
			String[] s = data.split("\\s*,\\s*");
			Hatch n = null;
			if (slide) {
				if (s.length >= 3) {
					n = new Hatch(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F,
							"hatch" + this.hatchList.size(), slide);

					this.hatchList.add(n);
				}

			} else if (s.length >= 6) {
				float mx = s.length >= 7 ? toFloat(s[6], -180.0F, 180.0F) : 90.0F;
				n = new Hatch(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]), toFloat(s[5]),
						mx, "hatch" + this.hatchList.size(), slide);

				this.hatchList.add(n);
			}

		} else if ((item.compareTo("addpartcanopy") == 0) || (item.compareTo("addpartslidecanopy") == 0)) {
			String[] s = data.split("\\s*,\\s*");
			boolean slide = item.compareTo("addpartslidecanopy") == 0;

			int canopyNum = this.canopyList.size();
			if (canopyNum > 0)
				canopyNum--;
			if (slide) {
				if (s.length >= 3) {
					Canopy c = new Canopy(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F,
							"canopy" + canopyNum, slide);

					this.canopyList.add(c);

					if (canopyNum == 0) {
						c = new Canopy(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), 0.0F, 0.0F, 0.0F, 90.0F, "canopy",
								slide);

						this.canopyList.add(c);
					}

				}

			} else if (s.length >= 6) {
				float mx = s.length >= 7 ? toFloat(s[6], -180.0F, 180.0F) : 90.0F;
				mx /= 90.0F;
				Canopy c = new Canopy(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
						toFloat(s[5]), mx, "canopy" + canopyNum, slide);

				this.canopyList.add(c);

				if (canopyNum == 0) {
					c = new Canopy(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
							toFloat(s[5]), mx, "canopy", slide);

					this.canopyList.add(c);
				}

			}
		} else if ((item.equalsIgnoreCase("AddPartLG")) || (item.equalsIgnoreCase("AddPartSlideRotLG"))
				|| (item.equalsIgnoreCase("AddPartLGRev")) || (item.equalsIgnoreCase("AddPartLGHatch"))) {

			String[] s = data.split("\\s*,\\s*");
			if ((!item.equalsIgnoreCase("AddPartSlideRotLG")) && (s.length >= 6)) {
				float maxRot = s.length >= 7 ? toFloat(s[6], -180.0F, 180.0F) : 90.0F;
				maxRot /= 90.0F;
				LandingGear n = new LandingGear(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]),
						toFloat(s[4]), toFloat(s[5]), "lg" + this.landingGear.size(), maxRot,
						item.equalsIgnoreCase("AddPartLgRev"), item.equalsIgnoreCase("AddPartLGHatch"));

				if (s.length >= 8) {
					n.enableRot2 = true;
					n.maxRotFactor2 = (s.length >= 11 ? toFloat(s[10], -180.0F, 180.0F) : 90.0F);
					n.maxRotFactor2 /= 90.0F;
					n.rot2 = Vec3.createVectorHelper(toFloat(s[7]), toFloat(s[8]), toFloat(s[9]));
				}

				this.landingGear.add(n);
			}
			if ((item.equalsIgnoreCase("AddPartSlideRotLG")) && (s.length >= 9)) {
				float maxRot = s.length >= 10 ? toFloat(s[9], -180.0F, 180.0F) : 90.0F;
				maxRot /= 90.0F;
				LandingGear n = new LandingGear(toFloat(s[3]), toFloat(s[4]), toFloat(s[5]), toFloat(s[6]),
						toFloat(s[7]), toFloat(s[8]), "lg" + this.landingGear.size(), maxRot, false, false);

				n.slide = Vec3.createVectorHelper(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]));

				this.landingGear.add(n);
			}
		} else if (item.equalsIgnoreCase("AddPartThrottle")) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 7) {
				float x = s.length >= 8 ? toFloat(s[7]) : 0.0F;
				float y = s.length >= 9 ? toFloat(s[8]) : 0.0F;
				float z = s.length >= 10 ? toFloat(s[9]) : 0.0F;
				Throttle c = new Throttle(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
						toFloat(s[5]), toFloat(s[6]), "throttle" + this.partThrottle.size(), x, y, z);

				this.partThrottle.add(c);
			}
		} else if (item.equalsIgnoreCase("AddPartRotation")) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 7) {
				boolean always = s.length >= 8 ? toBool(s[7]) : true;
				RotPart c = new RotPart(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]), toFloat(s[4]),
						toFloat(s[5]), toFloat(s[6]), always, "rotpart" + this.partThrottle.size());

				this.partRotPart.add(c);
			}
		} else if (item.compareTo("addpartcamera") == 0) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 3) {
				boolean ys = s.length >= 4 ? toBool(s[3]) : true;
				boolean ps = s.length >= 5 ? toBool(s[4]) : false;
				Camera c = new Camera(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), 0.0F, -1.0F, 0.0F,
						"camera" + this.cameraList.size(), ys, ps);

				this.cameraList.add(c);
			}
		} else if (item.equalsIgnoreCase("AddPartWheel")) {
			String[] s = splitParam(data);
			if (s.length >= 3) {
				float rd = s.length >= 4 ? toFloat(s[3], -1800.0F, 1800.0F) : 0.0F;
				float rx = s.length >= 7 ? toFloat(s[4]) : 0.0F;
				float ry = s.length >= 7 ? toFloat(s[5]) : 1.0F;
				float rz = s.length >= 7 ? toFloat(s[6]) : 0.0F;
				float px = s.length >= 10 ? toFloat(s[7]) : toFloat(s[0]);
				float py = s.length >= 10 ? toFloat(s[8]) : toFloat(s[1]);
				float pz = s.length >= 10 ? toFloat(s[9]) : toFloat(s[2]);
				this.partWheel.add(new PartWheel(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), rx, ry, rz, rd, px, py,
						pz, "wheel" + this.partWheel.size()));

			}

		} else if (item.equalsIgnoreCase("AddPartSteeringWheel")) {
			String[] s = splitParam(data);
			if (s.length >= 7) {
				this.partSteeringWheel.add(new PartWheel(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]),
						toFloat(s[4]), toFloat(s[5]), toFloat(s[6]), "steering_wheel" + this.partSteeringWheel.size()));

			}

		} else if (item.equalsIgnoreCase("AddTrackRoller")) {
			String[] s = splitParam(data);
			if (s.length >= 3) {
				this.partTrackRoller.add(new TrackRoller(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]),
						"track_roller" + this.partTrackRoller.size()));

			}

		} else if (item.equalsIgnoreCase("AddCrawlerTrack")) {
			this.partCrawlerTrack.add(createCrawlerTrack(data, "crawler_track" + this.partCrawlerTrack.size()));

		} else if (item.equalsIgnoreCase("PivotTurnThrottle")) {
			this.pivotTurnThrottle = toFloat(data, 0.0F, 1.0F);
		} else if (item.equalsIgnoreCase("TrackRollerRot")) {
			this.trackRollerRot = toFloat(data, -10000.0F, 10000.0F);
		} else if (item.equalsIgnoreCase("PartWheelRot")) {
			this.partWheelRot = toFloat(data, -10000.0F, 10000.0F);
		} else if (item.compareTo("camerazoom") == 0) {
			this.cameraZoom = toInt(data, 1, 10);
		} else if (item.equalsIgnoreCase("DefaultFreelook")) {
			this.defaultFreelook = toBool(data);
		} else if (item.equalsIgnoreCase("BoundingBox")) {
			String[] s = data.split("\\s*,\\s*");
			if (s.length >= 5) {
				float df = s.length >= 6 ? toFloat(s[5]) : 1.0F;
				MCH_BoundingBox c = new MCH_BoundingBox(toFloat(s[0]), toFloat(s[1]), toFloat(s[2]), toFloat(s[3]),
						toFloat(s[4]), df);

				this.extraBoundingBox.add(c);
				if (c.boundingBox.maxY > this.markerHeight) {
					this.markerHeight = ((float) c.boundingBox.maxY);
				}
				this.markerWidth = ((float) Math.max(this.markerWidth, Math.abs(c.boundingBox.maxX) / 2.0D));
				this.markerWidth = ((float) Math.max(this.markerWidth, Math.abs(c.boundingBox.minX) / 2.0D));
				this.markerWidth = ((float) Math.max(this.markerWidth, Math.abs(c.boundingBox.maxZ) / 2.0D));
				this.markerWidth = ((float) Math.max(this.markerWidth, Math.abs(c.boundingBox.minZ) / 2.0D));

				this.bbZmin = ((float) Math.min(this.bbZmin, c.boundingBox.minZ));
				this.bbZmax = ((float) Math.min(this.bbZmax, c.boundingBox.maxZ));
			}
		} else if (item.equalsIgnoreCase("RotorSpeed")) {
			this.rotorSpeed = toFloat(data, -10000.0F, 10000.0F);
			if (this.rotorSpeed > 0.01D)
				this.rotorSpeed = ((float) (this.rotorSpeed - 0.01D));
			if (this.rotorSpeed < -0.01D)
				this.rotorSpeed = ((float) (this.rotorSpeed + 0.01D));
		} else if (item.equalsIgnoreCase("OnGroundPitchFactor")) {
			this.onGroundPitchFactor = toFloat(data, 0.0F, 180.0F);
		} else if (item.equalsIgnoreCase("OnGroundRollFactor")) {
			this.onGroundRollFactor = toFloat(data, 0.0F, 180.0F);
		}
	}

	public CrawlerTrack createCrawlerTrack(String data, String name) {
		String[] s = splitParam(data);
		int PC = s.length - 3;

		boolean REV = toBool(s[0]);
		float LEN = toFloat(s[1], 0.001F, 1000.0F) * 0.9F;
		float Z = toFloat(s[2]);

		if (PC < 4) {
			return null;
		}
		double[] cx = new double[PC];
		double[] cy = new double[PC];
		for (int i = 0; i < PC; i++) {
			int idx = !REV ? i : PC - i - 1;
			String[] xy = splitParamSlash(s[(3 + idx)]);
			cx[i] = toFloat(xy[0]);
			cy[i] = toFloat(xy[1]);
		}

		List<CrawlerTrackPrm> lp = new ArrayList();

		lp.add(new CrawlerTrackPrm((float) cx[0], (float) cy[0]));
		double dist = 0.0D;
		for (int i = 0; i < PC; i++) {
			double x = cx[((i + 1) % PC)] - cx[i];
			double y = cy[((i + 1) % PC)] - cy[i];
			dist += Math.sqrt(x * x + y * y);
			double dist2 = dist;
			for (int j = 1; dist >= LEN; j++) {
				lp.add(new CrawlerTrackPrm((float) (cx[i] + x * (LEN * j / dist2)),
						(float) (cy[i] + y * (LEN * j / dist2))));

				dist -= LEN;
			}
		}

		for (int i = 0; i < lp.size(); i++) {
			CrawlerTrackPrm pp = (CrawlerTrackPrm) lp.get((i + lp.size() - 1) % lp.size());
			CrawlerTrackPrm cp = (CrawlerTrackPrm) lp.get(i);
			CrawlerTrackPrm np = (CrawlerTrackPrm) lp.get((i + 1) % lp.size());

			float pr = (float) (Math.atan2(pp.x - cp.x, pp.y - cp.y) * 180.0D / 3.141592653589793D);
			float nr = (float) (Math.atan2(np.x - cp.x, np.y - cp.y) * 180.0D / 3.141592653589793D);
			float ppr = (pr + 360.0F) % 360.0F;
			float nnr = nr + 180.0F;
			if ((nnr < ppr - 0.3D) || (nnr > ppr + 0.3D)) {
				if ((nnr - ppr < 100.0F) && (nnr - ppr > -100.0F)) {
					nnr = (nnr + ppr) / 2.0F;
				}
			}
			cp.r = nnr;
		}

		CrawlerTrack c = new CrawlerTrack(name);
		c.len = LEN;
		c.cx = cx;
		c.cy = cy;
		c.lp = lp;
		c.z = Z;
		c.side = (Z >= 0.0F ? 1 : 0);

		return c;
	}

	public String getTextureName() {
		String s = (String) this.textureNameList.get(this.textureCount);
		this.textureCount = ((this.textureCount + 1) % this.textureNameList.size());
		return s;
	}

	public String getNextTextureName(String base) {
		if (this.textureNameList.size() >= 2) {
			for (int i = 0; i < this.textureNameList.size(); i++) {
				String s = (String) this.textureNameList.get(i);
				if (s.equalsIgnoreCase(base)) {
					i = (i + 1) % this.textureNameList.size();
					return (String) this.textureNameList.get(i);
				}
			}
		}
		return base;
	}

	public void preReload() {
		this.creativeOnly = false;
		this.invulnerable = false;
		this.textureNameList.clear();
		this.textureNameList.add(this.name);
		this.cameraList.clear();
		this.cameraPosition.clear();
		this.canopyList.clear();
		this.flare = new Flare();
		this.hatchList.clear();
		this.hudList.clear();
		this.landingGear.clear();
		this.particleSplashs.clear();
		this.searchLights.clear();
		this.partThrottle.clear();
		this.partRotPart.clear();
		this.partCrawlerTrack.clear();
		this.partTrackRoller.clear();
		this.partWheel.clear();
		this.partSteeringWheel.clear();
		this.lightHatchList.clear();
		this.partWeapon.clear();
		this.partWeaponBay.clear();
		this.repellingHooks.clear();
		this.rideRacks.clear();
		this.seatList.clear();
		this.exclusionSeatList.clear();
		this.entityRackList.clear();
		this.extraBoundingBox.clear();
		this.weaponSetList.clear();
		this.lastWeaponIndex = -1;
		this.lastWeaponType = "";
		this.lastWeaponPart = null;
		this.wheels.clear();
		this.unmountPosition = null;
		this.thirdPersonDist = 4.0F;
	}

	public static String[] getCannotReloadItem() {
		return new String[] { "DisplayName", "AddDisplayName", "ItemID", "AddRecipe", "AddShapelessRecipe",
				"InventorySize", "Sound", "UAV", "SmallUAV", "TargetDrone", "Category" };
	}

	public boolean canReloadItem(String item) {
		String[] ignoreItems = getCannotReloadItem();
		for (String s : ignoreItems) {
			if (s.equalsIgnoreCase(item)) {
				return false;
			}
		}
		return true;
	}
}
