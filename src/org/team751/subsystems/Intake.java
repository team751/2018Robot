package src.org.team751.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;

public class Intake {
	private PWMVictorSPX[] intakeMotorControllers; 
	
	public Intake() {
		intakeMotorControllers = new PWMVictorSPX[2];
		
		intakeMotorControllers[0] = new PWMVictorSPX(6);
		intakeMotorControllers[1] = new PWMVictorSPX(8);
	}
	
	public void openMotion() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(0.75);
		}
	}
	
	public void stopMotion() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(0);
		}
	}
	
	public void closeMotion() {
		for(PWMVictorSPX p : intakeMotorControllers) {
			p.set(-0.75);
		}
	}
}
