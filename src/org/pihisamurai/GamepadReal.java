package org.pihisamurai;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

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

	private Writer writer = null;

	GamepadReal(int port) {
		// Initialization of variable values:
		this.port = port;
		driverStation = DriverStation.getInstance();
		lastPress = new boolean[] { false, false, false, false, false, false, false, false, false, false, false };
		lastPOV = -1;
	}

	GamepadReal(int port, String save) {
		this(port);
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream( System.getProperty("user.home") + "/" +  save), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

	// Functions to check if the input has changed since last update.

	public boolean ifButtonAChange() {
		if (getNumberedButton(BUTTON_A) != lastPress[BUTTON_A]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonBChange() {
		if (getNumberedButton(BUTTON_B) != lastPress[BUTTON_B]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonXChange() {
		if (getNumberedButton(BUTTON_X) != lastPress[BUTTON_X]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonYChange() {
		if (getNumberedButton(BUTTON_Y) != lastPress[BUTTON_Y]) {
			return true;
		}
		return false;
	}

	public boolean ifLeftBackChange() {
		if (getNumberedButton(BUTTON_LB) != lastPress[BUTTON_LB]) {
			return true;
		}
		return false;
	}

	public boolean ifRightBackChange() {
		if (getNumberedButton(BUTTON_RB) != lastPress[BUTTON_RB]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonBackChange() {
		if (getNumberedButton(BUTTON_BACK) != lastPress[BUTTON_BACK]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonLeftStickChange() {
		if (getNumberedButton(BUTTON_LEFT_STICK_PUSH) != lastPress[BUTTON_LEFT_STICK_PUSH]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonRightStickChange() {
		if (getNumberedButton(BUTTON_RIGHT_STICK_PUSH) != lastPress[BUTTON_RIGHT_STICK_PUSH]) {
			return true;
		}
		return false;
	}

	public boolean ifPOVChange() {
		if (this.getPOV() != lastPOV) {
			return true;
		}
		return false;
	}
	
	String temp;
	ArrayList<Object> commandChain = new ArrayList();
	// Updates the array for the gamepad:
	public void update() {
		for (byte i = 1; i < 11; i++) {
			lastPress[i] = getNumberedButton(i);
		}
		lastPOV = this.getPOV();
		/**
		if(writer != null && Robot.getInstance().modeTime() > 15000) {
			try {
				
				writer.close();
			} catch (IOException e) {
				// catch block
				e.printStackTrace();
			}
			writer = null;
		}
		*/
		/**
		if(writer != null){
		    try {
				writer.write(Robot.getInstance().modeTime() + "");
				writer.write(" " + getPOV());
				for(byte i = 1; i < 11; i++)
				writer.write(" " + getNumberedButton(i));
				for(byte i = 0; i < 6; i++)
					writer.write(" " + getRawAxis(i));
				writer.write("\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
		
		if(writer!=null)
		{
			//Arraylist
			temp = Robot.getInstance().modeTime() + " " + getPOV() + " ";
			for(byte i = 1; i < 11; i++)
			{
				temp += getNumberedButton(i) + " ";
			}
			for(byte j = 1; j < 6; j++)
			{
				temp += getRawAxis(j) + " ";
			}
			temp = temp.substring(0, temp.length()-2);
			//temp += "\n";
			
			commandChain.add(temp);
			//end of string and arraylist
		}
		
		if(writer != null && Robot.getInstance().modeTime() >= 15000)
		{
			try 
			{
			for(int i = 0; i <= commandChain.size(); i++)
			{
				writer.write((String) commandChain.get(i));
			}
			writer.close();
			}
			catch(Exception e) {e.printStackTrace();}
			writer = null;
		}
		
	}
}