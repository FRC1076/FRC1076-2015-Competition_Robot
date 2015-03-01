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
	public Test test;
	public Autonomous autonomous;

	public Gamepad gamepad1;
	public Gamepad gamepad2;
	
    CameraServer server;

    int session;
    Image frame;

	public void robotInit() {
		System.out.println("Robot Code Started");

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

		drivetrain = new Drivetrain(this);
		manipulator = new Manipulator(this);
		teleop = new Teleoperated(this);
		test = new Test(this);
		autonomous = new Autonomous(this);
		gamepad1 = new Gamepad(0);
		gamepad2 = new Gamepad(1);
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
		test.init();
	}

	public void testPeriodic() {
		test.run();
	}
}