package src.org.team751.arduino;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class ArduinoDataListener implements Runnable, PIDSource {
	private final double WHEELDIAMETER = 6.0;
	private final int MAGNETS = 6;

	private double orientation;
	private long leftPulses, rightPulses;
	private SerialPort port;
	private String message;
	private boolean overwrite;

	private PIDSourceType sourceType;

	public ArduinoDataListener() {
		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;

		message = "";
		overwrite = true;

		sourceType = PIDSourceType.kDisplacement;

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

	public long getLeftPulses() {
		return leftPulses;
	}

	public long getRightPulses() {
		return rightPulses;
	}

	public double getDistance() {
		// total pulses / 2 to find the average pulses
		// wheel circumference / number of magnets = distance travelled for each
		// magnet
		// inches convert to feet / 12
		// ***since we only have one reedswitch now, we won't / 2, but we will
		// ***
		return ((leftPulses + rightPulses) / 2) * Math.PI * WHEELDIAMETER / MAGNETS / 12.0;
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

		if (overwrite) {
			message = port.readString();
		}

		int bracketIndex = message.indexOf("[");
		if (bracketIndex == -1) {
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
			//System.out.println(message);
			String[] data = message.split("-");
			this.orientation = Double.valueOf(data[0]);
			this.leftPulses = Long.valueOf(data[1]);
			this.rightPulses = Long.valueOf(data[2]);
			overwrite = true;
		}

	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		sourceType = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return sourceType;
	}

	@Override
	public double pidGet() {
		switch (sourceType) {
		case kDisplacement:
			return getDistance();
		case kRate:
			return getOrientation();
		default:
			return 0.0;
		}
	}

}
