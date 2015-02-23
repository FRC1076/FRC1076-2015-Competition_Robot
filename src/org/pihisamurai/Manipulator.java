package org.pihisamurai;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator {

	private Robot robot;
	private Jaguar LiftMotor;
	private Encoder encoder;
	
	private static final int ENCODER_CHANNEL_A = 6;
	private static final int ENCODER_CHANNEL_B = 7;
	
	private static final int ELEVATOR_MOTOR_PORT = 6;

	private PIDController elevatorHeightPID;

	
	Manipulator(Robot r) {
		this.robot = r;
		LiftMotor = new Jaguar(ELEVATOR_MOTOR_PORT);
		encoder = new Encoder(ENCODER_CHANNEL_A, ENCODER_CHANNEL_B);
		
		SmartDashboard.putNumber("Elevator P", 0);
		SmartDashboard.putNumber("Elevator I", 0);
		SmartDashboard.putNumber("Elevator D", 0);
		
		PIDSource manipulatorHeight = new PIDSource() {
			public double pidGet() {
				
				//Needs to calibrate bottom/top limit switches
				return encoder.getDistance();
			} 
		};
		
		PIDOutput maniputlaorWrite = new PIDOutput () {
			public void pidWrite(double a) {
				liftPower(a);
			}
		};
		
		elevatorHeightPID = new PIDController(SmartDashboard.getNumber("Elevator P"), SmartDashboard.getNumber("Elevator I"), SmartDashboard.getNumber("Elevator D"), manipulatorHeight, maniputlaorWrite, 0.02);
		PIDUpdate.start();
	}
	
	Thread PIDUpdate = new Thread(new Runnable() {
		public void run() {
			while (true) {
				elevatorHeightPID.setPID(SmartDashboard.getNumber("Elevator P"), SmartDashboard.getNumber("Elevator I"),
						SmartDashboard.getNumber("Elevator D"));
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	});
	
	public void setTargetHeight(double h){
		elevatorHeightPID.setSetpoint(h);
		elevatorHeightPID.enable();
	}
	
	private void liftPower(double power){
		
		power *= 0.7;
		//TEMPORARY
		
		if (Math.abs(power) < 0.1) {
			LiftMotor.set(0);
		} else {
			LiftMotor.set(power);
		}
	}

	public void setLiftPower(double power) {
		elevatorHeightPID.disable();
		liftPower(power);
	}

	public boolean atTarget() {
		return elevatorHeightPID.onTarget(); //TODO
	}

}