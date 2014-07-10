package floje;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class Out implements Runnable {

	private OutputStream outputStream;
	private Socket socket;
	private CopyOnWriteArrayList<Pair<String, Order>> work;
	private boolean stop;
	private In in;

	public Out (OutputStream outputStream, Socket socket, In in) {
		this.socket = socket;
		work = new CopyOnWriteArrayList<Pair<String,Order>>();
		this.outputStream = outputStream;
		stop = false;
		this.in = in;
	}

	public void sendOrder (String login, Order order) {
		//Während die order gesendet wird darf unsere arbeitsliste nicht
		//verändert werden, also synchronized
		synchronized (work) 
		{
			work.add(Pair.of(login, order));
		}
	}

	@Override
	public void run() {
		ObjectOutputStream objectOutputStream = null;
		//Synchronized, weil wir nun mit dem socket arbeiten wollen und 
		//keiner "dazwischen-arbeiten" soll
		synchronized (socket) {
			try 
			{
				//Versuche den stream zu erstellen
				objectOutputStream = new ObjectOutputStream(outputStream);
				System.out.println("oos initialized");
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		//Wenn der stream nicht null ist..
		if (objectOutputStream != null) 
		{
			//Solange wir verbunden sind und nicht stoppen wollen
			while (socket.isConnected() && !stop) 
			{
				//Wir setzen die arbeit synchronized, damit wir keine manipulationen haben
				synchronized (work)
				{
					//Solange unsere arbeit nicht leer ist
					if (!work.isEmpty()) 
					{
						//Nehmen wir uns jeweils das erste element
						Pair<String, Order> todo = work.get(0);

						try 
						{
							//und versuchen es über den stream zu schreiben
							objectOutputStream.writeObject(todo.getKey());
							objectOutputStream.flush();
							int ret = 0;
							//"Nice" methode um auf antwort zu warten
							System.out.println("waiting for response");
							while ((ret = in.bla()) == 0 && !stop) 
							{
							}
							//Sobald eine antwort kam geht es weiter
							System.out.println("got response");
							if (ret == -1) 
							{
								//Wenn ein -1 kam zeigen wir einen fehler und 
								//entfernen das element aus der liste
								ViewCostumer.getInstance().showLoginError();
								work.remove(todo);
								continue;
							} else 
							{
								//Sollte das geklappt haben schreiben wir das element
								objectOutputStream.writeObject(todo.getValue());
								objectOutputStream.flush();
								objectOutputStream.reset();
							}
						} 
						catch (IOException e)
						{
							e.printStackTrace();
						}
						//nach erfolgreichem vorgang entnehmen wir das element
						work.remove(todo);

					}
				}
			}
			try 
			{
				//Wir versuchen eine -1 zu schreiben um das ende zu markieren
				objectOutputStream.writeObject(-1);
			} 
			catch (IOException e1)
			{
			}
			try 
			{
				//Dann wird noch versucht den stream zu schließen
				objectOutputStream.close();
			} 
			catch (IOException e) 
			{
			}
		} else 
		{
			System.out.println("Outputstream == null");
		}
	}

	public void stop () {
		//Synchronized weil wir hier nur atomar zugreifen können wollen
		synchronized ((Object)stop)
		{
			stop = true;
		}
	}

}
