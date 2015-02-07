package org.pihisamurai;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;

public class Manipulator {

	private Robot robot;
	private Jaguar LiftMotor;
	private DoubleSolenoid soleniod;

	Manipulator(Robot r) {
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