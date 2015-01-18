package org.pihisamurai;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	// Common Code
	public Drivetrain drivetrain;
	public Manipulator manipulator;

	// Modes
	public Teleoperated teleop;
	public Test test;
	public Autonomous autonomous;
	public Disabled disabled;

	public Gamepad gamepad;

	public void robotInit(){
		System.out.println("Robot Code Started");
		drivetrain = new Drivetrain(this);
		manipulator = new Manipulator(this);
		teleop = new Teleoperated(this);
		test = new Test(this);
		autonomous = new Autonomous(this);
		disabled = new Disabled(this);
		gamepad = new Gamepad(0); // Changed from 1 to 0
	}
	
	public void disabledInit(){
		System.out.println("Robot Disabled");
		disabled.init();
	}
	
	public void disabledPeriodic(){
		disabled.run();
	}
	
	public void autonomousInit() {
		System.out.println("Autonomous Mode");
		autonomous.init();
	}
	
	//Called about every 20ms during Autonomous Mode
	public void autonomousPeriodic(){
		autonomous.run();
	}

	public void teleopInit() {
		System.out.println("Teleoperated Mode");
		teleop.run();
	}
	
	public void teleopPeriodic(){
		teleop.run();
	}

	public void testInit() {
		System.out.println("Test Mode");
		test.init();
	}
	
	public void testPeriodic(){
		test.run();
	}
}
