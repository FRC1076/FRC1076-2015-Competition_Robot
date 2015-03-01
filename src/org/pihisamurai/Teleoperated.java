package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated {

	// A "robot" variable to access methods therein:
	private Robot robot;
	// Tracks the primary speed modifiers:
	double speedModifier = 1;
	// Tracks the lift motor speed modifiers:
	double liftModifier = 1;

	public Teleoperated(Robot r) {
		// Initialization of variable values:
		this.robot = r;
	}

	public void init() {
	}

	public void run() {
		// Increasing and decreasing the box count.
		if(robot.gamepad2.ifButtonAChange() && robot.gamepad2.getButtonA()) {
			robot.drivetrain.speedController.setBoxCount(0);
			// Zeros the box count.
		} if (robot.gamepad1.ifPOVChange() && robot.gamepad1.getPOV() == 0) {
			if (robot.drivetrain.speedController.getBoxCount() < 6) {
				robot.drivetrain.speedController.setBoxCount(
						robot.drivetrain.speedController.getBoxCount() + 1);
				// Increases the box count if it is below 6.
			}
		} else if (robot.gamepad1.ifPOVChange() && robot.gamepad1.getPOV() == 180) {
			if (robot.drivetrain.speedController.getBoxCount() > 0) {
				robot.drivetrain.speedController.setBoxCount(
						robot.drivetrain.speedController.getBoxCount() - 1);
				// Decreases the box count if it is above 0.
			}
		}
	
		// Updates the speed modifier.
		speedModifier = 0.5;
		if (robot.gamepad1.getButtonRightBack()) {
			speedModifier += 0.25;
			// Full speed if the right secondary trigger is pushed.
		} if(robot.gamepad1.getButtonLeftBack()) {
			speedModifier += 0.25;
			// Full speed if the left secondary trigger is pushed.
		}
	
		// If the D-pad input changed, do one of these turns
		if (robot.gamepad1.ifPOVChange()) {
			switch (robot.gamepad1.getPOV()) {
			case Gamepad.POV_UP: // 0
				break; // Currently Meaningless
			case Gamepad.POV_UP_RIGHT: // 45
				robot.drivetrain.turnAngle(1 * Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT: // 90
				robot.drivetrain.turnAngle(2 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_RIGHT: // 135
				robot.drivetrain.turnAngle(3 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN: // 180
				robot.drivetrain.turnAngle(4 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_LEFT: // 225
				robot.drivetrain.turnAngle(-3 * Math.PI / 4);
				break;
			case Gamepad.POV_LEFT: // 270
				robot.drivetrain.turnAngle(-2 * Math.PI / 4);
				break;
			case Gamepad.POV_UP_LEFT: // 315
				robot.drivetrain.turnAngle(-1 * Math.PI / 4);
				break;
			case Gamepad.POV_OFF: // -1
				break; // Do nothing
			default:
				System.out.println("UNKNOWN POV ANGLE: " + robot.gamepad1.getPOV());
				break;
			}
		}

		// The lift motor speed modifier for gamepad 2;
		// Begins at full speed and is slowed with each button pressed.
		liftModifier = 1;
		if(robot.gamepad2.getButtonLeftBack()) {
			liftModifier -= 0.25;
		} if(robot.gamepad2.getButtonRightBack()) {
			liftModifier -= 0.25;
		}

		if (Math.abs(robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) > 0.05) {
			// For gamepad 1:
			robot.manipulator.setLiftPower((robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) * 2);
		} else {
			//For gamepad 2:
			robot.manipulator.setLiftPower(liftModifier * (robot.gamepad2.getRightTrigger() - robot.gamepad2.getLeftTrigger()) * 2);
		}
	
		// Sets the modifier for the turn:
		robot.drivetrain.speedController.setTurnSpeedModifier(speedModifier);
	
		// Moves the robot in regards to the right stick by the speed multiplier.
		robot.drivetrain.setStrafe(robot.gamepad1.getRightX() * speedModifier);
		robot.drivetrain.setPrimary(robot.gamepad1.getRightY() * speedModifier);
	
		// Turns the robot in regards to the left stick by the speed multiplier.
		robot.drivetrain.setTurnRate(robot.gamepad1.getLeftX());

		// Prints out the encoder numbers.
		SmartDashboard.putNumber("Encoder Primary", robot.drivetrain.getDistPrimary());
		SmartDashboard.putNumber("Encoder Strafe", robot.drivetrain.getDistStrafe());
	}
}
