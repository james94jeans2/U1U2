package floje;

public class IDGenerator {
	
	private static final long maximumIDs = 999999;

	public static void generateIDForProduct (fpt.com.ProductList list, fpt.com.Product product) throws Exception {
		long maxID = 0;
		for (fpt.com.Product existingProduct : list) {
			if (existingProduct.getId() > maxID) {
				maxID = existingProduct.getId();
			}
		}
		if (maxID == maximumIDs) {
			throw new Exception("Maximum ID-Values reached!");
		}
		product.setId(maxID + 1);
	}
	
}
