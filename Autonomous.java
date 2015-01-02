package org.pihisamurai;

public class Autonomous implements RobotMode{
	private Robot robot;
	
	public Vision vision; //If teleop uses this, move to Robot.java
	
	public Autonomous(Robot r){
		vision = new Vision();
		this.robot = r;
	}
	
	public void run(){
        robot.drivetrain.setSpeed(0.3);
        robot.sleep(10000);
        robot.drivetrain.setSpeed(0);
	}
}
