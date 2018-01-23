package src.org.team751.jetson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import src.org.team751.Robot;

//import com.kauailabs.nav6.frc.IMUAdvanced;


public class StateSenderUDP {

	private final int port;
	private final InetAddress ipAddress;
		
	public StateSenderUDP(InetAddress ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;		
	}
	
	/**
	 * Instantiates a new UDP sender. 
	 *
	 * @param ipAddressString the ip address string. Must be prechecked for validity; this function will only check the validity of the address format.
	 * @param port the port
	 * @throws UnknownHostException the unknown host exception
	 */
	public StateSenderUDP(String ipAddressString, int port) throws UnknownHostException {
		this.ipAddress = InetAddress.getByName(ipAddressString);
		this.port = port;
	}
	
	public void sendState(RobotState state) throws IOException{
		DatagramSocket serverSocket = new DatagramSocket(port);
		InetAddress IPAddress = ipAddress;
		
        StringBuilder messageString = new StringBuilder("[");
//        messageString.append(Robot.drivetrain.leftEncoder.get() + ",");
//        messageString.append(Robot.drivetrain.rightEncoder.get() + "]");
        byte[] sendData = messageString.toString().getBytes();
        
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        serverSocket.send(sendPacket);
        serverSocket.close();
	}
}
