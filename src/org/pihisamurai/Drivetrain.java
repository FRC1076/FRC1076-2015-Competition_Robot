package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {

	private static final double HEADING_P = 0.2;
	private static final double HEADING_I = 0.005;
	private static final double HEADING_D = 0.05;

	private static final double ANGLE_P = 0.2;
	private static final double ANGLE_I = 0.005;
	private static final double ANGLE_D = 0.05;

	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 3;
	private static final int RIGHT_ENCODER_CHANNEL_B = 2;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * Math.PI / 360;

	private static final double MAX_TURN_SPEED = 5; // Radians per second

	private Encoder leftEncoder;
	private Encoder rightEncoder;

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

		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B);
		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B);
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

		PIDSource headingSpeed = new PIDSource() {
			public double pidGet() {
				HeadingRatePID.setPID(SmartDashboard.getNumber("P"), SmartDashboard.getNumber("I"),
						SmartDashboard.getNumber("D"));

				SmartDashboard.putNumber("LEFT DIST", leftEncoder.getDistance());
				SmartDashboard.putNumber("RIGHT DIST", rightEncoder.getDistance());
				SmartDashboard.putNumber("LEFT SPEED", leftEncoder.getRate());
				SmartDashboard.putNumber("RIGHT SPEED", rightEncoder.getRate());
				SmartDashboard.putNumber("ANGLE", (leftEncoder.getDistance() - rightEncoder.getDistance()) / (28));
				SmartDashboard.putNumber("ANGLE RATE", (leftEncoder.getRate() - rightEncoder.getRate()) / (28));
				SmartDashboard.putNumber("SETPOINT", HeadingRatePID.getSetpoint());
				return getTurnRate();
			} 
		};

		PIDOutput motorWrite = new PIDOutput() {
			public void pidWrite(double a) {
				SmartDashboard.putNumber("Write", a);
				if (speed > 1 - Math.abs(a))
					speed = 1 - Math.abs(a);
				else if (speed < -1+Math.abs(a))
					speed = -1+Math.abs(a);
				backLeftMotor.set(-a + speed);
				frontLeftMotor.set(-a + speed);
				backRightMotor.set(a + speed);
				frontRightMotor.set(a + speed);
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

		HeadingRatePID = new PIDController(HEADING_P, HEADING_I, HEADING_D, 0, headingSpeed, motorWrite, 20);

		HeadingAnglePID = new PIDController(ANGLE_P, ANGLE_I, ANGLE_D, 0, headingAbsolute, speedTargetWrite, 20);
		HeadingAnglePID.setInputRange(0, Math.PI * 2);
		HeadingAnglePID.setContinuous(true);

		SmartDashboard.putNumber("P", HEADING_P);
		SmartDashboard.putNumber("I", HEADING_I);
		SmartDashboard.putNumber("D", HEADING_D);
	}

	public double getAngle() {
		return ((rightEncoder.getDistance() - leftEncoder.getDistance()) / (28) + Math.PI) % (2 * Math.PI);
	}

	public double getTurnRate() {
		return (rightEncoder.getRate() - leftEncoder.getRate()) / (28);
	}

	public void setStrafe(double power) {
		if (power < 0.1 && power > -0.1)
			StrafeMotor.set(power);
		else
			StrafeMotor.set(0);
	}

	public void start() {
		HeadingRatePID.disable();
		HeadingRatePID.enable();
	}

	public void turn(double a) {
		HeadingAnglePID.enable(); // I DONT KNOW IF SETPOINT IS RELATIVE TO WHERE IT IS WHEN I STARTED IT
		HeadingAnglePID.setSetpoint(a + getAngle()); // I.E. WOULD SETPOINT 0 HERE BE WHERE ITS AT
	} // Dunno if the + getAngle() is neccasary

	public void setPrimary(double a) {
		speed = a;
	}

	public void setAngleTarget(double stickX) {
		if (stickX < 0.1 && stickX > -0.1)
			return;
		HeadingRatePID.setSetpoint(stickX * MAX_TURN_SPEED);
		HeadingAnglePID.disable();
	}
}
