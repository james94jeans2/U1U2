package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientLinkImpl extends UnicastRemoteObject implements ClientLink {
	ChatGUI gui;
	
	protected ClientLinkImpl(ChatGUI gui) throws RemoteException {
		this.gui = gui;
	}

	@Override
	public void receiveMessage(String userName, String message)
			throws RemoteException {
		gui.receiveMessage(userName, message);
		
	}

}
