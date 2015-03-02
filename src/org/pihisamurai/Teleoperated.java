package org.pihisamurai;


public class Teleoperated {

	// The robot being controlled
	private Robot robot;
	// Tracks the primary speed modifiers:
	double primaryModifier;
	double strafeModifier;
	double turnModifier;
	// Tracks the lift motor speed modifiers:
	double liftModifier;

	public Teleoperated() {
		// Initialization of variable values:
		this.robot = Robot.getInstance();
	}

	public void init() {
	}

	public void run() {
		// Increasing and decreasing the box count.
		if(robot.gamepad2.ifButtonAChange() && robot.gamepad2.getButtonA()) {
			robot.drivetrain.speedController.setBoxCount(0);
			// Zeros the box count.
		} if (robot.gamepad2.ifPOVChange() && robot.gamepad2.getPOV() == 0) {
			if (robot.drivetrain.speedController.getBoxCount() < 6) {
				robot.drivetrain.speedController.setBoxCount(
						robot.drivetrain.speedController.getBoxCount() + 1);
				// Increases the box count if it is below 6.
			}
		} else if (robot.gamepad2.ifPOVChange() && robot.gamepad2.getPOV() == 180) {
			if (robot.drivetrain.speedController.getBoxCount() > 0) {
				robot.drivetrain.speedController.setBoxCount(
						robot.drivetrain.speedController.getBoxCount() - 1);
				// Decreases the box count if it is above 0.
			}
		}
	
		// Updates the speed modifier.
		strafeModifier = 0.5;
		primaryModifier = 0.5;
		turnModifier = 0.35;
		if (robot.gamepad1.getButtonRightBack()) {
			strafeModifier = 0.75;
			primaryModifier = 0.75;
			turnModifier = 0.5;
		} if(robot.gamepad1.getButtonLeftBack()) {
			strafeModifier = 1;
			turnModifier = 1;
			primaryModifier = 1;
		}
		
		boolean stopTurn = false;
	
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
				stopTurn = true; //If release POV stop turning
				break; // Do nothing
			default:
				System.err.println("UNKNOWN POV ANGLE: " + robot.gamepad1.getPOV());
				break;
			}
		}

		// The lift motor speed modifier for gamepad 2;
		// Begins at full speed and is slowed with each button pressed.
		liftModifier = 1;
		if(robot.gamepad2.getButtonLeftBack())
			liftModifier = 0.5;
		if(robot.gamepad2.getButtonRightBack()) 
			liftModifier = 0.25;

		//If driver 1 using elevator do that, if not use secondary controller
		if (Math.abs(robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) > 0.05) 
			robot.manipulator.setLiftPower((robot.gamepad1.getRightTrigger() - robot.gamepad1.getLeftTrigger()) * 2);
		else
			robot.manipulator.setLiftPower(liftModifier * (robot.gamepad2.getRightTrigger() - robot.gamepad2.getLeftTrigger()) * 2);
		
	
		// Sets the modifier for the turn:
		robot.drivetrain.speedController.setTurnSpeedModifier(turnModifier);
	
		// Moves the robot in regards to the right stick by the speed multiplier.
		robot.drivetrain.setStrafe(robot.gamepad1.getRightX() * strafeModifier);
		robot.drivetrain.setPrimary(robot.gamepad1.getRightY() * primaryModifier);
	
		// Turns the robot in regards to the left stick by the speed multiplier.
		
		if(stopTurn || !robot.drivetrain.isTurning())
			robot.drivetrain.setTurnRate(robot.gamepad1.getLeftX());
	}
}
