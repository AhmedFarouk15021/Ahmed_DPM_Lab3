package ca.mcgill.ecse211.wall_following;

public interface UltrasonicController {

  public void processUSData(int distance);

  public int readUSDistance();
}
