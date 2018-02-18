/**
 * This class is meant as a skeleton for the odometer class to be used.
 * 
 * @author Rodrigo Silva
 * @author Dirk Dubois
 * @author Derek Yu
 * @author Karim El-Baba
 * @author Michael Smith
 */

package ca.mcgill.ecse211.odometer;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Odometer extends OdometerData implements Runnable{

  private OdometerData odoData;
  private static Odometer odo = null; // Returned as singleton
  private double x;
  private double y;
  private double theta;
  private int nowleftMotorTachoCount;
  private int nowrightMotorTachoCount;
  public static final double WHEEL_RAD = 2.05;// play with values of wheel radius and Track(wheel base) until robot return to it starting position
  public static final double TRACK = 9.7;

  private static Object lock;
  // Motors and related variables
  private int leftMotorTachoCount;
  private int rightMotorTachoCount;
  static EV3LargeRegulatedMotor leftMotor;
  static EV3LargeRegulatedMotor rightMotor;
  private double[] position;


  private static final long ODOMETER_PERIOD = 25; // odometer update period in ms

  /**
   * This is the default constructor of this class. It initiates all motors and variables once.It
   * cannot be accessed externally.
   * 
   * @param leftMotor
   * @param rightMotor
   * @throws OdometerExceptions
   */
  public Odometer(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor ,final double TRACK, final double WHEEL_RAD) {
	    this.leftMotor = leftMotor;
	    this.rightMotor = rightMotor;
	    this.x = 0.0;
	    this.y = 0.0;
	    this.theta = 0.0;
	    this.leftMotorTachoCount = 0;
	    this.rightMotorTachoCount = 0;
	    lock = new Object();
	  }

  /**
   * This method is meant to ensure only one instance of the odometer is used throughout the code.
   * 
   * @param leftMotor
   * @param rightMotor
   * @return new or existing Odometer Object
   * @throws OdometerExceptions
   */
  public synchronized static Odometer getOdometer(EV3LargeRegulatedMotor leftMotor,
      EV3LargeRegulatedMotor rightMotor, final double TRACK, final double WHEEL_RAD)
      throws OdometerExceptions {
    if (odo != null) { // Return existing object
      return odo;
    } else { // create object and return it
      odo = new Odometer(leftMotor, rightMotor, TRACK, WHEEL_RAD);
      return odo;
    }
  }

  /**
   * This class is meant to return the existing Odometer Object. It is meant to be used only if an
   * odometer object has been created
   * 
   * @return error if no previous odometer exists
   */
  public synchronized static Odometer getOdometer() throws OdometerExceptions {

    if (odo == null) {
      throw new OdometerExceptions("No previous Odometer exits.");

    }
    return odo;
  }

  /**
   * This method is where the logic for the odometer will run. Use the methods provided from the
   * OdometerData class to implement the odometer.
   */
  // run method (required for Thread)
  public void run() {
    long updateStart, updateEnd;
    int LtachocountOld, RtachocountOld;
    LtachocountOld = 0;
    RtachocountOld = 0; 
    double distL, distR, deltaD, deltaT, dX, dY, Theta, X, Y;
    
    X = 0;
    Y = 0;
    Theta = 0; 

    while (true) {
      updateStart = System.currentTimeMillis();

      leftMotorTachoCount = leftMotor.getTachoCount();
      rightMotorTachoCount = rightMotor.getTachoCount();
     

      // TODO Calculate new robot position based on tachometer counts
      //////////our code below////////////

      
      distL = 3.14159*WHEEL_RAD*(leftMotorTachoCount - LtachocountOld)/180;
      distR = 3.14159*WHEEL_RAD*(rightMotorTachoCount - RtachocountOld)/180;
      deltaD = 0.5*(distL+distR);
      deltaT = ((distL-distR)/TRACK);
      Theta += deltaT;
      if (Theta > (2*Math.PI)) {
    	  Theta = Theta - (2*Math.PI);
      }
      
      if (Theta < 0) {
   	   Theta = Theta + (2*Math.PI);
     } 
     dX = deltaD * Math.sin(Theta);
     dY = deltaD * Math.cos(Theta);
  /*   X += dX;
     Y += dY;
    */ 
     LtachocountOld = leftMotor.getTachoCount();
     RtachocountOld = rightMotor.getTachoCount();

       // TODO Update odometer values with new calculated values
      odo.update(dX, dY, (deltaT*180/Math.PI));

      // this ensures that the odometer only runs once every period
      updateEnd = System.currentTimeMillis();
      if (updateEnd - updateStart < ODOMETER_PERIOD) {
        try {
          Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
        } catch (InterruptedException e) {
          // there is nothing to be done
        }
      }
    }
  } public void getPosition(double[] position, boolean[] update) {
	    // ensure that the values don't change while the odometer is running
	    synchronized (lock) {
	      if (update[0])
	        position[0] = x;
	      if (update[1])
	        position[1] = y;
	      if (update[2])
	    	position[2] = getTheta();// changed theta to getTheta() to convert rad to degrees
	    }
	  }

	  public double getX() {
	    double result;

	    synchronized (lock) {
	      result = x;
	    }

	    return result;
	  }

	  public double getY() {
	    double result;

	    synchronized (lock) {
	      result = y;
	    }

	    return result;
	  }

	  public double getTheta() {
	    double result;

	    synchronized (lock) {
	    	
	      result = theta*(180/3.14159); // change rad to deg
	      
	    }

	    return result;
	  }
  

}
