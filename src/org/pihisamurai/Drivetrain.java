package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {

	private double speedModifier = 1;

	private static final double ANGLE_P = 3;
	private static final double ANGLE_I = 0.0;
	private static final double ANGLE_D = 0.1;

	private static final double RATE_P = 0.25;
	private static final double RATE_I = 0.00;
	private static final double RATE_D = 0.05;

	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 3;
	private static final int RIGHT_ENCODER_CHANNEL_B = 2;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * Math.PI / 360;

	private static final double MAX_TURN_SPEED = 2; // Radians per second

	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	private Gyro gyro;
	public double targetAngle;

	private Jaguar backLeftMotor;
	private Jaguar frontLeftMotor;
	private Jaguar backRightMotor;
	private Jaguar frontRightMotor;
	private Jaguar StrafeMotor;

	private PIDController HeadingRatePID;
	private PIDController HeadingAnglePID;

	private Robot robot;

	double speed = 0;

	public Drivetrain(Robot r) {
		this.robot = r;

		gyro = new Gyro(0);
		
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
				return getTurnRate();
			}
		};

		PIDOutput motorWrite = new PIDOutput() {
			public void pidWrite(double a) {
				if (speed > 1 - Math.abs(a))
					speed = 1 - Math.abs(a);
				else if (speed < -1 + Math.abs(a))
					speed = -1 + Math.abs(a);
				else if (Math.abs(speed + Math.abs(a)) < 0.1 && Math.abs(speed - Math.abs(a)) < 0.1) {
					backLeftMotor.set(0);
					frontLeftMotor.set(0);
					backRightMotor.set(0);
					frontRightMotor.set(0);
				} else {
					backLeftMotor.set(-a + speed);
					frontLeftMotor.set(-a + speed);
					backRightMotor.set(a + speed);
					frontRightMotor.set(a + speed);
				}
			}
		};

		PIDSource headingAbsolute = new PIDSource() {
			public double pidGet() { // Angle Robot at
				return getAngle();
			}
		};

		PIDOutput speedTargetWrite = new PIDOutput() {
			public void pidWrite(double a) {
				HeadingRatePID.setSetpoint(a);
			}
		};

		HeadingRatePID = new PIDController(RATE_P, RATE_I, RATE_D, 0, headingSpeed, motorWrite, 0.02);

		HeadingAnglePID = new PIDController(ANGLE_P, ANGLE_I, ANGLE_D, 0, headingAbsolute, speedTargetWrite, 0.02);
		HeadingAnglePID.setInputRange(0, Math.PI * 2);
		HeadingAnglePID.setContinuous(true);
		HeadingAnglePID.setOutputRange(-MAX_TURN_SPEED, MAX_TURN_SPEED);

		targetAngle = getAngle();
		
		// SmartDashboard.putNumber("P", HEADING_P);
		// SmartDashboard.putNumber("I", HEADING_I);
		// SmartDashboard.putNumber("D", HEADING_D);
	}

	public double getAngle() {
		return (gyro.getAngle()/180*Math.PI) % (Math.PI * 2);
		//return ((rightEncoder.getDistance() - leftEncoder.getDistance()) / (28) + Math.PI) % (2 * Math.PI);
	}

	public double getTurnRate() {
		return gyro.getRate()/180*Math.PI;    //7 mv per degree per second
		//return (rightEncoder.getRate() - leftEncoder.getRate()) / (28);
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
		HeadingAnglePID.setSetpoint( (a + getAngle()) % (Math.PI*2));
	}

	public void setPrimary(double a) {
		SmartDashboard.putNumber("Target Angle", HeadingAnglePID.getSetpoint());
		SmartDashboard.putNumber("Current Angle", getAngle());
		SmartDashboard.putNumber("Turn Rate", getTurnRate());
		speed = a;
	}

	public void setAngleTarget(double stickX) {
		if (Math.abs(stickX) < 0.1) {
			if (!HeadingAnglePID.isEnable()){
				HeadingAnglePID.setSetpoint(getAngle());
				HeadingAnglePID.enable();
			}
		} else {
			HeadingAnglePID.disable();
			HeadingRatePID.setSetpoint(stickX * MAX_TURN_SPEED);
		}
	}
}
