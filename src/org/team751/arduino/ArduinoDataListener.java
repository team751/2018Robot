package org.team751.arduino;

import java.io.IOException;
import java.net.*;

public class ArduinoDataListener implements Runnable {
	private boolean isRunning = true;
	private final int port;
	private double heading; // axes of operation
	
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

		if (clientSocket == null) return;
		
        byte[] receiveData = new byte[4];

        while (clientSocket.isBound() && isRunning) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
				clientSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

            String modifiedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
            // form: heading
         
            
            //String dividedString[] = modifiedSentence.replaceAll("\\[", "").replaceAll("\\]","").split(","); // "[x, y]" -> "x", " y"
            
            this.heading = Double.parseDouble(modifiedSentence);
        }
        clientSocket.close();	
	}
	
	public double getHeading() {
		return heading;
	}

	public void stop() {
		this.isRunning = false;
	}
}
