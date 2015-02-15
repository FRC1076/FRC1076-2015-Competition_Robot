package org.pihisamurai;

public class AutonomousForward {

	Robot robot;

	AutonomousForward(Robot robot) {
		this.robot = robot;
	}

	double start;

	public void run() {

		robot.drivetrain.speedController.setTurnSpeedModifier(1);
		
		robot.drivetrain.setAngleTarget(0);

		robot.drivetrain.setStrafe(0);
		if (robot.drivetrain.getPriamryDist() - start > 5)
			robot.drivetrain.setPrimary(0);
		else
			robot.drivetrain.setPrimary(-0.6);
	}

	public void init() {

		robot.drivetrain.setPrimaryLimit(99999999999.0);
		robot.drivetrain.setStrafeLimit(999999999999.0);
		robot.drivetrain.setAngleAccelLimit(99999999999999.0);
		robot.drivetrain.setMaxTurnSpeed(999999999.0);
		
		start = robot.drivetrain.getPriamryDist();
	}

}
