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

	// You are going to have to comment on this, Avery

	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 2;
	private static final int RIGHT_ENCODER_CHANNEL_B = 3;
	
	private static final int STRAFE_ENCODER_CHANNEL_A = 4;
	private static final int STRAFE_ENCODER_CHANNEL_B = 5;
	
	private static final int LEFT_MOTOR_A_PORT = 0;
	private static final int LEFT_MOTOR_B_PORT = 1;
	private static final int RIGHT_MOTOR_A_PORT = 2;
	private static final int RIGHT_MOTOR_B_PORT = 3;
	private static final int STRAFE_MOTOR_A_PORT = 4;
	private static final int STRAFE_MOTOR_B_PORT = 5;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * 0.0254 * Math.PI / 360;
	
	private static final double DISTANCE_PER_PULSE_STRAFE = 4 * 0.0254 * Math.PI / 360;

	private double maxTurnSpeed = 5; // Radians per second

	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private Encoder strafeEncoder;

	private Gyro gyro;

	public Jaguar leftMotorA;
	public Jaguar leftMotorB;
	public Jaguar rightMotorA;
	public Jaguar rightMotorB;
	public Jaguar strafeMotorA;
	public Jaguar strafeMotorB;

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

		SmartDashboard.putNumber("Angle P", 7);
		SmartDashboard.putNumber("Angle I", 0);
		SmartDashboard.putNumber("Angle D", 1);

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
			// Super uses gs as the unit, we use m/s^2, multiply by 1g
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
		strafeEncoder = new Encoder(STRAFE_ENCODER_CHANNEL_A, STRAFE_ENCODER_CHANNEL_B);
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		
		strafeEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_STRAFE);

		leftMotorA = new Jaguar(LEFT_MOTOR_A_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p * 0.85);
			}
		};
		leftMotorB = new Jaguar(LEFT_MOTOR_B_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p * 0.85);
			}
		};
		rightMotorA = new Jaguar(RIGHT_MOTOR_A_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p);
			}
		};
		rightMotorB = new Jaguar(RIGHT_MOTOR_B_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(p);
			}
		};
		strafeMotorA = new Jaguar(STRAFE_MOTOR_A_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p);
			}
		};
		strafeMotorB = new Jaguar(STRAFE_MOTOR_B_PORT) {
			public void set(double p) {
				if (Math.abs(p) < 0.05)
					super.set(0);
				else
					super.set(-p);
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
	
	public double getDistPrimary(){
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}
	
	public double getDistStrafe(){
		return strafeEncoder.getDistance();
	}
	
	public void goDistPrimary(double dist, double power) {
		(new Thread (new Runnable () {
			public void run () {
				double init = getDistPrimary();
				setPrimary(Math.abs(power) * ((dist < 0) ? -1 : 1));
				while (Math.abs(getDistPrimary() - init) <= Math.abs(dist)) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				setPrimary(0);
			}
		})).start();
	}
	
	public void goDistStrafe(double dist, double power) {
		(new Thread (new Runnable () {
			public void run () {
				double init = getDistStrafe();
				setStrafe(Math.abs(power) * ((dist < 0) ? -1 : 1));
				while (Math.abs(getDistStrafe() - init) <= Math.abs(dist)) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				setStrafe(0);
			}
		})).start();
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
		double currentStrafe = strafeMotorA.get();
		double exactTime = System.nanoTime();
		double loopTime = (exactTime - strafeAbsTime) / 1000000000.0;
		strafeAbsTime = exactTime;
		double accelLimit = strafeLimit * loopTime;
		System.out.println(accelLimit);
		if (currentStrafe - strafeTargetPower > accelLimit) {
			currentStrafe -= accelLimit;
		} else if (currentStrafe - strafeTargetPower < -accelLimit) {
			currentStrafe += accelLimit;
		} else {
			currentStrafe = strafeTargetPower;
		}

		strafeMotorA.set(currentStrafe);
		strafeMotorB.set(currentStrafe);
		/*try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

	public void setStrafe(double power) {
		strafeTargetPower = power;
	}

	public void start() {
		HeadingRatePID.disable();
		HeadingRatePID.enable();
	}

	/**
	 * In radians.
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
		leftMotorA.set(localSpeed - a);
		leftMotorB.set(localSpeed - a);
		rightMotorA.set(localSpeed + a);
		rightMotorB.set(localSpeed + a);

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
	
	public void setAll(double a) {
		leftMotorA.set(a);
		leftMotorB.set(a);
		rightMotorA.set(a);
		rightMotorB.set(a);
	}

	public void setPrimary(double a) {
		/*robot.ledcontroller.setDriveTrainLeft((a < 0.05) ? ((Math.abs(a) < 0.05) ? LEDcontroller.PROTOCOL_STOP 
				: LEDcontroller.PROTOCOL_BACKWARD) : LEDcontroller.PROTOCOL_FORWARD);
		robot.ledcontroller.setDriveTrainRight((a < 0.05) ? ((Math.abs(a) < 0.05) ? LEDcontroller.PROTOCOL_STOP 
				: LEDcontroller.PROTOCOL_BACKWARD) : LEDcontroller.PROTOCOL_FORWARD);*/
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
