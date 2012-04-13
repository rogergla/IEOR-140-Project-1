
import java.io.*;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;


/**
 * includes communications  
 * @author owner.GLASSEY
 */
public class GridNavComm extends GridNavSP
{

  Communicator comm;
  private boolean firstDest = true;

  GridNavComm(Tracker theTracker, SensorPort echo, int Xlength, int Ylength)
  {
    super(theTracker, echo, Xlength, Ylength);
    comm = new Communicator();
    comm.connect();
  }


  public void setDestination()
  {
    int xy[]= comm.getDestination();
    grid.setDestination(xy[0], xy[1]);
    if (grid.getDestination().isBlocked())
    {
      Sound.buzz();
    }
  }

  public void sendData(Node n)
  {    
      int code = 0;
      if (n.isBlocked())    code = 1;   
      comm.sendData(code, n.getX(),n.getY());
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
