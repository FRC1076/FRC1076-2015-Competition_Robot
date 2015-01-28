package org.pihisamurai;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;

public class Manipulator {

	private Robot robot;
	private Jaguar LiftMotor;
	private DoubleSolenoid soleniod;

	Manipulator(Robot r) {
		this.robot = r;
		
		LiftMotor = new Jaguar(5);
		
		soleniod = new DoubleSolenoid(0, 1);//need to check
		
	}
	
	public void liftPower(double power) {
		if(power == 0)
		{
			soleniod.set(DoubleSolenoid.Value.kForward);
		}
		else {
			soleniod.set(DoubleSolenoid.Value.kReverse);
		}
		
		
		LiftMotor.set(power); // For lift motors
	}
	
	public void rest()
	{
		
		soleniod.set(DoubleSolenoid.Value.kOff); //options are { kOff, kReverse and kForward } after Value.
	}
	
	
}