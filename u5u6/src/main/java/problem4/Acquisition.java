package problem4;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

public class Acquisition implements Runnable {

	//Die Liste mit unseren Kassen und den zugehörigen Threads
	private ArrayList<Cashpoint> cashpoints;
	private ArrayList<Thread> threads;
	//Zähler für alle Kunden
	private int customerCount;
	//die Bilanz, die alle kassen verwaltet
	private Balance bilanz;

	//Der Konstruktor für die Akquise
	public Acquisition () {
		//Es wird eine neue Bilanz erstellt die sich nachher alle Kassenobjekte teilen
		bilanz = new Balance();
		//Die Liste der Kassen wird initialisiert
		cashpoints = new ArrayList<Cashpoint>();
		threads = new ArrayList<Thread>();
		//Die Liste wird mit Kassenobjekten gefüllt
		for(int i = 0;i<6;i++)
		{
			cashpoints.add(new Cashpoint(i+1, bilanz));
		}
	}

	@Override
	public void run() {
		System.out.println("Kunden Akquise gestartet!");
		customerCount = 0;
		//Solange die längste Schlange weniger als 8 Kunden hat wird akquiriert
		while (getLongestQueueLength() < 8) {
			//Variable für die Arbeitszeit der Akquise
			long timeout;
			timeout = RandomUtils.nextLong(0, 2001);
			try 
			{
				Thread.sleep(timeout);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			//Die Gesamtzahl der Kunden wird erhöht
			++customerCount;
			//Es wird nach einer Warteschlange für die Kunden gesucht
			findQueueFor(customerCount);
		}
		//Kommen wir hier an haben wir irgendwo 8 Kunden anstehen und beenden die Akquise
		//und damit endet der Thread
		System.out.println("Kunden Aquise abgeschlossen!");
		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private int getLongestQueueLength () {
		//Initialisierung für die maximale Länge der Warteschlangen
		int maxLength = 0;
		//Hier wird eine Schleife für jede Kasse aufgerufen
		for (Cashpoint cash : cashpoints) {
			//Wir testen, ob die Warteschlangenlänge größer ist als das aktuelle Maximum...
			if (cash.getQueueLength() > maxLength)
			{
				//...und setzen es in dem Fall neu
				maxLength = cash.getQueueLength();
			}
		}
		return maxLength;
	}

	private void findQueueFor (int id) {
		//Es wird eine Kasse gesucht die die kleinste Warteschlange hat
		Cashpoint cash = lowestQueue();
		//Sollte diese Kasse "null" sein...
		if (cash == null)
		{
			//... öffnen wir eine neue Kasse (die Erste!)
			openCash(cashpoints.get(0));
			//An dieser Kasse wird dann ein Kunde eingereiht
			cashpoints.get(0).addCustomer(id);
			//die Methode wird verlassen da wir einen Kunden eingereiht haben
			return;
		}
		//Falls die Kasse nicht "null" war, schauen wir, ob die Warteschlanger der
		//zurückgegebenen Kasse kleiner als 6 ist...
		if (cash.getQueueLength() < 6)
		{
			//..sollte das der Fall sein reihen wir hier den neuen Kunden ein
			cash.addCustomer(id);
			//die Methode wird dann verlassen weil wir nichts weiter tun müssen
			return;
		}
		//... sollte allerdings die Schlange schon 6 Kunden erreicht haben...
		else
		{
			//... bilden wir eine neue Liste von Kassen
			ArrayList<Cashpoint> open = new ArrayList<Cashpoint>();
			//Eine Schleife für alle Kassen unserer Kassenliste wird aufgerufen
			for (Cashpoint cashp : cashpoints)
			{
				//Falls die Kasse offen ist...
				if (cashp.open())
				{
					//...wird sie natürlich in unsere neue Liste der offenen Kassen gelegt
					open.add(cashp);
				}
			}
			//Dann erstellen wir eine Kasse die die letzte Kasse angibt
			Cashpoint last = open.get(open.size() - 1);
			//Wenn diese eine Id kleiner als 6 hat...
			if (last.getId() < 6)
			{
				//...öffnen wir eine neue Kasse weil wir es diesmal noch nicht geschafft 
				//haben unseren neuen Kunden einzureihen
				openCash(cashpoints.get(last.getId()));
				//Dieser neu geöffneten Kasse übergeben wir dann unseren Kunden
				cashpoints.get(last.getId()).addCustomer(id);
				//Die Methode wird hier verlassen da der Kunde gut untergebracht wurde
				return;
			}
			//Wenn schon 6 kassen auf sind, wird nochmal nach der kürzesten schlange
			//gesucht und der kunde dann dort hinzugefügt
			cash = lowestQueue();
			cash.addCustomer(id);
		}
	}

	private Cashpoint lowestQueue () {
		//Wir setzen einen Wert für die Id der Kasse mit der kleinsten Schlange
		int lowestId = 0;
		//Als Minimum setzen wir erst einmal den maximalwert für Integer, da wir
		//ihn auf jeden Fall noch nach unten verändern sobald wir eine Kasse 
		//finden die eine kleinere Id hat
		int min = Integer.MAX_VALUE;
		//Eine Schleife für jede Kasse in unserer Kassenliste wird aufgerufen
		for (Cashpoint cash : cashpoints)
		{
			//Wenn die Kasse offen ist und ihre Warteschlange kleiner als das minimum ist...
			if (cash.open() && cash.getQueueLength() < min)
			{
				//... setze ein neues Minimum mit eben dieser Warteschlange
				min = cash.getQueueLength();
				//und setze auch die Id dieser Kasse
				lowestId = cash.getId();
			}
		}
		//Wenn die Id der Kasse mit der niedrigsten Warteschlange nicht "0" ist...
		if (lowestId != 0) {
			//Gib die Kasse mit dieser Id zurück (Das "-1" weil wir von der
			//Id auf den index in der Liste abbilden müssen
			return cashpoints.get(lowestId-1);
		}
		else
		{
			//... ansonsten gibt "null" zurück, heißt es gibt keine Kasse 
			//mit einer kleineren Warteschlange als die erste
			return null;
		}
	}

	private void openCash (Cashpoint cash) {
		//Es wird ein Thread mit der zu öffnenden Kasse erstellt
		Thread thread = new Thread(cash);
		//Dann wird der Name gesetzt + Id
		thread.setName("Kasse " + cash.getId());
		threads.add(thread);
		//Der Thread wird gestartet
		//start() != run() -> keine Nebenläufigkeit durch run()!
		thread.start();
	}
	
	//für statistische zwecke
	public int getCustomerCount () {
		return customerCount;
	}
	
	//für statistische zwecke
	public Balance getBalance () {
		return bilanz;
	}
	
}