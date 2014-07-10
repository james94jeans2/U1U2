package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	private static ServerSocket serverSocket;
	//private Socket client;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args){
		// Warenhaus erstellen und Thread der sich drum kümmert
		Warehouse warehouse = new Warehouse();
		Thread warehouseThread = new Thread(warehouse);
		warehouseThread.start();
		try
		{
			//ServerSocket erstellen
			serverSocket= new ServerSocket(6666);

			while (true) 
			{
				Socket client = null;
				try
				{
					//Blockierender aufruf accept() um auf clients zu warten
					client = serverSocket.accept();
					//Streams vom client holen
					inputStream = client.getInputStream();
					outputStream = client.getOutputStream();
				}
				catch(IOException e1) 
				{
					e1.printStackTrace();
				}

				//Die zu sendenden Daten verpacken und sendThread geben
				Send send = new Send(outputStream,client);
				final Thread sendThreat = new Thread(send);
				//ReadThread erstellen
				final Thread readThread = new Thread(new Read(
						client,inputStream,warehouse,send));

				//Beide Threads starten
				sendThreat.start();
				readThread.start();
			}		
		} 
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
	}
	//	private static boolean authentifizierung(String auth) {
	//		if(auth.contains("admin:admin")){
	//			return true;
	//		}
	//		return false;
	//	}
}