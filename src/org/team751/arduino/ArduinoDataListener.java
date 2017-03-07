package org.team751.arduino;

import java.io.IOException;
import java.net.*;

public class ArduinoDataListener implements Runnable {
	private boolean isRunning = true;
	private final int port;
	private double heading; // axes of operation
	private double distance;
	private double velocity;
	
	public ArduinoDataListener(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		System.out.println("Is going");

		if (clientSocket == null) return;
		
        byte[] receiveData = new byte[1024];

        while (clientSocket.isBound() && isRunning) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

            String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            System.out.println("Got " + modifiedSentence);
            // form: [heading,velocity,distance]
            modifiedSentence = modifiedSentence.replaceAll("\\[|\\]", ""); // remove the hard brackets on either side to prevent substrings because easier
            String[] stuff =  modifiedSentence.split(",");
            this.heading = Double.parseDouble(stuff[0]);
            this.velocity = Double.parseDouble(stuff[1]);
            this.distance = Double.parseDouble(stuff[2]);
        }
        clientSocket.close();	
	}
	
	public double getHeading() {
		return heading;
	}
	
	public double getDistance() {
		return distance;
	}

	public double getVelocity() {
		return velocity;
	}

	public void stop() {
		this.isRunning = false;
	}
}
