package floje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class In implements Runnable {

	private Socket socket;
	private ModelShop shop;
	
	public In (Socket socket, ModelShop shop) {
		this.socket = socket;
		this.shop = shop;
	}
	
	@Override
	public void run() {
		ObjectInputStream ois = null;
		synchronized (socket) {
		try {
			System.out.println("Test");
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("ois initialized");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if (ois != null) {
			while (socket.isConnected()) {
				
					try {
						Object order = ois.readObject();
						if (order instanceof Order) {
							shop.addOrder((Order) order);
						}
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
						break;
					}
			}
		}else {
			System.out.println("No object inputstream");
		}
	}
	}

