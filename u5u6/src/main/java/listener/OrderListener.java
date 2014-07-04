package listener;

import floje.Order;

public interface OrderListener {

	public void orderPerformend(String login, Order order);
	
}
