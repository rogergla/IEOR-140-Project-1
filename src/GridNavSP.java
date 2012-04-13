
import lejos.nxt.*;
import lejos.robotics.navigation.*;
import lejos.util.*;
import lejos.nxt.comm.RConsole;

/**
**Shortest path navigation <br>
Uses Tracker,  ButtonCounter, Node, Grid, and UltraSonic sensor  <br>
 * 
 */
public class GridNavSP
{

  public Tracker tracker;
  public UltrasonicSensor ping;
  public ButtonCounter bc = new ButtonCounter();
  public Grid grid;
  public Node position; // position coordinates
  /**
   *state variable - set by stateChanged() used by navigate()
   */
//	public boolean hit = false;
  /**
   * direction of movement; multiple of 90 degrees
   */
  public int heading;
  /**
   *set by go()
   */
  public boolean keepGoing = true;

  /**
   *initialize touch sensor, listener
   */
  public GridNavSP(Tracker theTracker, SensorPort echo, int Xlength, int Ylength)
  {
    tracker = theTracker;
    ping = new UltrasonicSensor(echo);
    grid = new Grid(Xlength, Ylength);
    position = grid.nodes[0][0];
    heading = 0;
  }

  /**
  Calibrate line first, then background, calls setDestination()<br>
  calls navigate()
   */
  public void go()
  {
    tracker.calibrate();
    keepGoing = true;
    heading = 0;
    RConsole.openBluetooth(0);
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

  public void setDestination()
  {
    bc.count("Destination");
    grid.setDestination((int) bc.getLeftCount(), (int) bc.getRightCount());
    if (grid.getDestination().isBlocked())
    {
      Sound.buzz();
      grid.setDestination(position.getX(), position.getY());
    }
    grid.recalc();
  }

  /**
   *shows position ; override in communicating subclasses
   */
  public void sendData(Node n)
  {
    int code = 0;
    if (n.isBlocked())
      code = 1;
    LCD.drawInt(n.getX(), 2, 0, 1 + code);
    LCD.drawInt(n.getY(), 2, 3, 1 + code);
    Sound.playTone(800 + 200 * code, 200);
  }

  /**
  called by go()
  Maintains position and heading, update after reaching a node<br>
  Return when destination is reached<br>
  calls tracker.turn(), tracker.trackline(), checkForData(), sendBlock(),sendPosition()
   */
  public void navigate()
  {
    grid.recalc();

    while (position.getDistance() > 0)
    {
      turnToBest();
      while (checkBlock())
        turnToBest();
      if (grid.getDestination().isBlocked())
      {
        Sound.buzz();
        return;
      }
      tracker.trackLine();
      position = grid.neighbor(position, heading);//update position
      sendData(position);
    }
    Sound.beepSequenceUp();
  }

  /**
   * check echo for a block in current heading. block the node and recalc shortest path. 
   * @return
   */
  public boolean checkBlock()
  {
    Delay.msDelay(50);
    int dist = ping.getDistance();
    LCD.drawInt(dist, 4, 0, 5);
    RConsole.println("checkBlock dist " + dist);
    if (dist > 27)
      return false;

    Node blockedNode = position;
//		for(int i = 0 ; i < n ; i++)  //which node is it really?
    {
      blockedNode = grid.neighbor(blockedNode, heading);
      if (blockedNode == null)
        return false;
    }
    RConsole.println("Block node " + blockedNode.getX() + " " + blockedNode.getY());
    if (blockedNode.isBlocked())
      return false;
    blockedNode.blocked();
    grid.recalc();
    sendData(blockedNode);
    return true;
  }

  protected void turnToBest()
  {

    int newHeading = bestDirection();
    RConsole.println("turn newHeading " + newHeading + " at " + position.getX() + " "
            + position.getY());
    tracker.turn(normalize(newHeading - heading));
    heading = newHeading;
  }

  /**
  uses and updates heading
   */
  protected int bestDirection()
  {
    Node n = grid.neighbor(position, heading);
    int minDist = Grid.BIG;
    int dir = heading;
    if (n != null && !n.isBlocked())
      minDist = n.getDistance();
    for (int d = 0; d < 4; d++)
    {//iterate over all neighbors
      n = grid.neighbor(position, d);
      if (n != null && !n.isBlocked() && n.getDistance()< minDist)
      {
        minDist = n.getDistance();
        dir = d;
        RConsole.println("Dir " + dir + " minDist " + minDist);
      }
    }

    return dir;
  }

  /**
   *updates heading, returns angle between -2 and 2
   */
  private int normalize(int angle)
  {
    if (angle < -2)
      angle += 4;
    else if (angle > 2)
      angle -= 4;
    return angle;
  }

  /**
   *calls  go() in new robot
   */
  public static void main(String[] args)
  {
    DifferentialPilot pilot = new DifferentialPilot(2.2f, 4.9f, Motor.A, Motor.C);
    LightSensor left = new LightSensor(SensorPort.S1);
    LightSensor right = new LightSensor(SensorPort.S4);
    Tracker tracker = new Tracker(pilot, left, right);
    GridNavSP gb = new GridNavSP(tracker, SensorPort.S2, 6, 8);;
    gb.go();


  }
}
