package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousForward {

	Robot robot;
	
	AutonomousForward(Robot robot) {
		this.robot = robot;
	}

	double start;
	double time;

	public void run() {

		
		if ((System.nanoTime() / 1000000000) - time > 5){

			robot.drivetrain.leftMotorA.set(0);
			robot.drivetrain.leftMotorB.set(0);
			robot.drivetrain.rightMotorA.set(0);
			robot.drivetrain.rightMotorB.set(0);
		} else {

			robot.drivetrain.leftMotorA.set(-0.5);
			robot.drivetrain.leftMotorB.set(-0.5);
			robot.drivetrain.rightMotorA.set(-0.5);
			robot.drivetrain.rightMotorB.set(-0.5);
		}
		
		
	}

	public void init() {
		time = System.nanoTime() / 1000000000;
	}

}
