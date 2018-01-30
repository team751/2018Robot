package src.org.team751.arduino;

import edu.wpi.first.wpilibj.SerialPort;

public class ArduinoDataListener implements Runnable {
	private final float WHEELRADIUS = 6.0f;
	private final int MAGNETS = 8;

	private double distance, velocity, heading;
	private double orientation;
	private long requestNumber = 0;
	private long leftPulses, rightPulses;
	private SerialPort port;

	public ArduinoDataListener() {
		distance = 0.0;
		velocity = 0.0;
		heading = 0.0;

		orientation = 0.0;
		leftPulses = 0;
		rightPulses = 0;

		port = new SerialPort(9600, SerialPort.Port.kUSB);
		port.setReadBufferSize(1024);
		port.setTimeout(0.001);
	}

	public double getOrientation() {
		return orientation;
	}

	public long getLeftPulses() throws InterruptedException {
		return leftPulses;
	}

	public long getRightPulses() throws InterruptedException {
		return rightPulses;
	}

	public double getVelocity() {
		return velocity;
	}

	public double getDistance() throws InterruptedException {
		// diveded by two to find the average
		return (leftPulses + rightPulses) * Math.PI * WHEELRADIUS / MAGNETS;
	}

	public double getHeading() throws InterruptedException {
		return heading;
	}

	private void refreshDistance() {
		//
	}

	@Override
	public void run() {
		while (true) {
			try {
				fetchData();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stop() {
	}

	private void fetchData() throws InterruptedException {
		boolean dataReceived = false;
		String message;
		String[] data = null;
		// while (!dataReceived && counter > 0) {
		port.writeString("Q-" + requestNumber);
		short waitLoop = 1;
		System.out.println("requestNumber: " + requestNumber);
		do {
			Thread.sleep(60);
			waitLoop--;
			message = port.readString();
			if (!message.contains("*") || !message.startsWith(Long.toString(requestNumber))) {
				message = port.readString();
				System.out.println("At the " + waitLoop + "'s call, the message:" + message);
			} else {
				message = message.substring(0, message.indexOf("*") - 1);
				System.out.println("At the " + waitLoop + "'s call, the message:" + message);
				data = message.split("-");
				this.orientation = Double.valueOf(data[1]);
				this.leftPulses = Long.valueOf(data[2]);
				this.rightPulses = Long.valueOf(data[3]);
				port.writeString("OK-" + requestNumber);
				dataReceived = true;
			}
		} while (waitLoop > 0 && !dataReceived);

		requestNumber++;
	}
}
