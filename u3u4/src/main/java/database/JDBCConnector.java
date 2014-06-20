package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import floje.Product;

public class JDBCConnector {
	//Variablen die wir brauchen
	Connection connection = null;
	ResultSet res = null;
	PreparedStatement prep = null;

	public void connect() throws SQLException
	{
		System.out.println("Verbindung wird aufgebaut");
		//Die Connection wird über den DriverManager aufgebaut
		connection = DriverManager.getConnection(
				"jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		System.out.println("Verbindung wurde aufgebaut");
	}

	public void close() throws SQLException
	{
		//Wenn die Connection nicht "null" ist wird sie geschlossen und auf "null" gesetzt

		if(connection!=null)
		{
			connection.close();
			connection = null;
		}
	}

	private void closeUsedData() throws SQLException
	{
		//Unsere Nutzdaten werden geschlossen und auf "null",
		//also ResultSet und Prepared Statement

		if(res!=null)
		{
			res.close();
			res=null;
		}
		if(prep!=null)
		{
			prep.close();
			prep=null;
		}
	}

	public void getURL()throws SQLException
	{
		//Hiermit holen wir uns über die MetaDaten die URL
		System.out.println(connection.getMetaData().getURL().split("//")[1]);
	}

	public void getUserName() throws SQLException
	{
		//Hiermit holen wir uns den UserName über MetaDaten
		System.out.println(connection.getMetaData().getUserName());
	}
	public void getTables() throws SQLException
	{
		//Es wird über die Connection ein ResultSet geliefert
		//Dabei lassen wir uns alle Tabellen anzeigen, die die Property "TABLE" haben
		res = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while(res.next())
		{
			//Der Wert 3 bezeichnet die TableNames
			//muss man mal in der Dokumentation von getTables nachsehen
			System.out.println(res.getString(3));
		}
		//Nutzdaten werden geschlossen
		closeUsedData();
	}

	public long insert(String name, double price, int quantity) throws SQLException
	{
		//Es wird eine SQL Abfrage erstellt
		String stmt2 = "INSERT INTO products (name, price, quantity) VALUES (?,?,?)";
		//Dann wird daraus ein PreparedStatement welches die ID als generatedKey ausgibt
		prep = connection.prepareStatement(
				stmt2, PreparedStatement.RETURN_GENERATED_KEYS);
		//Durch die Setter werden die Parameter in das Statement eingefügt
		prep.setString(1, name);
		prep.setDouble(2, price);
		prep.setInt(3, quantity);
		//Dann wird das Statement ausgeführt
		prep.executeUpdate();
		//Wir bekommen ein ResultSet mit den generatedKeys(In diesem Fall nur die ID)
		res = prep.getGeneratedKeys();
		while(res.next())
		{
			//Lese die ID aus dem ResultSet
			int id = res.getInt(res.getRow());
			//Schliesse die Nutzdaten
			closeUsedData();
			//Gibt die ID Zurück
			return id;
		}
		//Falls das INSERT nicht geklappt hat schliesse die Nutzdaten und gibt -1 zurück
		closeUsedData();
		return -1;
	}

	public void insert(Product product) throws SQLException
	{
		//Fast dasselbe wie oben
		String stmt = "INSERT INTO products (name, price, quantity) VALUES (?,?,?)";
		prep = connection.prepareStatement(stmt);
		prep.setString(1, product.getName());
		prep.setDouble(2, product.getPrice());
		prep.setInt(3, product.getQuantity());
		prep.executeUpdate();
		closeUsedData();
	}

	public Product read(long productId) throws SQLException
	{
		//Es wird eine Variable für das zu ladende Produkt angelegt
		Product product = null;
		String stmt = "SELECT id,name,price,quantity FROM products WHERE id=?";
		prep = connection.prepareStatement(stmt);
		prep.setLong(1, productId);
		res = prep.executeQuery();
		while(res.next())
		{
			//Die Produktdaten werden aus dem ResultSet geladen und gesetzt
			product = new Product();
			product.setId(productId);
			product.setName(res.getString("name"));
			product.setPrice(res.getDouble("price"));
			product.setQuantity(res.getInt("quantity"));
			closeUsedData();
			return product;
		}
		
		//Sollte das nicht geklappt haben gibt "null" zurück
		closeUsedData();
		return null;
	}

	public long getLastId() throws SQLException
	{
		//Variable für die id und Statement
		long id;
		String stmt = "SELECT * FROM products WHERE id = (SELECT MAX(id) FROM products)";
		prep = connection.prepareStatement(stmt);
		res = prep.executeQuery();
		while(res.next())
		{
			//setze die Id
			id = res.getLong("id");
			//Schliesse Nutzdaten
			closeUsedData();
			//gib die id zurück
			return id;
		}
		//Sollte das nicht geklappt haben schliesse die Nutzdaten und gib -1 aus
		closeUsedData();
		return -1;
	}
}