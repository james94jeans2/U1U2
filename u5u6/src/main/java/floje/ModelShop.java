package floje;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

import listener.AddEvent;
import listener.DeleteEvent;
import fpt.com.Product;

public class ModelShop extends Observable implements fpt.com.ProductList{
	

	private static final long serialVersionUID = -9111970668561441955L;
	private floje.ProductList products = new floje.ProductList();
	private CopyOnWriteArrayList<Order> orders;
	private In in;
	private Out out;
	private Thread tIn, tOut;
	private Socket socket;

	public ModelShop () {
		super();
		orders = new CopyOnWriteArrayList<Order>();
		try {
			socket = new Socket(InetAddress.getByName("localhost"), 6666);
			in = new In(socket, this);
			out = new Out(socket.getOutputStream(), socket, in);
			tIn = new Thread(in);
			tOut = new Thread(out);
			tIn.start();
			tOut.start();
		} catch (Exception e) {
			System.out.println("Couldn't connect to server!");
		}
	}
	
	public void closeConnnections () {
		if (in == null || out == null) {
			return;
		}
		out.stop();
		if (tOut.isAlive()) {
			try {
				tOut.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		in.stop();
		if (tIn.isAlive()) {
			try {
				tIn.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addOrder (Order order) {
		orders.add(order);
		super.setChanged();
		super.notifyObservers(null);
	}
	
	@Override
	public Iterator<Product> iterator() {
		return products.iterator();
	}
	
	public CopyOnWriteArrayList<Order> getOrders () {
		return orders;
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
