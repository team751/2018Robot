package org.team751.subsystems;

import org.team751.commands.ArmController;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arms extends Subsystem {
	public VictorSP armsController = new VictorSP(6);
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ArmController());
    }
}

