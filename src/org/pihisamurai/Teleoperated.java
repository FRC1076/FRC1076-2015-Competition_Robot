package org.pihisamurai;


public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
	}

	public void init() {
		robot.drivetrain.start();
	}

	double stickX, stickY;

	public void run() {
		GUI.update(); // getting gamepad input
		
		this.stickY = robot.gamepad.getRightY();// left joystick
		this.stickX = robot.gamepad.getRightX();// right joystick

		
		
	/*	double LeftTrigger = robot.gamepad.getLeftTrigger();
		double RightTrigger = robot.gamepad.getRightTrigger();
		robot.drivetrain.strafePower(LeftTrigger - RightTrigger);*/
		
		robot.drivetrain.strafePower(stickX);

		if (robot.gamepad.getPOV() == 0) {
			robot.manipulator.liftPower(0.5);
		} else if (robot.gamepad.getPOV() == 180) {
			robot.manipulator.liftPower(-0.5);
		} else {
			robot.manipulator.liftPower(0);
		}

		this.robot.drivetrain.setY(stickY);
		this.robot.drivetrain.modAngle(robot.gamepad.getLeftX());
	}

}