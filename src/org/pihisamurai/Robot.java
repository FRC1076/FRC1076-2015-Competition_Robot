package org.pihisamurai;

import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {

	// Common Code
	public Drivetrain drivetrain = new Drivetrain(this);
	public Manipulator manipulator = new Manipulator(this);

	// Modes
	public Teleoperated teleoperated = new Teleoperated(this);
	public Test test = new Test(this);
	public Autonomous autonomous = new Autonomous(this);

	public Gamepad gamepad = new Gamepad(1);

	public void autonomous() {
		System.out.println("Autonomous Mode");
		autonomous.run();
	}

	public void operatorControl() {
		System.out.println("Teleoperated Mode");
		teleoperated.run();
	}

	public void test() {
		System.out.println("Test Mode");
		test.run();
	}

	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
