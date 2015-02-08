package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;
	double buttonPOV;
	boolean buttonA;
	double div;
	double primary;
	double strafe;
	public int box;
	

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
		liftControl = true;
		div = 1;
		buttonA = false;
		buttonPOV = 0;
	}

	public void init() {
		robot.drivetrain.start();
	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		GUI.update();

		if(robot.gamepad2.getButtonA() != buttonA && !buttonA) {
			box = 0;
		} if(robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 0) {
			box++;
		} else if(robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 180) {
			box--;
		}
		buttonPOV = robot.gamepad2.getPOV();
		buttonA = robot.gamepad2.getButtonA();

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
		if(Math.abs(robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) > Math.abs(robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger())) {
			robot.manipulator.liftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		} else {
			robot.manipulator.liftPower((robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger()) * 2);
		}
		strafe = (robot.gamepad.getRightX() * div);
		primary = (robot.gamepad.getRightY() * div * 1.25);
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
		
		robot.drivetrain.setStrafe(strafe);
		robot.drivetrain.setPrimary(primary);
	}
}
