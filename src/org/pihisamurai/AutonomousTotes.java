package org.pihisamurai;

public class AutonomousTotes {

	Robot robot;

	UltrasonicSensor ultrasonic = new UltrasonicSensor(2);
	InfraredSensor infrared = new InfraredSensor(1);

	AutonomousTotes(Robot robot) {
		this.robot = robot;
	}

	private int state;
	private double startDist;

	public void run() {
		switch (state) {
		case 0:
			startDist = robot.drivetrain.getPriamryDist();
			state = 1;
			break;

		case 1:
			robot.drivetrain.setPrimary(0.3);
			if (robot.drivetrain.getPriamryDist() - startDist > startDist) {
				state = 2;
				robot.drivetrain.setPrimary(0);
			}
			robot.drivetrain.setPrimary(0.3);
			break;
		case 2:
			robot.manipulator.setTargetHeight(0); // bottom box

			if (robot.manipulator.atTarget()) {
				state = 3;
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount() + 1);
			}
			// elevetor down
			// if down enough state 3
			break;
		case 3:
			robot.manipulator.setTargetHeight(2); // higher box

			if (robot.manipulator.atTarget()) 
				if (robot.drivetrain.speedController.getBoxCount() < 3)
					state = 0;
				else
					state = 4; // done
		}
	}

	public void init() {
		state = 0;

	}

}
