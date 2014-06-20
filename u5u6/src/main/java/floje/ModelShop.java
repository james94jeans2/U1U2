package floje;

import java.util.Iterator;
import java.util.Observable;

import fpt.com.Product;
import listener.AddEvent;
import listener.DeleteEvent;

public class ModelShop extends Observable implements fpt.com.ProductList{
	

	private static final long serialVersionUID = -9111970668561441955L;
	private floje.ProductList products = new floje.ProductList();
	

	public ModelShop () {
		super();
		
		
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
	
}
