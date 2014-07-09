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
	private boolean stop;
	private In in;

	public Out (OutputStream out, Socket socket, In in) {
		this.socket = socket;
		work = new CopyOnWriteArrayList<Pair<String,Order>>();
		this.out = out;
		stop = false;
		this.in = in;
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
			while (socket.isConnected() && !stop) {
					synchronized (work) {
						if (!work.isEmpty()) {
							Pair<String, Order> todo = work.get(0);
							
							
							try {
								oos.writeObject(todo.getKey());
								oos.flush();
								int ret = 0;
								System.out.println("waiting for response");
								while ((ret = in.bla()) == 0 && !stop) {
								}
								System.out.println("got response");
								if (ret == -1) {
									ViewCostumer.getInstance().showError();
									work.remove(todo);
									continue;
								} else {
									oos.writeObject(todo.getValue());
									oos.flush();
									oos.reset();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							work.remove(todo);
							
						}
					}
			}
			try {
				oos.writeObject(-1);
			} catch (IOException e1) {
			}
			try {
				oos.close();
			} catch (IOException e) {
			}
		} else {
			System.out.println("Outputstream == null");
		}
	}
	
	public void stop () {
		synchronized ((Object)stop) {
			stop = true;
		}
	}

}
