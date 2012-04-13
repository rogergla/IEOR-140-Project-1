

import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

/**
 *
 *
 */
public class GridNavTest
{
 
   public static void main(String[] args)
   {
     DifferentialPilot pilot = PilotFactory.makeMetricGridbot();
    LightSensor left = new LightSensor(SensorPort.S1);
    LightSensor right = new LightSensor(SensorPort.S4);
    Tracker tracker = new Tracker(pilot, left, right);
    GridNav0 robot = new GridNav0(tracker);
    robot.go();
   }
}
