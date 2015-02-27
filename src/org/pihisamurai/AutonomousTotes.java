package org.pihisamurai;

public class AutonomousTotes {

	Robot robot;
	
	private String program;

	AutonomousTotes(Robot robot) {
		this.robot = robot;
	}

	public void run() {
		execute(parse(program));
	}

	public void init(String code) {
		program = code;
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
}
