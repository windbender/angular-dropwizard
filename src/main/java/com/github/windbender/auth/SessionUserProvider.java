package com.github.windbender.auth;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.github.windbender.core.User;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
public class SessionUserProvider implements  InjectableProvider<SessionUser, Type> {

	private static class AuthedUserInjectable extends AbstractHttpContextInjectable<User> {

		private boolean required;
		HttpServletRequest request;

		private AuthedUserInjectable(HttpServletRequest request, boolean required) {
			this.required = required;
			this.request = request;
		}

		@Override
		public User getValue(HttpContext c) {
			final User user = (User) request.getSession().getAttribute("user");
			if (required) {
				if (user == null) {
					throw new WebApplicationException(Response.Status.UNAUTHORIZED);
				}
			}
			return user;

		}

	}

	private final HttpServletRequest request;
	private boolean required;

	public SessionUserProvider(@Context HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Injectable<User> getInjectable(ComponentContext cc, SessionUser a,
			Type c) {
		required = a.required();

		if (c.equals(User.class)) {
			return new AuthedUserInjectable(request,required);
//			return this;
			
		}
		return null;
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

//	@Override
//	public User getValue() {
//		final User user = (User) request.getSession().getAttribute("user");
//		if (required) {
//			if (user == null) {
//				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//			}
//		}
//		return user;
//	}

}
