package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Send implements Runnable {

	private OutputStream outputStream;
	private Object output;
	private boolean changed, stop;
	private Socket socket;

	public Send(OutputStream outputStream,Socket socket){
		this.outputStream=outputStream;
		changed=false;
		this.socket=socket;
		output = new Object();
		stop = false;
	}

	public void run() {
		ObjectOutputStream objectOutputStream = null;
		//Synchronized damit niemand den socket manipuliert
		synchronized (socket) 
		{
			try
			{
				//Der Stream wird erzeugt
				objectOutputStream = new ObjectOutputStream(outputStream);
				System.out.println("oos initialized");
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		//Solange der Socket nicht geschlossen wurde und der stream nicht null
		//ist und wir nicht stoppen wollen
		while(!socket.isClosed() && objectOutputStream != null && !stop)
		{
			//synchronized (soc) {
			//Wenn daten geändert wurden..
			if(changed)
			{
				try
				{
					//Machen wir unseren output synchron damit er unverändert bleibt
					synchronized (output) 
					{
						//Und schreiben das objekt mit writeObject
						System.out.println("writing");
						objectOutputStream.writeObject(output);
						objectOutputStream.flush();
						//Wenn wir einen integer vorfinden
						if (output instanceof Integer)
						{
							//dann stoppen wir
							stop = true;
						}
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}	
				changed = false;
			}
			//}
		}
		try 
		{
			//Dann schließen wir den Stream
			objectOutputStream.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Outputconnection closed");
	}

	public void setOut(Object temp){
		//Wir synchen
		synchronized (output)
		{
			//Und setzen den output auf den übergebenen
			output=temp;
			//Und sagen dass was verändert wurdes
			changed = true;
		}
	}
}