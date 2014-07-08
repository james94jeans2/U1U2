package floje;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;

import fpt.com.Order;

public class ListOrderRenderer implements ListCellRenderer<Order> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Order> list,
			Order value, int index, boolean isSelected, boolean cellHasFocus) {
		String bestellungElemente="Es gibt eine Bestellung mit "+value.getQuantity()+" Produkten mit einem Gesamtwert von: "+value.getSum();
		Box box = Box.createVerticalBox();
		JPanel outer = new JPanel(new BorderLayout());
		outer.setBackground(Color.WHITE);
		JLabel n = new JLabel(bestellungElemente+"€");
		Font f = n.getFont();
		f = f.deriveFont(Font.BOLD, f.getSize() * 1.2f);
		n.setFont(f);
		outer.add(n, BorderLayout.NORTH);
		box.add(Box.createVerticalStrut(2));
		box.add(outer);
		JPanel content = new JPanel(new GridLayout(value.size() + 1, 3));
		content.setBackground(Color.WHITE);
		content.add(new JLabel("Anzahl", JLabel.CENTER));
		content.add(new JLabel("Name", JLabel.CENTER));
		content.add(new JLabel("Preis", JLabel.CENTER));
		JLabel label = new JLabel("random");
		f = label.getFont();
		f = f.deriveFont(Font.ITALIC, f.getSize());
		for (fpt.com.Product p : value) {
			label = new JLabel("" + p.getQuantity(), JLabel.CENTER);
			label.setFont(f);
			content.add(label);
			label = new JLabel(p.getName(), JLabel.CENTER);
			label.setFont(f);
			content.add(label);
			label = new JLabel("" + p.getPrice(), JLabel.CENTER);
			label.setFont(f);
			content.add(label);
		}
		box.add(content);
		box.add(Box.createVerticalStrut(2));
//		Box proList=Box.createHorizontalBox();
//
//		for(fpt.com.Product p:value){
//			proList.add(new JLabel("Anzahl"));
//			proList.add(new JLabel(""+p.getName()));
//			proList.add(new JLabel("("+p.getPrice()+")"));
//		}
//		box.add(proList);
		if (isSelected) {
			box.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		}else {
			box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		return box;
	}



}
