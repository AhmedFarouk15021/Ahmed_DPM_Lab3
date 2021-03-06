/*
 * SquareDriver.java
 */
package ca.mcgill.ecse211.odometer;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * This class is used to drive the robot on the demo floor.
 */
public class SquareDriver {
  public static final int FORWARD_SPEED = 250;
  public static final int ROTATE_SPEED = 100;
  public static final double TILE_SIZE = 30.48;

  /**
   * This method is meant to drive the robot in a square of size 2x2 Tiles. It is to run in parallel
   * with the odometer and Odometer correcton classes allow testing their functionality.
   * 
   * @param leftMotor
   * @param rightMotor
   * @param leftRadius
   * @param rightRadius
   * @param width
   */
  public static void drive(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
      double leftRadius, double rightRadius, double track) {
    // reset the motors
    for (EV3LargeRegulatedMotor motor : new EV3LargeRegulatedMotor[] {leftMotor, rightMotor}) {
      motor.stop();
      motor.setAcceleration(2000);
    }

    // Sleep for 2 seconds
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }

    for (int i = 0; i < 4; i++) {
      // drive forward two tiles
      leftMotor.setSpeed(FORWARD_SPEED);
      rightMotor.setSpeed(FORWARD_SPEED);
      /* TODO do another method instead of convert distance to calculate the distance of
       * the straight line connecting the two way points ()
       */

      leftMotor.rotate(convertDistance(leftRadius, (3 * TILE_SIZE)), true);

      rightMotor.rotate(convertDistance(rightRadius, (3  * TILE_SIZE )), false);

      // turn 90 degrees clockwise
      leftMotor.setSpeed(ROTATE_SPEED);
      rightMotor.setSpeed(ROTATE_SPEED);
      /* TODO do another method instead of convert distance to calculate the smallest
       * angle of rotation needed and its direction instead of 90 degrees
       */

      leftMotor.rotate(convertAngle(leftRadius, track, 90.0), true);
      rightMotor.rotate(-convertAngle(rightRadius, track, 90.0), false);
    }
  }

  /**
   * This method allows the conversion of a distance to the total rotation of each wheel need to
   * cover that distance.
   * 
   * @param radius
   * @param distance
   * @return
   */
  static int convertDistance(double radius, double distance) {
    return (int) ((180.0 * distance) / (Math.PI * radius));
  }

  static int convertAngle(double radius, double width, double angle) {
    return convertDistance(radius, Math.PI * width * angle / 360.0);
  }
}
