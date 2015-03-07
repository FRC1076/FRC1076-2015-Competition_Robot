package org.pihisamurai;

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

	// Analog input port for gyro
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
	Jaguar leftMotorA;
	Jaguar leftMotorB;
	Jaguar rightMotorA;
	Jaguar rightMotorB;
	Jaguar strafeMotorA;
	Jaguar strafeMotorB;

	// PID Controllers, see WPILib Documentation for how they work
	private PIDController turnRatePID;
	private PIDController turnAnglePID;

	// Speed controller class controls the below values as a function of box count
	SpeedController speedController;

	private double primaryPowerTarget = 0;
	private double strafePowerTarget = 0;
	private double angleSpeedTarget = 0;
	private double angleSpeed = 0;
	/** main drive motor power */
	private double primaryPower = 0;

	// Variables used by speed controlle
	private double angleRateChangeRate = Double.MAX_VALUE; // amount anglerSpeed can change value per second
	private double strafeChangeRate = Double.MAX_VALUE; // amount strafe can change value per second
	private double primaryChangeRate = Double.MAX_VALUE; // amount primarymotors can change value per second

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

		turnRatePID = new PIDController(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
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
				turnRatePID.setSetpoint(a);
			}
		};

		// Make PID Controller be able to handle inputs not in 0-2Pi range
		turnAnglePID = new PIDController(SmartDashboard.getNumber("Angle P"), SmartDashboard.getNumber("Angle I"),
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
		turnAnglePID.setInputRange(0, Math.PI * 2);
		turnAnglePID.setContinuous(true);
		turnAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);
		
		//Set tolarance for error for turn
		turnAnglePID.setAbsoluteTolerance(Math.PI*2/16);

		// Start PIDUpdate Thread
		PIDUpdate.start();
		
		turnRatePID.enable();
	}

	// Thread to check if smartdashboard has updated PID values and use them if they have been updated
	Thread PIDUpdate = new Thread(new Runnable() {
		public void run() {
			while (true) {
				turnRatePID.setPID(SmartDashboard.getNumber("Rate P"), SmartDashboard.getNumber("Rate I"),
						SmartDashboard.getNumber("Rate D"));
				turnAnglePID.setPID(SmartDashboard.getNumber("Angle P"), SmartDashboard.getNumber("Angle I"),
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

	//// Setters for speedcontroller

	public void setAngleRateChangeRate(double changeRate) {
		angleRateChangeRate = changeRate;
	}

	public void setStrafeChangeRate(double changeRate) {
		strafeChangeRate = changeRate;
	}

	public void setPrimaryChangeRate(double changeRate) {
		primaryChangeRate = changeRate;
	}

	public void setMaxTurnSpeed(double turnSpeed) {
		maxTurnSpeed = turnSpeed;
		turnAnglePID.setOutputRange(-maxTurnSpeed, maxTurnSpeed);
	}
	
	//// Primary Setters

	//Set Primary Power
	public void setPrimary(double power) {
		primaryPowerTarget = power;
	}
	
	//Set Strafe power
	public void setStrafe(double power) {
		strafePowerTarget = power;
	}

	//Turn robot angle (radians)
	public void turnAngle(double angle) {
		turnAnglePID.enable();
		turnAnglePID.setSetpoint(angle + gyro.getAngle());
	}
	
	//Function for whether turnAngle is complete
	
	public boolean isTurning(){
		return turnAnglePID.isEnable() && turnAnglePID.onTarget();
	}
	
	// boolean, represents wether the robot is in state of slowing down in order to enter heading controlled mode from
	// angular speed controlled mode
	// used to remove the jerkyness of releaseing angle joystick
	boolean slowing = false;
	
	//set target spinning speed
	public void setTurnRate(double turnStrength) {
		if (Math.abs(turnStrength) < 0.1) {
			if (!turnAnglePID.isEnable()) {
				slowing = true;
				turnAnglePID.setSetpoint(gyro.getAngle());
				turnAnglePID.enable();
			} else {
				if (slowing) {
					turnAnglePID.setSetpoint(gyro.getAngle());
					if (Math.abs(gyro.getRate()) < Math.PI * 2 * 0.0001) {
						slowing = false;
					}
				}
			}
		} else {
			slowing = false;
			turnAnglePID.disable();
			turnRatePID.setSetpoint(turnStrength * maxTurnSpeed);
		}
	}

	///// Update

	double lastTime = System.nanoTime();

	// Must be called to updated motor values
	// Not in thread for concurrency and performance reasons
	public void update() {
		speedController.update();

		double exactTime = System.nanoTime(); //Exact time
		double loopTime = (exactTime - lastTime) / 1000000000.0;
		lastTime = exactTime; //Use this loops time to calculate next loops difference

		double loopPrimaryLimit = primaryChangeRate * loopTime;
		if (primaryPower - primaryPowerTarget > loopPrimaryLimit) {
			primaryPower -= loopPrimaryLimit;
		} else if (primaryPower - primaryPowerTarget < -loopPrimaryLimit) {
			primaryPower += loopPrimaryLimit;
		} else {
			primaryPower = primaryPowerTarget;
		}

		double loopAngleLimit = angleRateChangeRate * loopTime;
		if (angleSpeed - angleSpeedTarget > loopAngleLimit) {
			angleSpeed -= loopAngleLimit;
		} else if (primaryPower - angleSpeedTarget < -loopAngleLimit) {
			angleSpeed += loopAngleLimit;
		} else {
			angleSpeed = angleSpeedTarget;
		}

		// Strafe update
		double currentStrafe = strafeMotorA.get();
		double accelLimit = strafeChangeRate * loopTime;
		if (currentStrafe - strafePowerTarget > accelLimit) {
			currentStrafe -= accelLimit;
		} else if (currentStrafe - strafePowerTarget < -accelLimit) {
			currentStrafe += accelLimit;
		} else {
			currentStrafe = strafePowerTarget;
		}
				//set strafe motors' power
		strafeMotorA.set(currentStrafe); 
		strafeMotorB.set(currentStrafe);

			//temp var to hold power of motors
		double localSpeed = primaryPower;
		if (localSpeed > 1 - Math.abs(angleSpeed)) // local speed cannot be higher than 1 - angle speed
			localSpeed = 1 - Math.abs(angleSpeed);
		// to compensate in the other direction
		else if (localSpeed < Math.abs(angleSpeed) - 1) // local speed connot be lower than angle speed - 1
			localSpeed = Math.abs(angleSpeed) - 1;

		
		
		// Set the motors corrected for the error
		double motL = localSpeed - angleSpeed;  // maybe something like this? (angleSpeed > 0 ? 10 : 2);
		double motR = localSpeed + angleSpeed;
		leftMotorA.set(motL);  leftMotorB.set(motL);
		rightMotorA.set(motR); rightMotorB.set(motR);
		
		

	}
}
