package floje;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerCostumer implements ActionListener {

	private ModelShop shop;
	private ViewCostumer view;
	
	public void link (ModelShop model, ViewCostumer costumer) {
		shop = model;
		view = costumer;
		shop.addObserver(costumer);
		view.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Perform login and order
		
	}

}
