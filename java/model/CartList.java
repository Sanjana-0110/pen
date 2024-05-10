package model;

import java.util.ArrayList;
import java.util.List;

public class CartList {
	private List<ProductInfo> cl;

	public CartList() {
		cl = new ArrayList<>();
	}

	public void addProducts(ProductInfo c) {

		cl.add(c);
	}

	public List<ProductInfo> getAllProducts() {
		return cl;
	}
}
