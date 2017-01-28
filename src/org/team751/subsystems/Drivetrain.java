package org.team751.subsystems;

import org.team751.Robot;
import org.team751.commands.JoystickDrive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
	static final int MAX_RPM = 5310; // taken from anymark for the motor controllers
    static final double kP = 0.01;
	static final double kI = 0.01;
	static final double kD = 0.01;
    
	public LeftWheels left = new LeftWheels();
	public RightWheels right = new RightWheels();
	
    public boolean isDrivingAutonomously;
    
    public Drivetrain() {
	}
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new JoystickDrive());
    }
    					
    /*	   X/Y orientation values for joystick
     * 					   +
     * 					   ^
     * 					   |
     * 		  			   |
     * -  <----------------+------------------> +
     * 					   |
     * 					   |
     * 					   |
     * 					   -
     */
    
    
    public void setRPMOnJoystick(double x, double y){
    	// do normal first
    	// do initialization to vertical movement
    	double right = y;
    	double left = y;
    	
    	// turning based off of x, add/subtract from each side
    	right -= x;
    	left += x;
    	
    	// check if either exceeds 1 (normalized max) or goes under -1 (normalized min). Basically x/y edge case adjustments
    	if(left > 1){
    		double difference = left - 1;
    		left = 1; // max out left
    		right -= difference; // adjust to prioritize turning rather than up/down action
    	}
    	
    	if(left < 1){
    		double difference = left + 1;
    		left = -1;
    		right += difference;
    	}
    	
    	// should only happen for one or the other
    	if(right < 1){
    		double difference = right + 1;
    		right = -1; // max out right
    		left += difference;
    		
    	}
    	
    	if(right > 1){
    		double difference = right - 1;
    		right = 1;
    		left -= difference;
    	}
    	
    	// scale from normalized to RPM
    	right*=MAX_RPM;
    	left*=MAX_RPM;
    	
    	this.left.setSetpoint(left);
    	this.right.setSetpoint(right);
    }
}

