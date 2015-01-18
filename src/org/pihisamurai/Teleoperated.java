
package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
	}
	
	public void init(){
		
	}

	double LeftSpeed, RightSpeed;
	public void run() {
			GUI.update(); //getting gamepad input
			this.LeftSpeed = robot.gamepad.getLeftY();//left joystick
			this.RightSpeed = robot.gamepad.getRightY();//right joystick
			double LeftTrigger = robot.gamepad.getRawAxis(robot.gamepad.F310_LEFT_TRIGGER_AXIS);
			double RightTrigger = robot.gamepad.getRawAxis(robot.gamepad.F310_RIGHT_TRIGGER_AXIS);
			robot.drivetrain.strafePower(LeftTrigger - RightTrigger);
			if(robot.gamepad.getPOV() == 0)
			{
				robot.drivetrain.liftPower(0.5);
			}
			else if(robot.gamepad.getPOV() == 180)
			{
				robot.drivetrain.liftPower(-0.5);
			}
			else
			{
				robot.drivetrain.liftPower(0);
			}
			this.robot.drivetrain.leftPower(LeftSpeed);
			this.robot.drivetrain.rightPower(RightSpeed);
		}
	
}
