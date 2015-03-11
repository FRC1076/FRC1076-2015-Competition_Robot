package org.pihisamurai;

import edu.wpi.first.wpilibj.AnalogInput;

public class InfraredSensor {
	AnalogInput analogInput;

	InfraredSensor(int port) {
		analogInput = new AnalogInput(port);
	}

	public double getDistance(){
		return (1/2.54) * 212.5866075 * Math.pow(Math.E,-1.068836995*analogInput.getVoltage());
	}
}
