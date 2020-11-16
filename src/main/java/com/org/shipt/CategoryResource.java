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

@Path("categories")
public class CategoryResource {

	CategoryRepository cat_repo = new CategoryRepository();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories(){

		return cat_repo.getCategories();
	}

	@GET
	@Path("category/{id}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Category> getCategory(@PathParam("id") int id) {

		return cat_repo.getCategory(id);
	}

	@POST
	@Path("category")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Category> createCategory(List<Category> cats) {

		cat_repo.createCategory(cats);
		return cats;
	}

	@PUT
	@Path("category")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Category> updateCategory(List<Category> categories) {

		cat_repo.update(categories);
		return categories;
	}

	@DELETE
	@Path("category/{id}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Category> deleteCategory(@PathParam("id") int id) {
		List<Category> categories;
		categories = cat_repo.getCategory(id);
		for(Category c: categories) {
			if (c.getCategory_id() == id) {
				cat_repo.delete(id);
			}
		}
		return categories;
	}

}
