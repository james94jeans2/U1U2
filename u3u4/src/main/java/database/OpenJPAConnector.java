package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.persistence.OpenJPAPersistence;

import floje.u3u4.Product;

public class OpenJPAConnector {

	EntityManagerFactory factory;
	EntityManager manager; 
	EntityTransaction transaction;

	public void initialize()
	{
		
		factory = getWithoutConfig();
//		factory = Persistence.createEntityManagerFactory(
//				"openjpa", System.getProperties());
		
		
		
		manager = factory.createEntityManager();
		transaction = manager.getTransaction();
		transaction.begin();
	}

	public void close()
	{
		transaction.commit();
		if(manager!=null)
		{
			manager.close();
		}
		if(factory!=null)
		{
			factory.close();
			factory = null;
		}
	}

	public void write(Product product)
	{
		product.setId(0);
		manager.persist(product);
	}

	public Product read(long id)
	{
		String stmt = String.format("SELECT p FROM Product p WHERE p.id=%d", id);
		@SuppressWarnings("unchecked")
		List<Product> result = (List<Product>)manager.createQuery(stmt).getResultList();
		if(!result.isEmpty())
		{
			return result.get(0);
		}
		else
		{
			return null;
		}
	}

	public long getLastId()
	{
		long id;
		String stmt = "SELECT p FROM Product p WHERE p.id = (SELECT MAX(p2.id) FROM Product p2)";
		//String stmt = "SELECT p FROM Product p ORDER BY p.id DESC";
		@SuppressWarnings("unchecked")
		List<Product> result = (List<Product>)manager.createQuery(stmt).getResultList();
		if(!result.isEmpty())
		{
			id = result.get(0).getId();
			return id;
		}
		return -1;
	}
	
	public EntityManagerFactory getWithoutConfig() {

		//Hashmap f�r die Verbindungsdaten ohne Konfiguratiosdatei
		Map<String, String> map = new HashMap<String, String>();

		map.put("openjpa.ConnectionURL",
				"jdbc:postgresql://java.is.uni-due.de/ws1011");
		map.put("openjpa.ConnectionDriverName", "org.postgresql.Driver");
		map.put("openjpa.ConnectionUserName", "ws1011");
		map.put("openjpa.ConnectionPassword", "ftpw10");
		map.put("openjpa.RuntimeUnenhancedClasses", "supported");
		map.put("openjpa.jdbc.SynchronizeMappings", "false");

		//Finde alle Klassen, die persistent gemacht werden k�nnen sollen
		List<Class<?>> types = new ArrayList<Class<?>>();
		types.add(Product.class);

		if (!types.isEmpty())
		{
			StringBuffer buffer = new StringBuffer();
			for (Class<?> classObject : types)
			{
				if (buffer.length() > 0)
				{
					buffer.append(";");
				}
				buffer.append(classObject.getName());
			}
			// <class>Product</class>
			map.put("openjpa.MetaDataFactory", "jpa(Types=" + buffer.toString()
					+ ")");
		}

		return OpenJPAPersistence.getEntityManagerFactory(map);
	}
}