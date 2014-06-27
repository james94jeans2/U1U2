package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer {
	
	public static void main(String args[]) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(6667);
		byte[] buf = new byte[5];
		byte[] sendData = new byte[19];
		Date datum = new Date();
		SimpleDateFormat datumfor = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		String tmp;
		DatagramPacket receivePacket;
		DatagramPacket date;
		while(true){
				receivePacket = new DatagramPacket(buf, buf.length);
				
				serverSocket.receive(receivePacket);
				String sentence = new String( receivePacket.getData());
				System.out.println("RECEIVED: " + sentence);
				if(sentence.equals("DATE")){
					datum = new Date();
					tmp = ""+ datumfor.format( datum.getTime());
					
					sendData=tmp.getBytes();
					InetAddress ip = receivePacket.getAddress();
					int port = receivePacket.getPort();
					date = new DatagramPacket(sendData, sendData.length, ip, port);
					serverSocket.send(date);
					System.out.println("IP: "+ip+"/nPort: "+port);
				}else{
					System.out.println("Error, not existing Order");
				}
				
				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				
		}       
	} 
	
	

}
