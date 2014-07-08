package floje;

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

	public ModelShop () {
		super();
		orders = new CopyOnWriteArrayList<Order>();
		try {
			Socket socket = new Socket(InetAddress.getByName("localhost"), 6666);
			in = new In(socket, this);
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
