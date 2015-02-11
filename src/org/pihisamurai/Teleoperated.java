package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;
	double buttonPOV;
	boolean buttonA;
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
		buttonA = false;
		buttonPOV = 0;
		absTime = System.nanoTime();
	}

	public void init() {
		robot.drivetrain.start();
		SmartDashboard.putNumber("Turn Speed 0 Boxes", 12);
		SmartDashboard.putNumber("Turn Speed 1 Boxes", 12);
		SmartDashboard.putNumber("Turn Speed 2 Boxes", 4.5);
		SmartDashboard.putNumber("Turn Speed 3 Boxes", 3.5);
		SmartDashboard.putNumber("Turn Speed 4 Boxes", 2.5);
		SmartDashboard.putNumber("Turn Speed 5 Boxes", 2);
		SmartDashboard.putNumber("Turn Speed 6 Boxes", 1.5);

		SmartDashboard.putNumber("Stick change rate 0 boxes", 1000);
		SmartDashboard.putNumber("Stick change rate 1 boxes", 1000);
		SmartDashboard.putNumber("Stick change rate 2 boxes", 1000);
		SmartDashboard.putNumber("Stick change rate 3 boxes", 0.7);
		SmartDashboard.putNumber("Stick change rate 4 boxes", 0.5);
		SmartDashboard.putNumber("Stick change rate 5 boxes", 0.3);
		SmartDashboard.putNumber("Stick change rate 6 boxes", 0.2);
	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		double speedModifier = 1;
		double turnSpeed = 1;
		
		GUI.update();
		
		period = (System.nanoTime() - absTime) / 1000000000.0;
		absTime = System.nanoTime();
		
		if(robot.gamepad2.getButtonA() != buttonA && !buttonA) {
			box = 0;
		}
		if (robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 0) {
			box++;
			if (box > 6) {
				box = 6;
			}
		} else if (robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 180) {
			box--;
			if (box < 0) {
				box = 0;
			}
		}
		buttonPOV = robot.gamepad2.getPOV();
		buttonA = robot.gamepad2.getButtonA();

		int POV = robot.gamepad.getPOV();

		speedModifier = 0.75;
		if (robot.gamepad.getButtonRightBack()) {
			speedModifier = 1;
		} else if(robot.gamepad.getButtonLeftBack()) {
			speedModifier = 0.5;
		}

		if (POV != lastPOV) {
			lastPOV = POV;
			switch (POV) {
			case Gamepad.POV_UP:
				// Currently Meaningless
				break;
			case Gamepad.POV_UP_RIGHT:
				robot.drivetrain.turn(1 * Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT:
				robot.drivetrain.turn(2 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_RIGHT:
				robot.drivetrain.turn(3 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN:
				robot.drivetrain.turn(4 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_LEFT:
				robot.drivetrain.turn(-3 * Math.PI / 4);
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
		if (Math.abs(robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) > Math.abs(robot.gamepad2
				.getLeftTrigger() - robot.gamepad2.getRightTrigger())) {
			robot.manipulator.liftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		} else {
			robot.manipulator.liftPower((robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger()) * 2);
		}
		switch (box){
		
		case 0: turnSpeed = SmartDashboard.getNumber("Turn Speed 0 Boxes");
				break;
		case 1: turnSpeed = SmartDashboard.getNumber("Turn Speed 1 Boxes");
				break;	
		case 2: turnSpeed = SmartDashboard.getNumber("Turn Speed 2 Boxes");
				break;
		case 3: turnSpeed = SmartDashboard.getNumber("Turn Speed 3 Boxes");
				break;
		case 4: turnSpeed = SmartDashboard.getNumber("Turn Speed 4 Boxes");
				break;
		case 5: turnSpeed = SmartDashboard.getNumber("Turn Speed 5 Boxes");
				break;
		case 6: turnSpeed = SmartDashboard.getNumber("Turn Speed 6 Boxes");
				break;				
		}
		
		double accelLimit = 0;
			
		switch (box){
		case 0: accelLimit = SmartDashboard.getNumber("Stick change rate 0 boxes");
				break;
		case 1: accelLimit = SmartDashboard.getNumber("Stick change rate 1 boxes");
				break;	
		case 2: accelLimit = SmartDashboard.getNumber("Stick change rate 2 boxes");
				break;
		case 3: accelLimit = SmartDashboard.getNumber("Stick change rate 3 boxes");
				break;
		case 4: accelLimit = SmartDashboard.getNumber("Stick change rate 4 boxes");
				break;
		case 5: accelLimit = SmartDashboard.getNumber("Stick change rate 5 boxes");
				break;
		case 6: accelLimit = SmartDashboard.getNumber("Stick change rate 6 boxes");
				break;		
		}
		
		accelLimit *= period;
		
		robot.drivetrain.setMaxTurnSpeed(turnSpeed);
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
		
	//	double limit = 0.5 / (box * 8 + 1) * period;
		newStrafe = robot.gamepad.getRightX() * speedModifier * 1.25;
		if(strafe - newStrafe > accelLimit * 1.5) {
			strafe -= accelLimit * 1.5;
		} else if(strafe - newStrafe < -accelLimit * 1.5) {
			strafe += accelLimit * 1.5;
		} else {
			strafe = newStrafe;
		}
		
		
		
		newPrimary = robot.gamepad.getRightY() * speedModifier;
		if(primary - newPrimary > accelLimit) {
			primary -= accelLimit;
		} else if(primary - newPrimary < -accelLimit) {
			primary += accelLimit;
		} else {
			primary = newPrimary;
		}

		robot.drivetrain.setStrafe(strafe);
		robot.drivetrain.setPrimary(primary);
	}
}
