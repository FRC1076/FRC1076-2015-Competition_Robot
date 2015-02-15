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

	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 3;
	private static final int RIGHT_ENCODER_CHANNEL_B = 2;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * 0.0254 * Math.PI / 360;

	private double maxTurnSpeed = 5; // Radians per second

	private Encoder leftEncoder;
	private Encoder rightEncoder;

	private Gyro gyro;

	private Jaguar backLeftMotor;
	private Jaguar frontLeftMotor;
	private Jaguar backRightMotor;
	private Jaguar frontRightMotor;
	private Jaguar strafeMotor;

	private PIDController HeadingRatePID;
	private PIDController HeadingAnglePID;

	private Robot robot;

	public SpeedController speedController;

	double angleSpeedTarget = 0;
	double angleSpeed = 0;
	double speed = 0;

	private BuiltInAccelerometer accelerometer;

	public Drivetrain(Robot r) {
		robot = r;

		speedController = new SpeedController(r);

		SmartDashboard.putNumber("Angle P", 8);
		SmartDashboard.putNumber("Angle I", 0);
		SmartDashboard.putNumber("Angle D", 0);

		SmartDashboard.putNumber("Rate P", 0.25);
		SmartDashboard.putNumber("Rate I", 0.0);
		SmartDashboard.putNumber("Rate D", 0.05);

		gyro = new Gyro(0) {
			public double getAngle() {
				double a = Math.toRadians(super.getAngle());
				while (a > Math.PI * 2)
					a -= Math.PI * 2;
				while (a < 0)
					a += Math.PI * 2;
				return a;
			}

			public double getRate() {
				return Math.toRadians(super.getRate());
			}
		};

		// gyro.setSensitivity(0.7);

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
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p);
			}
		};
		frontLeftMotor = new Jaguar(1) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p);
			}
		};
		backRightMotor = new Jaguar(2) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p); // * 0.85);
			}
		};
		frontRightMotor = new Jaguar(3) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p);// * 0.85);
			}
		};
		strafeMotor = new Jaguar(4) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p);
			}
		};

		PIDSource headingSpeed = new PIDSource() {
			public double pidGet() {
				return gyro.getRate();
			}
		};

		PIDOutput motorWrite = new PIDOutput() {
			public void pidWrite(double a) {
				angleSpeedTarget = a;
			}
		};

		PIDSource headingAbsolute = new PIDSource() {
			public double pidGet() { // Angle Robot at
				return gyro.getAngle();
			}
		};

		PIDOutput speedTargetWrite = new PIDOutput() {
			public void pidWrite(double a) {
				HeadingRatePID.setSetpoint(a);
			}
		};

		HeadingRatePID = new PIDController(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
				SmartDashboard.getNumber("Rate D"), 0, headingSpeed, motorWrite, 0.02);

		HeadingAnglePID = new PIDController(SmartDashboard.getNumber("Angle P"), SmartDashboard.getNumber("Angle I"),
				SmartDashboard.getNumber("Angle D"), 0, headingAbsolute, speedTargetWrite, 0.02) {
			public void setSetpoint(double a) {
				while (a > Math.PI * 2)
					a -= Math.PI * 2;
				while (a < 0)
					a += Math.PI * 2;
				super.setSetpoint(a);
			}
		};
		HeadingAnglePID.setInputRange(0, Math.PI * 2);
		HeadingAnglePID.setContinuous(true);
		HeadingAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);

		PIDUpdate.start();
	}

	Thread PIDUpdate = new Thread(new Runnable() {
		public void run() {
			while (true) {
				HeadingRatePID.setPID(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
						SmartDashboard.getNumber("Rate D"));
				HeadingAnglePID.setPID(SmartDashboard.getNumber("Angle P"), SmartDashboard.getNumber("Angle I"),
						SmartDashboard.getNumber("Angle D"));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	});
	
	public double getPriamryDist(){
		return (leftEncoder.getDistance()+rightEncoder.getDistance())/2;
	}

	double angleLimit = Double.MAX_VALUE;
	double strafeLimit = Double.MAX_VALUE;
	double primaryLimit = Double.MAX_VALUE;

	public void setAngleAccelLimit(double a) {
		angleLimit = a;
	}

	public void setStrafeLimit(double a) {
		strafeLimit = a;
	}

	public void setPrimaryLimit(double a) {
		primaryLimit = a;
	}

	double strafeTargetPower = 0;

	double strafeAbsTime = System.nanoTime();

	public void strafeUpdateLoop() {
		double currentStrafe = strafeMotor.get();
		double exactTime = System.nanoTime();
		double loopTime = (exactTime - strafeAbsTime) / 1000000000.0;
		strafeAbsTime = exactTime;
		double loopStrafeLimit = strafeLimit * loopTime;
		if (currentStrafe - strafeTargetPower > loopStrafeLimit) {
			currentStrafe -= loopStrafeLimit;
		} else if (currentStrafe - strafeTargetPower < -loopStrafeLimit) {
			currentStrafe += loopStrafeLimit;
		} else {
			currentStrafe = strafeTargetPower;
		}

		strafeMotor.set(currentStrafe);

		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setStrafe(double power) {
		strafeTargetPower = power;
	}

	public void start() {
		HeadingRatePID.disable();
		HeadingRatePID.enable();
	}

	/**
	 * in radians
	 * 
	 * @param a
	 */
	public void turn(double a) {
		HeadingAnglePID.enable();
		HeadingAnglePID.setSetpoint(a + gyro.getAngle());
	}

	double primaryTargetPower = 0;

	// Begin loop stuff

	double primaryAbsTime = System.nanoTime();

	public void primaryUpdateLoop() {
		double exactTime = System.nanoTime();
		double loopTime = (exactTime - primaryAbsTime) / 1000000000.0;
		primaryAbsTime = exactTime;
		double loopPrimaryLimit = primaryLimit * loopTime;
		if (speed - primaryTargetPower > loopPrimaryLimit) {
			speed -= loopPrimaryLimit;
		} else if (speed - primaryTargetPower < -loopPrimaryLimit) {
			speed += loopPrimaryLimit;
		} else {
			speed = primaryTargetPower;
		}
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void motoWriteLoop() {// Set the average of the speed on both sides to compensate for error
		// in one direction
		double localSpeed = speed;
		double a = angleSpeed;
		if (localSpeed > 1 - Math.abs(a))
			localSpeed = 1 - Math.abs(a);
		// to compensate in the other direction
		else if (localSpeed < Math.abs(a) - 1)
			localSpeed = Math.abs(a) - 1;
		// Set a deadzone for the speed

		/*
		 * if (Math.abs(localSpeed) - Math.abs(a) < 0.15) {
		 * // Set the speed to zero while in the deadzone
		 * localSpeed = 0;
		 * // There's no error because we just want to stop
		 * a = 0;
		 * }
		 */
		// Set the motors corrected for the error
		backLeftMotor.set(localSpeed - a);
		frontLeftMotor.set(localSpeed - a);
		backRightMotor.set(localSpeed + a);
		frontRightMotor.set(localSpeed + a);

	}

	double angleAbsTime = System.nanoTime();

	public void angleUpdateLoop() {
		double exactTime = System.nanoTime();
		double loopTime = (exactTime - angleAbsTime) / 1000000000.0;
		angleAbsTime = exactTime;
		double loopAngleLimit = angleLimit * loopTime;
		if (angleSpeed - angleSpeedTarget > loopAngleLimit) {
			angleSpeed -= loopAngleLimit;
		} else if (speed - angleSpeedTarget < -loopAngleLimit) {
			angleSpeed += loopAngleLimit;
		} else {
			angleSpeed = angleSpeedTarget;
		}
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// end Loop Stuff

	public void update() {
		speedController.update();
		primaryUpdateLoop();
		angleUpdateLoop();
		strafeUpdateLoop();
		motoWriteLoop();
	}

	public void setPrimary(double a) {
		primaryTargetPower = a;
	}

	boolean slowing = false;

	public void setAngleTarget(double stickX) {
		if (Math.abs(stickX) < 0.1) {
			if (!HeadingAnglePID.isEnable()) {
				slowing = true;
				HeadingAnglePID.setSetpoint(gyro.getAngle());
				HeadingAnglePID.enable();
			} else {
				if (slowing) {
					HeadingAnglePID.setSetpoint(gyro.getAngle());
					if (Math.abs(gyro.getRate()) < Math.PI * 2 * 0.0001) {
						slowing = false;
					}
				}
			}
		} else {
			slowing = false;
			HeadingAnglePID.disable();
			HeadingRatePID.setSetpoint(stickX * maxTurnSpeed);
		}
	}

	public void setMaxTurnSpeed(double speed) {
		maxTurnSpeed = speed;
		HeadingAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);
	}
}
