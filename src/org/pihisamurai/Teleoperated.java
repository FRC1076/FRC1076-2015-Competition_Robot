package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;
	double buttonPOV;
	boolean buttonA;
	double div;
	double newPrimary;
	double primary;
	double newStrafe;
	double strafe;
	double absTime;
	double period;
	public int box;
	

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
		liftControl = true;
		div = 1;
		buttonA = false;
		buttonPOV = 0;
		absTime = System.nanoTime();
	}

	public void init() {
		robot.drivetrain.start();
		SmartDashboard.putNumber("Divisor", 1);
	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		GUI.update();
		
		period = (System.nanoTime() - absTime) / 1000000000;
		absTime = System.nanoTime();
		SmartDashboard.putNumber("Nano", period);

		if(robot.gamepad2.getButtonA() != buttonA && !buttonA) {
			box = 0;
		} if(robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 0) {
			box++;
			if(box > 6) {
				box = 6;
			}
		} else if(robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 180) {
			box--;
			if(box < 0) {
				box = 0;
			}
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
		
		robot.drivetrain.setMaxTurnSpeed(7 - box / SmartDashboard.getNumber("Divisor"));
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
		
		double limit = 0.5 / (box * 8 + 1) / 0.02 * period;
		newStrafe = robot.gamepad.getRightX() * div * 1.25;
		if(strafe - newStrafe > limit * 1.5) {
			strafe -= limit * 1.5;
		} else if(strafe - newStrafe < -limit * 1.5) {
			strafe += limit * 1.5;
		} else {
			strafe = newStrafe;
		}
		
		newPrimary = robot.gamepad.getRightY() * div;
		if(primary - newPrimary > limit) {
			primary -= limit;
		} else if(primary - newPrimary < -limit) {
			primary += limit;
		} else {
			primary = newPrimary;
		}
		
		robot.drivetrain.setStrafe(strafe);
		robot.drivetrain.setPrimary(primary);
	}
}
