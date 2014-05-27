package floje.u3u4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JMenuItem;

import listener.*;

import com.thoughtworks.xstream.io.StreamException;

import database.JDBCConnector;
import fpt.com.SerializableStrategy;
import strategies.BinaryStrategy;
import strategies.JDBCStrategy;
import strategies.XMLStrategy;
import strategies.XStreamStrategy;

public class ControllerShop implements ActionListener, AddListener, DeleteListener{
	private ModelShop mShop;
	private ViewShop vShop;
	private BinaryStrategy binstrat = new BinaryStrategy();
	private XMLStrategy xmlstrat = new XMLStrategy();
	private XStreamStrategy xstreamstrat = new XStreamStrategy();
	private JDBCStrategy jdbcstrat = new JDBCStrategy(new JDBCConnector());
	private SerializableStrategy baseStrat;
	//Hier werden Model und View verkn�pft und den Buttons werden
	//Add- und DeleteListener hinzugef�gt
	public void link(ModelShop mShop, ViewShop vShop){
		this.mShop = mShop;
		this.mShop.addObserver(vShop);

		this.vShop = vShop;
		this.vShop.addAddListener(this);
		this.vShop.addDeleteListener(this);
		this.vShop.addActionListener(this);
	}

	//Hier wird die deletePerformed Methode implementiert welche das Interface 
	//DeleteListener fordert
	@Override
	public void deletePerfomed(Product[] product) {
		for(Product p:product)
		{
			mShop.delete(p);
		}

	}

	//Hier wird die addPerformed Methode implementiert welche das Interface
	//AddListener fordert
	@Override
	public void addPerfomed(Product product) {
		try
		{
			IDGenerator.generateIDForProduct(mShop, product);
		}
		catch(Exception e)
		{
			vShop.showError(e.getMessage());
		}
		mShop.add(product);
	}

	public void save()
	{
		for(fpt.com.Product saveProduct : mShop)
		{
			try
			{
				baseStrat.writeObject(saveProduct);
			}
			catch(IOException ex)
			{
				vShop.showError(ex.getMessage());
			}
		}
		try
		{
			baseStrat.close();
		}
		catch(IOException ex)
		{
			vShop.showError(ex.getMessage());
		}
	}

	public void load()
	{
		Product loadProduct;
		do {
			loadProduct=null;
			try
			{
				loadProduct = (Product)baseStrat.readObject();
				if(loadProduct != null)
				{
					mShop.add(loadProduct);
				}
			}
			catch(IOException | ArrayIndexOutOfBoundsException | StreamException ex)
			{
				if(ex.getClass().equals(EOFException.class)||
						ex.getClass().equals(ArrayIndexOutOfBoundsException.class)||
						ex.getClass().equals(StreamException.class))
				{
					break;
				}
				else
				{
					vShop.showError(ex.getMessage());
				}
			}				
		} while(true);
		//while (loadProduct != null);
		try
		{
			baseStrat.close();
		}
		catch(IOException ex)
		{
			vShop.showError(ex.getMessage());
		}
	}

	//Implementierung der actionPerformed Methode für den ActionListener
	//Diese Funktion ist für die Items der MenuBar zuständig
	@Override
	public void actionPerformed(ActionEvent event) {
		switch (((JMenuItem)event.getSource()).getText()) {
		case "None":
			baseStrat = null;
			vShop.deactivateLoadSaveMenu();
			System.out.println("None");
			break;

		case "Binary":
			baseStrat = binstrat;
			vShop.activateLoadSaveMenu();
			System.out.println("Binary");
			break;

		case "Beans":
			baseStrat = xmlstrat;
			vShop.activateLoadSaveMenu();
			System.out.println("Beans");
			break;

		case "XStream":
			baseStrat = xstreamstrat;
			vShop.activateLoadSaveMenu();
			System.out.println("XStream");
			break;
			
		case "OpenJPA Serialization":
			//TODO setStrategie
			vShop.activateLoadSaveMenu();
			System.out.println("OpenJPA Serialization");
			
		case "JDBC Serialization":
			baseStrat = jdbcstrat;
			try {
				jdbcstrat.getConnector().connect();
				vShop.activateLoadSaveMenu();
				System.out.println("JDBC Serialization");
			} catch (SQLException e) {
				vShop.showError(e.getMessage());
			}
			break;

		case "Load":
			load();
			vShop.deactivateLoadSaveMenu();
			break;

		case "Save":
			save();
			vShop.deactivateLoadSaveMenu();
			break;

		default:
			break;
		}
	}
}