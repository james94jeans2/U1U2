package floje;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class Out implements Runnable {
	
	private OutputStream out;
	private CopyOnWriteArrayList<Pair<String, Order>> work;

	public Out (OutputStream out) {
		work = new CopyOnWriteArrayList<Pair<String,Order>>();
	}
	
	public void sendOrder (String login, Order order) {
		synchronized (work) {
			work.add(Pair.of(login, order));
		}
	}
	
	@Override
	public void run() {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (oos != null) {
			while (true) {
				synchronized (work) {
					if (!work.isEmpty()) {
						Pair<String, Order> todo = work.get(0);
						try {
							oos.writeObject(todo.getKey());
							oos.flush();
							oos.writeObject(todo.getValue());
							oos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						work.remove(todo);
						
					}
				}
			}
		}
	}

}
