package floje.u1u2;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;

public class XStreamStrategy implements fpt.com.SerializableStrategy,AutoCloseable{
    
	private XStream xstream;
	private FileWriter fw=null;
	private FileReader fr=null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	public XStreamStrategy()throws Exception{
		xstream = new XStream(new DomDriver());
		xstream.aliasField("anzahl", floje.u1u2.Product.class, "quantity");
		xstream.aliasField("preis", floje.u1u2.Product.class, "price");
		
		xstream.useAttributeFor(floje.u1u2.Product.class, "id");
		xstream.registerLocalConverter(floje.u1u2.Product.class, "id", new idConverter());	
		xstream.registerLocalConverter(floje.u1u2.Product.class, "price", new priceConverter());

		xstream.alias("ware", floje.u1u2.Product.class);
		
		
		
		
	}
	
	@Override
	public Product readObject() throws IOException{
		if(fr==null){
			fr = new FileReader("XmlXStreamSer.xml");
		}
		if(in==null){
			in = xstream.createObjectInputStream(fr);
		}
		Product pr=null;
		try {
			pr = (Product)(in.readObject());
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}
        return pr;
		
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		
		if(fw==null){
			fw = new FileWriter("XmlXStreamSer.xml");
		}
		if(out==null){
			out = xstream.createObjectOutputStream(fw,"waren");
		}
	
		
		out.writeObject(obj);
		//xstream.toXML(obj, fw);
	
	}

	@Override
	public void close() throws IOException {
		if(out!=null){
			out.close();
			out=null;
		}
		if(fw!=null){
			fw.close();
			fw=null;
		}
		if(fr!=null){
			fr.close();
			fr=null;
		}
		
		
	}
	
	private void ersetzte(String id){
		int l = String.valueOf(id).length();
		while(l<7){
			id="0"+id;
			xstream.aliasField(id, Product.class, "id");
			l = String.valueOf(id).length();
			
		}
		
	}
	

}
