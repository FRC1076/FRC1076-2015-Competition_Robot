package org.pihisamurai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class Vision {

	ProcessBuilder pb;
	static final String fileName = "program.py";
	static final String dirName = "tempPythonScript";

	File dir;
	File program;

	public Vision() {
		String home = System.getProperty("user.home");
		dir = new File(home + "/" + dirName);
		program = new File(dir, fileName);
		dir.mkdirs();
		// program.deleteOnExit();
		try {
			program.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// dir.deleteOnExit();

		try {
			URL url = this.getClass().getClassLoader().getResource(fileName);

			InputStream in = url.openConnection().getInputStream();

			BufferedWriter out = new BufferedWriter(new FileWriter(home + "/"
					+ dirName + "/" + fileName));

			while (in.available() > 0)
				out.write(in.read());
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		pb = new ProcessBuilder("python", program.getAbsolutePath());
		// pb.directory(dir);

	}

	public String getData() {
		try {
			Process p;
			p = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
		
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	public static void main(String args[]) {
		System.out.println("CV Test");
		Vision vision = new Vision();
		System.out.print(vision.getData());
	}

}
