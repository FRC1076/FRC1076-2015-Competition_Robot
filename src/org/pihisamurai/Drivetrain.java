package org.pihisamurai;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {

	private double speedModifier = 1;

	private static final double ANGLE_P = 8;
	private static final double ANGLE_I = 0;
	private static final double ANGLE_D = 0;

	private static final double RATE_P = 0.25;
	private static final double RATE_I = 0.00;
	private static final double RATE_D = 0.05;

	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 3;
	private static final int RIGHT_ENCODER_CHANNEL_B = 2;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * Math.PI / 360;

	private double maxTurnSpeed = 5; // Radians per second

	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	private Gyro gyro;

	private Jaguar backLeftMotor;
	private Jaguar frontLeftMotor;
	private Jaguar backRightMotor;
	private Jaguar frontRightMotor;
	private Jaguar StrafeMotor;

	private PIDController HeadingRatePID;
	private PIDController HeadingAnglePID;

	private Robot robot;

	double speed = 0;

	private static final double TOTE_RADIUS = 1.2; // meters
	private BuiltInAccelerometer accelerometer;
	
	public Drivetrain(Robot r) {
		robot = r;
		
		gyro = new Gyro(0) {
			public double getAngle() {
				double a = Math.toRadians(super.getAngle());
				while (a > Math.PI*2) a -= Math.PI*2;
				while (a < 0) a += Math.PI*2;
				return a;
			}

			public double getRate() {
				return Math.toRadians(super.getRate());
			}
		};
		
		//gyro.setSensitivity(0.7);
		
		accelerometer = new BuiltInAccelerometer() {
			public double getX() {
				return super.getX() * 9.80665;
			}

			public double getY() {
				return super.getZ() * 9.80665;
			}

			public double getZ() {
				return super.getY() * 9.80665;
			}
		};
		
		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B);
		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B);
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);

		backLeftMotor = new Jaguar(0) {
			public void set(double p) {
				super.set(-p * speedModifier);
			}
		};
		frontLeftMotor = new Jaguar(1) {
			public void set(double p) {
				super.set(-p * speedModifier);
			}
		};
		backRightMotor = new Jaguar(2) {
			public void set(double p) {
				super.set(p * speedModifier * 0.85);
			}
		};
		frontRightMotor = new Jaguar(3) {
			public void set(double p) {
				super.set(p * speedModifier * 0.85);
			}
		};
		StrafeMotor = new Jaguar(4) {
			public void set(double p) {
				super.set(p * speedModifier);
			}
		};

		PIDSource headingSpeed = new PIDSource() {
			public double pidGet() {
				return gyro.getRate();
			}
		};

		PIDOutput motorWrite = new PIDOutput() {
			// `a` represents the amount to increase or decrease the motor speed
			// by to compensate for the rotation
			// TODO: Rename the variable
			public void pidWrite(double a) {
				// Set the average of the speed on both sides to compensate for error
				// in one direction
				if (speed > 1 - Math.abs(a))
					speed = 1 - Math.abs(a);
				// to compensate in the other direction
				else if (speed < Math.abs(a) - 1)
					speed = Math.abs(a) - 1;
				// Set a deadzone for the speed
				else if (Math.abs(speed + Math.abs(a)) < 0.1 &&
						 Math.abs(speed - Math.abs(a)) < 0.1) {
					// Set the speed to zero while in the deadzone
					speed = 0;
					// There's no error because we just want to stop
					a = 0;
				}
				// Set the motors corrected for the error
				backLeftMotor.set(speed - a);
				frontLeftMotor.set(speed - a);
				backRightMotor.set(speed + a);
				frontRightMotor.set(speed + a);
			}
		};

		PIDSource headingAbsolute = new PIDSource() {
			public double pidGet() { // Angle Robot at
				return gyro.getAngle();
			}
		};

		PIDOutput speedTargetWrite = new PIDOutput() {

			private double lastGyroSpeed = 0;
			private long lastGyroTime = System.nanoTime();
			
			public void pidWrite(double a) {				 
				HeadingRatePID.setSetpoint(a);
			}
		};

		HeadingRatePID = new PIDController(RATE_P, RATE_I, RATE_D, 0, headingSpeed, motorWrite, 0.02);

		HeadingAnglePID = new PIDController(ANGLE_P, ANGLE_I, ANGLE_D, 0, headingAbsolute, speedTargetWrite, 0.02){
			public void setSetpoint(double a){
				while (a > Math.PI*2) a -= Math.PI*2;
				while (a < 0) a += Math.PI*2;
				super.setSetpoint(a);
			}
		};
		HeadingAnglePID.setInputRange(0, Math.PI * 2);
		HeadingAnglePID.setContinuous(true);
		HeadingAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);
	}

	
	
	public void setStrafe(double power) {
		if (power < 0.1 && power > -0.1)
			StrafeMotor.set(0);
		else
			StrafeMotor.set(power);
	}
	
	public void start() {
		HeadingRatePID.disable();
		HeadingRatePID.enable();
	}

	public void turn(double a) {
		HeadingAnglePID.enable();
		HeadingAnglePID.setSetpoint(a + gyro.getAngle());
	}

	public void setPrimary(double a) {
		speed = a;
	}

	public void setAngleTarget(double stickX) {
		if (Math.abs(stickX) < 0.1) {
			if (!HeadingAnglePID.isEnable()){
				HeadingAnglePID.setSetpoint(gyro.getAngle());
				HeadingAnglePID.enable();
			}
		} else {
			HeadingAnglePID.disable();
			HeadingRatePID.setSetpoint(stickX * maxTurnSpeed);
		}
	}
	
	public void setMaxTurnSpeed(double speed) {
		maxTurnSpeed = speed;
		HeadingAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);
	}
}
