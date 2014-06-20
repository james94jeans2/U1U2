package listener;

import java.util.EventObject;

import floje.Product;

public class AddEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Product product;

	public AddEvent(Object source, Product product) {
		super(source);
		this.product=product;
	}

	//Methode zur R�ckgabe des Produktes aus dem Event
	public Product getProduct()
	{
		return this.product;
	}
}
