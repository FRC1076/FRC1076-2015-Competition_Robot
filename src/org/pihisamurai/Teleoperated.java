package org.pihisamurai;

public class Teleoperated {

	// A "robot" variable to access methods therein:
	private Robot robot;
	// Tracks the speed modifiers:
	double speedModifier = 1;

	public Teleoperated(Robot r) {
		// Initialization of variable values:
		this.robot = r;
	}

	public void init() {
		robot.drivetrain.start();
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
		speedModifier = 0.75;
		if (robot.gamepad1.getButtonRightBack()) {
			speedModifier += 0.25;
			// Full speed if the right secondary trigger is pushed.
		} if(robot.gamepad1.getButtonLeftBack()) {
			speedModifier -= 0.25;
			// Full speed if the left secondary trigger is pushed.
		}

		// If the D-pad input changed, do one of these turns
		if (robot.gamepad1.ifPOVChange()) {
			switch (robot.gamepad1.getPOV()) {
			case Gamepad.POV_UP: // 0
				break; // Currently Meaningless
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
				break; // Do nothing
			default:
				System.out.println("UNKNOWN POV ANGLE: " + robot.gamepad1.getPOV());
				break;
			}
		}

		// Decides which gamepad is pressing the triggers the hardest;
		// Said gamepad controls the lift.
		if (Math.abs(robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) > Math.abs(robot.gamepad2
				.getRightTrigger() - robot.gamepad2.getLeftTrigger())) {
			// For gamepad 1:
			robot.manipulator.setLiftPower((robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) * 2);
		} else {
			//For gamepad 2:
			robot.manipulator.setLiftPower((robot.gamepad2.getRightTrigger() - robot.gamepad2.getLeftTrigger()) * 2);
		}

		// Sets the modifier for the turn:
		robot.drivetrain.speedController.setTurnSpeedModifier(speedModifier);

		// Moves the robot in regards to the right stick by the speed multiplier.
		robot.drivetrain.setStrafe(robot.gamepad1.getRightX() * speedModifier * 1.33);
		robot.drivetrain.setPrimary(robot.gamepad1.getRightY() * speedModifier);

		// Turns the robot in regards to the left stick by the speed multiplier.
		robot.drivetrain.setAngleTarget(robot.gamepad1.getLeftX());
	}
}
