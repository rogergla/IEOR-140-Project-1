
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;


/**
 *
 * @author glassey
 */
public class Milestone1 {

    public static void main(String[] args) {
        System.out.println(" Hello World");
        Button.waitForAnyPress();
        Delay.msDelay(2000);

        Pilot1 pilot = new Pilot1(5.6f,12.5f);

        for (int i = 0; i < 8; i++) {
            pilot.travel(90);
            pilot.stop();
            pilot.rotate(90,true);
            pilot.stop();

        }

        pilot.rotate(90, true);
        for (int i = 0; i < 8; i++) {
            pilot.travel(90);
            pilot.stop();
            pilot.rotate(90,false);
            pilot.stop();
        }
//      Button.waitForAnyPress();
    }
}
