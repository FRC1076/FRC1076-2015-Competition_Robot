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

	// Define the ports we use on roborio digital IO for encoders
	private static final int LEFT_ENCODER_CHANNEL_A = 0;
	private static final int LEFT_ENCODER_CHANNEL_B = 1;

	private static final int RIGHT_ENCODER_CHANNEL_A = 2;
	private static final int RIGHT_ENCODER_CHANNEL_B = 3;

	private static final int STRAFE_ENCODER_CHANNEL_A = 4;
	private static final int STRAFE_ENCODER_CHANNEL_B = 5;

	// Define the ports we use on roborio PWM ports for jaguars
	private static final int LEFT_MOTOR_A_PORT = 0;
	private static final int LEFT_MOTOR_B_PORT = 1;
	private static final int RIGHT_MOTOR_A_PORT = 2;
	private static final int RIGHT_MOTOR_B_PORT = 3;
	private static final int STRAFE_MOTOR_A_PORT = 4;
	private static final int STRAFE_MOTOR_B_PORT = 5;

	// Analoginput port for gyro
	private static final int GYRO_PORT = 0;

	// Circumference of wheel, modified by gear ratio divided by number of pulses in one rotation of encoder
	private static final double DISTANCE_PER_PULSE_MAIN = 4 * 0.0254 * Math.PI / 360;

	private static final double DISTANCE_PER_PULSE_STRAFE = 4 * 0.0254 * Math.PI / 360;

	// Holds max turn speed for robot
	private double maxTurnSpeed = 5; // Radians per second

	// Encoders for drivetrain
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private Encoder strafeEncoder;

	// Gyro for rotate correction
	private Gyro gyro;

	// Jaguars (drive motors)
	public Jaguar leftMotorA;
	public Jaguar leftMotorB;
	public Jaguar rightMotorA;
	public Jaguar rightMotorB;
	public Jaguar strafeMotorA;
	public Jaguar strafeMotorB;

	// PID Controllers, see WPILib Documentation for how they work
	private PIDController HeadingRatePID;
	private PIDController HeadingAnglePID;

	// Speed controller class controls the below values as a function of box count
	public SpeedController speedController;

	double strafeTargetPower = 0;
	double angleSpeedTarget = 0;
	double angleSpeed = 0;
	double speed = 0;

	// Variables used by speed controlle
	double angleLimit = Double.MAX_VALUE; // amount anglerSpeed can change value per second
	double strafeLimit = Double.MAX_VALUE; // amount strafe can change value per second
	double primaryLimit = Double.MAX_VALUE; // amount primarymotors can change value per second

	public Drivetrain() {

		speedController = new SpeedController(this);

		// Put default PID values on dashboard
		SmartDashboard.putNumber("Angle P", 7);
		SmartDashboard.putNumber("Angle I", 0);
		SmartDashboard.putNumber("Angle D", 1);

		SmartDashboard.putNumber("Rate P", 0.25);
		SmartDashboard.putNumber("Rate I", 0.0);
		SmartDashboard.putNumber("Rate D", 0.05);

		// Make a gyro on analog port defined above that uses radians instead of degrees
		// Change getAngle so it always returns a value between 0 and two Pi
		gyro = new Gyro(GYRO_PORT) {
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

		// Declare the encoders using the ports defined in constants
		leftEncoder = new Encoder(LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B);
		rightEncoder = new Encoder(RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B);
		strafeEncoder = new Encoder(STRAFE_ENCODER_CHANNEL_A, STRAFE_ENCODER_CHANNEL_B);

		// Define the distance the robot moves per one pules of the encoder
		leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);
		rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_MAIN);

		strafeEncoder.setDistancePerPulse(DISTANCE_PER_PULSE_STRAFE);

		// Declare our motor controllers
		// Disable them for any low signal (below 5%)
		// Slow down the left side as it is faster
		// Flip direction of strafe
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

		// PID Loop based off heading

		// PID Input (angular speed)
		PIDSource headingSpeed = new PIDSource() {
			public double pidGet() {
				return gyro.getRate();
			}
		};

		// take PID functions output and make set angleSpeedTarget
		PIDOutput motorWrite = new PIDOutput() {
			public void pidWrite(double a) {
				angleSpeedTarget = a;
			}
		};

		HeadingRatePID = new PIDController(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
				SmartDashboard.getNumber("Rate D"), 0, headingSpeed, motorWrite, 0.02);

		// PID Loop based of orientation

		// PID input (robot angle)
		PIDSource headingAbsolute = new PIDSource() {
			public double pidGet() { // Angle Robot at
				return gyro.getAngle();
			}
		};

		// take PID functions output and make set headingRatePIDs target
		PIDOutput speedTargetWrite = new PIDOutput() {
			public void pidWrite(double a) {
				HeadingRatePID.setSetpoint(a);
			}
		};

		// Make PID Controller be able to handle inputs not in 0-2Pi range
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

		// Inform PIDControleer that 0 and 2Pi are continuous
		HeadingAnglePID.setInputRange(0, Math.PI * 2);
		HeadingAnglePID.setContinuous(true);
		HeadingAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);

		// Start PIDUpdate Thread
		PIDUpdate.start();
	}

	// Thread to check if smartdashboard has updated PID values and use them if they have been updated
	Thread PIDUpdate = new Thread(new Runnable() {
		public void run() {
			while (true) {
				HeadingRatePID.setPID(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
						SmartDashboard.getNumber("Rate D"));
				HeadingAnglePID.setPID(SmartDashboard.getNumber("Angle P"), SmartDashboard.getNumber("Angle I"),
						SmartDashboard.getNumber("Angle D"));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});

	// Distance traveled by primary motors, compare against self for difference
	public double getDistPrimary() {
		return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
	}

	// Strafe version
	public double getDistStrafe() {
		return strafeEncoder.getDistance();
	}

	// Function to go forward/backwards a distance at a specified power
	public void goDistPrimary(double dist, double power) {
		(new Thread(new Runnable() {
			public void run() {
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

	// function to strafe for distance at power
	public void goDistStrafe(double dist, double power) {
		(new Thread(new Runnable() {
			public void run() {
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

	// Setters for speedcontroller

	public void setAngleAccelLimit(double a) {
		angleLimit = a;
	}

	public void setStrafeLimit(double a) {
		strafeLimit = a;
	}

	public void setPrimaryLimit(double a) {
		primaryLimit = a;
	}

	double strafeAbsTime = System.nanoTime();

	public void setStrafe(double power) {
		strafeTargetPower = power;
	}

	public void start() {
		HeadingRatePID.disable();
		HeadingRatePID.enable();
	}

	// TODO NOT WORKING
	public void turn(double a) {
		HeadingAnglePID.enable();
		HeadingAnglePID.setSetpoint(a + gyro.getAngle());
	}

	double primaryTargetPower = 0;

	// Begin loop stuff

	double lastTime = System.nanoTime();

	// Must be called to updated motor values
	// Not in thread for concurrency and performance reasons
	public void update() {
		speedController.update();

		double exactTime = System.nanoTime(); //Exact time
		double loopTime = (exactTime - lastTime) / 1000000000.0;
		lastTime = exactTime; //Use this loops time to calculate next loops difference

		double loopPrimaryLimit = primaryLimit * loopTime;
		if (speed - primaryTargetPower > loopPrimaryLimit) {
			speed -= loopPrimaryLimit;
		} else if (speed - primaryTargetPower < -loopPrimaryLimit) {
			speed += loopPrimaryLimit;
		} else {
			speed = primaryTargetPower;
		}

		double loopAngleLimit = angleLimit * loopTime;
		if (angleSpeed - angleSpeedTarget > loopAngleLimit) {
			angleSpeed -= loopAngleLimit;
		} else if (speed - angleSpeedTarget < -loopAngleLimit) {
			angleSpeed += loopAngleLimit;
		} else {
			angleSpeed = angleSpeedTarget;
		}

		// Strafe update
		double currentStrafe = strafeMotorA.get();
		double accelLimit = strafeLimit * loopTime;
		if (currentStrafe - strafeTargetPower > accelLimit) {
			currentStrafe -= accelLimit;
		} else if (currentStrafe - strafeTargetPower < -accelLimit) {
			currentStrafe += accelLimit;
		} else {
			currentStrafe = strafeTargetPower;
		}


		double localSpeed = speed;
		if (localSpeed > 1 - Math.abs(angleSpeed))
			localSpeed = 1 - Math.abs(angleSpeed);
		// to compensate in the other direction
		else if (localSpeed < Math.abs(angleSpeed) - 1)
			localSpeed = Math.abs(angleSpeed) - 1;

		strafeMotorA.set(currentStrafe);
		strafeMotorB.set(currentStrafe);
		// Set the motors corrected for the error
		leftMotorA.set(localSpeed - angleSpeed);
		leftMotorB.set(localSpeed - angleSpeed);
		rightMotorA.set(localSpeed + angleSpeed);
		rightMotorB.set(localSpeed + angleSpeed);
	}

	public void setPrimary(double a) {
		primaryTargetPower = a;
	}

	// boolean, represents wether the robot is in state of slowing down in order to enter heading controlled mode from
	// angular speed controlled mode
	// used to remove the jerkyness of releaseing angle joystick
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
