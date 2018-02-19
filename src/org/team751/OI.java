package src.org.team751;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick driverStick = new Joystick(0);
	public Button autoButton = new JoystickButton(driverStick, 1); // x button, 
	public Joystick operatorStick = new Joystick(1);
	
	private int left = 12;
	private int center = 13;
	private int right = 14; // *
	private int delay = 15; // *
	private int preference1 = 16; // *
	private int preference2 = 17; // *
	private int preference3 = 18; // *
	private int unused = 19;
 	
 	public DigitalInput[] autoSwitches = {
 			new DigitalInput(left), // Left
 			new DigitalInput(center), // Center
 			new DigitalInput(right), // Right 
 			new DigitalInput(delay), // Delay 
 			new DigitalInput(preference1), // Left: Prefer scale over switch. Center: Left switch is not ok. Right: Prefer scale over switch.
 			new DigitalInput(preference2), // Left: Scale not ok. Center: Just cross the line (no switch or scale). Right: Scale not ok.
 			new DigitalInput(preference3), // Left: Just cross the line. Center: Just cross the line. Right: Just cross the line.
 			new DigitalInput(unused), // ***Unused***
 	};
	
	// XBOX controller button mappings found here: https://gist.github.com/calebreister/8018231
	
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

