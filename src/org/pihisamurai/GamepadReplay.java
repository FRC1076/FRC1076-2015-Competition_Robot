package org.pihisamurai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GamepadReplay implements Gamepad {

	// For the change of buttons and the D-pad:
	private boolean[] lastPress;
	private int lastPOV;
	
	private boolean[] currentPress;
	private double[] currentStick;
	private int currentPOV;

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
	
	private Scanner scanner;
	
	private long nextTime;
	private String[] nextData = null;
	boolean addedCommand = false;
	//File format, saved periodicly, each save seperated by newline
	//miliseconds_into_round POV_ANGLE BUTTON1 BUTTON2 BUTTON3... AXIS0 AXIS1 AXIS2... AXIS5
	
	GamepadReplay(String string) {
		
		try {
			scanner = new Scanner(new File(System.getProperty("user.home") + "/" + string));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		lastPress = new boolean[]{false,false,false,false,false,
				false,false,false,false,false,false};
		lastPOV = -1;
		
		readFile();
	}
	ArrayList<String> commandChain = new ArrayList<String>();
	int pos = 0;
	int temp = 1;
	private void readFile()
	{
		while(!addedCommand)
		{
			commandChain.add(scanner.next());
			addedCommand = true;
		}
		
		if(Robot.getInstance().modeTime() >= nextTime - 1){
			try {
			nextTime = Long.parseLong((String)commandChain.get(pos++));
			currentPOV = Integer.parseInt((String)commandChain.get(pos++));
			currentPress = new boolean[11];
			temp = 1;
			while(temp < 11)
			{
				currentPress[temp] = Boolean.parseBoolean((String)commandChain.get(pos));
				pos++;
				temp++;
			}
			temp = 1;
			currentStick = new double[6];
			while(temp < 6)
			{
				currentStick[temp] = Double.parseDouble((String)commandChain.get(pos));
				pos++;
				temp++;
			}
			
			
			System.out.println(currentPOV + " && " + Arrays.toString(currentPress) + " && " + Arrays.toString(currentStick) );;
			
			}
			catch(Exception ex) {ex.printStackTrace();}
			//nextData = scanner.nextLine().split("\\s+");
			
			readFile();
		}
	}
		
	
	/**
	private void readFile(){
		if(nextData == null){
			//nextData = scanner.nextLine().split("\\s+");
			nextTime = -1;
		}
		
		if(Robot.getInstance().modeTime() >= nextTime && scanner.hasNext()){
			nextTime = Long.parseLong(scanner.next());
			currentPOV = Integer.parseInt(scanner.next());
			currentPress = new boolean[11];
			for(int i = 1; i < 11; i++)
				currentPress[i] = Boolean.parseBoolean(scanner.next());
			currentStick = new double[6];
			for(int i = 1; i < 6; i++)
				currentStick[i] = Double.parseDouble(scanner.next());
			
			
			System.out.println(currentPOV + " && " + Arrays.toString(currentPress) + " && " + Arrays.toString(currentStick) );;
			
			
			//nextData = scanner.nextLine().split("\\s+");
			
			readFile();
		}
	}
	*/
	public int getPOV() {
		readFile();
		return currentPOV;
	}
	
	private double getRawAxis(int axis) {
		readFile();
		return currentStick[axis];
	}

	private boolean getNumberedButton(byte button) {
		readFile();
		return currentPress[button];
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
		if(getNumberedButton(BUTTON_A) != lastPress[BUTTON_A]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonBChange() {
		if(getNumberedButton(BUTTON_B) != lastPress[BUTTON_B]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonXChange() {
		if(getNumberedButton(BUTTON_X) != lastPress[BUTTON_X]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonYChange() {
		if(getNumberedButton(BUTTON_Y) != lastPress[BUTTON_Y]) {
			return true;
		}
		return false;
	}

	public boolean ifLeftBackChange() {
		if(getNumberedButton(BUTTON_LB) != lastPress[BUTTON_LB]) {
			return true;
		}
		return false;
	}

	public boolean ifRightBackChange() {
		if(getNumberedButton(BUTTON_RB) != lastPress[BUTTON_RB]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonBackChange() {
		if(getNumberedButton(BUTTON_BACK) != lastPress[BUTTON_BACK]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonLeftStickChange() {
		if(getNumberedButton(BUTTON_LEFT_STICK_PUSH) != lastPress[BUTTON_LEFT_STICK_PUSH]) {
			return true;
		}
		return false;
	}

	public boolean ifButtonRightStickChange() {
		if(getNumberedButton(BUTTON_RIGHT_STICK_PUSH) != lastPress[BUTTON_RIGHT_STICK_PUSH]) {
			return true;
		}
		return false;
	}

	public boolean ifPOVChange() {
		if(this.getPOV() != lastPOV) {
			return true;
		}
		return false;
	}

	// Updates the array for the gamepad:
	public void update() {
		for (byte i = 1; i < 11; i++) {
			lastPress[i] = getNumberedButton(i);
		}
		lastPOV = this.getPOV();
	}
}
