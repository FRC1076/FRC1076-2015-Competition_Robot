package org.pihisamurai;

public class Test {
	private Robot robot;

	public Test(Robot r) {
		this.robot = r;
	}

	public void run() {
		robot.drivetrain.setSpeed(0);

		while (true) {
			while (!robot.gamepad.getButtonA()) {
				robot.sleep(10);
			}

			// Display Something Cool
			System.out
					.println("  _____ _ _    _ _  _____                                 _ \n |  __ (_) |  | (_)/ ____|                               (_)\n | |__) || |__| |_| (___   __ _ _ __ ___  _   _ _ __ __ _ _ \n |  ___/ |  __  | |\\___ \\ / _` | '_ ` _ \\| | | | '__/ _` | |\n | |   | | |  | | |____) | (_| | | | | | | |_| | | | (_| | |\n |_|   |_|_|  |_|_|_____/ \\__,_|_| |_| |_|\\__,_|_|  \\__,_|_|");

			while (robot.gamepad.getButtonA()) {
				robot.sleep(10);
			}
		}
	}

}
