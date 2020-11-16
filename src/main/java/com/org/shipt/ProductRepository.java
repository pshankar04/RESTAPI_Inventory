package com.org.shipt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

 

public class ProductRepository {

	Connection con = null;
	
	public ProductRepository() {
		String url = "jdbc:mysql://localhost:3306/online_store?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String username = "root";
		String password = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public List<Product> getProducts(){
		System.out.println("Calling getProducts");
		List<Product> products = new ArrayList<Product>();
		String sql = "select * from Product";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Product p = new Product();
				p.setProduct_id(rs.getInt(1));
				p.setCategory_id(rs.getInt(2));
				p.setWeight(rs.getFloat(3));
				p.setProduct_name(rs.getString(4));
				p.setCompany(rs.getString(5));
				products.add(p);
			}


		}catch(Exception e) {
			System.out.println(e);
		}
		return products;
	}
	
	
	public Product getProduct(int id) {

		String sql = "select * from Product where product_id="+id;
		Product p = new Product();
		try {

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if(rs.next()) {
				p.setProduct_id(rs.getInt(1));
				p.setCategory_id(rs.getInt(2));
				p.setWeight(rs.getFloat(3));
				p.setProduct_name(rs.getString(4));
				p.setCompany(rs.getString(5));

			}

		}catch(Exception e) {
			System.out.println(e);
		}
		return p;
	}
	
	public void createProduct(Product p) {
		String sql = "Insert into Product values(?,?,?,?,?);";
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,p.getProduct_id());
			st.setInt(2,p.getCategory_id());
			st.setFloat(3,p.getWeight());
			st.setString(4,p.getProduct_name());
			st.setString(5,p.getCompany());
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void update(Product p) {
		String sql = "update Product set category_id=?,weight=?,product_name=?,company=? where product_id=?";
		try {

			PreparedStatement st = con.prepareStatement(sql);
			
			
			st.setInt(1,p.getCategory_id());
			st.setFloat(2,p.getWeight());
			st.setString(3,p.getProduct_name());
			st.setString(4,p.getCompany());
			st.setInt(5,p.getProduct_id());
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	public void delete(int id) {
		String sql = "delete from Product where product_id="+id;
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}
