package floje.u1u2;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fpt.com.Order;

public class ListOrderRenderer implements ListCellRenderer<Order> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Order> list,
			Order value, int index, boolean isSelected, boolean cellHasFocus) {
		String bestellungElemente="ES gibt eine Bestellung mit"+value.getQuantity()+"im GesammtPreis von"+value.getSum();
		Box box = Box.createVerticalBox();
		JLabel n = new JLabel(bestellungElemente+"€");
		Font f = n.getFont();
		f = f.deriveFont(Font.ITALIC, f.getSize() * 1.2f);
		box.add(n);
		Box proList=Box.createHorizontalBox();

		for(fpt.com.Product p:value){
			proList.add(new JLabel("Anzahl"));
			proList.add(new JLabel(""+p.getName()));
			proList.add(new JLabel("("+p.getPrice()*3+")"));
		}
		box.add(proList);
		return box;
	}



}
