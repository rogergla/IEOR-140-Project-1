
import java.io.*;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * includes communications  
 * @author owner.GLASSEY
 */
public class GridNavComm0 extends GridNavSP
{

  String connected = "Connected";
  String waiting = "Waiting";
  BTConnection btc;
  DataInputStream dataIn;
  DataOutputStream dataOut;
  private boolean firstDest = true;

  GridNavComm0(Tracker theTracker, SensorPort echo, int Xlength, int Ylength)
  {
    super(theTracker, echo, Xlength, Ylength);
  }

  public void go()
  {
    tracker.calibrate();
    keepGoing = true;
    heading = 0;
    connect();
    while (keepGoing)
    {
      setDestination();
      if (grid.getDestination().isBlocked())
      {
        Sound.buzz();
        return;
      }
      navigate();
      Sound.beepSequence();
      LCD.drawInt(10 * position.getX() + position.getY(), 0, 0);
    }
  }

  public void connect()
  {
    Sound.playTone(1600, 300);
    LCD.clear();
    LCD.drawString(waiting, 0, 0);
    LCD.refresh();
    BTConnection btc = Bluetooth.waitForConnection(); // this method is very patient. 
    LCD.clear();
    LCD.drawString(connected, 0, 0);
    LCD.refresh();
    try
    {
      dataIn = btc.openDataInputStream();
      dataOut = btc.openDataOutputStream();
    } catch (Exception e)
    {
    };
    Sound.beepSequence();
  }

  public void setDestination()
  {
    int xy[];
    int x = 0;
    int y = 0;
    try
    {
      x = dataIn.readInt();
      y = dataIn.readInt();
    } catch (IOException e)
    {
    };
    grid.setDestination(x, y);
    if (grid.getDestination().isBlocked())
    {
      Sound.buzz();
    }
  }

  public void sendData(Node n)
  {
    try
    {
      int code = 0;
      if (n.isBlocked())
        code = 1;
      dataOut.writeInt(code);
      dataOut.writeInt(n.getX());
      dataOut.writeInt(n.getY());
      dataOut.flush();
    } catch (IOException e)
    {
    };
    super.sendData(n);
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    DifferentialPilot pilot = new DifferentialPilot(2.2f, 4.9f, Motor.A, Motor.B);
    LightSensor left = new LightSensor(SensorPort.S1);
    LightSensor right = new LightSensor(SensorPort.S4);
    Tracker tracker = new Tracker(pilot, left, right);

    GridNavComm gb = new GridNavComm(tracker, SensorPort.S2, 4, 4);
    gb.go();
  }
}
