package src.org.team751.subsystems;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import src.org.team751.Robot;
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
	
	public void ejectAuto(){
		eject();

		synchronized (TimeUnit.SECONDS) {
		    try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		stop();
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
