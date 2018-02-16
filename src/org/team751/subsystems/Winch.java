package src.org.team751.subsystems;

import src.org.team751.commands.WinchController;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {
	public VictorSP winchMotorController1 = new VictorSP(6);
	public VictorSP winchMotorController2 = new VictorSP(7);
		
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new WinchController());
    }

    public void setSpeed(double speed){
    	winchMotorController1.set(speed);
    	winchMotorController2.set(speed);
    }
}

