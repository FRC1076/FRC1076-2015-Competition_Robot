package org.pihisamurai;

import edu.wpi.first.wpilibj.SerialPort;

public class LEDController {

	private double leftDirection;
	private double rightDirection;
	private double liftDirection;
	private boolean disable;

	private SerialPort serial;

	LEDController() {
		serial = new SerialPort(2400, SerialPort.Port.kMXP, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
		leftDirection = 0;
		rightDirection = 0;
		liftDirection = 0;
	}

	public void setLeftLED(double d) {
		leftDirection = d;
	}

	public void setRightLED(double direction) {
		rightDirection = direction;
	}

	public void setLiftLED(double direction) {
		liftDirection = direction;
	}

	public void update() {
		byte byteSend = 0;
		if (leftDirection > 0) {
			byteSend |= 1 << 0;
		}
		if (leftDirection < 0) {
			byteSend |= 1 << 1;
		}
		if (rightDirection > 0) {
			byteSend |= 1 << 2;
		}
		if (rightDirection < 0) {
			byteSend |= 1 << 3;
		}
		if (liftDirection > 0) {
			byteSend |= 1 << 4;
		}
		if (liftDirection < 0) {
			byteSend |= 1 << 5;
		}
		serial.write(new byte[] { byteSend }, 1);
	}
}
