package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.swing.SwingUtilities;

public class ChatClient extends UnicastRemoteObject implements ClientService
{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private ChatService server;
	private ViewChat view;
	
	public ChatClient(String userName) throws RemoteException, NotBoundException,
	MalformedURLException
	{
		this.userName = userName;
		server = (ChatService) Naming.lookup("//localhost/server");
		Naming.rebind(userName, this);
		server.login(this.userName);
	}
	
	public void setView (ViewChat cView) {
		view = cView;
	}
	
	public void sendToServer (String message) {
		//TODO send to server
	}
	
	public void logout() {
		try {
			server.logout(this.userName);
			Naming.unbind(userName);
		} catch (RemoteException | MalformedURLException | NotBoundException e1) {
			e1.printStackTrace();
		}
		server = null;
	}
	
	@Override
	public void send(String message) throws RemoteException {
		if (message.contains("betritt den Chat") || message.contains("verlässt den Chat")) {
			updateUsers(server.getUserList());
		}
		displayMessage(message);
	}
	
	private void updateUsers (final List<String> list) {
		if (view == null) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (view == null) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				view.updateUsers(list);
			}
		});
		t.start();
		} else {
			view.updateUsers(list);
		}
	}
	
	private void displayMessage (final String message) {
		if (view == null) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (view == null) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				view.addChatMessage(message);
			}
		});
		t.start();
		} else {
			view.addChatMessage(message);
		}
	}

	@Override
	public String getName() throws RemoteException {
		return this.userName;
	}
	
}
