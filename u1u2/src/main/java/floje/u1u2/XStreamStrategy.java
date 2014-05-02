package floje.u1u2;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;

public class XStreamStrategy implements fpt.com.SerializableStrategy,AutoCloseable{
    
	private XStream xstream;
	private FileWriter fw=null;
	private FileReader fr=null;
	
	public XStreamStrategy()throws Exception{
		xstream = new XStream(new DomDriver());
		xstream.aliasField("anzahl", Product.class, "quantity");
		xstream.aliasField("preis", Product.class, "price");
		
	}
	
	@Override
	public Product readObject() throws IOException {
		if(fr==null){
			fr = new FileReader("XmlXStreamSer.xml");
		}
		Product readProduct = null;
        readProduct = (Product) xstream.fromXML(fr); // Read Object
        return readProduct;
		
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if(fw==null){
			fw = new FileWriter("XmlXStreamSer.xml");
		}
		int l = String.valueOf(obj.getId()).length();
		ersetzte(l,String.valueOf(obj.getId()));
		
		xstream.toXML(obj, fw);
	
	}

	@Override
	public void close() throws IOException {
		if(fw!=null){
			fw.close();
		}
		if(fr!=null){
			fr.close();
		}
		
	}
	
	private void ersetzte(int l, String id){
		
		while(l<7){
			id="0"+id;
			xstream.aliasField(id, Product.class, "id");
			
		}
		
	}
	

}
