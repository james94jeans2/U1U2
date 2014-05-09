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
	private FileOutputStream fileOutputStream;
	private FileInputStream fileIntputStream;
	
	@Override
	public Product readObject() throws IOException {
		if (fileOutputStream == null && fileIntputStream == null) {
			fileIntputStream = new FileInputStream("products.xml");
		}
		if (fileIntputStream == null) {
			throw new IOException("This strategy is allready writing to products.xml!");
		}
		if (decoder == null) {
			decoder = new XMLDecoder(fileIntputStream);
		}
		Product product = (Product)decoder.readObject();
		return product;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fileOutputStream == null && fileIntputStream == null) {
			fileOutputStream = new FileOutputStream("products.xml");
		}
		if (fileOutputStream == null) {
			throw new IOException("This strategy is already reading from products.xml!");
		}
		if (encoder == null) {
			encoder = new XMLEncoder(fileOutputStream);
		}
		encoder.writeObject(obj);
		encoder.flush();
	}

	@Override
	public void close() throws IOException {
		if (decoder != null) {
			decoder.close();
			decoder = null;
		}
		if (encoder != null) {
			encoder.close();
			encoder = null;
		}
		if (fileIntputStream != null) {
			fileIntputStream.close();
			fileIntputStream = null;
		}
		if (fileOutputStream != null) {
			fileOutputStream.close();
			fileOutputStream = null;
		}
	}

}
