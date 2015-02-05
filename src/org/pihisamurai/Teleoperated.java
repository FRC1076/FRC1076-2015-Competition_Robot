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

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		GUI.update();

		int POV = robot.gamepad.getPOV();

		if (POV != lastPOV) {
			lastPOV = POV;
			switch (POV) {
			case Gamepad.POV_UP:
				// Currently Meaningless
				break;
			case Gamepad.POV_UP_RIGHT:
				robot.drivetrain.turn(Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT:
				robot.drivetrain.turn(Math.PI / 2);
				break;
			case Gamepad.POV_DOWN_RIGHT:
				robot.drivetrain.turn(-3 * Math.PI / 2);
				break;
			case Gamepad.POV_DOWN:
				robot.drivetrain.turn(Math.PI);
				break;
			case Gamepad.POV_DOWN_LEFT:
				robot.drivetrain.turn(-3 * Math.PI / 4);
				break;
			case Gamepad.POV_LEFT:
				robot.drivetrain.turn(-Math.PI / 2);
				break;
			case Gamepad.POV_UP_LEFT:
				robot.drivetrain.turn(-Math.PI / 4);
				break;
			case Gamepad.POV_OFF:
				//Do nothing
				break;
			default:
				System.out.println("UNKNOWN POV ANGLE: " + POV);
				break;
			}
		}
		robot.manipulator.liftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		robot.drivetrain.setStrafe(robot.gamepad.getRightX());
		robot.drivetrain.setPrimary(robot.gamepad.getRightY());
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
	}
}