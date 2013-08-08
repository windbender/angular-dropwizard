package com.github.windbender.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.windbender.auth.SessionUser;
import com.github.windbender.core.Book;
import com.github.windbender.core.User;
import com.github.windbender.dao.BookRepository;
import com.github.windbender.dao.DummyBookRepository;
import com.sun.jersey.api.ConflictException;
import com.sun.jersey.api.NotFoundException;
import com.yammer.metrics.annotation.Timed;

@Path("/books/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

	Logger log = LoggerFactory.getLogger(BookResource.class);
	private BookRepository br;


	public BookResource(String template, String defaultName) {
		br = new DummyBookRepository();
	}

	@GET
	@Timed
	public List<Book> list(@SessionUser User user) {
		return br.findAll();
	}

	@GET
	@Timed
	@Path("{id}")
	public Book fetch(@SessionUser User user, @PathParam("id") Long id) {
		return br.findById((Integer)id.intValue());
	}


	@POST
	@Timed
	public Response add(@SessionUser User user,  @Valid Book book) {
		log.info("Ok we have the following session user "+user);
		Book newBook = br.save(book);
		
		URI uri = UriBuilder.fromResource(BookResource.class).build(newBook.getId());
		log.info("the response uri will be "+uri);
		return Response.created(uri).build();
	}
	
	@PUT
	@Timed
	@Path("{id}")
	public Response update(@SessionUser User user, @PathParam("id") Long id, @Valid Book book) {
		if(!id.equals(new Long(book.getId()))) {
			throw new ConflictException("the location and internal ID do not match");
		}
		Book newBook = br.save(book);
		
		URI uri = UriBuilder.fromResource(BookResource.class).build(newBook.getId());
		log.info("the response uri will be "+uri);
		return Response.created(uri).build();
	}
	
	@DELETE
	@Timed
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public Response delete(@SessionUser User user, @PathParam("id") Long id) {
		
		Book deletableBook = br.findById((Integer)id.intValue());
		if(deletableBook == null) {
			throw new NotFoundException("sorry that book doesn't exist");
		}
		br.delete((Integer)id.intValue());
		return Response.ok().build();
	}

}
