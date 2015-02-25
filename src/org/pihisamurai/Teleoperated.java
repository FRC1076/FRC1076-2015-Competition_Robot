package org.pihisamurai;

import edu.wpi.first.wpilibj.Servo;

public class Teleoperated {

	// A "robot" variable to access methods therein
	private Robot robot;
	// For detecting button change on the D-pad of gamepad 1
	private int lastPOV = Gamepad.POV_OFF;

	public Teleoperated(Robot r) {
		// Initialization of variable values:
		this.robot = r;
	}

	public void init() {
		robot.drivetrain.start();
	}

	public void run() {
		
		double speedModifier = 1;
		int POV = robot.gamepad.ifPOVChange();

		// Increasing and decreasing the box count
		if(robot.gamepad2.ifButtonAChange() && robot.gamepad2.getButtonA()) {
			robot.drivetrain.speedController.setBoxCount(0);
			// Zeros the box count
		}
		if (POV == 0) {
			if (robot.drivetrain.speedController.getBoxCount() < 6) {
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount() + 1);
				// Increases the box count if it is below 6.
			}
		} else if (POV == 180) {
			if (robot.drivetrain.speedController.getBoxCount() > 0) {
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount() - 1);
				// Decreases the box count if it is above 0.
			}
		}

		speedModifier = 0.75;
		if (robot.gamepad.getButtonRightBack()) {
			speedModifier = 1;
		}
		else if(robot.gamepad.getButtonLeftBack()) {
			speedModifier = 0.5;
		}

		// If the D-pad input changed, do one of these turns
		if (POV != -2) {
			switch (POV) {
			case Gamepad.POV_UP: // 0
				// Currently Meaningless
				break;
			case Gamepad.POV_UP_RIGHT: // 45
				robot.drivetrain.turn(1 * Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT: // 90
				robot.drivetrain.turn(2 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_RIGHT: // 135
				robot.drivetrain.turn(3 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN: // 180
				robot.drivetrain.turn(4 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_LEFT: // 225
				robot.drivetrain.turn(-3 * Math.PI / 4);
				break;
			case Gamepad.POV_LEFT: // 270
				robot.drivetrain.turn(-2 * Math.PI / 4);
				break;
			case Gamepad.POV_UP_LEFT: // 315
				robot.drivetrain.turn(-1 * Math.PI / 4);
				break;
			case Gamepad.POV_OFF: // -1
				// Do nothing
				break;
			default:
				System.out.println("UNKNOWN POV ANGLE: " + POV);
				break;
			}
		}

		//Decides which gamepad is pressing the triggers the hardest;
		// Said gamepad controls the lift.
		if (Math.abs(robot.gamepad.getRightTrigger() - robot.gamepad.getLeftTrigger()) > Math.abs(robot.gamepad2
				.getRightTrigger() - robot.gamepad2.getLeftTrigger())) {
			robot.manipulator.setLiftPower((robot.gamepad.getRightTrigger() - robot.gamepad.getLeftTrigger()) * 2);
		} else {
			//For gamepad 2:
			robot.manipulator.setLiftPower((robot.gamepad2.getRightTrigger() - robot.gamepad2.getLeftTrigger()) * 2);
		}

		// Sets the modifier for the turn:
		robot.drivetrain.speedController.setTurnSpeedModifier(speedModifier);

		// Moves the robot in regards to the right stick by the speed multiplier.
		robot.drivetrain.setStrafe(robot.gamepad.getRightX() * speedModifier * 1.33);
		robot.drivetrain.setPrimary(robot.gamepad.getRightY() * speedModifier);

		// Turns the robot in regards to the left stick by the speed multiplier.
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
		
	}
}
