package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

public class Manipulator {

	// A "robot" variable to access methods therein:

	// For the motor of the lift:
	private Jaguar LiftMotor;
	private Encoder encoder;

	private static final int ENCODER_CHANNEL_A = 6;
	private static final int ENCODER_CHANNEL_B = 7;

	private static final int ELEVATOR_MOTOR_PORT = 6;

	Manipulator() {
		LiftMotor = new Jaguar(ELEVATOR_MOTOR_PORT) {
			public void set(double speed) {
				super.set(-speed);
			}
		};
		encoder = new Encoder(ENCODER_CHANNEL_A, ENCODER_CHANNEL_B);
	}

	public void setLiftPower(double power) {
		power *= 0.7;

		Robot.getInstance().ledController.setLiftLED(power);
		
		if (Math.abs(power) < 0.05) {
			LiftMotor.set(0);
		} else {
			LiftMotor.set(power);
		}
	}
}
