package org.pihisamurai;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public Drivetrain drivetrain;
	public Manipulator manipulator;

	public Teleoperated teleop;
	public Autonomous autonomous;

	public Gamepad gamepad1;
	public Gamepad gamepad2;
	
	private static Robot robot;
	
    CameraServer server;

    int session;
    Image frame;

	public void robotInit() {
		System.out.println("Robot Code Started");

		robot = this;
		
		SmartDashboard.putNumber("Autonomous Mode", 1);

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		try {
			// The camera name (ex.: "cam0") can be found through the roborio web interface
			session = NIVision.IMAQdxOpenCamera("cam0",
					NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(session);
			camGet.start();
		} catch (Exception e) {
			try {
				// The camera name (ex.: "cam1") can be found through the roborio web interface
				session = NIVision.IMAQdxOpenCamera("cam1",
						NIVision.IMAQdxCameraControlMode.CameraControlModeController);
				NIVision.IMAQdxConfigureGrab(session);
				camGet.start();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}

		drivetrain = new Drivetrain();
		manipulator = new Manipulator();
		teleop = new Teleoperated(this);
		autonomous = new Autonomous(this);
	}
	
	Thread camGet = new Thread(new Runnable(){
		public void run() {
	        NIVision.IMAQdxStartAcquisition(session);
			while(true){
		        NIVision.IMAQdxGrab(session, frame, 1);
		        CameraServer.getInstance().setImage(frame);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	        //NIVision.IMAQdxStopAcquisition(session);
		}
	});
	
	private long modeStart;
	
	//Returns time since mode was enabled
	public long modeTime(){
		return (System.nanoTime() - modeStart) / 1000000;
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
		
		gamepad1 = new GamepadReplay(SmartDashboard.getString("Gamepad File 1"));
		gamepad2 = new GamepadReplay(SmartDashboard.getString("Gamepad File 2"));
		
		autonomous.init((int)SmartDashboard.getNumber("Autonomous Mode"));
	}

	// The function called roughly every twenty milliseconds during autonomous mode 

	public void autonomousPeriodic() {
		autonomous.run();
		// drivetrain.update();
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
		drivetrain.update();
		gamepad1.update();
		gamepad2.update();
	}

	public void testInit() {
		System.out.println("Test Mode");
	}

	public void testPeriodic() {
	}

	public static Robot getInstance() {
		return robot;
	}
}