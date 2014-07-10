package floje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class In implements Runnable {

	private Socket socket;
	private ModelShop shop;
	private boolean stop, changed, returnValue;
	private ObjectInputStream objectInputStream;

	public In (Socket socket, ModelShop shop) {
		this.socket = socket;
		this.shop = shop;
		stop = false;
		changed = false;
		returnValue = false;
	}

	@Override
	public void run() {
		objectInputStream = null;
		//Synchronized damit der socket nicht von mehreren manipuliert wird
		synchronized (socket)
		{
			try
			{
				//Erstelle den stream vom stream des sockets
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				System.out.println("ois initialized");
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//Wenn steam nicht null
		if (objectInputStream != null) 
		{
			//und wenn socket verbunden und wir nicht stoppen wollen
			while (socket.isConnected() && !stop) 
			{
				try 
				{
					//Lese order
					Object order = objectInputStream.readObject();
					if (order instanceof Order) 
					{
						//und füge sie hinzu falls instanz von order
						shop.addOrder((Order) order);
					}
					if (order instanceof Boolean) 
					{
						//Oder setze returnValue und changed weil wir was geändert haben
						returnValue = (Boolean)order;
						changed = true;
					}
				}
				catch (SocketException e)
				{
				}
				catch (ClassNotFoundException | IOException e) 
				{
					e.printStackTrace();
					break;
				}
			}
			try 
			{
				//Schließe den stream nach allem
				objectInputStream.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}else 
		{
			System.out.println("No object inputstream");
		}
	}

	public void stop() {
		//Synchronized damit die stop variable nicht während oder
		//nach dem lesen verändert wird
		synchronized ((Object)stop) 
		{
			stop = true;
		}
		try 
		{
			//Stream schließen falls offen
			objectInputStream.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public int bla () {
		//Synchronized über die changed variable, weil wir hier nicht 
		//das selbe problem wollen wie bei stop(Nach der prüfung soll
		//keine änderung passieren
		synchronized ((Object)changed)
		{
			if (changed)
			{
				changed = false;
				return returnValue ? 1 : -1;
			}
		}
		return 0;
	}

}

