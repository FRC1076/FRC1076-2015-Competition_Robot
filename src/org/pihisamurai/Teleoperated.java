package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Teleoperated implements RobotMode {

	private Robot robot;
	public DriverStationGUI GUI;
	boolean liftControl;

	public Teleoperated(Robot r) {
		this.robot = r;
		GUI = new DriverStationGUI(r);
		cooldown = 0;
		liftControl = true;
	}

	public void init() {

	}

	double LeftSpeed, RightSpeed;
	double cooldown;
	double cooldown2;

	public void run() {
		GUI.update(); // getting gamepad input

		double LeftTrigger = robot.gamepad.getLeftTrigger();
		double RightTrigger = robot.gamepad.getRightTrigger();
		robot.drivetrain.strafePower(LeftTrigger - RightTrigger);
		if(liftControl) {
			robot.manipulator.liftPower(robot.gamepad2.getLeftTrigger() - robot.gamepad2.getRightTrigger());
		} else {
			robot.manipulator.liftPower(robot.gamepad.getLeftTrigger() - robot.gamepad.getRightTrigger());
		}
		//SmartDashboard.putDouble("right trigger", RightTrigger );
		//if (robot.gamepad.getPOV() == 0) {
		//	robot.manipulator.liftPower(0.5);
		//} else if (robot.gamepad.getPOV() == 180) {
		//	robot.manipulator.liftPower(-0.5);
		//} else {
		//	robot.manipulator.liftPower(0);
		//}

		if(robot.gamepad2.getButtonA() && cooldown <= 0) {
			if(robot.drivetrain.getDiv() <= 0.1) {
				robot.drivetrain.setDiv(1);
			} else {
				robot.drivetrain.setDiv(robot.drivetrain.getDiv() - 0.1);
			}
			cooldown = 20;
		} else if(cooldown > 0) {
			cooldown--;
		}
		
		if(robot.gamepad2.getButtonY() && cooldown <= 0) {
			if(robot.drivetrain.getDiv() >= 1) {
				robot.drivetrain.setDiv(0);
			} else {
				robot.drivetrain.setDiv(robot.drivetrain.getDiv() + 0.1);
			}
			cooldown = 20;
		}
		
		if(robot.gamepad2.getButtonX() && cooldown2 <= 0) {
			liftControl = !liftControl;
			cooldown2 = 20;
		} else if(cooldown2 > 0) {
			cooldown2--;
		}
		
		/*if(Math.abs(robot.gamepad2.getLeftY()) > Math.abs(robot.gamepad2.getRightY())) {
			robot.manipulator.liftPower(robot.gamepad2.getLeftY());
		} else {
			robot.manipulator.liftPower(robot.gamepad2.getRightY());
		}*/
		
		this.robot.drivetrain.leftPower(robot.gamepad.getLeftY());
		this.robot.drivetrain.rightPower(robot.gamepad.getRightY());
	}

}
