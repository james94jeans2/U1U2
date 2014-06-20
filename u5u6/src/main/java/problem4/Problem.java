package problem4;

public class Problem {

	public static void main(String[] args) {
		System.out.println("Simulation gestartet!");
		long startTime = System.currentTimeMillis();
		Acquisition ac = new Acquisition();
		//Es wird ein Thread aus einem Acquisition Objekt erstellt
		Thread thread = new Thread(ac);
		//Dann wird der Name gesetzt
		thread.setName("Kundenaquise");
		//Der Thread wird gestartet
		thread.start();
		try 
		{
			//Hiermit warten wir auf die Fertigstellung der Threads
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		long timeUsed = endTime - startTime;
		System.out.println("");
		System.out.println("");
		System.out.println("Simulation beendet!");
		System.out.println("----------");
		System.out.println("Finale Statistiken:");
		System.out.println("Es wurden " + ac.getCustomerCount() + " Kunden in " + (timeUsed / 1000) + " Sekunden bedient.");
		System.out.println(ac.getBalance());
	}

}
