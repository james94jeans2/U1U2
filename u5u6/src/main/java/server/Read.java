package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import floje.Order;

public class Read implements Runnable{

	private InputStream inputStream;
	//private Thread t2;
	private Send send;
	private Warehouse warehouse;
	private Socket socket;
	private boolean stop;

	public Read(Socket socket, InputStream inputStream, Warehouse warehouse, Send send){
		//t2=t;
		this.inputStream=inputStream;
		this.warehouse=warehouse;
		this.send=send;
		stop = false;
		this.socket=socket;
	}

	@Override
	public void run() {
		ObjectInputStream objectInputStream = null;
		//Synchronized damit der socket atomar zugegriffen wird
		synchronized (socket)
		{

			try
			{
				//Stream erzeugen
				objectInputStream = new ObjectInputStream(inputStream);
				System.out.println("ois initialized");
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		while(!socket.isClosed() && !stop)
		{
			String input = "";
			//synchronized (soc) {
			try
			{
				//Daten werden gelesen
				Object indata;
				indata = objectInputStream.readObject();
				//test ob das gelesene ein String ist
				if(indata instanceof String)
				{
					input = (String)indata;
					//Test auf legale nutzerdaten
					if(authentifizierung(input))
					{
						//Wenn nutzerdaten stimmen dann wird gelesen
						send.setOut(true);	
						System.out.println("waiting for Data");
						indata = objectInputStream.readObject();
						if(indata instanceof Order)
						{
							//Und wenn es eine order ist, wird sie ins 
							//warenhaus hinzugefügt
							Order inorder = (Order)indata;
							send.setOut(inorder);
							System.out.println("hello");
							warehouse.addOrder(inorder);
						}
					}else
					{
						System.out.println(input);
						send.setOut(false);

					}
				}
				//Wenn wir einen integer gelesen haben
				if (indata instanceof Integer)
				{
					//und dieser -1 ist
					if ((Integer)indata == -1)
					{
						//Hören wir auf weil wir das ende des Streams erreicht haben
						stop = true;
					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				stop = true;
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				stop = true;
			}
		}
		System.out.println("Shutting down ois!");
		try 
		{
			//Danach wird der stream geschlossen
			objectInputStream.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//}		
	}

	private boolean authentifizierung(String auth) {
		if(auth.contains("admin:admin"))
		{
			return true;
		}
		return false;
	}
}