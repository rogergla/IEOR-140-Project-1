
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 *
 * @author glassey
 */
public class Milestone2 {

    public static void main(String[] args) {
        System.out.println(" Hello World");
        Button.waitForAnyPress();
        Delay.msDelay(2000);
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 11.75f, Motor.A, Motor.B, false);
        pilot.setRotateSpeed(90);
//      pilot.setTravelSpeed(400);
        for (int i = 0; i < 8; i++) {
            pilot.travel(90);
            pilot.stop();
            pilot.rotate(90);
            pilot.stop();
//        System.out.println("tachoA" + Motor.A.getTachoCount());
//        System.out.println("tachoB" + Motor.B.getTachoCount());
        }

        pilot.rotate(90);
        for (int i = 0; i < 8; i++) {
            pilot.travel(90);
            pilot.stop();
            pilot.rotate(-90);
            pilot.stop();
        }
//      Button.waitForAnyPress();
    }
}
