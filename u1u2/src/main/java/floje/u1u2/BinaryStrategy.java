package floje.u1u2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fpt.com.Product;

public class BinaryStrategy implements fpt.com.SerializableStrategy, AutoCloseable {
	
	private FileOutputStream fos;
	private ObjectOutputStream out;
	private FileInputStream fis;
	private ObjectInputStream in;
	
	public BinaryStrategy() throws Exception {
		
	}

	@Override
	public Product readObject() throws IOException {
		if (fis == null && fos == null) {
			fis = new FileInputStream ("products.ser");
		}
		if (fis == null) {
			throw new IOException("This strategy is allready writing to products.xstream.xml!");
		}
		if (in == null) {
			in = new ObjectInputStream(fis);
		}
		Product pr = null;
		try {
				pr = (Product) (in.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fos == null && fis == null) {
			fos = new FileOutputStream("products.ser");
		}
		if (fos == null) {
			throw new IOException("This strategy is allready reading from products.xml!");
		}
		if (out == null) {
			out = new ObjectOutputStream(fos);
		}
		out.writeObject(obj);
		out.flush();
	}

	@Override
	public void close() throws IOException {
		if (out != null) {
			out.close();
			out = null;
		}
		if (in != null) {
			in.close();
			in = null;
		}
		if (fos != null) {
			fos.close();
			fos = null;
		}
		if (fis != null) {
			fis.close();
			fis = null;
		}
	}

}
