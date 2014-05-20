package floje.u3u4;

import java.util.ArrayList;

public class ProductList extends ArrayList<fpt.com.Product> implements fpt.com.ProductList{

	private static final long serialVersionUID = 5740297270208185775L;

	@Override
	public boolean delete(fpt.com.Product product) {
		return super.remove(product);
	}

	@Override
	public fpt.com.Product findProductById(long id) {
		for(fpt.com.Product product: this){
			if(product.getId()==id){
				return product;
			}			
		}
		return null;
	}

	@Override
	public fpt.com.Product findProductByName(String name) {
		for(fpt.com.Product product: this){
			if(product.getName().equals(name)){
				return product;
			}			
		}
		return null;
	}

}
