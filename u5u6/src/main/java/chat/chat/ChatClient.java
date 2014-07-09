package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ChatClient extends UnicastRemoteObject implements ClientService
{
	private static final long serialVersionUID = 1L;

	private String userName;
	private ChatService server;
	private ClientLink link;


	public ChatClient(String userName) throws RemoteException, NotBoundException,
	MalformedURLException
	{
		this.userName = userName;

		server = (ChatService) Naming.lookup("//localhost/server");

		try {
			server.login(this.userName);
		} catch (IllegalArgumentException e) {
			logout();
			throw e;
		}
	}

	public void logout() throws RemoteException {
		server.logout(this.userName);
		server = null;
	}

	@Override
	public void send(String message) throws RemoteException {
		server.send(message);

	}

	@Override
	public String getName() {
		return this.userName;
	}


	public void setLink(ClientLink link) {
		this.link = link;
	}

	public ClientLink getLink() {
		// TODO Auto-generated method stub
		return link;
	}

}
