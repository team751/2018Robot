package org.team751.subsystems;

import org.team751.commands.IntakeController;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem {
	public VictorSP intakeController = new VictorSP(7);
	
	private static double maxSpeed = 0.6; 
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new IntakeController());
    }
    
    public void intake() {
    	System.out.println("intake");
    	intakeController.set(maxSpeed);
    }
    
    public void disable() {
    	intakeController.set(0);
    }
    
    public void outtake() {
    	intakeController.set(-maxSpeed);
    }
}

