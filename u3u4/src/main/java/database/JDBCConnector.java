package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import floje.u3u4.Product;

public class JDBCConnector {
	Connection connection = null;

	private void connect()
	{
		try
		{
			System.out.println("Verbindung wird aufgebaut");
			connection = DriverManager.getConnection(
					"jdbc:postgresql://java.is.uni-due.de/ws1011", "ws1011", "ftpw10");
			System.out.println("Verbindung wurde aufgebaut");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() throws SQLException
	{
		if(connection!=null)
		{
			connection.close();
			connection = null;
		}
	}

	public void getURL()throws SQLException
	{
		connect();
		System.out.println(connection.getMetaData().getURL().split("//")[1]);
		close();
	}

	public void getUserName() throws SQLException
	{
		connect();
		System.out.println(connection.getMetaData().getUserName());
		close();
	}
	public void getTables() throws SQLException
	{
		connect();
		ResultSet res = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while(res.next())
		{
			//Der Wert 3 bezeichnet die TableNames
			//muss man mal in der Dokumentation von getTables nachsehen
			System.out.println(res.getString(3));
		}
		close();
	}

	public long insert(String name, double price, int quantity) throws SQLException
	{
		ResultSet res;
		String stmt2 = "INSERT INTO products (name, price, quantity) VALUES (?,?,?)";
		connect();
		PreparedStatement prep2 = connection.prepareStatement(
				stmt2, PreparedStatement.RETURN_GENERATED_KEYS);
		prep2.setString(1, name);
		prep2.setDouble(2, price);
		prep2.setInt(3, quantity);
		prep2.setMaxRows(10);
		prep2.executeUpdate();
		res = prep2.getGeneratedKeys();
		while(res.next())
		{
			close();
			return res.getInt(res.getRow());
		}
		close();
		return -1;
	}

	public void insert(Product product) throws SQLException
	{
		String stmt = "INSERT INTO products (name, price, quantity) VALUES (?,?,?)";
		connect();
		PreparedStatement prep = connection.prepareStatement(stmt);
		prep.setString(1, product.getName());
		prep.setDouble(2, product.getPrice());
		prep.setInt(3, product.getQuantity());
		prep.executeUpdate();
		close();
	}

	public Product read(long productId) throws SQLException
	{
		ResultSet res;
		Product product = null;
		String stmt = "SELECT id,name,price,quantity FROM products WHERE id=?";
		connect();
		PreparedStatement prep = connection.prepareStatement(stmt);
		prep.setLong(1, productId);
		prep.setMaxRows(1);
		res = prep.executeQuery();
		while(res.next())
		{
			product = new Product();
			product.setId(productId);
			product.setName(res.getString("name"));
			product.setPrice(res.getDouble("price"));
			product.setQuantity(res.getInt("quantity"));
			close();
			return product;
		}
		close();
		return null;
	}
}