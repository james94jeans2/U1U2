package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class ChatClient extends UnicastRemoteObject implements ClientService
{
	private static final long serialVersionUID = 1L;
	
	private String userName;
	//private ArrayList<String> messages; //Gedacht gewesen f�r alle nachrichten aber noch keine idee gehabt
	private ChatService server;
	
	public ChatClient(String userName) throws RemoteException, NotBoundException,
	MalformedURLException
	{
		this.userName = userName;
		//this.messages = new ArrayList<String>();
		
		server = (ChatService) Naming.lookup("//localhost/server");
		Naming.rebind(userName, this);
		try {
			server.login(this.userName);
		} catch (IllegalArgumentException e) {
			logout();
			throw e;
		}
	}
	
	public void logout() throws RemoteException {
		server.logout(this.userName);
		try {
			Naming.unbind(userName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		server = null;
	}
	
	@Override
	public void send(String message) {
		//(new SendMessageWorker(message)).execute();
		ChatGUI.getInstance().receiveMessage(message);
	}

	@Override
	public String getName() {
		return this.userName;
	}
	
	private class SendMessageWorker extends SwingWorker<Void, String> {

		private final String message;

		private SendMessageWorker(String message) {
			this.message = message;
		}

		@Override
		protected Void doInBackground() throws Exception {
			String sendMessage = userName + ": " + message;
			server.send(sendMessage);
			return null;
		}
	}
}
