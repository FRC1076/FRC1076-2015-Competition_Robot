package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public HumanControl humanControl;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		humanControl = new HumanControl(robot);
		GUI = new DriverStationGUI(robot);
	}

	public void init(){
		
	}
	public void run() {
		GUI.update();
	}
}
