package org.pihisamurai;

public class Autonomous {

	private Robot robot;
	double mode;
	
	public Autonomous(Robot r){
		this.robot = r;
	}
	
	double timer;
	
	public void run(){

		robot.drivetrain.setAngleTarget(0);
		robot.drivetrain.update();
		if( (timer - System.nanoTime() / 1000000000.0) < 2000){
			robot.drivetrain.setPrimary(0);
		}
	}
	
	public void init(double d){
		mode = d;
		
		robot.drivetrain.setPrimaryLimit(99999999999.0);
		robot.drivetrain.setStrafeLimit(999999999999.0);
		robot.drivetrain.setAngleAccelLimit(99999999999999.0);
		
		robot.drivetrain.setMaxTurnSpeed(999999999);
		robot.drivetrain.setAngleTarget(0);

		robot.drivetrain.setStrafe(0);
		robot.drivetrain.setPrimary(-0.6);
		
		timer = System.nanoTime();
	}
}
