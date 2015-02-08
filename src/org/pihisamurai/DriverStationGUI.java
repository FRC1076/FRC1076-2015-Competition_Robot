package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverStationGUI {

	Robot robot;
	double time;

	DriverStationGUI(Robot r) {
		robot = r;
	}

	public void update() {
		SmartDashboard.putNumber("Right X", robot.gamepad.getRightX());
		SmartDashboard.putNumber("Right Y", robot.gamepad.getRightY());
		SmartDashboard.putNumber("Box Count", robot.teleop.box);
	}

}
