package floje;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class Out implements Runnable {
	
	private OutputStream out;
	private Socket socket;
	private CopyOnWriteArrayList<Pair<String, Order>> work;

	public Out (OutputStream out, Socket socket) {
		this.socket = socket;
		work = new CopyOnWriteArrayList<Pair<String,Order>>();
		this.out = out;
	}
	
	public void sendOrder (String login, Order order) {
		synchronized (work) {
			work.add(Pair.of(login, order));
		}
	}
	
	@Override
	public void run() {
		ObjectOutputStream oos = null;
		synchronized (socket) {
			try {
				oos = new ObjectOutputStream(out);
				System.out.println("oos initialized");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (oos != null) {
			while (socket.isConnected()) {
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
		} else {
			System.out.println("Outputstream == null");
		}
	}

}
