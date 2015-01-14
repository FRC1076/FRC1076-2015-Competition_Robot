package org.pihisamurai;

public class Teleoperated implements RobotMode {

	private Robot robot;
	public HumanControl humanControl;
	public DriverStationGUI GUI;

	public Teleoperated(Robot r) {
		this.robot = r;
		humanControl = new HumanControl(robot);
		GUI = new DriverStationGUI(robot);
	}

	public void run() {
		while (true) {
			GUI.update();
			robot.sleep(10);
		}
	}
}
