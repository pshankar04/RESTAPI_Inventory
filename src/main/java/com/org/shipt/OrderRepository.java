package com.org.shipt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;



public class OrderRepository {

	Connection con = null;

	public OrderRepository() {

		String url = "jdbc:mysql://localhost:3306/online_store?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST";
		String username = "root";
		String password = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,username,password);
			updateOrdersTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the orders based on the order created date
	 * By default gets executed once the OrderRepository object is created.
	 * Order Status:
	 * 0 - 12 hours: "Ready"
	 * 12 - 24 hours: "On Its Way"
	 * After 24 Hours: "Delivered"
	 */

	public void updateOrdersTable() {

		long diffInMillies = 0l;
		int diffInHours = 0;
		long diff = 0;
		String STATUS = "";
		String sql = "select * from Orders";
		String sqlUpdate = "";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				diffInMillies = Math.abs(rs.getTimestamp(4).getTime() - new Date().getTime());
				diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
				diffInHours = ((int)(diff / 60)) + 1;

				if(diffInHours <= 12){
					STATUS = "Ready";
					sqlUpdate = "UPDATE Orders set status=? where ord_id=?";
					PreparedStatement ps = con.prepareStatement(sqlUpdate);
					ps.setString(1,STATUS);
					ps.setInt(2,rs.getInt(1));
					ps.executeUpdate();
				}
				else if(diffInHours > 12 && diffInHours <= 24) {
					STATUS = "On its way";
					sqlUpdate = "UPDATE Orders set status=? where ord_id=?";
					PreparedStatement ps = con.prepareStatement(sqlUpdate);
					ps.setString(1,STATUS);
					ps.setInt(2,rs.getInt(1));
					ps.executeUpdate();
				}
				else{
					STATUS = "Delivered";
					sqlUpdate = "UPDATE Orders set status=? where ord_id=?";
					PreparedStatement ps = con.prepareStatement(sqlUpdate);
					ps.setString(1,STATUS);
					ps.setInt(2,rs.getInt(1));
					ps.executeUpdate();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the orders placed
	 * @return List<Order> 
	 */
	public List<Order> getOrders(){
		List<Order> orders = new ArrayList<Order>();
		String sql = "select * from Orders";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Order o = new Order();
				o.setOrder_id(rs.getInt(1));
				o.setOrder_pid(rs.getInt(2));
				o.setStatus(rs.getString(3));
				o.setCreated_date(rs.getTimestamp(4));
				o.setCustomer_id(rs.getInt(5));
				o.setQuantity(rs.getInt(6));
				orders.add(o);
			}


		}catch(Exception e) {
			e.printStackTrace();
		}
		return orders;
	}


