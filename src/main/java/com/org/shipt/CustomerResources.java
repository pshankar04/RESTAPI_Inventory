package com.org.shipt;

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

@Path("customers")
public class CustomerResources {

	CustomerRepository repo = new CustomerRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> getCustomers(){
		
		return repo.getCustomers();
	}
	
	@GET
	@Path("customer/{id}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Customer getCustomer(@PathParam("id") int id) {
		
		return repo.getCustomer(id);
	}
	
	@POST
	@Path("customer")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Customer createCustomer(Customer a1) {
		
		repo.createCustomer(a1);
		return a1;
	}
	
	@PUT
	@Path("customer")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Customer updateCustomer(Customer a1) {
		
		repo.update(a1);
		return a1;
	}
	
	
 
	
	@DELETE
	@Path("customer/{id}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Customer deleteCustomer(@PathParam("id") int id) {
		
		Customer a = repo.getCustomer(id);
		if (a.getCustomer_id() == id) {
			repo.delete(id);
		}
		return a;
	}
	
}

