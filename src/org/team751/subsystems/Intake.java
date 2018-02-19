package src.org.team751.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import src.org.team751.commands.IntakeController;

public class Intake extends Subsystem{
	private PWMVictorSPX[] intakeMotorControllers = {new PWMVictorSPX(9), 
													 new PWMVictorSPX(8)}; 
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new IntakeController());
    }
	
	public void eject() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(0.75);
		}
	}
	
	public void stop() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(0);
		}
	}
	
	public void takeIn() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(-0.75);
		}
	}
}
