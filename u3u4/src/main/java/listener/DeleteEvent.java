package listener;

import java.util.EventObject;

import floje.Product;

public class DeleteEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Product product;

	public DeleteEvent(Object source, Product product) {
		super(source);
		this.product=product;
	}

	//Methode zur R�ckgabe des Produktes aus dem Event
	public Product getProduct()
	{
		return this.product;
	}
}
