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
	private JScrollPane leftSide;
	private JTable productTable;
	private Object[][] data;
	private  JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JList<fpt.com.Order> orderList; 
	
	public ViewCostumer(){
		super("PC-Hardware Shop Costumer");
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
		int screenWidth, screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setLocation(((screenWidth - width) / 2), ((screenHeight - height) / 2)+height);
		Order order=new Order();
		for(int j =0;j<2;j++){
			Product product = new Product();
			product.setPrice(5);
			product.setQuantity(4);
			product.setName(""+j);
			order.add(product);
			
		}
				
		orderList = new JList<fpt.com.Order>();
		
		orderList.setCellRenderer(new ListOrderRenderer());
		orderList.setListData(new Order[]{order});
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
			public boolean isCellEditable(int x, int y) {
				if(y==3){
		        	return true;
		        }
				return false;
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
		super.paint(g);
	}
	


}
