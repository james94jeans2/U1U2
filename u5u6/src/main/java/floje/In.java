package floje;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class In implements Runnable {

	private Socket socket;
	private ModelShop shop;
	private boolean stop, changed, returnValue;
	private ObjectInputStream ois;
	
	public In (Socket socket, ModelShop shop) {
		this.socket = socket;
		this.shop = shop;
		stop = false;
		changed = false;
		returnValue = false;
	}
	
	@Override
	public void run() {
		ois = null;
		synchronized (socket) {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("ois initialized");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if (ois != null) {
			while (socket.isConnected() && !stop) {
				
					try {
						Object order = ois.readObject();
						if (order instanceof Order) {
							shop.addOrder((Order) order);
						}
						if (order instanceof Boolean) {
							returnValue = (Boolean)order;
							changed = true;
						}
					}catch (SocketException e) {
					}catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
						break;
					}
			}
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("No object inputstream");
		}
	}
	
	public void stop() {
		synchronized ((Object)stop) {
			stop = true;
		}
		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int bla () {
		synchronized ((Object)changed) {
			if (changed) {
				changed = false;
				return returnValue ? 1 : -1;
			}
		}
		return 0;
	}
	
}

