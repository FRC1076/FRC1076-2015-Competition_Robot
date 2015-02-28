package org.pihisamurai;

public class Autonomous {

	private Robot robot;
	int mode;
	AutonomousForward autnomousForward;
	AutonomousNothing autonomousNothing;
	AutonomousTotes autonomousTotes;

	public Autonomous(Robot r) {
		this.robot = r;
		autnomousForward = new AutonomousForward(r);
		autonomousNothing = new AutonomousNothing(r);
		autonomousTotes = new AutonomousTotes(r);
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
			autonomousTotes.run();
			break;
		}
	}

	public void init(int d) {
		mode = d;

		System.out.print(mode);
		switch (mode) {
		case 0:
			autonomousNothing.run();
			break;
		case 1:
			autnomousForward.init();
			break;
		case 2:
			autonomousTotes.init("2 d 1 u 5 l");
			break;
		}
	}
}
