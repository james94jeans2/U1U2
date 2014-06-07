package problem4;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;

public class Acquisition implements Runnable {

	private Cashpoint[] cashpoints;
	
	public Acquisition () {
		Balance b = new Balance();
		cashpoints = new Cashpoint[6];
		for (int i = 0; i < 6; ++i) {
			cashpoints[i] = new Cashpoint(i+1, b);
		}
	}
	
	@Override
	public void run() {
		System.out.println("Kunden Aquise gestartet!");
		int customerCount = 0;
		while (getLongestQueueLength() < 8) {
			long timeout;
			timeout = RandomUtils.nextLong(0, 2001);
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			++customerCount;
			findQueueFor(customerCount);
		}
		System.out.println("Kunden Aquise abgeschlossen!");
	}
	
	private int getLongestQueueLength () {
		int maxLength = 0;
		for (Cashpoint p : cashpoints) {
			if (p.getQueueLength() > maxLength) {
				maxLength = p.getQueueLength();
			}
		}
		return maxLength;
	}
	
	private void findQueueFor (int id) {
		Cashpoint cash = lowestQueue();
		if (cash == null) {
			openCash(cashpoints[0]);
			cashpoints[0].addCustomer(id);
			return;
		}
		if (cash.getQueueLength() < 6) {
			cash.addCustomer(id);
			return;
		}else {
			List<Cashpoint> open = new ArrayList<Cashpoint>();
			for (Cashpoint p : cashpoints) {
				if (p.open()) {
					open.add(p);
				}
			}
			Cashpoint last = open.get(open.size() - 1);
			if (last.getId() < 6) {
				openCash(cashpoints[last.getId()]);
				cashpoints[last.getId()].addCustomer(id);
				return;
			}
		}
		cash = lowestQueue();
		cash.addCustomer(id);
	}
	
	private Cashpoint lowestQueue () {
		int lid = 0;
		int lc = Integer.MAX_VALUE;
		for (Cashpoint cp : cashpoints) {
			if (cp.open() && cp.getQueueLength() < lc) {
				lc = cp.getQueueLength();
				lid = cp.getId();
			}
		}
		if (lid != 0) {
			return cashpoints[lid - 1];
		}else {
			return null;
		}
	}
	
	private void openCash (Cashpoint p) {
		Thread thread = new Thread(p);
		thread.setName("Kasse " + p.getId());
		thread.start();
	}

}
