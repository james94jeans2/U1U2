package floje.u1u2;

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
	private FileOutputStream fos;
	private FileInputStream fis;
	
	@Override
	public Product readObject() throws IOException {
		if (fos == null && fis == null) {
			fis = new FileInputStream("products.xml");
		}
		if (fis == null) {
			throw new IOException("This strategy is allready writing to products.xml!");
		}
		if (decoder == null) {
			decoder = new XMLDecoder(fis);
		}
		fpt.com.Product product = (fpt.com.Product) decoder.readObject();
		return product;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fos == null && fis == null) {
			fos = new FileOutputStream("products.xml");
		}
		if (fos == null) {
			throw new IOException("This strategy is allready reading from products.xml!");
		}
		if (encoder == null) {
			encoder = new XMLEncoder(fos);
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
		if (fis != null) {
			fis.close();
			fis = null;
		}
		if (fos != null) {
			fos.close();
			fos = null;
		}
	}

}
