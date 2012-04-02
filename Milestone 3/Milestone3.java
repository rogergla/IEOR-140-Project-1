import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.DifferentialPilot;

/**
 *
 * @author Team 3
 */
public class Milestone3 {
    
    public static void main(String[] args) {
        System.out.println("hello world!");
        Button.waitForAnyPress();
        Delay.msDelay(2000);
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 12.1f, Motor.B, Motor.A);
        PolyTracer geomPilot = new PolyTracer(pilot);
        // trace a triangle with side length of 24 inches , making turns to the left.
        geomPilot.polygon(3, 61, "left");
        Button.waitForAnyPress();
        Delay.msDelay(2000);
        //trace a pentagon with the same length, but making turns to the right. 
        geomPilot.polygon(5, 61, "right");
        Button.waitForAnyPress();
        Delay.msDelay(2000);
        //trace a semi-circle, turning to the left with radius 36" 
        geomPilot.arc(91.4, 180, "left");
        Button.waitForAnyPress();
        Delay.msDelay(2000);
        //trace a quarter circle, turning right with a radius of 12" 
        geomPilot.arc(30.5, 90, "right");
        System.out.println("thanks for watching!");
    }
    
}
