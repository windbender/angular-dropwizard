package com.github.windbender;


import org.eclipse.jetty.server.session.SessionHandler;

import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle;
import com.github.windbender.auth.SessionUserProvider;
import com.github.windbender.resources.BookResource;
import com.github.windbender.resources.UserResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class RESTServer extends Service<RESTServerConfiguration> {
	public static void main(String[] args) throws Exception {
		new RESTServer().run(args);
	}

	private RESTServer() {
		super();
	}
	@Override
	public void initialize(Bootstrap<RESTServerConfiguration> bootstrap) {
		bootstrap.setName("hello-world");
	    //bootstrap.addBundle(new AssetsBundle("/assets", "/"));
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/"));
    }

	@Override
	public void run(RESTServerConfiguration configuration,
			Environment environment) {
		final String template = configuration.getTemplate();
		final String defaultName = configuration.getDefaultName();
		
		environment.addResource(new BookResource(template, defaultName));
		
		environment.addResource(new UserResource());
		
		environment.setSessionHandler(new SessionHandler());
		environment.addProvider(SessionUserProvider.class);
		
	}
}
