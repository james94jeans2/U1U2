package listener;

import java.util.EventObject;

import floje.Order;

public class OrderEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private Order order;
	
	public OrderEvent(Object source, Order order) {
		super(source);
		this.order = order;
	}
	
}
