package org.pihisamurai;

public class Teleoperated {

	private Robot robot;
	boolean liftControl;
	double buttonPOV;
	boolean buttonA;
	
	public Teleoperated(Robot r) {
		this.robot = r;
		liftControl = true;
		buttonA = false;
		buttonPOV = 0;
	}

	public void init() {
		robot.drivetrain.start();
	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		
		double speedModifier = 1;
		
		if(robot.gamepad2.getButtonA() != buttonA && !buttonA) {
			robot.drivetrain.speedController.setBoxCount(0);
		}
		if (robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 0) {
			if (robot.drivetrain.speedController.getBoxCount() < 6) {
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount()+1);
			}
		} else if (robot.gamepad2.getPOV() != buttonPOV && robot.gamepad2.getPOV() == 180) {
			if (robot.drivetrain.speedController.getBoxCount() > 0) {
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount()-1);
			}
		}
		buttonPOV = robot.gamepad2.getPOV();
		buttonA = robot.gamepad2.getButtonA();

		int POV = robot.gamepad.getPOV();

		speedModifier = 0.75;
		if (robot.gamepad.getButtonRightBack()) 
			speedModifier = 1;
		 else if(robot.gamepad.getButtonLeftBack()) 
			speedModifier = 0.5;
		

		if (POV != lastPOV) {
			lastPOV = POV;/*
			if(POV == 0);
			else if(POV <= 180)
				robot.drivetrain.turn((POV/45) * Math.PI / 4);
			else if(POV > 180 && POV < 360){
				POV-=180;
				robot.drivetrain.turn((-POV/45) * Math.PI / 4);
			}*/
			switch (POV) {
			case Gamepad.POV_UP://0
				// Currently Meaningless
				break;
			case Gamepad.POV_UP_RIGHT://45\9
				robot.drivetrain.turn(1 * Math.PI / 4);
				break;
			case Gamepad.POV_RIGHT://90
				robot.drivetrain.turn(2 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_RIGHT://135
				robot.drivetrain.turn(3 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN://180
				robot.drivetrain.turn(4 * Math.PI / 4);
				break;
			case Gamepad.POV_DOWN_LEFT://225
				robot.drivetrain.turn(-3 * Math.PI / 4);
				break;
			case Gamepad.POV_LEFT://270
				robot.drivetrain.turn(-2 * Math.PI / 4);
				break;
			case Gamepad.POV_UP_LEFT://315
				robot.drivetrain.turn(-1 * Math.PI / 4);
				break;
			case Gamepad.POV_OFF://-1
				// Do nothing
				break;
			default:
				System.out.println("UNKNOWN POV ANGLE: " + POV);
				break;
			}
		}
		if (Math.abs(robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) > Math.abs(robot.gamepad2
				.getLeftTrigger() - robot.gamepad2.getRightTrigger())) {
			robot.manipulator.setLiftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		} else {
			robot.manipulator.setLiftPower((robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger()) * 2);
		}
		
		robot.drivetrain.speedController.setTurnSpeedModifier(speedModifier);
		
		robot.drivetrain.setStrafe(robot.gamepad.getRightX()*speedModifier*1.33);
		robot.drivetrain.setPrimary(robot.gamepad.getRightY()*speedModifier);

		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());
		
	}
}
