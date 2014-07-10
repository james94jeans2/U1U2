package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer extends UnicastRemoteObject implements ChatService {

	private static final long serialVersionUID = 1L;

	private CopyOnWriteArrayList<ClientService> clients;

	protected ChatServer() throws RemoteException {
		clients = new CopyOnWriteArrayList<ClientService>();
	}

	@Override
	public void login(String userName) throws RemoteException{
		//synchronized, damit nur ein chatclient gleichzeitig die liste bearbeiten kann
		synchronized (clients) 
		{
			//Für alle clients die bereits angemeldet sind...
			for (ClientService loggedOnClient : clients)
			{
				//...wird getestet, ob der name schon vergeben wurde...
				if (loggedOnClient.getName().equals(userName))
				{
					//...und falls ja wird eine exception geworfen
					throw new IllegalArgumentException("Es wurde bereits ein Client mit diesem Namen"
							+ " angemeldet!");
				}
			}
			try 
			{
				//Es wird versucht einen client hinzuzufügen indem wir ihn aus der
				//registry suchen mit naming.lookup
				clients.add((ClientService)Naming.lookup(userName));
			} 
			catch (MalformedURLException | NotBoundException e)
			{
				e.printStackTrace();
			}

			System.out.println("Client angemeldet: '" + userName + "'");
		}
		//send methode wird aufgerufen
		send("'" + userName + "' betritt den Chat");
	}

	@Override
	public void logout(String userName) throws RemoteException{
		try 
		{
			//Wir testen ob das löschen des clients aus der liste funktioniert hat...
			if (clients.remove((ClientService)Naming.lookup(userName))) 
			{
				//... und wenn ja senden wir eine nachricht dass er abgemeldet wurde.
				System.out.println("- Client abgemeldet: '" + userName + "'");
				send("'" + userName + "' verlässt den Chat");
			}
		} 
		catch (MalformedURLException | NotBoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void send(String message) throws RemoteException {
		//Synchronisiere die clients(lock) damit keiner beim senden den chat betritt
		synchronized (clients) {
			//Rufe für jeden angemeldeten client...
			for (ClientService loggedOnClient : clients)
			{
				//... die send methode auf
				loggedOnClient.send(message);
			}
		}
	}

	@Override
	public java.util.List<String> getUserList() throws RemoteException {
		List<String> userList = new ArrayList<String>();
		//Für alle angemeldeten user...
		for (ClientService loggedOnClient : clients) 
		{
			//... füge diesen user in die liste ein...
			userList.add(loggedOnClient.getName());
		}
		//... und gib sie zurück
		return userList;
	}

	public static void main(String[] args) {
		try 
		{ 
			//Erstelle die registry
			LocateRegistry.createRegistry(1099);
			ChatService server = new ChatServer();
			//und melde den server an
			Naming.rebind("//localhost/server", server);
		} 
		catch (RemoteException e)
		{
			e.printStackTrace();
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
	}
}