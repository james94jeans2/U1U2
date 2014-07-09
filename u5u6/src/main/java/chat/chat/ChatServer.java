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
		synchronized (clients) {
			for (ClientService loggedOnClient : clients)
			{
				if (loggedOnClient.getName().equals(userName))
				{
					throw new IllegalArgumentException("Es wurde bereits ein Client mit diesem Namen"
							+ " angemeldet!");
				}
			}
			try 
			{
				clients.add(new ChatClient(userName));
			} 
			catch (MalformedURLException | NotBoundException e)
			{
				e.printStackTrace();
			}

			System.out.println("Client angemeldet: '" + userName + "'");
			send("'" + userName + "' betritt den Chat");
		}
	}

	@Override
	public void logout(String userName) throws RemoteException{
		try {
			if (clients.remove(new ChatClient(userName))) {
				System.out.println("- Client abgemeldet: '" + userName + "'");
				send("'" + userName + "' verl�sst den Chat");
			}
		} 
		catch (MalformedURLException | NotBoundException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void send(String message) throws RemoteException {
		int i = 0;
		ChatClient client;
		synchronized (clients) {
			for (ClientService loggedOnClient : clients)
			{
				client = (ChatClient)clients.get(i);
				try {
					client.getLink().receiveMessage(client.getName(), message);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public java.util.List<String> getUserList() throws RemoteException {
		List<String> userList = new ArrayList<String>();
		synchronized (clients) {
			for (ClientService loggedOnClient : clients) 
			{
				userList.add(loggedOnClient.getName());
			}
		}
		return userList;
	}

	public static void main(String[] args) {
		try { 
			LocateRegistry.createRegistry(1099);
			ChatService server = new ChatServer();
			Naming.rebind("//localhost/server", server);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}