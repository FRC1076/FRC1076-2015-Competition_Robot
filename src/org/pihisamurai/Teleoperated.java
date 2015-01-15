
package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(robot);
	}
	
	public void init(){
		
	}

	double LeftSpeed, RightSpeed;
	public void run() {
			GUI.update();//getting gamepad input
			LeftSpeed = robot.gamepad.getLeftY();//left joystick
			RightSpeed = robot.gamepad.getRightY();//right joystick
			boolean LeftTrigger = robot.gamepad.getNumberedButton( robot.gamepad.LEFT_TRIGGER);
			boolean RightTrigger = robot.gamepad.getNumberedButton( robot.gamepad.RIGHT_TRIGGER);
			
			if (LeftTrigger && RightTrigger == false) //controlling central motor based on triggers
			{
				robot.drivetrain.strafePower(-0.25);
			}
			else if (RightTrigger && LeftTrigger == false)
			{
				robot.drivetrain.strafePower(0.25);
			}
			else
			{
				robot.drivetrain.strafePower(0);
			}
			robot.drivetrain.leftPower(LeftSpeed);
			robot.drivetrain.rightPower(RightSpeed);
		}
	
}
