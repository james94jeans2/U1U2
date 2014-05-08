package strategies;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import floje.u1u2.Product;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamStrategy implements fpt.com.SerializableStrategy,AutoCloseable{

	private XStream xstream;
	private FileWriter fileWriter=null;
	private FileReader fileReader=null;
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
		if(fileReader==null){
			fileReader = new FileReader("XmlXStreamSer.xml");
		}
		if(objectInputStream==null){
			objectInputStream = xstream.createObjectInputStream(fileReader);
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

		if(fileWriter==null){
			fileWriter = new FileWriter("XmlXStreamSer.xml");
		}
		if(objectOutputStream==null){
			objectOutputStream = xstream.createObjectOutputStream(fileWriter,"waren");
		}

		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
	}

	@Override
	public void close() throws IOException {
		if(objectOutputStream!=null){
			objectOutputStream.close();
//			objectInputStream=null;
		}
		if(objectInputStream!=null){
			objectInputStream.close();
//			objectInputStream=null;
		}
		if(fileWriter!=null){
			fileWriter.close();
//			fileWriter=null;
		}
		if(fileReader!=null){
			fileReader.close();
//			fileReader=null;
		}
	}
}
