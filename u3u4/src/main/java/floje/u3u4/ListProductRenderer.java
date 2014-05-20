package floje.u3u4;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.TitledBorder;

import fpt.com.Product;

public class ListProductRenderer implements ListCellRenderer<Product> {

	public Component getListCellRendererComponent(
			JList<? extends Product> list, Product value, int index,
			boolean isSelected, boolean cellHasFocus) {

		String namex = value.getName();
		Box box = Box.createVerticalBox();
		JPanel box1 = new JPanel(new GridLayout(2, 3));
		box1.setBackground(Color.WHITE);
		DecimalFormat df = new DecimalFormat("#.##");
		JLabel p = new JLabel(df.format(value.getPrice()) + "€");
		JLabel q = new JLabel("" + value.getQuantity());
		JLabel pr = new JLabel("Preis:");
		JLabel qu = new JLabel("Anzahl:");
		Font f = p.getFont();
		f = f.deriveFont(Font.ITALIC, f.getSize() * 1.2f);
		p.setFont(f);
		q.setFont(f);
		f = pr.getFont();
		f = f.deriveFont(f.getSize() * 1.4f);
		pr.setFont(f);
		qu.setFont(f);
		box1.add(Box.createHorizontalBox());
		box1.add(pr);
		box1.add(qu);
		box1.add(Box.createHorizontalBox());
		box1.add(p);
		box1.add(q);
		box.add(box1);
		if (isSelected) {
			TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), namex);
			f = f.deriveFont(Font.BOLD, f.getSize() * 1.5f);
			title.setTitleFont(f);
			box.setBorder(title);
		}else {
			TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), namex);
			f = f.deriveFont(Font.BOLD, f.getSize() * 1.5f);
			title.setTitleFont(f);
			box.setBorder(title);
		}
		return box;
	}
}
