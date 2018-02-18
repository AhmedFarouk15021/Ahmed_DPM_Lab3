package ca.mcgill.ecse211.lab3;

import ca.mcgill.ecse211.wall_following.UltrasonicController;
import ca.mcgill.ecse211.odometer.*;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import ca.mcgill.ecse211.odometer.*;

public class ObNavigation implements Runnable{
	
	private static final int FORWARD_SPEED = 200;
	private static final int ROTATE_SPEED = 100;
	
	private static final double WHEEL_RAD = 2.2;
	private static final double TRACK = 10.4;

	private static Odometer odometer;
	private static OdometerData odoData;
	private static EV3LargeRegulatedMotor leftMotor;
	private static EV3LargeRegulatedMotor rightMotor;
	private static EV3UltrasonicSensor ultraSensor;
	
	  static final Port usPort = LocalEV3.get().getPort("S1");
	  
	//private double angle = 0;		//the heading angle
	
	private double x, y;
	private double theta;
	private double currentx, currenty;
	private double currenttheta;
	private double dx;
	private double dy;
	private double dtheta;
	private boolean IsAvoid;
	private int distance;
	 @SuppressWarnings("resource") // Because we don't bother to close this resource
	    EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(usPort); // usSensor is the instance
	    SampleProvider usDistance = usSensor.getMode("Distance"); // usDistance provides samples from
	                                                              // this instance
	    float[] usData = new float[usDistance.sampleSize()]; // usData is the buffer in which data are
	                                                         // returned
	

	
	//double leftRadius, rightRadius, width;
	
	
	private int[] X = {0, 1, 0, 2, 2, 1};
	private int[] Y = {0, 1, 2, 2, 1, 0};
	
	
	public ObNavigation(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Odometer odometer) /*, double x, double y) /*throws OdometerExceptions */{
		this.odometer = odometer;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		//this.x = x;
		//this.y = y;
	}
	
	
	
	public void run(){
		
		
		for (int i = 0; i < X.length; i++) {
			travelTo(X[i] * 30.48, Y[i] * 30.48);
		}
	}

	
	
public void travelTo (double x, double y){
		
		currentx = odometer.getXYT()[0];
		currenty = odometer.getXYT()[1];
		
		dx = x - currentx;
		dy = y - currenty;
		
		theta = Math.atan(dx/dy);
	
		//moving = true;
			
			
			
		/*if (dx < 0 ){
			theta = theta - Math.PI;
		}
		else if(dy < 0 && dx > 0){
			theta += Math.PI;
		}*/
			// based on deltaY and deltaX here we figure out the actual angle the robot is heading at
			if(dy>0){
				theta = Math.atan(dx/dy);
			}
			else if(dy<0 && dx>0){
				theta = Math.atan(dx/dy) + Math.PI;
			}
			else if (dy<0 && dx<0) {
				theta = Math.atan(dx/dy) - Math.PI;
			}
			usSensor.fetchSample(usData, 0);
			int ll = (int) usData [0] * 100;
			
		turnTo(theta * 180 / Math.PI);
			
		leftMotor.setSpeed(FORWARD_SPEED);
		rightMotor.setSpeed(FORWARD_SPEED);
			
			leftMotor.rotate(convertDistance(WHEEL_RAD, Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2))), true);
			rightMotor.rotate(convertDistance(WHEEL_RAD, Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2))), true);
			while (ll < 10){
				turnTo(90);
				leftMotor.rotate(10, true);
				 rightMotor.rotate(10, false);
			}
			
			//if close to the destination then stop
			//if(Math.abs(currentX - x) <= 0.5 && Math.abs(currentY - y) <= 0.5){
			//leftMotor.forward();
			//rightMotor.forward();	
			
			
			//this.moving = false;
				
				//isMoving = false;
				
				//leftMotor.stop(true);
				//rightMotor.stop();
				//break;
			//}
			
		}
		
	
	
	public void turnTo (double theta){
		
		//turning = true;
		
		currenttheta = odometer.getXYT()[2] ;
		 
		
		dtheta = theta - currenttheta;
		
		if ( dtheta > 180){
			dtheta -= 360; }
		else if (dtheta < -180){
			dtheta += 360; }
			
	
	 
	
	 leftMotor.setSpeed(ROTATE_SPEED);
	 rightMotor.setSpeed(ROTATE_SPEED);
	 
	
	 leftMotor.rotate(convertAngle(WHEEL_RAD, TRACK, dtheta), true);
	 rightMotor.rotate(-convertAngle(WHEEL_RAD,TRACK, dtheta), false);
	 
	 //leftMotor.setSpeed(FORWARD_SPEED);
	 //rightMotor.setSpeed(FORWARD_SPEED);
	 
	 //turning = false;
	 }
	 
	
	//public boolean isNavigating(){
		
		//return isNavigating;
		//return turning || moving;
		//currentx = odometer.getXYT()[0];
		//currenty = odometer.getXYT()[1];
		
		//if(currentx == x && currenty == y){
		//	return false;
		//}
		//else {
		//	return true;
		//}
	//}
	
	
	
	private static int convertDistance(double radius, double distance) {
	    return (int) ((180.0 * distance) / (Math.PI * radius));
	  }

	private static int convertAngle(double radius, double width, double angle) {
	    return convertDistance(radius, Math.PI * width * angle / 360.0);
	  }



/*	@Override
	public void processUSData(int d) {
		this.distance = d;
	
	}



	@Override
	public int readUSDistance() {
		// TODO Auto-generated method stub
		return this.distance;
	}*/
	}
