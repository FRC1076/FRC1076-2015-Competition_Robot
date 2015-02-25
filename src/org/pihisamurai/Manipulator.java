package org.pihisamurai;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator {

	// A "robot" variable to access methods therein:
	private Robot robot;
	// For the motor of the lift:
	private Jaguar LiftMotor;
	private Encoder encoder;

	private static final int ENCODER_CHANNEL_A = 6;
	private static final int ENCODER_CHANNEL_B = 7;

	private static final int ELEVATOR_MOTOR_PORT = 6;

	private boolean locked;

	private double targetPower;

	private DigitalInput limitSwitch = new DigitalInput(8);
	Servo servo = new Servo(9);

	Manipulator(Robot r) {
		// Initialization of variable values:
		this.robot = r;
		locked = false;
		targetPower = 0;
		LiftMotor = new Jaguar(ELEVATOR_MOTOR_PORT){
			public void set(double speed){
				super.set(-speed);
			}
		};
		encoder = new Encoder(ENCODER_CHANNEL_A, ENCODER_CHANNEL_B);
		manipUpdate.start();
	}

	private void liftPower(double power) {
		power *= 0.7;

		if (Math.abs(power) < 0.1) {
			targetPower = 0;
		} else {
			targetPower = power;
		}

	}

	Thread manipUpdate = new Thread(new Runnable() {
		public void run() {
			while (true) {
				if (targetPower > 0) {
					if (limitSwitch.get()) {
						lock();
						LiftMotor.set(targetPower);
					}
				} else if (targetPower < 0) {
					unlock();
					LiftMotor.set(targetPower);
				} else {
					LiftMotor.set(0);
				}

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});

	private void lock() {
		locked = true;
		if (!locked) {
			servo.set(0.5);
		}
	}

	private void unlock() {
		locked = false;
		if (locked) {
			servo.set(0);
			LiftMotor.set(1);
			try {
				Thread.sleep(20); // not exact may be to short or too long
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			LiftMotor.set(0);
		}
	}

	public void setLiftPower(double power) {
		liftPower(power);
	}
}
