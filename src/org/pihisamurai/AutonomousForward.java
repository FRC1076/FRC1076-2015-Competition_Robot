package org.pihisamurai;


public class AutonomousForward {

	Robot robot;

	double time;

	AutonomousForward(Robot robot) {
		this.robot = robot;
	}

	
	public void setAll(double a) {
		robot.drivetrain.leftMotorA.set(a);
		robot.drivetrain.leftMotorB.set(a);
		robot.drivetrain.rightMotorA.set(a);
		robot.drivetrain.rightMotorB.set(a);
	}

	public void run() {
		if ((System.nanoTime() / 1000000000) - time > 5){
			setAll(0);
		} else {
			setAll(-0.5);
		}
		// robot.drivetrain.update();
	}

	public void init() {
		time = System.nanoTime() / 1000000000;
		// robot.drivetrain.goDistPrimary(1, -0.5);
	}

}
