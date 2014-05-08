package strategies;

import java.io.IOException;

import floje.u1u2.Product;
import fpt.com.SerializableStrategy;

public class BaseStrategy implements SerializableStrategy{
	private SerializableStrategy strategy;

	public void setStrat(SerializableStrategy strategy)
	{
		this.strategy = strategy;
	}
	@Override
	public Product readObject() throws IOException {
		return (Product)this.strategy.readObject();
	}
	@Override
	public void close() throws IOException {
		this.strategy.close();
	}
	@Override
	public void writeObject(fpt.com.Product product) throws IOException {
		this.strategy.writeObject(product);
	}
}
