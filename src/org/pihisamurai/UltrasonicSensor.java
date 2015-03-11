package org.pihisamurai;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicSensor {
	AnalogInput analogInput;
	UltrasonicSensor(int port){
		analogInput = new AnalogInput(port);
	}
	
	public double getDistance(){
		double vcc = 5; //volts input
		double vi = vcc/512; //volts per inch
		
		double vm = analogInput.getVoltage(); //mesured voltage
		double ri = vm/vi; //distance inches
		
		return ri;
	}
}
