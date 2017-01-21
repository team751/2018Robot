package org.team751.subsystems;

import org.team751.commands.WinchController;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {
	public Relay winchHBridgeController = new Relay(1);
		
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new WinchController());
    }
    
    public void forward() {
    	System.out.println("intake");
    	winchHBridgeController.set(Relay.Value.kForward);
    }
    
    public void disable() {
    	winchHBridgeController.set(Relay.Value.kOff);
    }
    
    public void reverse() {
    	winchHBridgeController.set(Relay.Value.kReverse);
    }
}

