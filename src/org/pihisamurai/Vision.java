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

	public Vision() {

	}
	
	//return true if see tote
	public boolean seeTote(){
		return false;
	}
	
	//Return true if see bin
	public boolean seeBin(){
		return false;
	}

	//return 0 if tote aligned horizontaly
	//return negative if to left and positve if to right
	public double toteHorizontal() {
		return 0;
	}
	
	//Angle of tote in front
	public double toteAngle(){
		return 0;
	}

}
