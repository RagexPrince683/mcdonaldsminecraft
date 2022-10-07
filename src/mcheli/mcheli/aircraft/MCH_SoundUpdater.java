package mcheli.aircraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcheli.wrapper.W_SoundUpdater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;









@SideOnly(Side.CLIENT)
public class MCH_SoundUpdater
  extends W_SoundUpdater
{
  private final MCH_EntityAircraft theAircraft;
  private final EntityPlayerSP thePlayer;
  private boolean isMoving;
  private boolean silent;
  private float aircraftPitch;
  private float aircraftVolume;
  private float addPitch;
  private boolean isFirstUpdate;
  private double prevDist;
  private int soundDelay = 0;
  
  public MCH_SoundUpdater(Minecraft mc, MCH_EntityAircraft aircraft, EntityPlayerSP entityPlayerSP)
  {
    super(mc, aircraft);
    this.theAircraft = aircraft;
    this.thePlayer = entityPlayerSP;
    this.isFirstUpdate = true;
  }
  




  public void update()
  {
    if ((this.theAircraft.getSoundName().isEmpty()) || (this.theAircraft.getAcInfo() == null))
    {
      return;
    }
    if (this.isFirstUpdate)
    {
      this.isFirstUpdate = false;
      initEntitySound(this.theAircraft.getSoundName());
    }
    
    MCH_AircraftInfo info = this.theAircraft.getAcInfo();
    
    boolean isBeforeMoving = this.isMoving;
    boolean isDead = this.theAircraft.isDead;
    

    if ((isDead) || ((!this.silent) && (this.aircraftVolume == 0.0F)))
    {



      if (isDead) { stopEntitySound(this.theAircraft);
      }
      




      this.silent = true;
      
      if (isDead)
      {
        return;
      }
    }
    
    boolean isRide = this.theAircraft.getSeatIdByEntity(this.thePlayer) >= 0;
    boolean isPlaying = isEntitySoundPlaying(this.theAircraft);
    boolean onPlaySound = false;
    
    if ((!isPlaying) && (this.aircraftVolume > 0.0F))
    {


      if (this.soundDelay > 0)
      {
        this.soundDelay -= 1;
      }
      else
      {
        this.soundDelay = 20;
        playEntitySound(this.theAircraft.getSoundName(), this.theAircraft, this.aircraftVolume, this.aircraftPitch, true);
      }
      





      this.silent = false;
      onPlaySound = true;
    }
    

    float prevVolume = this.aircraftVolume;
    float prevPitch = this.aircraftPitch;
    this.isMoving = (info.soundVolume * this.theAircraft.getSoundVolume() >= 0.01D);
    if (this.isMoving)
    {
      this.aircraftVolume = (info.soundVolume * this.theAircraft.getSoundVolume());
      this.aircraftPitch = (info.soundPitch * this.theAircraft.getSoundPitch());
      if (!isRide)
      {
        double dist = this.thePlayer.getDistance(this.theAircraft.posX, this.thePlayer.posY, this.theAircraft.posZ);
        double pitch = this.prevDist - dist;
        if (Math.abs(pitch) > 0.3D)
        {
          this.addPitch = ((float)(this.addPitch + pitch / 40.0D));
          float maxAddPitch = 0.2F;
          if (this.addPitch < -maxAddPitch) this.addPitch = (-maxAddPitch);
          if (this.addPitch > maxAddPitch) this.addPitch = maxAddPitch;
        }
        this.addPitch = ((float)(this.addPitch * 0.9D));
        this.aircraftPitch += this.addPitch;
        this.prevDist = dist;
      }
      if (this.aircraftPitch < 0.0F)
      {
        this.aircraftPitch = 0.0F;
      }
    }
    else if (isBeforeMoving)
    {
      this.aircraftVolume = 0.0F;
      this.aircraftPitch = 0.0F;
    }
    

    if (!this.silent)
    {
      if (this.aircraftPitch != prevPitch)
      {

        setEntitySoundPitch(this.theAircraft, this.aircraftPitch);
      }
      
      if (this.aircraftVolume != prevVolume)
      {
        setEntitySoundVolume(this.theAircraft, this.aircraftVolume);
      }
    }
    

    boolean updateLocation = false;
    


    updateLocation = true;
    






    if ((updateLocation) && (this.aircraftVolume > 0.0F))
    {
      if (isRide)
      {
        updateSoundLocation(this.theAircraft);
      }
      else
      {
        double px = this.thePlayer.posX;
        double py = this.thePlayer.posY;
        double pz = this.thePlayer.posZ;
        double dx = this.theAircraft.posX - px;
        double dy = this.theAircraft.posY - py;
        double dz = this.theAircraft.posZ - pz;
        double dist = info.soundRange / 16.0D;
        dx /= dist;
        dy /= dist;
        dz /= dist;
        updateSoundLocation(px + dx, py + dy, pz + dz);
      }
      

    }
    else if (isEntitySoundPlaying(this.theAircraft))
    {
      stopEntitySound(this.theAircraft);
    }
  }
}
