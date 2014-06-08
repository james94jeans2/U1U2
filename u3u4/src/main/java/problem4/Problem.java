package problem4;

public class Problem {

	public static void main(String[] args) {
		//Es wird ein Thread aus einem Acquisition Objekt erstellt
		Thread thread = new Thread(new Acquisition());
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
	}

}
