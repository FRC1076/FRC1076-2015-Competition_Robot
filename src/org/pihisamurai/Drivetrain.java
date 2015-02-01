package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Drivetrain {

	Jaguar backLeftMotor;
	Jaguar frontLeftMotor;
	Jaguar backRightMotor;
	Jaguar frontRightMotor;
	Jaguar StrafeMotor;

	private double divisor;

	private Robot robot;

	public Drivetrain(Robot r) {
		robot = r;

		backLeftMotor = new Jaguar(0); // Changed ?? to 0 - 4
		frontLeftMotor = new Jaguar(1);
		backRightMotor = new Jaguar(2);
		frontRightMotor = new Jaguar(3);
		StrafeMotor = new Jaguar(4);

		divisor = 1;
	}

	public void rightPower(double power) {
		backRightMotor.set(power * divisor);
		frontRightMotor.set(power * divisor); // May need to be opposite directions
	}

	public void leftPower(double power) {
		backLeftMotor.set(-power * divisor);
		frontLeftMotor.set(-power * divisor); // May need to be opposite directions
	}

	public void strafePower(double power) {
		StrafeMotor.set(-power * divisor);
	}
	
	public void setDiv(double number) {
		divisor = number;
	}
	
	public double getDiv() {
		return divisor;
	}

	/*
	 * Implement for Autonomous once encoders on robot
	 * 
	 * public void moveDistance(double distance, double speed){
	 * 
	 * }
	 * 
	 * public void moveArc(double radius, double angle, double speed){
	 * 
	 * }
	 * 
	 * public void sharpTurn(double angle, double speed){
	 * 
	 * }
	 * 
	 * public void strafeDistance(double distance, double speed){
	 * 
	 * }
	 */
}
