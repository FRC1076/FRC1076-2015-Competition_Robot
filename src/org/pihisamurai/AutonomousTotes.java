package org.pihisamurai;

public class AutonomousTotes {

	Robot robot;

	// UltrasonicSensor ultrasonic = new UltrasonicSensor(2);
	// InfraredSensor infrared = new InfraredSensor(1);

	AutonomousTotes(Robot robot, String code) {
		this.robot = robot;
		execute(parse(code));
	}

	private int[] parse(String code) {
		String[] lines = code.split("");
		int[] list = new int[lines.length];
		for (int i = 0; i < lines.length; i++) {
			list[i] = -1;
			try { 
				list[i] = Integer.parseInt(lines[i]);
			} catch (Exception e) {
				switch (lines[i]) {
				case ";": list[i] = 10; break;
				case "/": list[i] = 11; break;
				case "f": list[i] = 12; break;
				case "b": list[i] = 13; break;
				case "l": list[i] = 14; break;
				case "r": list[i] = 15; break;
				case "u": list[i] = 16; break;
				case "d": list[i] = 17; break;
				case "p": list[i] = 18; break;
				default: break;
				}
			}
		}
		return list;
	}

	public void execute(int[] c) {
		double[] stack = new double[12];
		int pointer = 0;
		for (int i = 0; i < c.length; i++) {
			if(pointer < 1) {
				pointer++;
			}
			if (c[i] < 10) {
				stack[pointer] = Math.pow(10, stack[pointer]) + (c[i] / 10);
			} else {
				double time = System.nanoTime();
				switch (c[i]) {
				case 10: pointer++; break;
				case 11:
					stack[pointer] /= 10;
					break;
				case 12:
					robot.drivetrain.goDistPrimary(stack[pointer], 0.5);
					stack[pointer] = 0;
					pointer--;
					break;
				case 13:
					robot.drivetrain.goDistPrimary(stack[pointer], -0.5);
					stack[pointer] = 0;
					pointer--;
					break;
				case 14:
					robot.drivetrain.goDistStrafe(stack[pointer], 0.5);
					stack[pointer] = 0;
					pointer--;
					break;
				case 15:
					robot.drivetrain.goDistStrafe(stack[pointer], -0.5);
					stack[pointer] = 0;
					pointer--;
					break;
				case 16:
					while ((System.nanoTime() - time) / 1000000000 > 10000 * stack[pointer]) {
						robot.manipulator.setLiftPower(0.5);
					}
					stack[pointer] = 0;
					pointer--;
					break;
				case 17:
					while ((System.nanoTime() - time) / 1000000000 > 10000 * stack[pointer]) {
						robot.manipulator.setLiftPower(-0.5);
					}
					stack[pointer] = 0;
					pointer--;
					break;
				case 18:
					stack[pointer] = 0;
					pointer--;
					break;
				}
			}
		}
	}

	/*
	private int state;
	private double startDist;

	public void run() {
		switch (state) {
		case 0:
			startDist = robot.drivetrain.getPriamryDist();
			state = 1;
			break;

		case 1:
			robot.drivetrain.setPrimary(0.3);
			if (robot.drivetrain.getPriamryDist() - startDist > startDist) {
				state = 2;
				robot.drivetrain.setPrimary(0);
			}
			robot.drivetrain.setPrimary(0.3);
			break;
		case 2:
			robot.manipulator.setTargetHeight(0); // bottom box

			if (robot.manipulator.atTarget()) {
				state = 3;
				robot.drivetrain.speedController.setBoxCount(robot.drivetrain.speedController.getBoxCount() + 1);
			}
			// elevetor down
			// if down enough state 3
			break;
		case 3:
			robot.manipulator.setTargetHeight(2); // higher box

			if (robot.manipulator.atTarget()) 
				if (robot.drivetrain.speedController.getBoxCount() < 3)
					state = 0;
				else
					state = 4; // done
		}
	}

	public void init() {
		state = 0;

	} */

}
