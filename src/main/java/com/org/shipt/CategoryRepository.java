package com.org.shipt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

	Connection con = null;

	public CategoryRepository() {

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

	public List<Category> getCategories(){
		System.out.println("Calling getCategories");
		List<Category> categories = new ArrayList<Category>();
		String sql = "select * from Category";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Category c = new Category();

				c.setCategory_id(rs.getInt(1));
				c.setProduct_id(rs.getInt(2));
				c.setCategory_name(rs.getString(3));
				c.setTotal_products(rs.getInt(4));

				categories.add(c);
			}


		}catch(Exception e) {
			System.out.println(e);
		}
		return categories;
	}


	public List<Category> getCategory(int id) {

		List<Category> categories = new ArrayList<Category>();
		String sql = "select * from Category where category_id="+id;
		Category c = new Category();
		try {

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()) {
				c.setCategory_id(rs.getInt(1));
				c.setProduct_id(rs.getInt(2));
				c.setCategory_name(rs.getString(3));
				c.setTotal_products(rs.getInt(4));
				categories.add(c);

			}

		}catch(Exception e) {
			System.out.println(e);
		}
		return categories;
	}

	public void createCategory(List<Category> cats) {

		for(Category c: cats) {
			String sql = "Insert into Category values(?,?,?,?);";
			try {

				PreparedStatement st = con.prepareStatement(sql);

				st.setInt(1,c.getCategory_id());
				st.setInt(2,c.getProduct_id());
				st.setString(3,c.getCategory_name());
				st.setInt(4,c.getTotal_products());

				st.executeUpdate();



			}catch(Exception e) {
				System.out.println(e);
			}
		}

	}

	public void update(List<Category> categories) {

		for(Category c: categories) {
			String sql = "update Category set product_id=?,customer_name=?,product_count=? where category_id=?";
			try {

				PreparedStatement st = con.prepareStatement(sql);

				st.setInt(1,c.getProduct_id());
				st.setString(2,c.getCategory_name());
				st.setInt(3,c.getTotal_products());
				st.setInt(4,c.getCategory_id());
				st.executeUpdate();



			}catch(Exception e) {
				System.out.println(e);
			}
		}

	}


	public void delete(int id) {
		String sql = "delete from Category where category_id="+id;
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.executeUpdate();

		}catch(Exception e) {
			System.out.println(e);
		}

	}

}


