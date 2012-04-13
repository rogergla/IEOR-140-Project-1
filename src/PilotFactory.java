import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 *
 * @author owner.GLASSEY
 */
public class PilotFactory
{

   /**
    * @param args the command line arguments
    */
   public static DifferentialPilot  makeWideEnglih()
   
   {
     return new DifferentialPilot(2.16,5.28, Motor.A, Motor.C);
   }
    
   public static DifferentialPilot  makeWideMetric() 
   {
     return new DifferentialPilot(5.6,13.7, Motor.A, Motor.C);
   }
    public static DifferentialPilot  makeMetricGridbot() 
   {
     return new DifferentialPilot(5.6,12, Motor.A, Motor.C);
   }
}
