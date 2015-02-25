package org.pihisamurai;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;

public class Manipulator {

	// A "robot" variable to access methods therein:
	private Robot robot;
	// For the motor of the lift:
	private Jaguar LiftMotor;
	// For the solenoid of the lift:
	private DoubleSolenoid soleniod;

	Manipulator(Robot r) {
		// Initialization of variable values:
		this.robot = r;
		LiftMotor = new Jaguar(5);
		soleniod = new DoubleSolenoid(0, 1);
	}

	public void liftPower(double power) {
		if (power < 0.1 && power > -0.1) {
			soleniod.set(DoubleSolenoid.Value.kForward);
			LiftMotor.set(0);
		} else {
			soleniod.set(DoubleSolenoid.Value.kReverse);
			LiftMotor.set(power);
		}
	}

}