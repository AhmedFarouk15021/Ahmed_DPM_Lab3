package ca.mcgill.ecse211.odometer;

import ca.mcgill.ecse211.wall_following.UltrasonicPoller;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Navigation extends Thread{
	private double trackX;
	private double trackY;
	
	private static Odometer odometer;
	private double distance;
	
	private static boolean isTurning = false;
	private static boolean isMoving = false; 

	private double  posY;
	private double  posX;
	

private double AngleTheta;
	
public Navigation(Odometer odometer){
	this.odometer = odometer;
}

/**
 * 
 */


public void travelTo(double x, double y){
	
	
	
	y = y*SquareDriver.TILE_SIZE;
	
	x = x*SquareDriver.TILE_SIZE;

	posY = odometer.getXYT() [1];	
	trackY = y - posY;

	
	posX = odometer.getXYT() [0];
	trackX = x - posX;
	
	if (trackY <= 7 && trackY >= -7){
		if (x > 0){
			
			AngleTheta = 87;
		}
		
		if (x < 0){
			
			AngleTheta = -90;
			
		}
	}
	
	if (trackY > 0){
		
		AngleTheta =( Math.atan(trackX/trackY) * 180 / Math.PI);
	}
	
	else if (trackX > 0 && trackY < 0){
		
		AngleTheta = (Math.atan(trackX/trackY) * 180 / Math.PI) + 180;
	}
	
	else if (trackX < 0 && trackY < 0){
		
		AngleTheta =( Math.atan(trackX/trackY) * 180 / Math.PI) - 180;
		
	}
	

	isMoving = true;          
	
	distance = Math.sqrt( Math.pow(trackX, 2) + Math.pow(trackY, 2));
	
		turnTo(AngleTheta);
		
	    Odometer.leftMotor.setSpeed(200);
		Odometer.rightMotor.setSpeed(200);
	   Odometer.leftMotor.rotate(SquareDriver.convertDistance(Lab3.WHEEL_RAD, (distance)), true);
	    Odometer.rightMotor.rotate(SquareDriver.convertDistance(Lab3.WHEEL_RAD, (distance)), false);


    isMoving = false;
    
}

private void turnTo(double AngleTheta){
	
	isTurning = true;
	
	Odometer.leftMotor.setSpeed(SquareDriver.ROTATE_SPEED);
	
	Odometer.rightMotor.setSpeed(SquareDriver.ROTATE_SPEED);
	

	double currentTheta = odometer.getXYT()[2];
	
	double theta = AngleTheta - currentTheta;
	
	  if(theta > 180){
		  
		  theta = theta - 360;
			 }
			 else if (theta < -180){
				 
				 theta = theta + 360;
			  }
	
	Odometer.leftMotor.rotate(SquareDriver.convertAngle(Lab3.WHEEL_RAD , Lab3.TRACK, theta), true);
	Odometer.rightMotor.rotate(- SquareDriver.convertAngle(Lab3.WHEEL_RAD, Lab3.TRACK, theta), false);


	isTurning = false;
	
}
private boolean isNavigating(){
	
	return isMoving || isTurning;
	
}


}
