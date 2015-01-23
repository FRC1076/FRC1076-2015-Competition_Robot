package org.pihisamurai;

public class Teleoperated implements RobotMode {

	private Robot			robot;
	public DriverStationGUI	GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
	}

	public void init() {

	}

	double	LeftSpeed, RightSpeed;

	public void run() {
		GUI.update(); // getting gamepad input

		this.LeftSpeed = robot.gamepad.getLeftY();// left
													// joystick
		this.RightSpeed = robot.gamepad.getRightY();// right
													// joystick

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

		this.robot.drivetrain.setLeftSpeed(LeftSpeed);
		this.robot.drivetrain.setRightSpeed(RightSpeed);
	}

}
