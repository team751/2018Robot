package src.org.team751.commands;

import src.org.team751.Robot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WinchController extends Command {

    public WinchController() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.winch);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	final double triggerRight = Robot.oi.driverStick.getRawAxis(3);
    	final double triggerLeft = Robot.oi.driverStick.getRawAxis(2);
    	final double speed = triggerRight - triggerLeft;
    	Robot.winch.setSpeed(speed);
    	

    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
