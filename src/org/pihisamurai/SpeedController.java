package org.pihisamurai;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpeedController {
	
	Drivetrain drivetrain;
	private int box;
	
	SpeedController(Drivetrain d){
		drivetrain = d;
		
		SmartDashboard.putNumber("Turn Speed 0 Boxes", 4.0);
		SmartDashboard.putNumber("Turn Speed 1 Boxes", 4.0);
		SmartDashboard.putNumber("Turn Speed 2 Boxes", 3.5);
		SmartDashboard.putNumber("Turn Speed 3 Boxes", 3.0);
		SmartDashboard.putNumber("Turn Speed 4 Boxes", 2.0);
		SmartDashboard.putNumber("Turn Speed 5 Boxes", 1.5);
		SmartDashboard.putNumber("Turn Speed 6 Boxes", 1.3);

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
	}
	
	double turnSpeedModifier = 1;
	
	public void setTurnSpeedModifier(double a){
		turnSpeedModifier = a;
	}
	
	public void update(){

		SmartDashboard.putNumber("Box Count", box);
		
		double turnSpeed =  SmartDashboard.getNumber("Turn Speed " + box + " Boxes");
		double accelLimit =  SmartDashboard.getNumber("Stick change rate " + box + " boxes");
		double turnAccelLimit = SmartDashboard.getNumber("Angle stick rate " + box + " boxes");

		drivetrain.setPrimaryLimit(accelLimit);
		drivetrain.setStrafeLimit(999.0);
		drivetrain.setAngleAccelLimit(turnAccelLimit);
		
		drivetrain.setMaxTurnSpeed(turnSpeed*turnSpeedModifier);
	}
}
