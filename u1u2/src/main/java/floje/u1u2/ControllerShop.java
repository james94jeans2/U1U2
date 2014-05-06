package floje.u1u2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControllerShop implements ActionListener {

	private ViewShop view;
	private ModelShop model;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("add")) {
			fpt.com.Product product;
			if ((product = view.getNewProduct()) != null) {
				try {
					IDGenerator.generateIDForProduct(model, product);
				} catch (Exception e) {
					e.printStackTrace();
				}
				model.add(product);
			}
		} else {
			List<fpt.com.Product> selected = view.getSelected();
			for (fpt.com.Product product : selected) {
				model.delete(product);
				System.out.println("Deleted ID: " + product.getId());
			}
		}
	}
	
	public void link (ModelShop model, ViewShop view) {
		this.model = model;
		this.view = view;
		model.addObserver(view);
		view.addActionListener(this);
	}

}
