package src.org.team751.subsystems;

import src.org.team751.commands.WinchController;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Winch extends Subsystem {
	public PWMVictorSPX winchMotorController1 = new PWMVictorSPX(6);
	public PWMVictorSPX winchMotorController2 = new PWMVictorSPX(7);
		
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
    
    public void stop() {
    	winchMotorController1.setSpeed(0.0);
    	winchMotorController2.setSpeed(0.0);
    }
}

