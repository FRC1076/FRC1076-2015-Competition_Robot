package org.pihisamurai;

public class Teleoperated implements RobotMode {

	private Robot			robot;
	public DriverStationGUI	GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
	}

	public void init() {
		robot.drivetrain.start();
	}

	double	LeftSpeed, RightSpeed;

	public void run() {
		GUI.update(); // getting gamepad input

		this.LeftSpeed = robot.gamepad.getLeftY();// left
													// joystick
		this.RightSpeed = robot.gamepad.getRightY();// right
													// joystick
		if(LeftSpeed < 0.1 && LeftSpeed > -0.1) LeftSpeed = 0;
		if(RightSpeed < 0.1 && RightSpeed > -0.1) RightSpeed = 0;

		double LeftTrigger = robot.gamepad.getLeftTrigger();
		double RightTrigger = robot.gamepad.getRightTrigger();
		robot.drivetrain.setStrafeSpeed(LeftTrigger - RightTrigger);

		if (robot.gamepad.getPOV() == 0) {
			robot.drivetrain.liftPower(0.5);
		} else if (robot.gamepad.getPOV() == 180) {
			robot.drivetrain.liftPower(-0.5);
		} else {
			robot.drivetrain.liftPower(0);
		}

		this.robot.drivetrain.setLeftSpeed(LeftSpeed*90);
		this.robot.drivetrain.setRightSpeed(RightSpeed*90);
	}

}
