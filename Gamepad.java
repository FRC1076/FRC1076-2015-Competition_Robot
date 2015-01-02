/*
 * Gamepad class to encapsulate FRC 1076 PiHiSamuari's Logitech Gamepads
 * Adopted from FRC 830 Rat Packs C++ Version
 * 
 */

package org.pihisamurai;
import edu.wpi.first.wpilibj.Joystick; 
import edu.wpi.first.wpilibj.DriverStation; 

/**
 *
 * @author PiHi Samurai 1076
 */

public class Gamepad extends Joystick{

    static final int LEFT_BUMPER = 5;
    static final int RIGHT_BUMPER = 6;
    static final int LEFT_TRIGGER = 7;
    static final int RIGHT_TRIGGER = 8;    
    
    static final int F310_A = 1;
    static final int F310_B = 2;
    static final int F310_X = 3;
    static final int F310_Y = 4;
    static final int F310_LB = 5;
    static final int F310_RB = 6;
    static final int F310_L_BACK = 7;
    static final int F310_R_START = 8;
    
    static final int F310_LEFT_Y = 2;
    static final int F310_LEFT_X = 1;
    static final int F310_RIGHT_Y = 5;
    static final int F310_RIGHT_X = 4;
    static final int F310_TRIGGER_AXIS = 3;
    static final int F310_DPAD_X_AXIS = 6;
    
    int port;
    DriverStation driverStation;
    Gamepad(int port){
        super(port);
        this.port = port;
        driverStation = DriverStation.getInstance();
    }
    
    double getLeftX(){
        return getRawAxis(F310_LEFT_X);
    }

    double getLeftY(){
        return getRawAxis(F310_LEFT_Y);
    }

    double getRightX(){
        return getRawAxis(F310_RIGHT_X);
    }

    double getRightY(){
        return getRawAxis(F310_RIGHT_Y);
    }

    public double getRawAxis(int axis){
            return driverStation.getStickAxis(port, axis);
    }
    
    //These aren't called anywhere in this project.
    /*boolean getNumberedButton(int button){
        return ((0x1 << (button-1)) & driverStation.getStickButtons(port)) != 0;
    }
    
    public boolean getButtonA(){
        return getNumberedButton(1);
    }*/

}
