package floje;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import listener.OrderListener;

public class ControllerCostumer implements ActionListener, OrderListener {

	private ModelShop shop;
	private ViewCostumer view;
	
	//link model und view
	public void link (ModelShop model, ViewCostumer costumer) {
		shop = model;
		view = costumer;
		shop.addObserver(costumer);
		view.addActionListener(this);
		view.addOrderListener(this);
	}
	
	//Actionperformed methode für das login fenster
	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = new LoginDialog(view);
		dialog.setVisible(true);
	}

	//Methode die aufgerufen wird wenn es eine ordergab
	@Override
	public void orderPerformend(String login, Order order) {
		shop.performOrder(login, order);
	}

}
