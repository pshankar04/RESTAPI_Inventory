package com.org.shipt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("orders")
public class OrderResources {

	OrderRepository repo = new OrderRepository();

	/**
	 * http://localhost:8080/shipt/online_store/orders
	 * @return: a list of JSON objects representing all the orders placed so far.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Order> getOrders(){

		return repo.getOrders();
	}

	/**
	 * http://localhost:8080/shipt/online_store/orders/order/read/1
	 * @param id
	 * @return: a JSON object representing a specific order (in the above link: for order number 1)
	 */
	@GET
	@Path("order/read/{id}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Order> getOrder(@PathParam("id") int id) {

		List<Order> orders = repo.getOrder(id);
		return orders;
	}

	/**
	 * http://localhost:8080/shipt/online_store/orders/order/customer/2
	 * @param cid: customer ID (2 in the above url)
	 * @return: list of JSON objects representing the orders placed by customer with ID 2.
	 */
	@GET
	@Path("order/customer/{cid}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Order> getCustomerOrder(@PathParam("cid") int cid) {

		if (cid == 0) {
			return repo.getCustomerOrder(cid);
		}
		return repo.getCustomerOrder(cid);
	}

	/**
	 * http://localhost:8080/shipt/online_store/orders/order/create/6/10-1,1-2,15-2
	 * @param cid: Customer ID is 6
	 * @param order_string:{product ID 10 with 1 quantity, product ID 1 with 2 quantity, product ID 15 with 2 quantities}
	 * @return List<Order>: orders with above specifications
	 */

	@POST
	@Path("order/create/{cid}/{pids}")
	//	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Order> createOrder(@PathParam("cid") int cid, @PathParam("pids") String order_string) {
		String[] pids = order_string.split(",");
		List<Order> order_list = repo.createOrder(cid,pids);
		return order_list;
	}

	/**
	 * http://localhost:8080/shipt/online_store/orders/order/update/6/14/10-2,13-1,22-1
	 * Updates the previous order (retakes the entire order string and drops the previous products made on the same order ID) 
	 * @param cid: customer ID 6
	 * @param oid: earlier order items with order ID 14 (14 was obtained through getMaxIdfromOrders();
	 * @param product_ids: new order string (10-2,13-1,22-1), will replace the initial order string (10-2,1-1,50-3)
	 * @return
	 */
	@PUT
	@Path("order/update/{cid}/{oid}/{pids}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Order> updateOrder(@PathParam("cid") int cid,@PathParam("oid") int oid, @PathParam("pids") String product_ids) {
		String[] pr_ids = product_ids.split(",");
		List<Order> orders = repo.update(cid,oid,pr_ids);
		return orders;
	}

	/**
	 * http://localhost:8080/shipt/online_store/orders/order/delete/8
	 * Deletes the order with given ID
	 * @param id: order ID
	 * @return: List<Order> Order deleted
	 */
	@DELETE
	@Path("order/delete/{id}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Order> deleteOrder(@PathParam("id") int id) {

		List<Order> order_list =  repo.getOrder(id);

		for(Order o: order_list) {
			if (o.getOrder_id() == id) {
				repo.delete(id);
			}
		}
		return order_list;
	}
	
	/**
	 * http://localhost:8080/shipt/online_store/orders/order/byDate/2020-11-01/2020-11-08
	 * Returns products sold between begin and end dates 
	 * @param endDate
	 * @param beginDate
	 * @return List<ProductQuantity>: all the products sold between dates
	 */
	
	@GET
	@Path("order/byDate/{begin_date}/{end_date}")
	public List<ProductQuantity> getOrdersByDate(@PathParam("begin_date") String beginDate, @PathParam("end_date") String endDate) {

		List<ProductQuantity> order_list = null;
		try {
			order_list = repo.getOrdersByDate(beginDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return order_list;
	}
	
	/**
	 * http://localhost:8080/shipt/online_store/orders/order/onDate/2020-11-05
	 * Returns the products with category and product names sold on a specific date
	 * @param ondate
	 * @return
	 */
	@GET
	@Path("order/onDate/{date}")
	public List<ProductQuantity> getOrdersByDate(@PathParam("date") String ondate) {

		List<ProductQuantity> order_list = null;
		try {
			order_list = repo.getOrdersOnDate(ondate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return order_list;
	}

}


