package org.pihisamurai;

public class AutonomousTotes {

	Robot robot;
	
	private String program;
	
	Thread programmingParse;

	AutonomousTotes(Robot robot) {
		this.robot = robot;
		programmingParse = new Thread(new Runnable () {
			public void run () {
				execute(parse(program));
			}

			private int[] parse(String code) {
				String[] lines = code.split("");
				int[] list = new int[lines.length];
				for (int i = 0; i < lines.length; i++) {
					list[i] = -1;
					try { 
						list[i] = Integer.parseInt(lines[i]);
					} catch (Exception e) {
						list[i] = 10 + ";/fblrudp".indexOf(code);
						if (list[i] == 9) {
							list[i] = -1;
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
							robot.manipulator.setLiftPower(0.5);
							try {
								Thread.sleep((long) (stack[pointer] * 1000));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							stack[pointer] = 0;
							pointer--;
							break;
						case 17:
							robot.manipulator.setLiftPower(-0.5);
							try {
								Thread.sleep((long) (stack[pointer] * 1000));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							stack[pointer] = 0;
							pointer--;
							break;
						case 18:
							stack[pointer] = 0;
							pointer--;
							break;
						}
						
						robot.manipulator.setLiftPower(0);
					}
				}
			}
		});
	}

	public void init(String code) {
		program = code;
		programmingParse.start();
	}

	public void run () {
		robot.drivetrain.update();
	}
}
