package chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


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
		//Server wird aus der registry gesucht via Naming.lookup
		server = (ChatService) Naming.lookup("//localhost/server");
		//Der client wird an der registry angemeldet
		//Unterschied rebind<->bind = rebind bindet neu falls schon ein
		//objekt verknüpft wurde, bind nicht
		Naming.rebind(userName, this);
		//der client wird am server angemeldet
		server.login(this.userName);
	}

	//Methode zum setzen der zuständigen view
	public void setView (ViewChat cView) {
		view = cView;
	}

	//Methode zum abmelden
	public void logout() {
		try {
			//Es wird versucht sich vom server abzumelden
			server.logout(this.userName);
			//Dann wird das binding aufgehoben
			Naming.unbind(userName);
		} 
		catch (RemoteException | MalformedURLException | NotBoundException e1)
		{
			e1.printStackTrace();
		}
		//Server für diesen client wird auf null gesetzt weil wir uns abgemeldet haben
		server = null;
	}

	//Senden-Methode
	@Override
	public void send(String message) throws RemoteException {
		//Wenn die nachricht das betreten oder austreten signalisiert...
		if (message.contains("betritt den Chat") || message.contains("verlässt den Chat"))
		{
			//...message aktualisiere die userist
			updateUsers(server.getUserList());
		}
		//zeige die nachricht an
		displayMessage(message);
	}

	//Methode zum updaten der user
	private void updateUsers (final List<String> list) {
		//Wenn die view null ist...
		if (view == null) 
		{
			//...erstelle einen Thread...
			Thread t = new Thread(new Runnable() 
			{
				@Override
				public void run() {
					//... der solange wartet, bis eine view existiert, dies soll verhindern
					//das der client hängt oder nicht aktualisiert wenn es noch keine
					//view zum anzeigen gibt
					while (view == null) 
					{
						try 
						{
							Thread.sleep(2000);
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					}
					//Wenn die view dann bereit ist aktualisiere die userlist
					view.updateUsers(list);
				}
			});
			t.start();
		} else 
		{
			//sollte es schon eine view geben können wir ohne thread direkt aktualisieren
			view.updateUsers(list);
		}
	}

	//Methode zum anzeigen der Nachrichten
	private void displayMessage (final String message) {
		//hier zum anzeigen das selbe Prinzip wie beim updaten um ein einfrieren des
		//clients zu verhindern
		if (view == null)
		{
			Thread t = new Thread(new Runnable() 
			{
				@Override
				public void run() {
					while (view == null) 
					{
						try 
						{
							Thread.sleep(2000);
						} 
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					}
					view.addChatMessage(message);
				}
			});
			t.start();
		} else 
		{
			view.addChatMessage(message);
		}
	}

	@Override
	public String getName() throws RemoteException {
		return this.userName;
	}

}
