package floje;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Observable;

import fpt.com.Product;
import listener.AddEvent;
import listener.DeleteEvent;

public class ModelShop extends Observable implements fpt.com.ProductList{
	

	private static final long serialVersionUID = -9111970668561441955L;
	private floje.ProductList products = new floje.ProductList();
	private Runnable in;
	private Out out;

	public ModelShop () {
		super();
		//TODO create Connection
		try {
			final Socket socket = new Socket(InetAddress.getByName("localhost"), 6666);
			final InputStream input = socket.getInputStream();
			final ModelShop shop = this;
			in = new Runnable() {
				@Override
				public void run() {
					ObjectInputStream ois = null;
					synchronized (socket) {
						try {
							ois = new ObjectInputStream(input);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (ois != null) {
						while (socket.isConnected()) {
							synchronized (socket) {
								try {
									Object order = ois.readObject();
									if (order instanceof Order) {
										shop.addOrder((Order) order);
									}
								} catch (ClassNotFoundException | IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			};
			out = new Out(socket.getOutputStream(), socket);
			Thread t1, t2;
			t1 = new Thread(in);
			t2 = new Thread(out);
			t1.start();
			t2.start();
		} catch (Exception e) {
			System.out.println("Couldn't connect to server!");
		}
	}

	public void addOrder (Order order) {
		//TODO
	}
	
	@Override
	public Iterator<Product> iterator() {
		return products.iterator();
	}

	@Override
	public boolean add(Product e) {
		if( products.add(e)){
			super.setChanged();
			super.notifyObservers(new AddEvent(this, (floje.Product)e));
			return true;
		}
		return false;
		
	}

	@Override
	public boolean delete(Product e) {
		if( products.remove(e)){
			super.setChanged();
			super.notifyObservers(new DeleteEvent(this, (floje.Product)e));
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		
		return products.size();
	}

	@Override
	public Product findProductById(long id) {
		return products.findProductById(id);
	}

	@Override
	public Product findProductByName(String name) {
		return products.findProductByName(name);
	}
	
	public Product[] toArray(){
		return  products.toArray(new Product[0]);
	}
	
	public void performOrder (String login, Order order) {
		//TODO redirect to TCP/IP
		if (in != null && out != null) {
			out.sendOrder(login, order);
		}
	}
	
}
