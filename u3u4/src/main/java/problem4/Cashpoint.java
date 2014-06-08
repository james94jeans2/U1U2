package problem4;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.RandomUtils;

public class Cashpoint implements Runnable {

	//Hier haben wir Variablen für die id der Kasse, ob sie geöffnet ist die 
	//Warteschlange und die geteilte Bilanz
	private int id;
	private boolean open;
	private CopyOnWriteArrayList<String> queue;
	private Balance balance;

	public Cashpoint (int id, Balance balance) {

		//Wir übernehmen die übergebene id
		this.id = id;
		//Wir initialisieren die Warteschlange
		queue = new CopyOnWriteArrayList<String>();
		//Zunächst ist die Kasse noch geschlossen
		open = false;
		//Hier übernehmen wir unserere geteilte Bilanz
		this.balance = balance;
	}

	@Override
	public void run() {
		//Hier wird die Kasse geöffnet also open = true
		open = true;
		System.out.println("Kasse " + id + " wird geöffnet!");
		try 
		{
			Thread.sleep(6000);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		System.out.println("Kasse " + id + " ist jetzt geöffnet!");
		//Schleife solange die Warteschlange nicht leer ist
		while (!queue.isEmpty()) {
			//Variable für die Arbeitszeit der Kasse
			long timeout;
			timeout =  RandomUtils.nextLong(6000, 10001);
			try 
			{
				Thread.sleep(timeout);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Variable für die Zahlung des Kunden
			double amountPaid = 0;
			//Sie wird damit nicht alles noch komplizierter wird einfach
			//randomisiert erzeugt
			amountPaid = RandomUtils.nextDouble(1, 1000);
			//Dann wird die Zahlung über die Methode der Bilanz verrechnet
			balance.addToBilanz(id, amountPaid);
			System.out.println("Kasse " + id + " hat " + queue.get(0) + " abgearbeitet");
			//Der Kunde wurde abgearbeitet und wird nun aus der Warteschlange genommen
			queue.remove(0);
			System.out.println("Bei Kasse " + id + " stehen noch " + queue.size() + " Kunden an.");
		}
		//Tritt das Programm aus der Schleife aus ist die Warteschlange leer und die Kasse damit
		//fertig und sie wird geshlossen open=false;
		open = false;
	}

	public int getQueueLength () {
		return queue.size();
	}

	public void addCustomer (int id) {
		//Es wird ein Kunde in die Warteschlange eingefügt
		queue.add("Kunde " + id);
		System.out.println("Kasse " + this.id + " hat neuen Kunden erhalten: Kunde " + id);
	}

	public boolean open () {
		return open;
	}

	public int getId () {
		return id;
	}
}