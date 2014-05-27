package strategies;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fpt.com.Product;

public class BinaryStrategy implements fpt.com.SerializableStrategy, AutoCloseable {

	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;

	@Override
	public Product readObject() throws IOException {
		//		if (fileInputStream == null) {
		//			throw new IOException("This strategy is allready writing to products.xml!");
		//		}
		if (objectInputStream == null) {
			objectInputStream = new ObjectInputStream(new FileInputStream ("products.ser"));
		}

		Product product = null;

		try {
			product = (Product) (objectInputStream.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return product;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		//		if (fileOutputStream == null) {
		//			throw new IOException("This strategy is already reading from products.xml!");
		//		}
		if (objectOutputStream == null) {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream("products.ser"));
		}

		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
	}

	@Override
	public void close() throws IOException {
		try
		{
			if (objectOutputStream != null) {
				objectOutputStream.close();
				objectOutputStream = null;
			}
		}
		finally
		{
			if (objectInputStream != null) {
				objectInputStream.close();
				objectInputStream = null;
			}
		}
	}
}
