package org.team751.subsystems;

import org.team751.commands.GooglyController;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GooglyEyes extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new GooglyController());
    }
    
    public void peekLeft() {
    	
    }
    
    public void peekRight() {
    	
    }
    
    public void center() {
    	
    }
    
}

