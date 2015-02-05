package org.pihisamurai;

import edu.wpi.first.wpilibj.DriverStation;

public class Gamepad {

	private static final byte BUTTON_A = 1;
	private static final byte BUTTON_B = 2;
	private static final byte BUTTON_X = 3;
	private static final byte BUTTON_Y = 4;
	private static final byte BUTTON_LB = 5;
	private static final byte BUTTON_RB = 6;
	private static final byte BUTTON_BACK = 7;
	private static final byte BUTTON_START = 8;
	private static final byte BUTTON_LEFT_STICK_PUSH = 7;
	private static final byte BUTTON_RIGHT_STICK_PUSH = 8;

	private static final byte AXIS_LEFT_X = 0;
	private static final byte AXIS_LEFT_Y = 1; 
	private static final byte AXIS_LEFT_TRIGGER = 2;
	private static final byte AXIS_RIGHT_TRIGGER = 3;
	private static final byte AXIS_RIGHT_Y = 5;
	private static final byte AXIS_RIGHT_X = 4;

	public static final int POV_UP = 0;
	public static final int POV_UP_RIGHT = 45;
	public static final int POV_RIGHT = 90;
	public static final int POV_DOWN_RIGHT = 135;
	public static final int POV_DOWN = 180;
	public static final int POV_DOWN_LEFT = 225;
	public static final int POV_LEFT = 270;
	public static final int POV_UP_LEFT = 315;
	public static final int POV_OFF = -1;

	private int port;
	private DriverStation driverStation;

	Gamepad(int port) {
		this.port = port;
		driverStation = DriverStation.getInstance();
	}

	int getPOV() {
		return driverStation.getStickPOV(port, 0);
	}

	double getLeftX() {
		return getRawAxis(AXIS_LEFT_X);
	}

	double getLeftY() {
		return getRawAxis(AXIS_LEFT_Y);
	}

	double getRightX() {
		return getRawAxis(AXIS_RIGHT_X);
	}

	double getRightY() {
		return getRawAxis(AXIS_RIGHT_Y);
	}

	double getRightTrigger() {
		return getRawAxis(AXIS_RIGHT_TRIGGER);
	}

	double getLeftTrigger() {
		return getRawAxis(AXIS_LEFT_TRIGGER);
	}

	private double getRawAxis(int axis) {
		return driverStation.getStickAxis(port, axis);
	}

	private boolean getNumberedButton(byte button) {
		return driverStation.getStickButton(port, button);
	}

	public boolean getButtonA() {
		return getNumberedButton(BUTTON_A);
	}

	public boolean getButtonB() {
		return getNumberedButton(BUTTON_B);
	}

	public boolean getButtonX() {
		return getNumberedButton(BUTTON_X);
	}

	public boolean getButtonY() {
		return getNumberedButton(BUTTON_Y);
	}

	public boolean getButtonBack() {
		return getNumberedButton(BUTTON_BACK);
	}

	public boolean getButtonStart() {
		return getNumberedButton(BUTTON_START);
	}

	public boolean getButtonLeftBack() {
		return getNumberedButton(BUTTON_LB);
	}

	public boolean getButtonRightBack() {
		return getNumberedButton(BUTTON_RB);
	}
	
	public boolean getButtonLeftStick() {
		return getNumberedButton(BUTTON_LEFT_STICK_PUSH);
	}

	public boolean getButtonRightStick() {
		return getNumberedButton(BUTTON_RIGHT_STICK_PUSH);
	}

}