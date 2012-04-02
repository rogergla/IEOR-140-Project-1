
import lejos.nxt.*;
import lejos.util.Delay;
import lejos.robotics.navigation.DifferentialPilot;

/**
 *
 * @author Team 3
 */
public class PolyTracer {

    DifferentialPilot pilot;

    public PolyTracer(DifferentialPilot pilot) {
        this.pilot = pilot;
        pilot.stop();
    }

    public void polygon(int sides, int length, String direction) { //length should be in cm.
        if (!direction.equals("left") && !direction.equals("right")) {
            System.out.println("WTF is that direction?");
            Button.waitForAnyPress();
            System.exit(1);
            return;
        } else {
            int turn_angle = 180-((sides - 2) * 180 / sides);
            for (int i = 0; i < sides; i++) {
                pilot.stop();
                pilot.travel(length);
                if (direction.equals("right")) {
                    pilot.rotate(turn_angle * -1);
                } else {
                    pilot.rotate(turn_angle);
                }
//                pilot.stop();
            }
        }
    }

    public void arc(double radius, double degrees, String direction) {
        if (!direction.equals("left") && !direction.equals("right")) {
            System.out.println("WTF is that direction?");
            Button.waitForAnyPress();
            System.exit(1);
            return;
        } else {
            if (direction.equals("right")) {
                pilot.arc(-1*radius, -1*degrees);
            } else {
                pilot.arc(radius, degrees);
            }
            pilot.stop();
        }
    }
}
