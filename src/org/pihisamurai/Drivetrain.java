package org.pihisamurai;

import edu.wpi.first.wpilibj.Jaguar;

public class Drivetrain {

	Jaguar backLeftMotor;
	Jaguar frontLeftMotor;
	Jaguar backRightMotor;
	Jaguar frontRightMotor;
	Jaguar StrafeMotor;
	
	private Robot robot;

	public Drivetrain(Robot r) {
		this.robot = r;
		
		backLeftMotor = new Jaguar(??);
		frontLeftMotor = new Jaguar(??);
		backRightMotor = new Jaguar(??);
		frontRightMotor = new Jaguar(??);
		StrafeMotor = new Jaguar(??);
	}
	
	public void rightPower(double power){
		backRightMotor.set(power);
		frontRightMotor.set(power); //May need to be oposite directions
	}
	
	public void leftPower(double power){
		backLeftMotor.set(power);
		frontLeftMotor.set(power); //May need to be oposite directions
	}
	
	public void strafePower(double power){
		StrafeMotor.set(power);
	}
	
	/* Implement for Autonomous once encoders on robot
	
	public void moveDistance(double distance, double speed){
		
	}
	
	public void moveArc(double radius, double angle, double speed){
	
	}
	
	public void sharpTurn(double angle, double speed){
	
	}
	
	public void strafeDistance(double distance, double speed){
	
	}
	*/
}
