package floje;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ViewCostumer extends JFrame implements Observer{
	
	private static final long serialVersionUID = -5399845903830908290L;
	private static final int width = 800, height = 400;
	private JPanel rightSide = new JPanel();
	private JScrollPane leftSide;
	private JTable productTable;
	private Object[][] data;
	private  JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JList<fpt.com.Order> orderList; 
	private ModelShop model;
	
	public ViewCostumer(ModelShop model){
		super("PC-Hardware Shop Costumer");
		this.model = model;
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
		int screenWidth, screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setLocation(((screenWidth - width) / 2), ((screenHeight - height) / 2)+height);
				
		orderList = new JList<fpt.com.Order>();
		
		orderList.setCellRenderer(new ListOrderRenderer());
		leftSide = new JScrollPane(orderList);		
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		rightSide.setLayout(new BorderLayout());;
		
		String[] columnNames = {"Name",
                "Preis",
                "MaxCount",
                "OrderCount"};

		
		data=new Object[0][0];

		
		productTable=new JTable(data,columnNames){
			private static final long serialVersionUID = -4503641078531367197L;

			public boolean isCellEditable(int x, int y) {
				if(y==3){
		        	return true;
		        }
				return false;
            }
			
			public Class<?> getColumnClass(int columnCount){
				if (columnCount==3){
					return Integer.class;
				}
				return String.class;
				
			}
        };
		
		
		scrollPane = new JScrollPane(productTable);
		
		productTable.setFillsViewportHeight(true);
		//productTable.setEditingColumn(4);
		
		buttonPanel = new JPanel();
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
		fpt.com.Product[] product = model.toArray();
		data=new Object[model.size()][4];
		for(int i=0;i<product.length;i++){
			
			
			data[i][0]=product[i].getName();
			data[i][1]=product[i].getPrice();
			data[i][2]=product[i].getQuantity();							
			
		}
		productTable.repaint();
	}
	
	public void paint (Graphics g) {
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		super.paint(g);
	}
	


}
