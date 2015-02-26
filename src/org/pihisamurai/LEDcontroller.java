package org.pihisamurai;

import edu.wpi.first.wpilibj.SerialPort;

public class LEDcontroller {
	
	private Robot robot;
	private SerialPort port;
	
	private static final byte DRIVETRAIN_LEFT = 0;
	private static final byte DRIVETRAIN_RIGHT = 1;
	private static final byte LIFT = 2;

	private static final byte PROTOCOL_FORWARD = 1;
	private static final byte PROTOCOL_BACKWARD = 2;
	private static final byte PROTOCOL_STOP = 0;

	LEDcontroller (Robot r) {
		this.robot = r;
		port = new SerialPort(2400, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
	}
	
	public void setLights(byte light, byte direction) {
		port.write(new byte[]{light, direction}, 2);
	}
	
	public void setDriveTrainLeft(byte direction) {
		port.write(new byte[]{LEDcontroller.DRIVETRAIN_LEFT, direction}, 2);
	}
	
	public void setDriveTrainRight(byte direction) {
		port.write(new byte[]{LEDcontroller.DRIVETRAIN_RIGHT, direction}, 2);
	}
	
	public void setLift(byte direction) {
		port.write(new byte[]{LEDcontroller.LIFT, direction}, 2);
	}
}
