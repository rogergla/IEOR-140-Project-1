
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.*;
import java.io.*;

/**
This class needs a higher level controller to implement the navigtion logic<br>
Responsibility: keep robot on the line till it senses a marker, then stop <br>
also controls turning to a new line at +- 90 or 180 deg<br>
Hardware: Two light sensors , shielded, 2 LU above floor.
Classes used:  Pilot, LightSensors<br>
Control Algorithm:  proportional control. estimate distance from centerline<br>
Calibrates both sensors to line, background
Updated 9/10/2007  NXT hardware
@author Roger Glassey
 */
public class Tracker
{

  /**
   * controls the motors
   */
  public DifferentialPilot pilot;
  /**
   *set by constructor , used by trackline()
   */
  private LightSensor leftEye;
  /**
   *set by constructor , used by trackline()
   */
  private LightSensor rightEye;
  /**
   * scaled lightSensor value for marker; used by atMarker()
   **/
  private int _marker = -20; // will be reset by calibration
  
  private int _turnDirection = 1;


  /**
   *constructor - specifies which sensor ports are left and right
   */
//	public Tracker( Pilot thePilot,SensorPort leftI,SensorPort rightI)
  public Tracker(DifferentialPilot thePilot, LightSensor leftEye , LightSensor  rightEye)
  {
    pilot = thePilot;
    pilot.setTravelSpeed(20);
    pilot.setRotateSpeed(180);
    pilot.setAcceleration(8000);
    this.leftEye = leftEye;
    this.leftEye.setFloodlight(true);
    this.rightEye = rightEye;
    this.rightEye.setFloodlight(true);
  }

 

  /**
  follow line till intersection is detected
  uses proportional  control <br>
  Error signal is supplied by CLdistance()<br>
  uses CLdistance(), pilot.steer()
  loop execution about 65 times per second in 1 sec.<br>
   */
  public void trackLine()
  {
    float gain = 0.30f;// proportional control 1.0
    pilot.forward();
    boolean atMarker = false;
    int error = 0;// approximate offset from center of line, units about 0.12 mm

    while (!atMarker)
    {                                                 
      int lval = leftEye.getLightValue();
      int rval = rightEye.getLightValue();
      LCD.drawInt(lval,4, 0, 3);
      LCD.drawInt(rval,4, 8, 3);
      atMarker = (Math.min(lval, rval) < _marker);
      if (!atMarker)
      {
        error = CLDistance(lval, rval);
        int control = (int) (gain * error );
        LCD.drawInt(control, 4, 12, 3);
        pilot.steer(control);
      } else
      {
        float advance = 8.5f; // distance from light censor to wheel base
        float markerSize = 4;
        Sound.playTone(1000, 100);
        atMarker = true;
        pilot.travel(advance,true);// let the navigator do something before stopping
        while(pilot.getMovementIncrement()< markerSize); //sensor is past the marker
      }
    }
  }

  /**
   * helper method for Tracker; calculates distance from centerline, used as error by trackLine()
   * @param left light reading
   * @param right light reading
   * @return  distance
   */
  int CLDistance(int left, int right)
  {
    int zone = 90;  // so bright that sensor is not seeing the line
    int dist = 0;
    
    if (left > right +60)    _turnDirection = -1;  //turn away from light
    else if(right > left + 60) _turnDirection = 1;
   
    if (left < zone && right < zone)   dist = right - left;  // both sensors see line   
    else dist = _turnDirection * (right + left);
    LCD.drawInt(left,4,0,1);
    LCD.drawInt(right,3, 5, 1);
    LCD.drawInt(dist, 4,10, 1);
    LCD.drawInt(_turnDirection,2,14,1);
    return dist;
  }

  /**
  Turn to line at angle (multiple of 90 degrees) from current heading<br>
  motors stop on exit if arg non zero, else no motor change;
   */
  public void turn(int angle)
  {
    if (angle == 0)
    {
      return;
    } else
    {
      while (pilot.isMoving())
      {
        Thread.yield();
      }
    }
    pilot.rotate(angle * 90);
  }

  public void stop()
  {
    pilot.stop();
  }

  /**
  calibrates for line first, then background, then marker with left sensor.  displays light sensor readings on LCD (percent)<br>
  Then displays left sensor (scaled value).  Move left sensor  over marker, press Enter to set marker value to sensorRead()/2
   */
  public void calibrate()
  {
      System.out.println("Calibrate Tracker");
    File data = new File("Track.dat");
    if (!data.exists())
    {
      try
      {
        data.createNewFile();
      } catch (IOException ioe)
      {
      }
      while (0 < Button.readButtons())
      {
        Thread.yield();// wait for release
      }
      for (byte i = 0; i < 3; i++)
      {
        while (0 == Button.readButtons())//wait for press
        {
          LCD.drawInt(leftEye.getLightValue(), 4, 6, 1 + i);
          LCD.drawInt(rightEye.getLightValue(), 4, 12, 1 + i);
          if (i == 0)
          {
            LCD.drawString("LOW", 0, 1 + i);
          } else if (i == 1)
          {
            LCD.drawString("HIGH", 0, 1 + i);
          } else
          {
            LCD.drawString("BLACK", 0, 1 + i);
          }
        }
        Sound.playTone(1000 + 200 * i, 100);
        if (i == 0)
        {
          leftEye.calibrateLow();
          rightEye.calibrateLow();
        } else if (i == 1)
        {
          rightEye.calibrateHigh();
          leftEye.calibrateHigh();
        } else
        {
          _marker = leftEye.getLightValue() / 2;
        }
        while (0 < Button.readButtons())
        {
          Thread.yield();//button released
        }
        try
        {
          DataOutputStream dos = new DataOutputStream(new FileOutputStream(data));
          dos.writeInt(leftEye.getLow());
          dos.writeInt(rightEye.getLow());
          dos.writeInt(leftEye.getHigh());
          dos.writeInt(rightEye.getHigh());
          dos.writeInt(_marker);
          dos.flush();
          dos.close();
        } catch (IOException ioe)
        {
        }
      }
    } else
    {
      try
      {
        DataInputStream dis = new DataInputStream(new FileInputStream(data));
        leftEye.setLow(dis.readInt());
        rightEye.setLow(dis.readInt());
        leftEye.setHigh(dis.readInt());
        rightEye.setHigh(dis.readInt());
        _marker = dis.readInt();
      } catch (IOException ioe)
      {
      }

    }
    while (0 == Button.readButtons())// while no press
    {
      int lval = leftEye.getLightValue();
      int rval = rightEye.getLightValue();
      LCD.drawInt(lval, 4, 0, 5);
      LCD.drawInt(rval, 4, 4, 5);
      LCD.drawInt(CLDistance(lval, rval), 4, 12, 5);
      LCD.refresh();
    }
    LCD.clear();
  }

}

