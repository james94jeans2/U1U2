package problem4;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.RandomUtils;

public class Cashpoint implements Runnable {
	
	private int id;
	private boolean open;
	private CopyOnWriteArrayList<String> queue;
	private Balance balance;

	public Cashpoint (int pId, Balance b) {
		id = pId;
		queue = new CopyOnWriteArrayList<String>();
		open = false;
		balance = b;
	}
	
	@Override
	public void run() {
		open = true;
		System.out.println("Kasse " + id + " wird geöffnet!");
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("Kasse " + id + " ist jetzt geöffnet!");
		while (!queue.isEmpty()) {
			long timeout;
			timeout =  RandomUtils.nextLong(6000, 10001);
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			double amountPait = 0;
			amountPait = RandomUtils.nextDouble(1, 1000);
			balance.addToBilanz(id, amountPait);
			System.out.println("Kasse " + id + " hat " + queue.get(0) + " abgearbeitet");
			queue.remove(0);
			System.out.println("Bei Kasse " + id + " stehen noch " + queue.size() + " Kunden an.");
		}
		open = false;
	}
	
	public int getQueueLength () {
		return queue.size();
	}
	
	public void addCustomer (int pid) {
		queue.add("Kunde " + pid);
		System.out.println("Kasse " + id + " hat neuen Kunden erhalten: Kunde " + pid);
	}
	
	public boolean open () {
		return open;
	}
	
	public int getId () {
		return id;
	}

}
