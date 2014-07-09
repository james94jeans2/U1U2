package floje;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import listener.AddEvent;
import listener.DeleteEvent;
import listener.OrderListener;

public class ViewCostumer extends JFrame implements Observer{
	
	private static final long serialVersionUID = -5399845903830908290L;
	private static final int width = 800, height = 400;
	private JPanel rightSide = new JPanel(), leftSide;
	private JTable productTable;
	private Object[][] data;
	private  JScrollPane scrollPane;
	private JPanel buttonPanel;
	private JList<Order> orderList; 
	DefaultTableModel modelt;
	private String[] columnNames = {"Name",
            "Preis",
            "MaxCount",
            "OrderCount"};
	private DatagramSocket datagramm;
	private final JLabel date;
	private JButton ok;
	private ProductList products;
	private OrderListener listener;
	private static ViewCostumer instance;
	
	public ViewCostumer(){
		super("PC-Hardware Shop Costumer");
		instance = this;
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.X_AXIS));
		int screenWidth, screenHeight;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
		this.setLocation(((screenWidth - width) / 2), ((screenHeight - height) / 2)+height);
		products = new ProductList();
				
		orderList = new JList<Order>();
		
		orderList.setCellRenderer(new ListOrderRenderer());
		leftSide = new JPanel(new BorderLayout());		
		leftSide.add(new JScrollPane(orderList), BorderLayout.CENTER);
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		rightSide.setLayout(new BorderLayout());;
		

		
		data=new Object[0][4];

		
		productTable=new JTable(consModel());

		
		
		scrollPane = new JScrollPane(productTable);
		
		productTable.setFillsViewportHeight(true);

		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		
		ok = new JButton("ok");
		ok.setPreferredSize(new Dimension(100,70));
		
		
		buttonPanel.add(ok, BorderLayout.WEST);
		date=new JLabel("");
		buttonPanel.add(date, BorderLayout.CENTER);
		
		
		
		
		rightSide.add(scrollPane, BorderLayout.CENTER);
		rightSide.add(buttonPanel, BorderLayout.SOUTH);
		
		this.add(leftSide);
		this.add(rightSide);
		
		this.pack();
		
		this.setVisible(true);
		try {
			datagramm = new DatagramSocket();
		} catch (SocketException e) {
			datagramm = null;
			e.printStackTrace();
		}
		try {
			abfrageDatum();
			abrufenDatum();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		//Wenn das Model eine Änderung mitgeteilt hat, werden nicht vorhande
		//Produkte der Liste hinzugefügt und gelöschte Objekte gegebenenfalls
		//entfernt
		if (arg != null) {
			if(arg.getClass().equals(AddEvent.class))
			{
				products.add(((AddEvent)arg).getProduct());
			}
			else
			{
				if(arg.getClass().equals(DeleteEvent.class))
				{
					products.remove(((DeleteEvent)arg).getProduct());
				}
			}
	
			data=new Object[products.size()][4];
			
			Product[] product = products.toArray(new Product[0]);
			
			for(int i=0;i<product.length;i++){
				
				
				data[i][0]=product[i].getName();
				data[i][1]=product[i].getPrice();
				data[i][2]=product[i].getQuantity();
											
				
			}		
			productTable.setModel(consModel());	
		} else {
			orderList.setListData(((ModelShop) o).getOrders().toArray(new Order[0]));
		}
	}
	
	public void paint (Graphics g) {
		leftSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.4), this.getHeight()));
		rightSide.setPreferredSize(new Dimension((int)(this.getWidth() * 0.6), this.getHeight()));
		super.paint(g);
	}
	
	private DefaultTableModel consModel(){
		modelt = new DefaultTableModel(data,columnNames){
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
		return modelt;
	}

	private void abfrageDatum() throws SocketException{
		InetAddress ia  = null;
		try {
			ia = InetAddress.getByName("localhost");
		} catch (UnknownHostException e2) {
			
			e2.printStackTrace();
		}
		System.out.println("Anfrage");
		final String command = "DATE:";
		final byte buffer[];
		buffer = command.getBytes();
		final DatagramPacket packet = new DatagramPacket(buffer,buffer.length, ia, 6667);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				try {
					datagramm.send(packet);
				} catch (IOException e1) {
					e1.printStackTrace();
					
				}
			}
		}, 0, 1000);
	}

	private void abrufenDatum() throws SocketException{
		System.out.println("Abfrage");
		System.out.println(""+datagramm.getPort());
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				byte answer[] = new byte[1024];
				DatagramPacket inpack = new DatagramPacket(answer, answer.length);
				try {
					datagramm.receive(inpack);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				final String ding = new String(inpack.getData()).trim();
				SwingUtilities.invokeLater(new Runnable() {
				    public void run() {
				      date.setText(ding);
				    }
				  });
				
			}
		}, 0, 1000);
	}
	
	public JTable getTable(){
		return productTable;
	}
	
	public void addActionListener (ActionListener listener) {
		ok.addActionListener(listener);
	}
	
	public void addOrderListener (OrderListener listener) {
		this.listener = listener;
	}
	
	public void performOrder (LoginDialog dialog) {
		String login = "";
		login += dialog.getUsername();
		login += ":";
		login += dialog.getPassword();
		Order order = new Order();
		Vector<Vector<Object>> data = modelt.getDataVector();
		int j;
		for (int i = 0; i < data.size(); ++i) {
			if (((Integer)data.get(i).get(3)) != null) {
				j = (Integer) data.get(i).get(3);
				if (j > 0) {
					Product product = (Product) products.get(i);
					product.setQuantity(j);
					order.add(product);
				}
			}
		}
		listener.orderPerformend(login, order);
	}
	
	public static ViewCostumer getInstance () {
		return instance;
	}
	
	public void showError () {
		JOptionPane.showMessageDialog(this, "Wrong Login Credentials, please try again!", "Authentification Error", JOptionPane.ERROR_MESSAGE);
	}
	
}
