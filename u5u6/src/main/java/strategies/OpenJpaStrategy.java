package strategies;

import java.io.EOFException;
import java.io.IOException;

import database.OpenJPAConnector;

import fpt.com.Product;

public class OpenJpaStrategy implements fpt.com.SerializableStrategy {

	private OpenJPAConnector connector;
	private int counter = 0;
	private long currentId = -1;


	public OpenJpaStrategy(OpenJPAConnector connector){
		this.connector=connector;
	}

	@Override
	public Product readObject() throws IOException {
		Product product = null;
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
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		connector.write((floje.Product)obj);
	}

	@Override
	public void close() throws IOException {
		connector.close();		
	}

	public OpenJPAConnector getConnector()
	{
		return this.connector;
	}
}
