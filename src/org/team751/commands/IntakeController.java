package src.org.team751.commands;

import edu.wpi.first.wpilibj.command.Command;
import src.org.team751.Robot;

public class IntakeController extends Command{
	public IntakeController(){
		requires(Robot.intake);
	}
	
	protected void initalize() {}

	protected void execute(){
		final boolean out = Robot.oi.driverStick.getRawButton(11);
		final boolean in = Robot.oi.driverStick.getRawButton(12);
		
		if(out && in){
			Robot.intake.stopMotion();
		}else if(out){
			Robot.intake.openMotion();
		}else if(in){
			Robot.intake.closeMotion();
		}else{
			Robot.intake.stopMotion();
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	protected void end() {}
}
