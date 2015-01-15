package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public HumanControl humanControl;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		humanControl = new HumanControl(robot);
		GUI = new DriverStationGUI(robot);
	}

	Jaguar LeftMotor = new Jaguar(0); //assumed positions of motors (will talk to Samuel about actual locations)
	Jaguar RightMotor = new Jaguar(1);
	Jaguar StrafeMotor = new Jaguar(2);
	public void run() {
		double LeftSpeed, RightSpeed;
		while (true) {
			GUI.update();//getting gamepad input
			LeftSpeed = robot.gamepad.getLeftY();//left joystick
			RightSpeed = robot.gamepad.getRightY();//right joystick
			boolean LeftTrigger = robot.gamepad.getNumberedButton( robot.gamepad.LEFT_TRIGGER);
			boolean RightTrigger = robot.gamepad.getNumberedButton( robot.gamepad.RIGHT_TRIGGER);
			
			if (LeftTrigger && RightTrigger == false) //controlling central motor based on triggers
			{
				StrafeMotor.set(-0.25);
			}
			else if (RightTrigger && LeftTrigger == false)
			{
				StrafeMotor.set(0.25);
			}
			else
			{
				StrafeMotor.set(0);
			}
			
			LeftMotor.set(LeftSpeed); //joystick input setting motor speed
			RightMotor.set(RightSpeed); 
			robot.sleep(10);
		}
	}
}
