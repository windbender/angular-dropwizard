package com.github.windbender.resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.windbender.auth.SessionUser;
import com.github.windbender.core.CurUser;
import com.github.windbender.core.LoginObject;
import com.github.windbender.core.User;
import com.github.windbender.dao.DummyPersonDAO;
import com.github.windbender.dao.PersonDAO;
import com.yammer.metrics.annotation.Timed;

@Path("/users/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	Logger log = LoggerFactory.getLogger(BookResource.class);

	PersonDAO pd = new DummyPersonDAO();
	
	@GET
	@Timed
	@Path("menus")
	public List<MenuItem> getMenus(@SessionUser(required=false) User user) {
		log.info("looking for menus for user "+user);
		List<MenuItem> list = new ArrayList<MenuItem>();
		if(user != null) {
			list.add(new MenuItem("Books","#/books"));  // <a href="#/books">Books</a>
		}
		return list;
	}
	
	@GET
	@Timed
	@Path("getLoggedIn")
	public CurUser getLoggedInUser(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null) return new CurUser(null);
		String username = user.getUsername();
		CurUser cu = new CurUser(username);
		return cu;
	}
	@POST
	@Timed
	@Path("login")
	public Response login(  @Valid LoginObject login, @Context HttpServletRequest request) {
		log.info("attempting login for user "+login.getUsername());
		
		if(pd.checkPassword(login.getUsername(), login.getPassword())) {
			log.info("success");
			
			request.getSession().setAttribute("user", new User(login.getUsername()));

			return Response.status(Response.Status.OK).build();
		    
		} else {
			log.info("fail");
			request.getSession().setAttribute("user", null);

			return Response.status(Response.Status.FORBIDDEN).build();

		}

	}
	
	

	@POST
	@Timed
	@Path("logout")
	@Consumes(MediaType.APPLICATION_XML)
	public Response logout(@SessionUser User user, @Context HttpServletRequest request) {
		log.info("attempting logout for user "+user.toString());
		request.getSession().setAttribute("user", null);

		return Response.status(Response.Status.OK).build();

	}
}
