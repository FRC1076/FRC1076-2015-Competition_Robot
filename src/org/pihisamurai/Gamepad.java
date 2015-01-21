/*
 * Gamepad class to encapsulate FRC 1076 PiHiSamuari's Logitech Gamepads
 * Adopted from FRC 830 Rat Packs C++ Version
 * 
 */

package org.pihisamurai;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author PiHi Samurai 1076
 */

/* Emily is testing commit
 * Avery's Note 1/14/14
 * 
 * I recommend re-writing it to contain a joystick rather then extend one as to
 * make it easier to use
 * 
 * http://first.wpi.edu/FRC/roborio/release/docs/java/
 * classedu_1_1wpi_1_1first_1_1wpilibj_1_1Joystick.html
 * 
 * It should contain a function to get every button, get the x/y of the sticks,
 * get values of throttles, and get directional pad direction
 * 
 * Right now it uses constants and a get button function, lets change those
 * constants to be private and have a specific function for each button
 * 
 * It should add the rumble functionality
 */

public class Gamepad extends Joystick {

	static final int LEFT_BUMPER = 5;
	static final int RIGHT_BUMPER = 6;

	static final int F310_A = 1;
	static final int F310_B = 2;
	static final int F310_X = 3;
	static final int F310_Y = 4;
	static final int F310_LB = 5;
	static final int F310_RB = 6;
	static final int F310_L_BACK = 7;
	static final int F310_R_START = 8;

	static final int F310_LEFT_X = 0;
	static final int F310_LEFT_Y = 1; // Corrected these.
	static final int F310_LEFT_TRIGGER_AXIS = 4;
	static final int F310_RIGHT_TRIGGER_AXIS = 5;
	static final int F310_RIGHT_Y = 3;
	static final int F310_RIGHT_X = 2;
	static final int F310_DPAD_X_AXIS = 6;

	int port;
	DriverStation driverStation;

	Gamepad(int port) {
		super(port);
		this.port = port;
		driverStation = DriverStation.getInstance();
	}

	double getLeftX() {
		return getRawAxis(F310_LEFT_X);
	}

	double getLeftY() {
		return getRawAxis(F310_LEFT_Y);
	}

	double getRightX() {
		return getRawAxis(F310_RIGHT_X);
	}

	double getRightY() {
		return getRawAxis(F310_RIGHT_Y);
	}

	public double getRawAxis(int axis) {
		return driverStation.getStickAxis(port, axis);
	}

	// No idea why this is needed with getRawButton() which is part of joystick
	boolean getNumberedButton(int button) {
		return ((0x1 << (button - 1)) & driverStation.getStickButtons(port)) != 0;
	}

	public boolean getButtonA() {
		return getNumberedButton(1);
	}

}