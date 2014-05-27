package strategies;

import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;

import database.JDBCConnector;
import fpt.com.Product;
import fpt.com.SerializableStrategy;

public class JDBCStrategy implements SerializableStrategy{
	private JDBCConnector connector;
	private long counter = 0;
	private long currentId = -1;

	public JDBCStrategy(JDBCConnector connector)
	{
		this.connector=connector;
	}

	@Override
	public Product readObject() throws IOException {
		Product product = null;
		try {
			if(currentId == -1)
			{
				currentId = connector.getLastId();
			}
			if(counter <10)
			{
				product = connector.read(currentId--);
				counter++;
				return product;
			}
			else
			{
				counter = 0;
				currentId = -1;
				throw new EOFException("Maximum reads reached");
			}
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		try {
			connector.insert((floje.u3u4.Product)obj);
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public void close() throws IOException {
		try {
			connector.close();
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public JDBCConnector getConnector()
	{
		return this.connector;
	}
}