package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousStrafe {

	Robot robot;

	double time;

	AutonomousStrafe(Robot robot) {
		this.robot = robot;
	}

	public void run() {
		if ((System.nanoTime() / 1000000000) - time > 5){
			robot.drivetrain.strafeMotorA.set(0);
			robot.drivetrain.strafeMotorB.set(0);
		} else {
			robot.drivetrain.strafeMotorA.set(-0.5);
			robot.drivetrain.strafeMotorB.set(-0.5);
		}
		// robot.drivetrain.update();
	}

	public void init() {
		time = System.nanoTime() / 1000000000;
		// robot.drivetrain.goDistStrafe(-1, -0.5);
	}

}
