package strategies;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fpt.com.Product;

public class BinaryStrategy implements fpt.com.SerializableStrategy, AutoCloseable {
	
	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;

	@Override
	public Product readObject() throws IOException {
		
		if (fileInputStream == null && fileOutputStream == null) {
			fileInputStream = new FileInputStream ("products.ser");
		}
		if (fileInputStream == null) {
			throw new IOException("This strategy is allready writing to products.xml!");
		}
		if (objectInputStream == null) {
			objectInputStream = new ObjectInputStream(fileInputStream);
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
		
		if (fileOutputStream == null && fileInputStream == null) {
			fileOutputStream = new FileOutputStream("products.ser");
		}
		if (fileOutputStream == null) {
			throw new IOException("This strategy is already reading from products.xml!");
		}
		if (objectOutputStream == null) {
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
		}
		
		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
	}

	@Override
	public void close() throws IOException {
		
		if (objectOutputStream != null) {
			objectOutputStream.close();
			objectOutputStream = null;
		}
		
		if (objectInputStream != null) {
			objectInputStream.close();
			objectInputStream = null;
		}
		
		if (fileOutputStream != null) {
			fileOutputStream.close();
			fileOutputStream = null;
		}
		
		if (fileInputStream != null) {
			fileInputStream.close();
			fileInputStream = null;
		}
		
	}

}
