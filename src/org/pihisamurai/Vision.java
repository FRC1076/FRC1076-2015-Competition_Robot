package org.pihisamurai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Vision {
	 

	ProcessBuilder pb;
	static final String fileName = "program.py";
	static final String dirName = "FRC1076-2015-Python-Scripts";
	
	public Vision(){
		String home = System.getProperty("user.home");
		File dir = new File(home+"/"+dirName);
		File program = new File(fileName);
		program.delete();
		
		program.deleteOnExit();
		dir.deleteOnExit();
		
		String prg = "import sys\nprint avery rules\n";
		//Need to make this nicer
		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(fileName));
			out.write(prg);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
		pb = new ProcessBuilder("python",fileName/*, arguments, arguments, arguments*/);
		pb.directory(program);
	
	}
	
	public String getData(){
		try {
			Process p;
			p = pb.start();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String rtrn;
			rtrn = in.readLine();
			return rtrn;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
}
