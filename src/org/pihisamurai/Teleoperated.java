package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;
	double buttonPOV;
	boolean buttonA;
	public int box;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
		liftControl = true;
		buttonA = false;
		buttonPOV = 0;
		

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
		
		SmartDashboard.putNumber("Angle stick rate 0 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 1 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 2 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 3 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 4 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 5 boxes", 10000);
		SmartDashboard.putNumber("Angle stick rate 6 boxes", 10000);
	}

	public void init() {
		robot.drivetrain.start();
	

	}

	private int lastPOV = Gamepad.POV_OFF;

	public void run() {
		double speedModifier = 1;
		
		GUI.update();
		
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
		if (robot.gamepad.getButtonRightBack()) 
			speedModifier = 1;
		 else if(robot.gamepad.getButtonLeftBack()) 
			speedModifier = 0.5;
		

		if (POV != lastPOV) {
			lastPOV = POV;
			if(POV == 0);
			else if(POV <= 180)
				robot.drivetrain.turn((POV/45) * Math.PI / 4);
			else if(POV > 180 && POV < 360){
				POV-=180;
				robot.drivetrain.turn((-POV/45) * Math.PI / 4);
			}
			/*switch (POV) {
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
			}*/
		}
		if (Math.abs(robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) > Math.abs(robot.gamepad2
				.getLeftTrigger() - robot.gamepad2.getRightTrigger())) {
			robot.manipulator.liftPower((robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger()) * 2);
		} else {
			robot.manipulator.liftPower((robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger()) * 2);
		}
		
		double turnSpeed =  SmartDashboard.getNumber("Turn Speed " + box + " Boxes");
		double accelLimit =  SmartDashboard.getNumber("Stick change rate " + box + " boxes");
		double turnAccelLimit = SmartDashboard.getNumber("Angle stick rate " + box + " boxes");

		robot.drivetrain.setPrimaryLimit(accelLimit);
		robot.drivetrain.setStrafeLimit(accelLimit);
		robot.drivetrain.setAngleAccelLimit(turnAccelLimit);
		
		robot.drivetrain.setMaxTurnSpeed(turnSpeed*speedModifier);
		robot.drivetrain.setAngleTarget(robot.gamepad.getLeftX());

		robot.drivetrain.setStrafe(robot.gamepad.getRightX()*speedModifier*1.33);
		robot.drivetrain.setPrimary(robot.gamepad.getRightY()*speedModifier);
	}
}
