package org.pihisamurai;

import edu.wpi.first.wpilibj.SerialPort;

public class LEDcontroller {
	
	private Robot robot;
	private SerialPort port;
	
	public static final byte DRIVETRAIN_LEFT = 0;
	public static final byte DRIVETRAIN_RIGHT = 1;
	public static final byte LIFT = 2;
	public static final byte BOOT = 3;
	public static final byte DISABLED = 4;

	public static final byte PROTOCOL_FORWARD = 1;
	public static final byte PROTOCOL_BACKWARD = 2;
	public static final byte PROTOCOL_STOP = 0;

	private byte[] state;

	LEDcontroller (Robot r) {
		this.robot = r;
		port = new SerialPort(2400, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		state = new byte[]{4,4,4};
	
		setDriveTrainLeft(PROTOCOL_STOP);
		setDriveTrainRight(PROTOCOL_STOP);
		setLift(PROTOCOL_STOP);
	}

	private void setLights(byte light, byte direction) {
		if(state[light] != direction){
			state[light] = direction;
			port.write(new byte[]{light, direction}, 2);
		}
	}
	
	public void setDriveTrainLeft(byte direction) {
		setLights(LEDcontroller.DRIVETRAIN_LEFT, direction);
	}
	
	public void setDriveTrainRight(byte direction) {
		setLights(LEDcontroller.DRIVETRAIN_RIGHT, direction);
	}
	
	public void setLift(byte direction) {
		setLights(LEDcontroller.LIFT, direction);
	}
	
	public void disable(){
		setLights(LEDcontroller.DRIVETRAIN_RIGHT, LEDcontroller.DISABLED);
		setLights(LEDcontroller.DRIVETRAIN_LEFT, LEDcontroller.DISABLED);
		setLights(LEDcontroller.LIFT, LEDcontroller.DISABLED);
	}
	
	
}