	/**
	 * Returns the order by ID.
	 * It returns a list as an order is a list of Products
	 * @param id
	 * @return List<Order>
	 */
	public List<Order> getOrder(int id) {

		List<Order> orders = new ArrayList<Order>();
		String sql = "select * from Orders where ord_id="+id;
		Order o;
		try {

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()) {
				o = new Order();
				o.setOrder_id(rs.getInt(1));
				o.setOrder_pid(rs.getInt(2));
				o.setStatus(rs.getString(3));
				o.setCreated_date(rs.getTimestamp(4));
				o.setCustomer_id(rs.getInt(5));
				o.setQuantity(rs.getInt(6));
				orders.add(o);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Returns orders placed by a specific customer by customer id.
	 * @param id - customer Id
	 * @return List<Order>
	 */
	public List<Order> getCustomerOrder(int id) {

		List<Order> orders_by_date = new ArrayList<Order>();
		String sql;
		if (id == 0) {
			sql = "select * from Orders order by cust_id,created_date";
		}
		else {
			sql = "select * from Orders where cust_id="+id+" order by created_date DESC";
		}
		Order o;
		try {

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()) {
				o = new Order();
				o.setOrder_id(rs.getInt(1));
				o.setOrder_pid(rs.getInt(2));
				o.setCustomer_id(rs.getInt(3));
				o.setCreated_date(rs.getTimestamp(4));
				o.setQuantity(rs.getInt(5));
				o.setStatus(rs.getString(6));
				orders_by_date.add(o);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
		return orders_by_date;
	}

	/**
	 * Returns orders placed between begin and end date
	 * @param beginDate (date in the format of yyyy-MM-dd)
	 * @param endDate (date in the format of yyyy-MM-dd)
	 * @return
	 * @throws java.text.ParseException 
	 */
	public List<ProductQuantity> getOrdersByDate(String beginDate, String endDate) throws ParseException {

		List<ProductQuantity> orders_by_date = new ArrayList<ProductQuantity>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date start = sdf.parse(beginDate);
			Date end = sdf.parse(endDate);
			java.sql.Date start_date = new java.sql.Date(start.getTime());
			java.sql.Date end_date = new java.sql.Date(end.getTime());
			String sql = "Select category.category_name,product.product_name,orders.ord_prod_id, sum(quantity) as Quantity, DATE(created_date) from orders,product,category where orders.ord_prod_id = category.product_id and orders.ord_prod_id = product.product_id and DATE(created_date) between '"+start_date+"' and '"+end_date+"' group by category_name,product_name,ord_prod_id,Date(created_date) order by Date(created_date) DESC";
			System.out.println(sql);
			ProductQuantity pq;

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()) {
				pq = new ProductQuantity();
				pq.setCategory_name(rs.getString(1));
				pq.setProduct_name(rs.getString(2));
				pq.setProduct_id(rs.getInt(3));
				pq.setQuantity(rs.getInt(4));
				pq.setDate(rs.getString(5));
				orders_by_date.add(pq);

			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return orders_by_date;
	}

	/**
	 * Returns the products sold on a specific date
	 * @param date (date in the format of yyyy-MM-dd)
	 * @return List<ProductQuantity> 
	 * @throws java.text.ParseException 
	 */
	public List<ProductQuantity> getOrdersOnDate(String date) throws ParseException {

		List<ProductQuantity> orders_by_date = new ArrayList<ProductQuantity>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date start = sdf.parse(date);
			java.sql.Date start_date = new java.sql.Date(start.getTime());
			String sql = "Select category.category_name,product.product_name,ORD_PROD_ID, sum(quantity) as Quantity,DATE(created_date) from orders,product,category where orders.ORD_PROD_ID = category.product_id and orders.ORD_PROD_ID = product.product_id and DATE(created_date) = '"+start_date+"' group by category_name,product_name,ORD_PROD_ID, Date(created_date) order by Date(created_date) DESC";
			System.out.println(sql);
			ProductQuantity pq;

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			while(rs.next()) {
				pq = new ProductQuantity();
				pq.setCategory_name(rs.getString(1));
				pq.setProduct_name(rs.getString(2));
				pq.setProduct_id(rs.getInt(3));
				pq.setQuantity(rs.getInt(4));
				pq.setDate(rs.getString(5));
				orders_by_date.add(pq);

			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return orders_by_date;
	}

	/**
	 * Creates an order for a customer specified by customer and product id along with the quantity
	 * @param cid (integer customer id)
	 * @param pids (in the format of = "product_id-quantity, product_id-quantity...")
	 * @return List<Order> 
	 */
	public List<Order> createOrder(int cid, String[] pids) {
		
		List<Order> orders = new ArrayList<Order>();
		int max_order_id = getMaxIdfromOrders();
		String sql;
		Order order;
		int pid = 1;
		int quantity = 1;
		for(String product_id : pids) {
			order = new Order();
			if (product_id.contains("-")){
				pid = Integer.valueOf(product_id.split("-")[0]);
				quantity = Integer.valueOf(product_id.split("-")[1]);
				if(pid == 0) {
					pid = 1;
				}
				if(quantity == 0) {
					quantity = 1;
				}
				if (pid == 0 && quantity == 0) {
					pid = 1;
					quantity = 1;
				}
			}else {
				pid = Integer.valueOf(product_id);
				quantity = 1;
			}

			try {
				sql = "Insert into Orders values(?,?,?,?,?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1,max_order_id);
				st.setInt(2,pid);
				st.setInt(3, cid);	
				long time = System.currentTimeMillis();
				Timestamp timestamp = new java.sql.Timestamp(time);
				st.setTimestamp(4, timestamp);
				st.setInt(5, quantity);
				st.setString(6,"Ready");
				
				st.executeUpdate();
				order.setOrder_id(max_order_id);
				order.setCustomer_id(cid);
				order.setCreated_date(timestamp);
				order.setOrder_pid(Integer.valueOf(pid));
				order.setStatus("Ready");
				order.setQuantity(quantity);
				orders.add(order);

			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return orders;

	}

	/**
	 * Returns the maximum ID of all orders;
	 * @return Integer - Id for a fresh order
	 */
	public int getMaxIdfromOrders() {

		int maxId = 0 ;
		String sqlQuery = "select MAX(ord_id) from orders";
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			if(rs.next()) {
				maxId = rs.getInt(1);
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("MAX ID:"+maxId);
		return (maxId + 1);
	}

	/**
	 * Updates the orders
	 * @param cid (customer Id)
	 * @param oid (Order Id)
	 * @param pids  (in the format of = "product_id-quantity, product_id-quantity...")
	 * @return
	 */
	public List<Order> update(int cid, int oid, String[] pids) {
		delete(oid);
		String sql;
		int pid = 0;
		int quantity = 1;
		Order o;
		List<Order> orders = new ArrayList<Order>();
		try {
			for(int i = 0 ; i < pids.length; i++){
				o = new Order();
				pid = Integer.valueOf(pids[i].split("-")[0]);
				quantity =  Integer.valueOf(pids[i].split("-")[1]);
				sql = "Insert into Orders values(?,?,?,?,?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1,oid);
				st.setInt(2,Integer.valueOf(pid));
				st.setInt(3,cid);
				st.setTimestamp(4,new java.sql.Timestamp(new Date().getTime()));
				st.setInt(5, quantity);
				st.setString(6, "Ready");
				st.executeUpdate();
				o.setCreated_date(new java.sql.Timestamp(new Date().getTime()));
				o.setOrder_id(oid);
				o.setOrder_pid(pid);
				o.setStatus("Ready");
				o.setCustomer_id(cid);
				o.setQuantity(quantity);
				orders.add(o);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}

	/**
	 * Deletes an order with Id
	 * @param id (Order Id)
	 */
	public void delete(int id) {
		String sql = "delete from Orders where ord_id="+id;
		try {

			PreparedStatement st = con.prepareStatement(sql);
			st.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

}


