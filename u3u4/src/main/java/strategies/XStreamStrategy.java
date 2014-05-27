package strategies;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import floje.u3u4.Product;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamStrategy implements fpt.com.SerializableStrategy,AutoCloseable{

	private XStream xstream;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;

	public XStreamStrategy(){
		xstream = new XStream(new DomDriver());
		xstream.aliasField("anzahl", Product.class, "quantity");
		xstream.aliasField("preis", Product.class, "price");

		xstream.useAttributeFor(Product.class, "id");
		xstream.registerLocalConverter(Product.class, "id", new IDConverter());	
		xstream.registerLocalConverter(Product.class, "price", new PriceConverter());

		xstream.alias("ware", Product.class);
	}

	@Override
	public Product readObject() throws IOException{
		//		//Error zurückgeben wenn schon verwendet (gilt fuer alle strategies)
		//		if (fileReader == null) {
		//			throw new IOException("This strategy is allready writing to products.xml!");
		//		}
		if(objectInputStream==null){
			objectInputStream = xstream.createObjectInputStream(new FileReader("products.xstream.xml"));
		}
		Product pr=null;
		try {
			pr = (Product)(objectInputStream.readObject());
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return pr;

	}

	@Override
	public void writeObject(fpt.com.Product obj) throws IOException {
		//		if (fileWriter == null) {
		//			throw new IOException("This strategy is already reading from products.xml!");
		//		}
		if(objectOutputStream==null){
			objectOutputStream = xstream.createObjectOutputStream(new FileWriter("products.xstream.xml"),"waren");
		}

		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
	}

	@Override
	public void close() throws IOException {
		try
		{
			if(objectOutputStream!=null){
				objectOutputStream.close();
				objectInputStream=null;
			}
		}
		finally
		{
			if(objectInputStream!=null){
				objectInputStream.close();
				objectInputStream=null;
			}
		}
	}
}
