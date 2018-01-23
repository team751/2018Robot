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
		//diveded by two to find the average
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
		try {
			fetchData();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
	}

	private void fetchData() throws InterruptedException {
		boolean dataReceived = false;
		String message;
		String[]data = null;
		long receivedNumber = 0;
		while(!dataReceived){
		port.writeString("Q-" + requestNumber);
		short waitLoop = 3;
		
		do{
			Thread.sleep((5 - waitLoop) * 5);
			waitLoop--;
			message = port.readString();
			if(message != ""){
			data = message.split("-");	
			receivedNumber = Long.parseLong(data[0]);
			}else{
				requestNumber = -1;
			}
		}while(waitLoop >= 0 && receivedNumber != requestNumber);
		
		if(receivedNumber == requestNumber){
			this.orientation = Double.parseDouble(data[1]);
			this.leftPulses = Long.parseLong(data[2]);
			this.rightPulses = Long.parseLong(data[3]);
			port.writeString("OK-" + requestNumber);
			dataReceived = true;
		}
		requestNumber++;
	}
	}
}
