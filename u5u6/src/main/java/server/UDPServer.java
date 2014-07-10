package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer {

	public static void main(String args[]) throws Exception{
		@SuppressWarnings("resource")
		//Datagram socket wird erstellt
		DatagramSocket serverSocket = new DatagramSocket(6667);
		//Buffer und sendData werden vorbereitet
		byte[] buf = new byte[5];
		byte[] sendData = new byte[19];
		//Das datum wird vorbereitet
		Date datum = new Date();
		SimpleDateFormat datumfor = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
		String tmp;
		//Die packets werden deklariert
		DatagramPacket receivePacket;
		DatagramPacket date;

		//Solange der server lebt
		while(true)
		{
			//Baue empfägerpaket
			receivePacket = new DatagramPacket(buf, buf.length);

			//Und empfange
			serverSocket.receive(receivePacket);
			//Gebe aus was empfangen wurde
			String sentence = new String( receivePacket.getData());
			System.out.println("RECEIVED: " + sentence);
			if(sentence.equals("DATE:"))
			{
				//Falls DATE: empfangen wurde baue das datum
				datum = new Date();
				tmp = ""+ datumfor.format( datum.getTime());

				//Verpacke die zu sendenden Daten
				sendData=tmp.getBytes();
				InetAddress ip = receivePacket.getAddress();
				int port = receivePacket.getPort();
				date = new DatagramPacket(sendData, sendData.length, ip, port);
				//Und versende sie
				serverSocket.send(date);
				System.out.println("IP: "+ip+"/nPort: "+port);
			}else
			{
				System.out.println("Error, not existing Order");
			}

			//Daten in Großschrift umwandeln
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
		}       
	} 
}