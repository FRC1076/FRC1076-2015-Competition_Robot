package org.pihisamurai;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import edu.wpi.first.wpilibj.DriverStation;

public class GamepadReal implements Gamepad {

	// Button constants on the gamepad
	private static final byte BUTTON_A = 1;
	private static final byte BUTTON_B = 2;
	private static final byte BUTTON_X = 3;
	private static final byte BUTTON_Y = 4;
	private static final byte BUTTON_LB = 5;
	private static final byte BUTTON_RB = 6;
	private static final byte BUTTON_BACK = 7;
	private static final byte BUTTON_START = 8;
	private static final byte BUTTON_LEFT_STICK_PUSH = 9;
	private static final byte BUTTON_RIGHT_STICK_PUSH = 10;

	// Axis constants
	private static final byte AXIS_LEFT_X = 0;
	private static final byte AXIS_LEFT_Y = 1;
	private static final byte AXIS_LEFT_TRIGGER = 2;
	private static final byte AXIS_RIGHT_TRIGGER = 3;
	private static final byte AXIS_RIGHT_X = 4;
	private static final byte AXIS_RIGHT_Y = 5;

	private int port;
	private DriverStation driverStation;

	// For the change of buttons and the D-pad:
	private boolean[] lastPress;
	private int lastPOV;

	private String filePath = null;

	// An array-list of all gamepad inputs over time
	private LinkedList<Object[]> data;

	GamepadReal(int port) {
		// Initialization of variable values:
		this.port = port;
		driverStation = DriverStation.getInstance();
		lastPress = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false };
		lastPOV = -1;
	}

	GamepadReal(int port, String save) {
		this(port);
		data = new LinkedList<Object[]>();
		filePath = System.getProperty("user.home") + "/" + save;
	}

	public int getPOV() {
		return driverStation.getStickPOV(port, 0);
	}

	public double getLeftX() {
		return getRawAxis(AXIS_LEFT_X);
	}

	public double getLeftY() {
		return getRawAxis(AXIS_LEFT_Y);
	}

	public double getRightX() {
		return getRawAxis(AXIS_RIGHT_X);
	}

	public double getRightY() {
		return getRawAxis(AXIS_RIGHT_Y);
	}

	public double getRightTrigger() {
		return getRawAxis(AXIS_RIGHT_TRIGGER);
	}

	public double getLeftTrigger() {
		return getRawAxis(AXIS_LEFT_TRIGGER);
	}

	private double getRawAxis(byte axis) {
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

	// Functions to check if the input has changed since last update.

	public boolean ifButtonAChange() {
		return getNumberedButton(BUTTON_A) != lastPress[BUTTON_A];
	}

	public boolean ifButtonBChange() {
		return getNumberedButton(BUTTON_B) != lastPress[BUTTON_B];
	}

	public boolean ifButtonXChange() {
		return getNumberedButton(BUTTON_X) != lastPress[BUTTON_X];
	}

	public boolean ifButtonYChange() {
		return getNumberedButton(BUTTON_Y) != lastPress[BUTTON_Y];
	}

	public boolean ifLeftBackChange() {
		return getNumberedButton(BUTTON_LB) != lastPress[BUTTON_LB];
	}

	public boolean ifRightBackChange() {
		return getNumberedButton(BUTTON_RB) != lastPress[BUTTON_RB];
	}

	public boolean ifButtonBackChange() {
		return getNumberedButton(BUTTON_BACK) != lastPress[BUTTON_BACK];
	}

	public boolean ifButtonLeftStickChange() {
		return getNumberedButton(BUTTON_LEFT_STICK_PUSH) != lastPress[BUTTON_LEFT_STICK_PUSH];
	}

	public boolean ifButtonRightStickChange() {
		return getNumberedButton(BUTTON_RIGHT_STICK_PUSH) != lastPress[BUTTON_RIGHT_STICK_PUSH];
	}

	public boolean ifPOVChange() {
		return this.getPOV() != lastPOV;
	}

	// Updates the array for the gamepad:
	public void update() {
		for (byte i = 1; i < 11; i++) {
			lastPress[i] = getNumberedButton(i);
		}
		lastPOV = this.getPOV();

		if (data != null) {
			Object[] temp = new Object[20];
			temp[0] = Robot.getInstance().modeTime();
			temp[1] = getPOV();
			for(byte i = 1; i <= 10; i++) {
				temp[i + 1] = getNumberedButton(i);
			}
			for(byte i = 0; i <= 6; i++) {
				temp[i + 12] = getRawAxis(i);
			}
			data.add(temp);
		}
		if (filePath != null && data != null && Robot.getInstance().modeTime() >= 15200) {
			Serializable writeObj = data;
			data = null;
			try {
				(new Thread(new Runnable() {
					public void run() {
						try {
							ObjectOutputStream oos;
							oos = new ObjectOutputStream(new FileOutputStream(filePath));
							oos.writeObject(writeObj);
							oos.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				})).start();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}