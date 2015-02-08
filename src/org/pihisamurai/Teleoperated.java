package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;
	double div;
	boolean buttonA;
	boolean isSlow;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
		liftControl = true;
		div = 0;
		buttonA = false;
	}

	public void init() {
		robot.drivetrain.start();
	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		GUI.update();

		int POV = robot.gamepad.getPOV();

		div = 0.75;
		if (robot.gamepad.getButtonRightBack()) {
			div = 1;
		} else if(robot.gamepad.getButtonLeftBack()) {
			div = 0.5;
		}

		if (POV != lastPOV) {
			lastPOV = POV;
			switch (POV) {
			case Gamepad.POV_UP:
				// Currently Meaningless
				break;
			case Gamepad.POV_UP_RIGHT:
				robot.drivetrain.turn( 1* Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT:
				robot.drivetrain.turn( 2 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_RIGHT:
				robot.drivetrain.turn( 3 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN:
				robot.drivetrain.turn( 4 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_LEFT:
				robot.drivetrain.turn( -3 * Math.PI / 4);
				break;
			case Gamepad.POV_LEFT:
				robot.drivetrain.turn(-2 * Math.PI / 4);
				break;
			case Gamepad.POV_UP_LEFT:
				robot.drivetrain.turn(-1 * Math.PI / 4);
				break;
			case Gamepad.POV_OFF:
				// Do nothing
				break;
			default:
				System.out.println("UNKNOWN POV ANGLE: " + POV);
				break;
			}
		}
		robot.manipulator.liftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		robot.drivetrain.setStrafe(robot.gamepad.getRightX() * div);
		robot.drivetrain.setPrimary(robot.gamepad.getRightY() * div);
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
	}
}
