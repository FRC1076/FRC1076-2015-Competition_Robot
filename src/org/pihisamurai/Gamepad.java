package org.pihisamurai;

public interface Gamepad {
	// Constants for D-pad values
	public static final int POV_UP = 0;
	public static final int POV_UP_RIGHT = 45;
	public static final int POV_RIGHT = 90;
	public static final int POV_DOWN_RIGHT = 135;
	public static final int POV_DOWN = 180;
	public static final int POV_DOWN_LEFT = 225;
	public static final int POV_LEFT = 270;
	public static final int POV_UP_LEFT = 315;
	public static final int POV_OFF = -1;
	
	int getPOV();

	double getLeftX();

	double getLeftY();

	double getRightX();

	double getRightY();

	double getRightTrigger();

	double getLeftTrigger();

	public boolean getButtonA();

	public boolean getButtonB();

	public boolean getButtonX();

	public boolean getButtonY();

	public boolean getButtonBack();

	public boolean getButtonStart();

	public boolean getButtonLeftBack();

	public boolean getButtonRightBack();

	public boolean getButtonLeftStick();

	public boolean getButtonRightStick();

	public boolean ifButtonAChange();

	public boolean ifButtonBChange();

	public boolean ifButtonXChange();

	public boolean ifButtonYChange();

	public boolean ifLeftBackChange();

	public boolean ifRightBackChange();

	public boolean ifButtonBackChange();

	public boolean ifButtonLeftStickChange();

	public boolean ifButtonRightStickChange();

	public boolean ifPOVChange();

	public void update();
}