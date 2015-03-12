package org.pihisamurai;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public Drivetrain drivetrain;
	public Manipulator manipulator;

	public Teleoperated teleop;

	public Gamepad gamepad1;
	public Gamepad gamepad2;
	
	public LEDController ledController;
	
	private static Robot robot;

	private long modeStart;
	
	//Returns time since mode was enabled
	public long modeTime(){
		return (System.nanoTime() - modeStart) / 1000000;
	}


	public static Robot getInstance() {
		return robot;
	}
    
	public void robotInit() {
		

		SmartDashboard.putString("Gamepad Read File", "file");
		robot = this;
		
		
		System.out.println("Robot Code Started");

		ledController = new LEDController();
		
		drivetrain = new Drivetrain();
		manipulator = new Manipulator();
		teleop = new Teleoperated();
	}
	
	// The initial function called on start of disabled mode 

	public void disabledInit() {
		System.out.println("Robot Disabled");
	}

	// The function called roughly every twenty milliseconds during disabled mode 

	public void disabledPeriodic() {
		
	}

	// The initial function called on start of autonomous mode 
	
	public void autonomousInit() {
		System.out.println("Autonomous Mode");

		modeStart = System.nanoTime();
		gamepad1 = new GamepadReplay(SmartDashboard.getString("Gamepad Read File") + "-driver.gamepad");
		gamepad2 = new GamepadReplay(SmartDashboard.getString("Gamepad Read File") + "-operator.gamepad");
		teleop.init();
	}

	// The function called roughly every twenty milliseconds during autonomous mode 

	public void autonomousPeriodic() {
		if (15000 > this.getInstance().modeTime()) {
			 teleopPeriodic();
		}
	}

	// The initial function called on start of teleop

	public void teleopInit() {
		System.out.println("Teleoperated Mode");
		modeStart = System.nanoTime();

		gamepad1 = new GamepadReal(0);
		gamepad2 = new GamepadReal(1);
		teleop.init();
	}

	// The function called roughly every twenty milliseconds during teleop 

	public void teleopPeriodic() {
		teleop.run();
		gamepad1.update();
		gamepad2.update();
		drivetrain.update();
		ledController.update();
	}

	public void testInit() {
		System.out.println("Test Mode");
		modeStart = System.nanoTime();
		
		gamepad1 = new GamepadReal(0, SmartDashboard.getString("Gamepad Read File") + "-driver.gamepad");
		gamepad2 = new GamepadReal(1, SmartDashboard.getString("Gamepad Read File") + "-operator.gamepad");

		teleop.init();
	}

	public void testPeriodic() {
		 teleopPeriodic();
	}
}