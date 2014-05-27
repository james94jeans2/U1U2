package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import floje.u3u4.Product;

public class JDBCConnector {
	Connection connection = null;
	ResultSet res = null;
	PreparedStatement prep = null;

	public void connect() throws SQLException
	{
		System.out.println("Verbindung wird aufgebaut");
		connection = DriverManager.getConnection(
				"jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
		System.out.println("Verbindung wurde aufgebaut");
	}

	public void close() throws SQLException
	{
		if(connection!=null)
		{
			connection.close();
			connection = null;
		}
	}

	private void closeUsedData() throws SQLException
	{
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
		System.out.println(connection.getMetaData().getURL().split("//")[1]);
	}

	public void getUserName() throws SQLException
	{
		System.out.println(connection.getMetaData().getUserName());
	}
	public void getTables() throws SQLException
	{
		res = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while(res.next())
		{
			//Der Wert 3 bezeichnet die TableNames
			//muss man mal in der Dokumentation von getTables nachsehen
			System.out.println(res.getString(3));
		}
		closeUsedData();
	}

	public long insert(String name, double price, int quantity) throws SQLException
	{
		String stmt2 = "INSERT INTO products (name, price, quantity) VALUES (?,?,?)";
		prep = connection.prepareStatement(
				stmt2, PreparedStatement.RETURN_GENERATED_KEYS);
		prep.setString(1, name);
		prep.setDouble(2, price);
		prep.setInt(3, quantity);
		prep.executeUpdate();
		res = prep.getGeneratedKeys();
		while(res.next())
		{
			int id = res.getInt(res.getRow());
			closeUsedData();
			return id;
		}
		closeUsedData();
		return -1;
	}

	public void insert(Product product) throws SQLException
	{
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
		Product product = null;
		String stmt = "SELECT id,name,price,quantity FROM products WHERE id=?";
		prep = connection.prepareStatement(stmt);
		prep.setLong(1, productId);
		res = prep.executeQuery();
		while(res.next())
		{
			product = new Product();
			product.setId(productId);
			product.setName(res.getString("name"));
			product.setPrice(res.getDouble("price"));
			product.setQuantity(res.getInt("quantity"));
			closeUsedData();
			return product;
		}
		closeUsedData();
		return null;
	}

	public long getLastId() throws SQLException
	{
		long id;
		String stmt = "SELECT * FROM products WHERE id = (SELECT MAX(id) FROM products)";
		prep = connection.prepareStatement(stmt);
		res = prep.executeQuery();
		while(res.next())
		{
			id = res.getLong("id");
			closeUsedData();
			return id;
		}
		closeUsedData();
		return -1;
	}
}