package strategies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAPersistence;

import fpt.com.Product;

public class OpenJpaStrategy implements fpt.com.SerializableStrategy {

	private EntityManagerFactory fac = null;
	private EntityManager e = null;
	private EntityTransaction t = null;
	private Product p;
	private List<Product> list;
	private int counter = 0;
	
	
	public OpenJpaStrategy(){
		list=null;		
	}
	
	@Override
	public Product readObject() throws IOException {
		if(fac==null) fac = getWithoutConfig();
		if(e==null) e = fac.createEntityManager();
		if(t==null) t = e.getTransaction();
		
		if(list==null){
			t.begin();
			Query q = e.createQuery("SELECT p FROM Product p ORDER BY");// p.id DESC");
			q.setFirstResult (counter);
			//q.setMaxResults(10); //setzten von einer Maximalen Anzahl and Einträgen die geladen werden sollen
			t.commit();		
			list=q.getResultList();
		}
		
		if(counter-1<list.size()-1){
			counter++;
			return (Product)list.get(counter-1);			
		}
		
		return null;		
		
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if(fac==null) fac = getWithoutConfig();
		if(e==null) e = fac.createEntityManager();
		if(t==null) t = e.getTransaction();
		if(obj.getId()==0l){
			t.begin();
			e.persist(obj);
			t.commit();
		}
		
	}

	@Override
	public void close() throws IOException {
		if(list!=null){ 	
			list=null;
		}
		
		if(t!=null){			
			t=null;
		}
		
		if(e!=null){ 
			e.close();
			e=null;
		}
		
		if(fac!=null){ 
			fac.close();
			fac=null;
		}
		
		
	}
	
	private EntityManagerFactory getWithoutConfig() {
			
		Map<String, String> map = new HashMap<String, String>();

		map.put("openjpa.ConnectionURL",
				"jdbc:postgresql://java.is.uni-due.de/ws1011");
		map.put("openjpa.ConnectionDriverName", "org.postgresql.Driver");
		map.put("openjpa.ConnectionUserName", "ws1011");
		map.put("openjpa.ConnectionPassword", "ftpw10");
		map.put("openjpa.RuntimeUnenhancedClasses", "supported");
		map.put("openjpa.jdbc.SynchronizeMappings", "false");
		
		List<Class<?>> types = new ArrayList<Class <?>>();
		types.add(floje.u3u4.Product.class );
		if (!types.isEmpty()) {
			StringBuffer buf = new StringBuffer (); for (Class<?> c : types) {
			if (buf.length() > 0) { buf.append(";"); } buf . append ( c . getName ( ) ) ;
			} // <class>Product</class>
			map.put("openjpa.MetaDataFactory", "jpa(Types=" + buf.toString() + ")");
		}
		
		return OpenJPAPersistence.getEntityManagerFactory(map);
		
	}

}
