package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {

	private static final double	P						= 0.2;
	private static final double	I						= 0.005;
	private static final double	D						= 0.05;
	private static final double	F						= 0;

	private static final int	LEFT_ENCODER_CHANNEL_A	= 0;
	private static final int	LEFT_ENCODER_CHANNEL_B	= 1;

	private static final int	RIGHT_ENCODER_CHANNEL_A	= 3;
	private static final int	RIGHT_ENCODER_CHANNEL_B	= 2;

	// private static final int STRAFE_ENCODER_CHANNEL_A = 0;
	// private static final int STRAFE_ENCODER_CHANNEL_B = 1;

	// Circumference of wheel, modified by gear ratio
	// divided by number of
	// pulses in one rotation of encoder
	private static final double	DISTANCE_PER_PULSE_MAIN	= 4 * Math.PI / 360;

	Encoder						leftEncoder;
	Encoder						rightEncoder;
	// Encoder strafeRateEncoder;

	Jaguar						backLeftMotor;
	Jaguar						frontLeftMotor;
	Jaguar						backRightMotor;
	Jaguar						frontRightMotor;
	Jaguar						StrafeMotor;

	PIDController				PIDHeading;

	private Robot				robot;
	
	double speed = 0;

	public Drivetrain(Robot r) {
		this.robot = r;

		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B, false);
		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B, false);
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);

		backLeftMotor = new Jaguar(0) {
			public void set(double p) {
				super.set(-p);
			}
		};
		frontLeftMotor = new Jaguar(1) {
			public void set(double p) {
				super.set(-p);
			}
		};
		backRightMotor = new Jaguar(2);
		frontRightMotor = new Jaguar(3);
		StrafeMotor = new Jaguar(4);
		
		PIDSource robotAngle = new PIDSource() {
			public double pidGet() {
				PIDHeading.setPID(SmartDashboard.getNumber("P"), SmartDashboard.getNumber("I"), SmartDashboard.getNumber("D"));
				
				SmartDashboard.putNumber("LEFT DIST" , leftEncoder.getDistance());
				SmartDashboard.putNumber("RIGHT DIST" , rightEncoder.getDistance());
				SmartDashboard.putNumber("LEFT SPEED" , leftEncoder.getRate());
				SmartDashboard.putNumber("RIGHT SPEED" , rightEncoder.getRate());
				SmartDashboard.putNumber("ANGLE" , (leftEncoder.getDistance()-rightEncoder.getDistance())/(28));
				SmartDashboard.putNumber("ANGLE RATE" , (leftEncoder.getRate()-rightEncoder.getRate())/(28));
				SmartDashboard.putNumber("SETPOINT" , PIDHeading.getSetpoint());
					return (-leftEncoder.getRate()+rightEncoder.getRate())/(28);
			}
		};
		
		PIDOutput angleWrite = new PIDOutput(){
			public void pidWrite(double a) {
				SmartDashboard.putNumber("Write", a);
				backLeftMotor.set(-a+speed);
				frontLeftMotor.set(-a+speed);
				backRightMotor.set(a+speed);
				frontRightMotor.set(a+speed);
			}
		};

		PIDHeading = new PIDController(P, I, D, F, robotAngle, angleWrite);
		

		SmartDashboard.putNumber("P" , P);
		SmartDashboard.putNumber("I" , I);
		SmartDashboard.putNumber("D" , D);
	}


	public void strafePower(double power) {
		StrafeMotor.set(power);
	}

	public void start() {
		PIDHeading.disable();
		PIDHeading.enable();
		
	}


	public void setY(double stickY) {
		speed = stickY;
	}


	public void modAngle(double stickX) {
	//	stickX = stickX + PIDHeading.getSetpoint();
	//	System.out.println(stickX);
		
		PIDHeading.setSetpoint(stickX*5);
	}
}
