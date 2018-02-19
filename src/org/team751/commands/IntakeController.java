package src.org.team751.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import src.org.team751.Robot;

public class IntakeController extends Command{
	
	private final int intakeLimitSwitchPin = 0;
	
	public IntakeController(){
		requires(Robot.intake);
	}
	
	protected void initalize() {}

	//Intake
	protected void execute(){
		/*
		DigitalInput limitSwitch = new DigitalInput(intakeLimitSwitchPin);
		
		boolean intakeLimit = limitSwitch.get(); //True on high
		
		final boolean out = Robot.oi.driverStick.getRawButton(2); //Send out -> B
		final boolean in = Robot.oi.driverStick.getRawButton(1); //Suck in -> A
				
		if(out && in){ //If out and in buttons are pressed, do not do anything
			Robot.intake.stop();
		}else if(out){ //If the out button is pressed, go out
			Robot.intake.eject();
		}else if(in && !intakeLimit){ //If the in button is pressed, go in while limit switch is not pressed
			Robot.intake.takeIn();
		}else{ //If none of the above conditions are met, do not do anything to be safe
			Robot.intake.stop();
		}
		*/
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {}
}
