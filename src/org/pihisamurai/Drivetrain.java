package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class Drivetrain {

	private static double P = 1;
	private static double I = 0;
	private static double D = 0;
	private static double F = 0;

	private static double DISTANCE_PER_PULSE_MAIN = 4*16 / 128; //Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static double DISTANCE_PER_PULSE_STRAFE = 4*16 / 128; //Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	
	Encoder leftEncoder;
	Encoder rightEncoder;
	Encoder strafeEncoder;
	
	Jaguar backLeftMotor;
	Jaguar frontLeftMotor;
	Jaguar backRightMotor;
	Jaguar frontRightMotor;
	Jaguar StrafeMotor;
	Jaguar LiftMotor;
	
	PIDController leftPIDControl;
	PIDController rightPIDControl;
	PIDController strafePIDControl;

	private Robot robot;

	public Drivetrain(Robot r) {
		this.robot = r;

		leftEncoder = new Encoder(0,1, true); //TODO ports, oreintation
		rightEncoder = new Encoder(2,3, false);
		strafeEncoder = new Encoder(2,3, false);
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN); //TODO placeholder
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		strafeEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_STRAFE);
		leftEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
		rightEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
		strafeEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
		
		
		
		backLeftMotor = new Jaguar(0); // Changed ?? to 0 - 4
		frontLeftMotor = new Jaguar(1);
		backRightMotor = new Jaguar(2);
		frontRightMotor = new Jaguar(3);
		StrafeMotor = new Jaguar(4);
		LiftMotor = new Jaguar(5);

		PIDOutput leftMotors = new PIDOutput(){
			public void pidWrite(double power) {
				backLeftMotor.set(-power);
				frontLeftMotor.set(-power); // May need to be opposite directions
			}
		};
		
		PIDOutput rightMotors = new PIDOutput(){
			public void pidWrite(double power) {
				backLeftMotor.set(-power);
				frontLeftMotor.set(-power); // May need to be opposite directions
			}
		};
		

		leftPIDControl = new PIDController(P, I, D, F, leftEncoder, leftMotors);
		rightPIDControl = new PIDController(P, I, D, F, leftEncoder, rightMotors);
		strafePIDControl = new PIDController(P, I, D, F, leftEncoder, StrafeMotor);
	}
	
	public void setRightSpeed(double speed){
		rightPIDControl.setSetpoint(speed);
	}
	
	public void setLeftSpeed(double speed){
		leftPIDControl.setSetpoint(speed);
	}
	
	public void setStrafeSpeed(double speed){
		strafePIDControl.setSetpoint(speed);
	}
	
	/*

	public void rightPower(double power) {
		backRightMotor.set(power);
		frontRightMotor.set(power); // May need to be opposite directions
	}

	public void leftPower(double power) {
		backLeftMotor.set(-power);
		frontLeftMotor.set(-power); // May need to be opposite directions
	}

	public void strafePower(double power) {
		StrafeMotor.set(-power);
	}*/

	public void liftPower(double power) {
		LiftMotor.set(power); // For lift motors
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
