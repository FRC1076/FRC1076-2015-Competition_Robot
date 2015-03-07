package org.pihisamurai;

public class Autonomous {

	private Robot robot;
	int mode;
	AutonomousNothing autonomousNothing;
	AutonomousForward autnomousForward;
	AutonomousStrafe autonomousStrafe;
	AutonomousTotes autonomousTotes;

	public Autonomous(Robot r) {
		this.robot = r;
		autonomousNothing = new AutonomousNothing(robot);
		autnomousForward = new AutonomousForward(robot);
		autonomousStrafe = new AutonomousStrafe(robot);
		autonomousTotes = new AutonomousTotes(robot);
	}

	public void run() {
		switch (mode) {
		case 0:
			autonomousNothing.run();
			break;
		case 1:
			autnomousForward.run();
			break;
		case 2:
			autonomousStrafe.run();
			break;
		case 3:
			autonomousTotes.run();
			break;
		}
	}

	public void init(int d) {
		mode = d;

		System.out.print(mode);
		switch (mode) {
		case 0:
			autonomousNothing.init();
			break;
		case 1:
			autnomousForward.init();
			break;
		case 2:
			autonomousStrafe.init();
			break;
		case 3:
			autonomousTotes.init("2 d 1 u 5 l");
			break;
		}
	}
}
