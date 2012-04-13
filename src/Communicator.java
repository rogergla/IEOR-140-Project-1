import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import java.io.*;
import lejos.nxt.*;


/**
 * test communications with GrigNavControl
 * @author glassey
 */
public class Communicator {
   
   DataInputStream dataIn;
   DataOutputStream dataOut;
      private int x = 0;
      private  int y = 0;

      
      /**
       * connects 
       */
  public void connect()
 {

      LCD.drawString("waiting",0,0);
      BTConnection btc = Bluetooth.waitForConnection(); // this method is very patient. 
      LCD.clear();
      LCD.drawString("connected",0,0);
      try 
      {
         dataIn = btc.openDataInputStream();
         dataOut = btc.openDataOutputStream();
      } catch(Exception e) {};
      Sound.beepSequence();
   }
      public int[] getDestination()
   {
      try
      {     
         x = dataIn.readInt();
         y = dataIn.readInt();
      }catch (IOException e) {System.out.println("InputError");}

     LCD.drawString("Read ",0,3);
      LCD.drawInt(x, 4, 0,4);
      LCD.drawInt(y, 4, 8,4);
      int xy[] = {x,y};
      return xy;
   }
   
   public void sendData(int code, int x, int y)
   {
      try 
      {
         dataOut.writeInt(code);
         dataOut.writeInt(x);
         dataOut.writeInt(y);
         dataOut.flush();
      }catch (IOException e) {};
      LCD.drawString("SEND "+code,0,5);
      LCD.drawInt(x, 4, 0,6);
      LCD.drawInt(y, 4, 8,6);
   }
  
}
