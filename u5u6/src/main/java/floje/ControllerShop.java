package floje;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JMenuItem;

import listener.*;

import com.thoughtworks.xstream.io.StreamException;

import database.JDBCConnector;
import database.OpenJPAConnector;
import fpt.com.SerializableStrategy;
import strategies.BinaryStrategy;
import strategies.JDBCStrategy;
import strategies.OpenJpaStrategy;
import strategies.XMLStrategy;
import strategies.XStreamStrategy;

public class ControllerShop implements ActionListener, AddListener, DeleteListener{
	private ModelShop mShop;
	private ViewShop vShop;
	private ViewCostumer vClient;
	private BinaryStrategy binstrat = new BinaryStrategy();
	private XMLStrategy xmlstrat = new XMLStrategy();
	private XStreamStrategy xstreamstrat = new XStreamStrategy();
	private JDBCStrategy jdbcstrat = new JDBCStrategy(new JDBCConnector());
	private OpenJpaStrategy openstrat = new OpenJpaStrategy(new OpenJPAConnector());
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
	
	public void link(ModelShop mShop, ViewCostumer vCostumer){
		this.mShop = mShop;
		this.mShop.addObserver(vCostumer);

		this.vClient = vCostumer;
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
		mShop.add(product);
		vShop.activateSaveMenu();
	}

	public void initializeDatabase()
	{
		//Je nach Strategie eine Verbindung aufbauen
		if(baseStrat.equals(jdbcstrat))
		{
			try 
			{
				jdbcstrat.getConnector().connect();
			}
			catch (SQLException e)
			{
				vShop.showError(e.getMessage());
				return;
			}
		}
		if(baseStrat.equals(openstrat))
		{
			openstrat.getConnector().initialize();
		}
	}

	public void save()
	{
		//Ruft für jedes Produkt die writeObject Methode zum Serialisieren auf
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
			//Danach wird die Strategie geschlossen
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
			//Durch dieses catch brechen wir ab falls keine Produkte mehr gelesen werden können
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
		try
		{
			//Strategie wird geschlossen
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
			baseStrat = openstrat;
			vShop.activateLoadSaveMenu();
			System.out.println("OpenJPA Serialization");
			break;

		case "JDBC Serialization":
			baseStrat = jdbcstrat;
			vShop.activateLoadSaveMenu();
			System.out.println("JDBC Serialization");
			break;

		case "Load":
			initializeDatabase();
			load();
			break;

		case "Save":
			initializeDatabase();
			save();
			break;

		default:
			break;
		}
	}
}