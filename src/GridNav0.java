
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.*;
import lejos.util.*;

/**
 * input x,y from buttons.   No obstacles

 */
public class GridNav0
{

 
  /**
   *constructor - specifies which motors and sensor ports are left and right
   */
  public GridNav0(Tracker theTracker)
  {
    tracker = theTracker;
  }

  /**
   *go to the destination
   */
  void navigate()
  {
    
    int turnAngle;
    int nextHeading;
    while (!equals(_position, _destination))
    {
      nextHeading = newHeading();
      turnAngle = (int) (nextHeading - _heading);
      if(turnAngle < -2) turnAngle +=4;
      if(turnAngle > 2) turnAngle -= 4;
      if (turnAngle != 0)
      {
        while (tracker.pilot.isMoving())
          Thread.yield();
        tracker.turn(turnAngle);
        _heading = nextHeading;
      }

      tracker.trackLine();
      if (_heading < 2)
      {
        _position[_heading]++;// increase in x or y
      } else
      {
        _position[_heading - 2]--;
      }
      System.out.println("head " + _heading + " X " + _position[0] + " Y " + _position[1]);
      Sound.playTone(800 + 50 * _position[0], 100);
      Sound.playTone(800 + 50 * _position[1], 100);

    }
  }

  /**
   *returns heading between -1 and 2
   */
  private int newHeading()
  {
    int head = (int) Math.signum(_destination[0] - _position[0]);
    if (head == 0)
    {  //move in y direction
      head =  (int) Math.signum(_destination[1] - _position[1]);
    } else
    {
      head = 1 - head;
    }
    return (int) head;
  }
  
/**
   * destination coordinates from button counter
   */
  public void getDestination()
  {

    bc.count("Dest x,y", _destination[0], _destination[1]);
    _destination[0] = bc.getLeftCount();
    _destination[1] = bc.getRightCount();
  }


  boolean equals(int[]a, int[] b)
  {
    return a[0] == b[0] && a[1] == b[1];
  }
  
/**
   * carries out the mission
   */
  public  void go()
  {
    tracker.calibrate();
    while(true)
    {
      getDestination();
      navigate();
    }
  }

   /**
   *responsible for following the line to a marker, turning at the marker
   */
  Tracker tracker;

  int[] _position = new int[2];  // where am i
  int[] _destination = new int[2];// where am I going?
  int _heading = 0; // direction I am facing
  ButtonCounter bc = new ButtonCounter();

}
