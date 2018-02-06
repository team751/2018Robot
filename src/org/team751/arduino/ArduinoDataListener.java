package src.org.team751.arduino;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private final double WHEELDIAMETER = 6.0;
	private final int MAGNETS = 6;

	private double distance, velocity, orientation;
	private long requestNumber = 0;
	private long leftPulses, rightPulses;
	private SerialPort port;
	private String message;
	private boolean overwrite;

	public ArduinoDataListener() {
		distance = 0.0;
		velocity = 0.0;
		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;
		
		message = "";
		overwrite = true;

		try {
			port = new SerialPort(9600, SerialPort.Port.kUSB);
			port.setReadBufferSize(1);
			port.setTimeout(0.001);
		} catch (RuntimeException e) {
			System.out.println("No arduino serial connection");
		}
	}

	public double getOrientation() {
		return orientation;
	}

	public long getLeftPulses(){
		return leftPulses;
	}

	public long getRightPulses(){
		return rightPulses;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getDistance()  {
		//total pulses / 2 to find the average pulses
		//wheel circumference / number of magnets = distance travelled for each magnet
		//inches convert to feet / 12
		return (leftPulses + rightPulses) / 2.0 * Math.PI * WHEELDIAMETER / MAGNETS / 12.0;
	}

	private void refreshDistance() {
		//
	}

	@Override
	public void run() {
		while (true) {
			fetchData();
		}
	}

	public void stop() {
	}

	private void fetchData() {
		if(overwrite){
		message = port.readString();
		}
		
		int bracketIndex = message.indexOf("[");
		if (bracketIndex == -1){
			return;
		}
		message = message.substring(bracketIndex);
		int endOfMessage = message.lastIndexOf(']');
		int startOfMessage = message.lastIndexOf('[', endOfMessage);

		if (endOfMessage <= startOfMessage) {
			message += port.readString();
			overwrite = false;
		} else {
			message = message.substring(startOfMessage + 1, endOfMessage);
//			System.out.println("Received String: " + message);
			String[] data = message.split("-");
			this.orientation = Double.valueOf(data[0]);
			this.leftPulses = Long.valueOf(data[1]);
			this.rightPulses = Long.valueOf(data[2]);
			overwrite = true;
		}


	}

}
