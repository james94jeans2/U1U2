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

import floje.Product;

public class OpenJPAConnector {

	//Unsere Entity - "Helfer"
	EntityManagerFactory factory;
	EntityManager manager; 
	EntityTransaction transaction;

	public void initialize()
	{
		//Erstellung ohne Konfig oder mit Persistence.xml
		factory = getWithoutConfig();
//		factory = Persistence.createEntityManagerFactory(
//				"openjpa", System.getProperties());
		
		
		//Erstellung von manager und transaction
		manager = factory.createEntityManager();
		transaction = manager.getTransaction();
		//Starten der Transaktion -> Wird an anderer Stelle geschlossen
		transaction.begin();
	}

	public void close()
	{
		//Wir machen einen Commit
		transaction.commit();
		//Und schliessen danach Manager und Factory
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
		//Dem Produkt wird der Standardwert "0" gegeben damit wir serialisieren dürfen
		product.setId(0);
		//Danach wird mit persist in die Datenbank geschrieben
		manager.persist(product);
	}

	public Product read(long id)
	{
		String stmt = String.format("SELECT p FROM Product p WHERE p.id=%d", id);
		@SuppressWarnings("unchecked")
		List<Product> result = (List<Product>)manager.createQuery(stmt).getResultList();
		//Wenn die Liste nicht leer ist gibt das erste(und wahrscheinlich einzige wenn die 
		//Implementierung der Datenbank funktioniert) Produkt aus
		if(!result.isEmpty())
		{
			return result.get(0);
		}
		else
		{
			//Gibt es kein produkt mit dieser Id gibt "null" zurück
			return null;
		}
	}

	public long getLastId()
	{
		
		//Variable zur Speicherung der id
		long id;
		//Vielleicht nicht das beste Statement aber es funktioniert
		String stmt = "SELECT p FROM Product p WHERE p.id = (SELECT MAX(p2.id) FROM Product p2)";
		//String stmt = "SELECT p FROM Product p ORDER BY p.id DESC";
		@SuppressWarnings("unchecked")
		List<Product> result = (List<Product>)manager.createQuery(stmt).getResultList();
		if(!result.isEmpty())
		{
			//Gib die id zurück
			id = result.get(0).getId();
			return id;
		}
		//oder gib -1 aus wenn es einen Fehler gab
		return -1;
	}
	
	public EntityManagerFactory getWithoutConfig() {

		//Hier wird alles hardgecoded was auch in der Persistence.xml stehen kann
		
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