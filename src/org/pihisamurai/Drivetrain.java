package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {

	private static final double	P							= 0.015;
	private static final double	I							= 0.000;
	private static final double	D							= 0.0025;
	private static final double	F							= 0;

	private static final int	LEFT_ENCODER_CHANNEL_A		= 0;
	private static final int	LEFT_ENCODER_CHANNEL_B		= 1;

	private static final int	RIGHT_ENCODER_CHANNEL_A		= 3;
	private static final int	RIGHT_ENCODER_CHANNEL_B		= 2;

	//private static final int	STRAFE_ENCODER_CHANNEL_A	= 0;
	//private static final int	STRAFE_ENCODER_CHANNEL_B	= 1;

	// Circumference of wheel, modified by gear ratio
	// divided by number of
	// pulses in one rotation of encoder
	private static final double	DISTANCE_PER_PULSE_MAIN		= 4 * Math.PI / 360;
	private static final double	DISTANCE_PER_PULSE_STRAFE	= 4 * Math.PI / 360;

	Encoder						leftRateEncoder;
	Encoder						rightRateEncoder;
	//Encoder						strafeRateEncoder;

	Jaguar						backLeftMotor;
	Jaguar						frontLeftMotor;
	Jaguar						backRightMotor;
	Jaguar						frontRightMotor;
	Jaguar						StrafeMotor;
	Jaguar						LiftMotor;

	PIDController				leftPIDSpeedControl;
	PIDController				rightPIDSpeedControl;
	//PIDController				strafePIDSpeedControl;

	//PIDController				leftPIDDistControl;
	//PIDController				rightPIDDistControl;
	//PIDController				strafePIDDistControl;

	private Robot				robot;

	public Drivetrain(Robot r) {
		this.robot = r;

		leftRateEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false); // TODO
																								// ports,
																								// oreintation
		rightRateEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, false);
	//	strafeRateEncoder = new Encoder(STRAFE_ENCODER_CHANNEL_A, STRAFE_ENCODER_CHANNEL_B, false);
		leftRateEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN); // TODO
																		// placeholder
		rightRateEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
	//	strafeRateEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_STRAFE);
		leftRateEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
		rightRateEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
	//	strafeRateEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);


																		// placeholder
	//	rightDistEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
	//	strafeDistEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_STRAFE);
	//	leftDistEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
	//	rightDistEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
	//	strafeDistEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);

		backLeftMotor = new Jaguar(0);
		frontLeftMotor = new Jaguar(1);
		backRightMotor = new Jaguar(2);
		frontRightMotor = new Jaguar(3);
		StrafeMotor = new Jaguar(4);
		LiftMotor = new Jaguar(5);

		PIDOutput leftMotors = new PIDOutput() {
			public void pidWrite(double power) {
			//	leftPIDSpeedControl.setPID(SmartDashboard.getDouble("DB/Slider 1"), SmartDashboard.getDouble("DB/Slider 2"), SmartDashboard.getDouble("DB/Slider 3"));
			//	rightPIDSpeedControl.setPID(SmartDashboard.getDouble("DB/Slider 1"), SmartDashboard.getDouble("DB/Slider 2"), SmartDashboard.getDouble("DB/Slider 3"));
			//	System.out.println(leftPIDSpeedControl.getP());
				if(power < 0.2 && power > -0.2){
					backLeftMotor.set(0);
					frontLeftMotor.set(0);
				}else{
					backLeftMotor.set(-power);
					frontLeftMotor.set(-power);
				}
			}
		};

		PIDOutput rightMotors = new PIDOutput() {
			public void pidWrite(double power) {
				if(power < 0.2 && power > -0.2){
					backRightMotor.set(0);
					frontRightMotor.set(0);
				} else {
					backRightMotor.set(power);
					frontRightMotor.set(power);
				}
				
			}
		};

		SmartDashboard.putDouble("DB/Slider 1", P);
		SmartDashboard.putDouble("DB/Slider 2", I);
		SmartDashboard.putDouble("DB/Slider 3", D);
		
		leftPIDSpeedControl = new PIDController(P, I, D, F, leftRateEncoder, leftMotors);
		rightPIDSpeedControl = new PIDController(P, I, D, F, rightRateEncoder, rightMotors);
	//	strafePIDSpeedControl = new PIDController(P, I, D, F, leftRateEncoder, StrafeMotor);

	//	leftPIDDistControl = new PIDController(P, I, D, F, leftDistEncoder, leftMotors);
	//	rightPIDDistControl = new PIDController(P, I, D, F, leftDistEncoder, rightMotors);
	//	strafePIDDistControl = new PIDController(P, I, D, F, leftDistEncoder, StrafeMotor);
	}
	
	
	
	public void start(){
		leftPIDSpeedControl.disable();
		rightPIDSpeedControl.disable();
		leftPIDSpeedControl.enable();
		rightPIDSpeedControl.enable();
	}

	public void setRightSpeed(double speed) {
	//	System.out.println("Left: " + leftRateEncoder.getRate());
	//	System.out.println("Right: " + rightRateEncoder.getRate());
		
	//	rightPIDDistControl.disable();
	//	rightPIDSpeedControl.enable();

	//	SmartDashboard.putDouble("Slider 1", leftRateEncoder.getRate());
	//  SmartDashboard.putDouble("Slider 2", leftRateEncoder.getDistance());
		rightPIDSpeedControl.setSetpoint(speed);
	}

	public void setLeftSpeed(double speed) {
	//	SmartDashboard.putDouble("Slider 3", speed);
	//	leftPIDDistControl.disable();
	//	leftPIDSpeedControl.enable();
		leftPIDSpeedControl.setSetpoint(speed);
	}

	public void setStrafeSpeed(double speed) {
	//	strafePIDDistControl.disable();
	//	strafePIDSpeedControl.enable();
	//	strafePIDSpeedControl.setSetpoint(speed);
	}

	/*public void setRightDist(double dist) {
		rightPIDSpeedControl.disable();
		rightPIDDistControl.enable();
		rightPIDDistControl.setSetpoint(dist);
	}

	public void setLeftDist(double dist) {
		leftPIDSpeedControl.disable();
		leftPIDDistControl.enable();
		leftPIDDistControl.setSetpoint(dist);
	}

	public void setStrafeDist(double dist) {
		strafePIDSpeedControl.disable();
		strafePIDDistControl.enable();
		strafePIDDistControl.setSetpoint(dist);
	}*/

	/*
	 * 
	 * public void rightPower(double power) {
	 * backRightMotor.set(power);
	 * frontRightMotor.set(power); // May need to be
	 * opposite directions }
	 * 
	 * public void leftPower(double power) {
	 * backLeftMotor.set(-power);
	 * frontLeftMotor.set(-power); // May need to be
	 * opposite directions }
	 * 
	 * public void strafePower(double power) {
	 * StrafeMotor.set(-power); }
	 */

	public void liftPower(double power) {
		LiftMotor.set(power); // For lift motors
	}

	/*
	 * Implement for Autonomous once encoders on robot
	 * 
	 * public void moveDistance(double distance, double
	 * speed){
	 * 
	 * }
	 * 
	 * public void moveArc(double radius, double angle,
	 * double speed){
	 * 
	 * }
	 * 
	 * public void sharpTurn(double angle, double speed){
	 * 
	 * }
	 * 
	 * public void strafeDistance(double distance, double
	 * speed){
	 * 
	 * }
	 */
}
