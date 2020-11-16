package com.org.shipt;

public class Category {

	private int category_id;
	private int product_id;
	private String category_name;
	private int total_products;
	
	
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getTotal_products() {
		return total_products;
	}
	public void setTotal_products(int total_products) {
		this.total_products = total_products;
	}
}
