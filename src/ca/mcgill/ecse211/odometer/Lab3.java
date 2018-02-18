//Some code is commented out because we would get an exception error when we try to run the PCntroller 

package ca.mcgill.ecse211.odometer;



import lejos.hardware.lcd.TextLCD;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

import lejos.hardware.Button;

import lejos.hardware.ev3.LocalEV3;



public class Lab3 {
	
  // Motors
  private static final EV3LargeRegulatedMotor leftMotor =
		   new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
  private static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  
//MAP FOR X

  public static double [] locationX = {0,1,2,2,1};//2,2,0,1//2,1,2,2,1
  
//MAP FOR Y (

  public static double [] locationY = {2,1,2,1,0};//1,2,2,1

  public static final double WHEEL_RAD = 2.08;
  
  public static final double TRACK = 10.5;

 public static void main(String[] args) throws OdometerExceptions {
	 
    int buttonChoice;
    
    // Setup objects
    
    final TextLCD t = LocalEV3.get().getTextLCD();
    
    Odometer odometer = Odometer.getOdometer(leftMotor, rightMotor, TRACK, WHEEL_RAD);
    
    Display odometryDisplay = new Display(t);
    
    
   
    OdometerData data = new OdometerData();
    
    final Navigation navigation = new Navigation(odometer);
    do {
      // clear the display
      t.clear();

      // ask the user to press left button to start
      t.drawString("press left button", 0, 0);


      buttonChoice = Button.waitForAnyPress();
    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);

    if (buttonChoice == Button.ID_LEFT) {

      // Start the odometers and navigation
      Thread odometerThread = new Thread(odometer);
      Thread odometryDisplayThread = new Thread(odometryDisplay);
      //usPoller.start();
      Thread navigateThread = new Thread(navigation);

      data.start();
      odometerThread.start();
      
      odometryDisplayThread.start();
      navigateThread.start();
      
      

      // spawn a new Thread to avoid navigation from blocking
   (new Thread() {
        public void run() {


          // Map1
        //  navigation.travelTo(0, 0);
         for (int i = 0; i < locationX.length; i++){
        	 
        	 navigation.travelTo(locationX [i], locationY [i]);
        	 
         }
        }
        
      }).start();
   
    }
   

    while (Button.waitForAnyPress() != Button.ID_ESCAPE);
    
    System.exit(0);
  

 
}
  
}

