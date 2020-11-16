package com.org.shipt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

	Connection con = null;
	
	public CustomerRepository() {
		
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
	
	public List<Customer> getCustomers(){
		System.out.println("Calling getCustomers");
		List<Customer> Customers = new ArrayList<Customer>();
		String sql = "select * from Customer";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Customer p = new Customer();
				p.setCustomer_id(rs.getInt(1));
				p.setCustomer_name(rs.getString(2));
				p.setCustomer_email(rs.getString(3));
				p.setCustomer_cellno(rs.getString(4));
				Customers.add(p);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return Customers;
	}
	
	
	public Customer getCustomer(int id) {

		String sql = "select * from Customer where customer_id="+id;
		Customer p = new Customer();
		try {

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if(rs.next()) {
				p.setCustomer_id(rs.getInt(1));
				p.setCustomer_name(rs.getString(2));
				p.setCustomer_email(rs.getString(3));
				p.setCustomer_cellno(rs.getString(4));

			}

		}catch(Exception e) {
			System.out.println(e);
		}
		return p;
	}
	
	public void createCustomer(Customer p) {
		String sql = "Insert into Customer values(?,?,?,?);";
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,p.getCustomer_id());
			st.setString(2,p.getCustomer_name());
			st.setString(3,p.getCustomer_email());
			st.setString(4,p.getCustomer_cellno());
			
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void update(Customer p) {
		String sql = "update Customer set customer_name=?,customer_email=?,customer_phone=? where customer_id=?";
		try {

			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1,p.getCustomer_name());
			st.setString(2,p.getCustomer_email());
			st.setString(3,p.getCustomer_cellno());
			st.setInt(4,p.getCustomer_id());
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	public void delete(int id) {
		String sql = "delete from Customer where customer_id="+id;
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.executeUpdate();

			 

		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}

