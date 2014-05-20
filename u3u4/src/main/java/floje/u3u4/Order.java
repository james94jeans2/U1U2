package floje.u3u4;

import java.util.ArrayList;

public class Order extends ArrayList<fpt.com.Product> implements fpt.com.Order {

	private static final long serialVersionUID = 7388262443460976053L;
	private int quantity;
	private double sum;
	
	@Override
	public boolean add(fpt.com.Product e) {
		if (super.add(e)) {
			this.quantity += e.getQuantity();
			this.sum += e.getQuantity() * e.getPrice();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean delete(fpt.com.Product p) {
		if (this.remove(p)) {
			this.quantity -= p.getQuantity();
			this.sum -= p.getQuantity() * p.getPrice();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public fpt.com.Product findProductById(long id) {
		for (fpt.com.Product p : this) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	@Override
	public fpt.com.Product findProductByName(String name) {
		for (fpt.com.Product p : this) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	@Override
	public double getSum() {
		return sum;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

}
