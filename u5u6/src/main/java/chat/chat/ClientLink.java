package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientLink extends Remote {

	public void receiveMessage (String userName, String message) throws RemoteException;
	
}
