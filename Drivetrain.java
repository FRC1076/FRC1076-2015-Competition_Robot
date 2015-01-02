package org.pihisamurai;

public class Drivetrain {
	private Robot robot;

	public Drivetrain(Robot r) {
		this.robot = r;
		Jaguar motorFrontRight = new Jaguar(1);
		Jaguar motorFrontLeft = new Jaguar(4);
		Jaguar motorBackRight = new Jaguar(2);
		Jaguar motorBackLeft = new Jaguar(3);
	}

	public void setSpeed(double speed) {
		motorFrontRight.set(-speed);
		motorFrontLeft.set(speed);
		motorBackRight.set(-speed);
		motorBackLeft.set(speed);
	}

	public void setSpeed(double speedLeft, double speedRight) {
		motorFrontRight.set(-speedRight);
		motorFrontLeft.set(speedLeft);
		motorBackRight.set(-speedRight);
		motorBackLeft.set(speedLeft);
	}

}
