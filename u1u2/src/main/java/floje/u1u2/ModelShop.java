package floje.u1u2;

import java.util.Iterator;
import java.util.Observable;

import fpt.com.Product;

public class ModelShop extends Observable implements fpt.com.ProductList{
	

	private static final long serialVersionUID = -9111970668561441955L;
	private floje.u1u2.ProductList products = new floje.u1u2.ProductList();
	

	public ModelShop () {
		super();
		
		
	}

	
	private void change(){
		super.setChanged();
		super.notifyObservers();
		
	}

	@Override
	public Iterator<Product> iterator() {
		return products.iterator();
	}

	@Override
	public boolean add(Product e) {
		if( products.add(e)){
			change();
			return true;
		}
		return false;
		
	}

	@Override
	public boolean delete(Product e) {
		if( products.remove(e)){
			change();
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
