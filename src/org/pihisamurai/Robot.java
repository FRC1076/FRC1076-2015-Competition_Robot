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
	public Disabled disabled;
	

	public Gamepad gamepad;
	public Gamepad gamepad2;
	
    CameraServer server;

  //  int session;
  //  Image frame;

	public void robotInit() {
		System.out.println("Robot Code Started");



		SmartDashboard.putNumber("Autonomous Mode", 1);

    //    frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
    //    session = NIVision.IMAQdxOpenCamera("cam0",
   //             NIVision.IMAQdxCameraControlMode.CameraControlModeController);
    //    NIVision.IMAQdxConfigureGrab(session);
        
		drivetrain = new Drivetrain(this);
		manipulator = new Manipulator(this);
		teleop = new Teleoperated(this);
		test = new Test(this);
		autonomous = new Autonomous(this);
		disabled = new Disabled(this);
		gamepad = new Gamepad(0); // Changed from 1 to 0
		gamepad2 = new Gamepad(1); // Changed from 1 to 0
		
	//	camGet.start();
	}
	
	/*Thread camGet = new Thread(new Runnable(){
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
	});*/

	public void disabledInit() {
		System.out.println("Robot Disabled");
		disabled.init();
	}

	public void disabledPeriodic() {
		disabled.run();
	}

	public void autonomousInit() {
		System.out.println("Autonomous Mode");
		autonomous.init(SmartDashboard.getNumber("Autonomous Mode"));
	}

	public void autonomousPeriodic() {
		autonomous.run();
		drivetrain.update();
	}

	public void teleopInit() {
		System.out.println("Teleoperated Mode");
		teleop.init();
	}

	public void teleopPeriodic() {
		teleop.run();
		drivetrain.update();
	}

	public void testInit() {
		System.out.println("Test Mode");
		test.init();
	}

	public void testPeriodic() {
		test.run();
	}
}
