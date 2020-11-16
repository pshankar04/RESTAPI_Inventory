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

@Path("products")
public class ProductResource {

	ProductRepository repo = new ProductRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getProducts(){
		
		return repo.getProducts();
	}
	
	@GET
	@Path("product/{id}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Product getProduct(@PathParam("id") int id) {
		
		return repo.getProduct(id);
	}
	
	@POST
	@Path("product")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Product createProduct(Product a1) {
		
		repo.createProduct(a1);
		return a1;
	}
	
	@PUT
	@Path("product")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Product updateProduct(Product a1) {
		
		repo.update(a1);
		return a1;
	}
	
	
//	@PATCH
//	@Path("Product")
//	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//	public Product updateProduct(Product a1) {
//		
//		repo.update(a1);
//		return a1;
//	}
	
	@DELETE
	@Path("product/{id}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Product deleteProduct(@PathParam("id") int id) {
		
		Product a = repo.getProduct(id);
		if (a.getProduct_id() == id) {
			repo.delete(id);
		}
		return a;
	}
	
}
