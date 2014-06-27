package floje;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
	DefaultTableModel modelt;
	private String[] columnNames = {"Name",
            "Preis",
            "MaxCount",
            "OrderCount"};
	private DatagramSocket datagramm;
	private final JLabel date;
	private JButton ok;
	
	
	
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
		buttonPanel.add(date);
		
		
		
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
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
		productTable.setModel(consModel());	
		
		System.out.println("update");
		
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
		Timer timer = new Timer(1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					datagramm.send(packet);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}			
			
		});
		timer.setRepeats(true);
		timer.start();
	}

	private void abrufenDatum() throws SocketException{
		System.out.println("Abfrage");
		System.out.println(""+datagramm.getPort());
		Timer timer = new Timer(1000, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				byte answer[] = new byte[1024];
				DatagramPacket inpack = new DatagramPacket(answer, answer.length);
				try {
					datagramm.receive(inpack);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				updateDate(new String(inpack.getData()));
				
			}			
			
		});
		timer.setRepeats(true);
		timer.start();
	}
	
	private void updateDate(final String txt){
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      date.setText(txt);;
		    }
		  });
	}
	
	public JTable getTable(){
		return productTable;
	}
	
	public void addOKListener(ActionListener al){
		ok.addActionListener(al);;
	}

	
}
