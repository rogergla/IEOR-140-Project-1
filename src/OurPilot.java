import lejos.nxt.*;

/**
 * 
 * @author Team 3
 */
public class OurPilot {

	boolean clockwise;
	float track_width;
	float diameter;

	public OurPilot(float diameter, float track_width) {
		this.track_width = track_width;
		this.diameter = diameter;

	}

	void rotate(int angle, boolean clockwise) {
		float amount_rotate = angle * track_width / diameter;

		if (clockwise) {
			Motor.B.rotate((int) amount_rotate, true);
			Motor.A.rotate((int) (-1 * amount_rotate));
		} else {
			Motor.B.rotate((int) (-1 * amount_rotate), true);
			Motor.A.rotate((int) amount_rotate);
		}
	}

	void stop() {
		Motor.A.stop(true);
		Motor.B.stop();
	}

	void travel(double distance) {
		double rotate_angle = 360 * distance / (Math.PI * diameter);

		Motor.A.rotate((int) rotate_angle, true);
		Motor.B.rotate((int) rotate_angle);
	}
}
