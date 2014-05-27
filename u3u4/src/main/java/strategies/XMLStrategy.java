package strategies;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fpt.com.Product;
import fpt.com.SerializableStrategy;

public class XMLStrategy implements SerializableStrategy, AutoCloseable {

	private XMLEncoder encoder;
	private XMLDecoder decoder;

	@Override
	public Product readObject() throws IOException {
		//		if (fileIntputStream == null) {
		//			throw new IOException("This strategy is allready writing to products.xml!");
		//		}
		if (decoder == null) {
			decoder = new XMLDecoder(new FileInputStream("products.xml"));
		}
		Product product = (Product)decoder.readObject();
		return product;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		//		if (fileOutputStream == null) {
		//			throw new IOException("This strategy is already reading from products.xml!");
		//		}
		if (encoder == null) {
			encoder = new XMLEncoder(new FileOutputStream("products.xml"));
		}
		encoder.writeObject(obj);
		encoder.flush();
	}

	@Override
	public void close() throws IOException {
		try
		{
			if (decoder != null) {
				decoder.close();
				decoder = null;
			}
		}
		finally
		{
			if (encoder != null) {
				encoder.close();
				encoder = null;
			}
		}
	}

}
