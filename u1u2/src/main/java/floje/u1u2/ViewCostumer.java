package floje.u1u2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;



public class ViewCostumer extends JFrame implements Observer{
	
	private static final int width = 800, height = 400;
	private JPanel rightSide = new JPanel();
	private JPanel leftSide = new JPanel();
	private JTable productTable;
	private Object[][] data;
	private  JScrollPane scrollPane;
	private JPanel buttonPanel;
	
	public ViewCostumer(fpt.com.Product[] product){
		super("PC-Hardware Shop Costumer");
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(600, 275));
		//this.setMaximumSize(new Dimension(width, height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		leftSide.setLayout(new FlowLayout());
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		rightSide.setLayout(new BorderLayout());;
		
		String[] columnNames = {"Name",
                "Preis",
                "MaxCount",
                "OrderCount"};
		data=new Object[product.length][4];
		
//		String[][] data = {
//				{"name1", "price1", "maxcount1", "ordercount1"},
//				{"name2", "price2", "maxcount2", "ordercount1"},
//				{"name3", "price3", "maxcount3", "ordercount1"},
//				
//		};
		
		for(int i=0;i<product.length;i++){
			
			
			data[i][0]=product[i].getName();
			data[i][1]=product[i].getPrice();
			data[i][2]=product[i].getQuantity();	
			data[i][3]=0;
			
		}
		
		productTable=new JTable(data,columnNames);
		
		scrollPane = new JScrollPane(productTable);
		
		productTable.setFillsViewportHeight(true);
		//productTable.setEditingColumn(4);
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(rightSide.getWidth() * 0.6),70));
		productTable.setPreferredSize(new Dimension(((int)(rightSide.getWidth() * 0.6)),(rightSide.getHeight()-70)));
		scrollPane.setPreferredSize(new Dimension(rightSide.getWidth(),rightSide.getHeight()-70));
		buttonPanel.setLayout(new BorderLayout());
		
		JButton ok = new JButton("ok");
		ok.setPreferredSize(new Dimension(100,70));
		
		
		buttonPanel.add(ok, BorderLayout.WEST);
		
		
		
		
		rightSide.add(scrollPane, BorderLayout.CENTER);
		rightSide.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(leftSide);
		this.add(rightSide);
		
		this.pack();
		
		this.setVisible(true);
		

		
	}

	@Override
	public void update(Observable o, Object arg) {
//		fpt.com.Product[] product = ((ModelShop)arg).toArray();
//		data=new Object[((ModelShop)arg).size()][4];
//		for(int i=0;i<((ModelShop)arg).size();i++){
//			
//			
//			data[i][0]=product[i].getName();
//			data[i][1]=product[i].getPrice();
//			data[i][2]=product[i].getQuantity();							
//			
//		}
		
	}
	
	public void paint (Graphics g) {
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
//		buttonPanel.setPreferredSize(new Dimension((int)(rightSide.getWidth() * 0.6),70));
//		productTable.setPreferredSize(new Dimension(((int)(rightSide.getWidth() * 0.6)),(rightSide.getHeight()-70)));
//		scrollPane.setPreferredSize(new Dimension(rightSide.getWidth(),rightSide.getHeight()-70));
		this.pack();
		super.paint(g);
	}

}
