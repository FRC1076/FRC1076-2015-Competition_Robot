package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpeedController {
	
	Robot robot;
	private int box;
	
	SpeedController(Robot robot){
		this.robot = robot;
		
		SmartDashboard.putNumber("Turn Speed 0 Boxes", 5.1);
		SmartDashboard.putNumber("Turn Speed 1 Boxes", 5.1);
		SmartDashboard.putNumber("Turn Speed 2 Boxes", 4.5);
		SmartDashboard.putNumber("Turn Speed 3 Boxes", 3.5);
		SmartDashboard.putNumber("Turn Speed 4 Boxes", 2.5);
		SmartDashboard.putNumber("Turn Speed 5 Boxes", 2.0);
		SmartDashboard.putNumber("Turn Speed 6 Boxes", 1.5);

		SmartDashboard.putNumber("Stick change rate 0 boxes", 2.0);
		SmartDashboard.putNumber("Stick change rate 1 boxes", 2.0);
		SmartDashboard.putNumber("Stick change rate 2 boxes", 1.5);
		SmartDashboard.putNumber("Stick change rate 3 boxes", 0.7);
		SmartDashboard.putNumber("Stick change rate 4 boxes", 0.5);
		SmartDashboard.putNumber("Stick change rate 5 boxes", 0.3);
		SmartDashboard.putNumber("Stick change rate 6 boxes", 0.2);
		
		SmartDashboard.putNumber("Angle stick rate 0 boxes", 2.0);
		SmartDashboard.putNumber("Angle stick rate 1 boxes", 2.0);
		SmartDashboard.putNumber("Angle stick rate 2 boxes", 1.25);
		SmartDashboard.putNumber("Angle stick rate 3 boxes", 1.2);
		SmartDashboard.putNumber("Angle stick rate 4 boxes", 0.8);
		SmartDashboard.putNumber("Angle stick rate 5 boxes", 0.6);
		SmartDashboard.putNumber("Angle stick rate 6 boxes", 0.3);
	}
	
	public int getBoxCount(){
		return box;
	}
	
	public void setBoxCount(int box){
		this.box = box;
		SmartDashboard.putNumber("Box Count", box);
	}
	
	double turnSpeedModifier = 1;
	
	public void setTurnSpeedModifier(double a){
		turnSpeedModifier = a;
	}
	
	public void update(){
		double turnSpeed =  SmartDashboard.getNumber("Turn Speed " + box + " Boxes");
		double accelLimit =  SmartDashboard.getNumber("Stick change rate " + box + " boxes");
		double turnAccelLimit = SmartDashboard.getNumber("Angle stick rate " + box + " boxes");

		robot.drivetrain.setPrimaryLimit(accelLimit);
		robot.drivetrain.setStrafeLimit(accelLimit);
		robot.drivetrain.setAngleAccelLimit(turnAccelLimit);
		
		robot.drivetrain.setMaxTurnSpeed(turnSpeed*turnSpeedModifier);
	}
}
