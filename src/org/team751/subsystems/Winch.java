package src.org.team751.subsystems;

import src.org.team751.commands.WinchController;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {
	public VictorSP winchMotorController = new VictorSP(6);
		
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new WinchController());
    }
    
    public void forward() {
    	System.out.println("intake");
    	winchMotorController.set(1.0);
    }
    
    public void disable() {
    	winchMotorController.set(0.0);
    }
    
    public void reverse() {
    	winchMotorController.set(-1.0);
    }
}

