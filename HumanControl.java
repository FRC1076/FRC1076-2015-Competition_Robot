package org.pihisamurai;

public class HumanControl {
	Robot robot;

	public HumanControl(Robot r) {
		robot = r;
	}

	public double getLeftSpeed() {
		return robot.gamepad.getLeftY();
	}

	public double getRightSpeed() {
		return robot.gamepad.getRightY();
	}

}
