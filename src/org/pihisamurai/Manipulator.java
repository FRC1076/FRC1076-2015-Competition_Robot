package org.pihisamurai;

public class Manipulator {

	private Robot robot;
	private Jaguar LiftMotor;

	Manipulator(Robot r) {
		this.robot = r;
		
		LiftMotor = new Jaguar(5);
	}
	
	public void liftPower(double power) {
		LiftMotor.set(power); // For lift motors
	}
}
