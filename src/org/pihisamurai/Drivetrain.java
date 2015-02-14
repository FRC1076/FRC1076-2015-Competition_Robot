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

	private static final double ANGLE_P = 8;
	private static final double ANGLE_I = 0; // Keep at 0
	private static final double ANGLE_D = 0;//6;

	private static final double RATE_P = 0.25;
	private static final double RATE_I = 0.0; // Keep at 0
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
	private Jaguar strafeMotor;

	private PIDController HeadingRatePID;
	private PIDController HeadingAnglePID;

	private Robot robot;

	double angleSpeedTarget = 0;
	double angleSpeed = 0;
	double speed = 0;

	private BuiltInAccelerometer accelerometer;

	public Drivetrain(Robot r) {
		robot = r;

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
					super.set(p * 0.85);
			}
		};
		frontRightMotor = new Jaguar(3) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p * 0.85);
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

		HeadingRatePID = new PIDController(RATE_P, RATE_I, RATE_D, 0, headingSpeed, motorWrite, 0.02);
		
		HeadingAnglePID = new PIDController(ANGLE_P, ANGLE_I, ANGLE_D, 0, headingAbsolute, speedTargetWrite, 0.02) {
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

		strafeUpdate.start();
		primaryUpdate.start();
		angleUpdate.start();
		motoWrite.start();
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

	Thread strafeUpdate = new Thread(new Runnable() {
		public void run() {
			double currentStrafe = strafeMotor.get();
			double absTime = System.nanoTime();
			while (true) {
				double exactTime = System.nanoTime();
				double loopTime = (exactTime - absTime) / 1000000000.0;
				absTime = exactTime;
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
		}
	});

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

	Thread primaryUpdate = new Thread(new Runnable() {
		public void run() {
			double absTime = System.nanoTime();
			while (true) {
				double exactTime = System.nanoTime();
				double loopTime = (exactTime - absTime) / 1000000000.0;
				absTime = exactTime;
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
		}
	});

	Thread motoWrite = new Thread(new Runnable() {
		public void run() {// Set the average of the speed on both sides to compensate for error
			// in one direction
			while (true) {
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
		}
	});

	Thread angleUpdate = new Thread(new Runnable() {
		public void run() {
			double absTime = System.nanoTime();
			while (true) {
				double exactTime = System.nanoTime();
				double loopTime = (exactTime - absTime) / 1000000000.0;
				absTime = exactTime;
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
		}
	});

	public void setPrimary(double a) {
		primaryTargetPower = a;
	}

	public void setAngleTarget(double stickX) {
		if (Math.abs(stickX) < 0.1) {
			if (!HeadingAnglePID.isEnable()) {
				HeadingRatePID.setSetpoint(0);
				System.out.println(gyro.getRate() + " < " + Math.PI *2 *0.01);
				if (Math.abs(gyro.getRate()) < Math.PI * 2 * 0.0001) {
					HeadingAnglePID.setSetpoint(gyro.getAngle());
					HeadingAnglePID.enable();
				}
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
